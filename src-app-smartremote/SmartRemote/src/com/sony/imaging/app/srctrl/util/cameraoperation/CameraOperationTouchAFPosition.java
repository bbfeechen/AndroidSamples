package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.util.Arrays;

import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import com.sony.imaging.app.base.common.DisplayModeObserver;
import com.sony.imaging.app.base.menu.IController.NotSupportedException;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager.OnFocusInfo;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.FocusAreaController;
import com.sony.imaging.app.base.shooting.camera.FocusModeController;
import com.sony.imaging.app.base.shooting.camera.PictureSizeController;
import com.sony.imaging.app.base.shooting.camera.executor.BaseShootingExecutor;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.fw.RunStatus;
import com.sony.imaging.app.srctrl.util.OperationReceiver;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.FocusAreaInfos;
import com.sony.scalar.hardware.avio.DisplayManager.DeviceStatus;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFPositionParams;

public class CameraOperationTouchAFPosition
{
    private static final String tag = CameraOperationTouchAFPosition.class.getName();
    private static final CameraOperationTouchAFPosition s_TouchAFPosition = new CameraOperationTouchAFPosition(); // this
    // instance
    
    private TouchAFCurrentPositionParams mCurrentPosition = new TouchAFCurrentPositionParams();
    private String mPreviousFocusArea = null;
    private String mPreviousFocusMode = null;
    
    private CameraOperationTouchAFPosition()
    {
        mCurrentPosition.set = false;
        mCurrentPosition.touchCoordinates = new double[]
        {};
    }
    
    public static final TouchAFCurrentPositionParams get()
    {
        synchronized (s_TouchAFPosition)
        {
            TouchAFCurrentPositionParams clone = new TouchAFCurrentPositionParams();
            clone.set = s_TouchAFPosition.mCurrentPosition.set;
            clone.touchCoordinates = Arrays.copyOf(s_TouchAFPosition.mCurrentPosition.touchCoordinates, s_TouchAFPosition.mCurrentPosition.touchCoordinates.length);
            return clone;
        }
    }
    public static void enableSetTouchAFPosition(boolean set) {
        synchronized (s_TouchAFPosition)
        {
                s_TouchAFPosition.mCurrentPosition.set = set;
                boolean toBeNotified = ParamsGenerator.updateTouchAFPostionParams(set);
                if(toBeNotified) {
                    ServerEventHandler.getInstance().onServerStatusChanged();
                }
        }
    }
    
    public static Boolean set(Double X, Double Y, CameraNotificationListener notificationListener)
    {
        synchronized (s_TouchAFPosition)
        {
            FocusAreaController fac = FocusAreaController.getInstance();
            FocusModeController fmc = FocusModeController.getInstance();

            if(RunStatus.RUNNING != RunStatus.getStatus()) {
                Log.e(tag, "CAMERA STATUS ERROR: Camera is not running (Status="+RunStatus.getStatus()+") in " + Thread.currentThread().getStackTrace()[2].toString());
                return Boolean.FALSE;
            }
            BaseShootingExecutor shootingExecutor = ExecutorCreator.getInstance().getSequence();
            
            Log.v(tag, "Cancel AF once before start.");
            shootingExecutor.cancelAutoFocus();
            
            String fm = fmc.getValue();
            if (FocusModeController.MANUAL.equals(fm))
            {
                Log.v(tag, "Drive mode is MF.  Touch AF was canceled.");
                Log.v(tag, "Leave touch AF mode.");
                leaveTouchAFMode();
                return false;
            }
            
            if (null == X || null == Y)
            {
                Log.v(tag, "Cancel touch AF position.");
                leaveTouchAFMode();
                return true;
            }
            else
            {
                double x = X.doubleValue();
                double y = Y.doubleValue();
                
                if(DisplayModeObserver.getInstance().isPanelReverse())
                {
                    x = 100.0f - x;
                }
                
                int scalar_x = (int) net2scalar(x);
                int scalar_y = (int) net2scalar(y);
                
                try
                {
                    if (null == s_TouchAFPosition.mPreviousFocusArea)
                    {
                        String focusArea = fac.getValue();
                        s_TouchAFPosition.mPreviousFocusArea = focusArea;
                        fac.setValue(FocusAreaController.FLEX); // set the focus
                                                                // mode
                    }
                    if (null == s_TouchAFPosition.mPreviousFocusMode)
                    {
                        String focusMode = fmc.getValue();
                        s_TouchAFPosition.mPreviousFocusMode = focusMode;
                        Log.v(tag, "Set Focus Mode from " + s_TouchAFPosition.mPreviousFocusMode
                                + "to DMF forcibly for touch AF.");
                        fmc.setValue(FocusModeController.DMF); // DMF
                    }
                    fac.setFocusPoint(scalar_x, scalar_y); // set the position
                    
                    // execute the AF
                    CameraNotificationManager notificationManager = CameraNotificationManager.getInstance();
                    Log.v(tag, "Register to notificationManager.");
                    notificationListener.registerTo(notificationManager);
                    Log.v(tag, "Start AF for TouchAF");
                    shootingExecutor.autoFocus(null);
                    
                    Log.v(tag, "Transitting to S1OnEEStateForTouchAF...");
                    boolean check = OperationReceiver.changeToS1OnStateForTouchAF();
                    if (!check)
                    {
                        Log.e(tag,
                                "State transition to S1OnEEStateForTouchAF failed.  Unregister from notificationManager.");
                        notificationListener.unregister();
                        Log.v(tag, "Leave touch AF mode.");
                        leaveTouchAFMode();
                    }
                    
                    return check;
                }
                catch (NotSupportedException e)
                {
                    e.printStackTrace();
                    Log.e(tag, "Unregister from notificationManager.");
                    notificationListener.unregister();
                    Log.v(tag, "Leave touch AF mode.");
                    leaveTouchAFMode();
                }
            }
            return false;
        }
    }
    
    private static void leaveTouchAFMode()
    {
        leaveTouchAFMode(true);
    }

    public static void leaveTouchAFMode(boolean goToS1Off)
    {
        synchronized (s_TouchAFPosition)
        {
            if(RunStatus.RUNNING != RunStatus.getStatus()) {
                Log.e(tag, "CAMERA STATUS ERROR: Camera is not running (Status="+RunStatus.getStatus()+") in " + Thread.currentThread().getStackTrace()[2].toString());
            } else {
                BaseShootingExecutor shootingExecutor = ExecutorCreator.getInstance().getSequence();
                Log.v(tag, "Cancel AF once for cancel in leaveTouchAFMode.");
                shootingExecutor.cancelAutoFocus();
            }
            
            FocusAreaController fac = FocusAreaController.getInstance();
            FocusModeController fmc = FocusModeController.getInstance();
            if (null != s_TouchAFPosition.mPreviousFocusArea)
            {
                Log.v(tag, "Revert Focus Area to " + s_TouchAFPosition.mPreviousFocusArea);
                fac.setValue(s_TouchAFPosition.mPreviousFocusArea);
                s_TouchAFPosition.mPreviousFocusArea = null;
            }
            if (null != s_TouchAFPosition.mPreviousFocusMode)
            {
                Log.v(tag, "Revert AF Mode to " + s_TouchAFPosition.mPreviousFocusMode);
                fmc.setValue(s_TouchAFPosition.mPreviousFocusMode);
                s_TouchAFPosition.mPreviousFocusMode = null;
            }
            CameraOperationTouchAFPosition.enableSetTouchAFPosition(false);

            if(goToS1Off)
            {
                Log.v(tag, "Transitting to S1OffEEState...");
                OperationReceiver.changeToS1OffState();
            }
        }
    }
    
    public static class CameraNotificationListener implements NotificationListener
    {
        public boolean mReturned = false;
        public final TouchAFPositionParams params = new TouchAFPositionParams();
        
        private CameraNotificationManager mCameraNotifier;
        protected static final int ILLUMINATOR_FOCUS_INFO_INDEX = 0; // Copied
                                                                     // protected
                                                                     // value
                                                                     // from
                                                                     // AvstractAFView
        
        public CameraNotificationListener()
        {
            params.AFType = "";
        }
        
        @Override
        public String[] getTags()
        {
            return new String[]
            { CameraNotificationManager.DONE_AUTO_FOCUS };
        }
        
        private enum AFType
        {
            FLEXIBLE_SPOT, WIDE, FACE
        };
        
        private static String getAFTypeStr(OnFocusInfo onFocusInfo)
        {
            AFType afType = AFType.FLEXIBLE_SPOT;
            if (null != onFocusInfo.area)
            {
                for (int index : onFocusInfo.area)
                {
                    if (index == ILLUMINATOR_FOCUS_INFO_INDEX)
                    {
                        afType = AFType.WIDE;
                        break;
                    }
                }
            }
            
            String afTypeStr = null;
            switch (afType)
            {
            case FLEXIBLE_SPOT:
                afTypeStr = "Touch";
                break;
            case FACE:
            default:
                afTypeStr = "Wide";
                break;
            }
            Log.v(tag, "Current AFType is " + afTypeStr + "(" + afType + ")");
            return afTypeStr;
        }
        
        private void onFocusSucceeded(OnFocusInfo onFocusInfo)
        {
            synchronized (s_TouchAFPosition)
            {
                synchronized (params)
                {
                    params.AFResult = true;
                    params.AFType = getAFTypeStr(onFocusInfo);
                    
                    /*
                     * FocusAreaController fac =
                     * FocusAreaController.getInstance(); Pair<Integer, Integer>
                     * focusPoint = fac.getFocusPoint();
                     * params.touchCoordinates[0] =
                     * scalar2net(focusPoint.first.doubleValue());
                     * params.touchCoordinates[1] =
                     * scalar2net(focusPoint.second.doubleValue());
                     */
                    
                    /*
                     * DisplayModeObserver displayModeNotifier =
                     * DisplayModeObserver.getInstance(); FocusAreaInfos
                     * areaInfo = getCurrentFocusAreaInfos(displayModeNotifier,
                     * ParametersModifier.FOCUS_AREA_MODE_FLEX_SPOT); if
                     * (areaInfo != null) { Rect afFrame =
                     * getAreaRectOnScalarCordinate(areaInfo, focusPoint); if
                     * (null != afFrame) { params.AFBoxLeftTop[0] =
                     * scalar2net(afFrame.left); params.AFBoxLeftTop[1] =
                     * scalar2net(afFrame.top); params.AFBoxRightBottom[0] =
                     * scalar2net(afFrame.right); params.AFBoxRightBottom[1] =
                     * scalar2net(afFrame.bottom); } }
                     */
                }
                
                CameraOperationTouchAFPosition.enableSetTouchAFPosition(true);
            }
        }
        
        private void onFocusFailed(OnFocusInfo onFocusInfo)
        {
            synchronized(params)
            {
                params.AFResult = false;
                params.AFType = getAFTypeStr(onFocusInfo);
            }

            Log.v(tag, "[TAPO] Leave touch AF mode.");
            leaveTouchAFMode();
        }
        
        @Override
        public void onNotify(String tag)
        {
            synchronized (this)
            {
                if (mReturned)
                {
                    Log.v(tag, "Already processed.  Return.");
                    return;
                }
                
                if (null == mCameraNotifier)
                {
                    Log.v(tag, "Notifier is null.  Return.");
                    return;
                }
                
                if (CameraNotificationManager.DONE_AUTO_FOCUS.equals(tag))
                {
                                        CameraNotificationManager.OnFocusInfo onFocusInfo = (OnFocusInfo) mCameraNotifier.getValue(tag);
                    if (onFocusInfo != null)
                    {
                        switch (onFocusInfo.status)
                        {
                        case CameraEx.AutoFocusDoneListener.STATUS_CONTINUOUS:
                        case CameraEx.AutoFocusDoneListener.STATUS_LOCK:
                            // Handle the forcus event.
                            Log.v(tag, "Handle a focus success event.");
                            onFocusSucceeded(onFocusInfo);
                            break;
                        case CameraEx.AutoFocusDoneListener.STATUS_LOCK_WARM:
                            // focus error
                            Log.v(tag, "Handle a focus failure event.");
                            onFocusFailed(onFocusInfo);
                            break;
                        default:
                            // ignore
                            Log.e(tag, "Unknown focus result: " + onFocusInfo.status);
                            // onFocusFailed(onFocusInfo);
                            return;
                        }
                    }
                    
                    // can unregister myself?
                    Log.v(tag, "Unregister myself.");
                    unregister();
                    
                    mReturned = true;
                    Log.v(tag, "Notify other threads.");
                    notifyAll();
                }
            }
        }
        
        public void registerTo(CameraNotificationManager notificationManager)
        {
            synchronized (this)
            {
                mCameraNotifier = notificationManager;
                mCameraNotifier.setNotificationListener(this);
            }
        }
        
        public void unregister()
        {
            synchronized (this)
            {
                if (null != mCameraNotifier)
                {
                    mCameraNotifier.removeNotificationListener(this);
                    mCameraNotifier = null;
                }
            }
        }
    }
    
    private static double net2scalar(double net_pos)
    {
        return 2000 * net_pos / 100 - 1000;
    }
    
    private static double scalar2net(double scalar_pos)
    {
        return (scalar_pos + 1000) * 100 / 2000;
    }
    
    // //////////////////////////////////////////////
    // Copied from AbstractView.java
    protected static FocusAreaInfos getFocusAreaInfos(int aspect, int viewPattern, String focusAreaMode)
    {
        FocusAreaInfos[] areaInfos = CameraSetting.getInstance().getFocusAreaRectInfos(aspect, viewPattern);
        FocusAreaInfos ret = null;
        if (areaInfos != null)
        {
            for (FocusAreaInfos areaInfo : areaInfos)
            {
                if (areaInfo.focusAreaMode.equals(focusAreaMode))
                {
                    ret = areaInfo;
                    break;
                }
            }
        }
        return ret;
    }
    
    public static FocusAreaInfos getCurrentFocusAreaInfos(DisplayModeObserver displayModeNotifier, String focusAreaMode)
    {
        DeviceStatus deviceStatus = displayModeNotifier.getActiveDeviceStatus();
        
        int viewPattern = deviceStatus == null ? DisplayModeObserver.VIEW_PATTERN_OSD_NORMAL : deviceStatus.viewPattern; // IMDLAPP2-871.
        String picAsp = PictureSizeController.getInstance().getValue(PictureSizeController.MENU_ITEM_ID_ASPECT_RATIO);
        int imagerAsp = -1;
        if (PictureSizeController.ASPECT_16_9.equals(picAsp))
        {
            imagerAsp = CameraEx.PREVIEW_ASPECT_TYPE_16_9;
        }
        else if (PictureSizeController.ASPECT_3_2.equals(picAsp))
        {
            imagerAsp = CameraEx.PREVIEW_ASPECT_TYPE_3_2;
        }
        return getFocusAreaInfos(imagerAsp, viewPattern, focusAreaMode);
    }
    
    // //////////////////////////////////////////////
    // Copied from AFFlex.java
    protected static final int FLEXIBLE_MOVE_AREA_INDEX = 1;
    protected static final int FLEXIBLE_FRAME_INFO_INDEX = 2;
    
    public static Rect getAreaRectOnScalarCordinate(FocusAreaInfos flexSpotForcusAreaInfo, Pair<Integer, Integer> fPoint)
    {
        Rect areaRect = flexSpotForcusAreaInfo.rectInfos[FLEXIBLE_MOVE_AREA_INDEX].rect;
        Rect frameRect = flexSpotForcusAreaInfo.rectInfos[FLEXIBLE_FRAME_INFO_INDEX].rect;
        
        int imagerWidth = frameRect.right - frameRect.left;
        int imagerHeight = frameRect.bottom - frameRect.top;
        int imagerLeft = fPoint.first - imagerWidth / 2;
        int imagerRight = imagerLeft + imagerWidth;
        int imagerTop = fPoint.second - imagerHeight / 2;
        int imagerBottom = imagerTop + imagerHeight;
        
        if (imagerLeft < areaRect.left)
        {
            imagerLeft = areaRect.left;
            imagerRight = imagerLeft + imagerWidth;
        }
        if (imagerTop < areaRect.top)
        {
            imagerTop = areaRect.top;
            imagerBottom = imagerTop + imagerHeight;
        }
        if (imagerRight > areaRect.right)
        {
            imagerRight = areaRect.right;
            imagerLeft = imagerRight - imagerWidth;
        }
        if (imagerBottom > areaRect.bottom)
        {
            imagerBottom = areaRect.bottom;
            imagerTop = imagerBottom - imagerHeight;
        }
        
        return new Rect(imagerLeft, imagerTop, imagerRight, imagerBottom);
    }
}
