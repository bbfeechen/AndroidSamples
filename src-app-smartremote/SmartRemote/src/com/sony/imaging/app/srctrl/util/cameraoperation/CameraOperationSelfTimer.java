package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.AvailableManager;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.definition.StatusCode;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;

/**
 * Added by Kailiang Chen
 * @author 0000131654
 *
 * To improve Zoom performance by using cache value in ParamGenerator
 * when PMM calls getEvent
 * 
 * refer to CameraProxySelfTimer
 */

public class CameraOperationSelfTimer
{
    private static final String TAG = CameraOperationSelfTimer.class.getSimpleName();
    
    private static WeakReference<NotificationListener> s_NotificationListenerRef = new WeakReference<NotificationListener>(
            null);
    
    public static NotificationListener getNotificationListener()
    {
        NotificationListener notificationListener = s_NotificationListenerRef.get();
        if (null != notificationListener)
        {
            return notificationListener;
        }
        notificationListener = new NotificationListener()
        {
            @Override
            public String[] getTags()
            {
                return new String[]
                { 
                	CameraNotificationManager.SELFTIMER_COUNTDOWN_STATUS,   
                	CameraNotificationManager.DRIVE_MODE,
               	};			
            }
            
            @Override
            public void onNotify(String tag)
            {
            	if (CameraNotificationManager.SELFTIMER_COUNTDOWN_STATUS.equals(tag) || CameraNotificationManager.DRIVE_MODE.equals(tag)) 
            	{
            		Integer current = get();
                    if (null == current)
                    {
                        current = ParamsGenerator.s_INVALID_SELFTIMER_VALUE;
                    }
                    int[] available = getAvailable();
                    if (null == available)
                    {
                        available = SRCtrlConstants.s_EMPTY_INT_ARRAY;
                    }
	                
	                boolean toBeNotified = ParamsGenerator.updateSelfTimerParams(current, available);
	                if (toBeNotified) {
	                    ServerEventHandler.getInstance().onServerStatusChanged();
	                }
            	}
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    public static Integer get()
    {
        Boolean result = DriveModeController.getInstance().isSelfTimer();
        if (null != result)
        {
            if (result.booleanValue())
            {
                return CameraSetting.getInstance().getParameters().second.getSelfTimer();
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return null;
        }
    }
    
    public static int[] getAvailable()
    {
        // return SupportedValueCache.getInstance().getAvailableSelfTimer(); //
        // Deferred.
        ArrayList<Integer> available = new ArrayList<Integer>();
        boolean isSingleAvailable = true;
        String tmpMode = ExposureModeControllerEx.getInstance().getValue(ExposureModeControllerEx.EXPOSURE_MODE);
        
        if (null != tmpMode && ExposureModeControllerEx.SCENE_SELECTION_MODE.equals(tmpMode))
        {
            tmpMode = ExposureModeControllerEx.getInstance().getValue(ExposureModeControllerEx.SCENE_SELECTION_MODE);
            if (ExposureModeControllerEx.SPORTS.equals(tmpMode))
            {
                isSingleAvailable = false;
            }
        }
        boolean isTimerAvailable = false;
        List<String> list = DriveModeController.getInstance().getAvailableValue(DriveModeController.DRIVEMODE);
        
        if (isSingleAvailable)
        {
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.SINGLE.equals(str))
                    {
                        available.add(0);
                    }
                    else if (DriveModeController.SELF_TIMER.equals(str))
                    {
                        isTimerAvailable = true;
                    }
                }
            }
        }
        else
        {
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.BURST.equals(str))
                    {
                        available.add(0);
                    }
                    else if (DriveModeController.SELF_TIMER.equals(str))
                    {
                        isTimerAvailable = true;
                    }
                }
            }
        }
        if (isTimerAvailable)
        {
            list = DriveModeController.getInstance().getAvailableValue(DriveModeController.SELF_TIMER);
            
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.SELF_TIMER_2S.equals(str))
                    {
                        available.add(2);
                    }
                }
            }
        }
        int[] availableArray = new int[available.size()];
        int i = 0;
        for (Integer value : available)
        {
            availableArray[i++] = value;
        }
        return availableArray;
    } 
    
    /**
     * refer to CameraProxySelfTimer
     * the common way is to use the following methods in OperateReceiver
     */
//  public static boolean isAvailableSelfTimer() {}    
//  public static boolean isAvailableSingle() {}    
//  public static boolean isAvailableBurst() {}    
//  public static int set(int timer) {}
//  public static int[] getSupported() {}    
}
