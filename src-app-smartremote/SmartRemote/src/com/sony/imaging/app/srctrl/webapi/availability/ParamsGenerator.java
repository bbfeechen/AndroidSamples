package com.sony.imaging.app.srctrl.webapi.availability;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.menu.IController.NotSupportedException;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.FocusAreaController;
import com.sony.imaging.app.base.shooting.camera.FocusModeController;
import com.sony.imaging.app.base.shooting.widget.ISOSensitivityView;
import com.sony.imaging.app.srctrl.liveview.LiveviewLoader;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationSelfTimer;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureCompensation;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFNumber;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFlashMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFocusArea;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFocusMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationIsoNumber;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationProgramShift;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationShutterSpeed;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationTouchAFPosition;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationWhiteBalance;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationZoom;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyIsoNumber;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyPostviewImageSize;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyShootMode;
import com.sony.imaging.app.srctrl.webapi.definition.ReceiveEventStatus;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventAvailableApiListParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventCameraStatusParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventExposureCompensationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventExposureModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFNumberParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFlashModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFocusModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventIsoSpeedRateParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventLiveviewStatusParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventPostviewImageSizeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventProgramShiftParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventSelfTimerParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventShootModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventShutterSpeedParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventTakePictureParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventTouchAFPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventWhiteBalanceParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventZoomInformationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParamCandidate;

/**
 * 
 * Generate return value of receiveEvent.
 * 
 * @author 0000138134
 * 
 */
public class ParamsGenerator
{
    private static final String tag = ParamsGenerator.class.getName();
    
    private static void reset()
    {
        synchronized(s_CameraStatusParamsLock) {
            s_CameraStatusParams = createGetEventCameraStatusParams(null);
        }
        
        synchronized(s_LiveviewStatusParamsLock) {
            s_LiveviewStatusParams = null;
            s_LiveviewStatusUpdated = false;
        }

        synchronized(s_PostviewImageSizeParamsLock) {
            s_PostviewImageSizeUpdated = false;
            s_PostviewImageSizeParams = null;
        }
        
        synchronized(s_ExposureCompensationParamsLock) {
            s_ExposureCompensationParams = createGetEventExposureCompensationParams(null);
            s_EXPOSURE_COMPENSATION_VALUE_INITIALIZED = false;
        }
        
        synchronized(s_SelfTimerParamsLock) {
            s_SelfTimerParams = createGetEventSelftimerParams(null);
            s_SELFTIMER_VALUE_INITIALIZED = false;
        }
        
        synchronized(s_AvailableApiListParamsLock) {
            s_AvailableApiListUpdated = false;
            s_AvailableApiListParams = null;
        }

        synchronized(s_ShutterSpeedParamsLock) {
            s_ShutterSpeedParams = null;
            s_ShutterSpeedUpdated = false;
        }
        
        synchronized(s_FNumberParamsLock) {
            s_FNumberParams = null;
            s_FNumberUpdated = false;
        }
        
        synchronized(s_FocusModeParamsLock) {
            s_FocusModeParams = createGetEventFocusModeParams(null);
            s_FocusModeUpdated = false;
        }
        
        synchronized(s_FocusAreaParamsLock) {
            s_FocusAreaParams = createGetEventFocusAreaParams(null);
            s_FocusAreaUpdated = false;
        }
        
        synchronized(s_TouchAFPositionParamsLock) {
            s_TouchAFPositionParams = createGetEventTouchAFPositionParams(null);
            s_TouchAFPositionUpdated = false;
        }
        
        synchronized(s_WhiteBalanceParamsLock) {
            s_WhiteBalanceParams = createGetEventWhiteBalanceParams(null);
            s_WhiteBalanceUpdated = false;
            s_WhiteBalanceAvaialble = CameraOperationWhiteBalance.s_EMPTY_CANDIDATE;
        }
        
        synchronized(s_ShootModeParamsLock) {
            s_ShootModeParams = null;
        }
        
        synchronized(s_ExposureModeParamsLock) {
            s_ExposureModeParams = createGetEventExposureModeParams(null);
            s_ExposureModeUpdated = false;
        }
        
        synchronized(s_IsoSpeedRateParamsLock) {
            s_IsoSpeedRateParams = createGetEventIsoSpeedRateParams(null);
            s_IsoSpeedRateUpdated = false;
        }
        
        synchronized(s_ProgramShiftParamsLock) {
            s_ProgramShiftParams = null;
            s_ProgramShiftUpdated = false;
        }
        
        synchronized(s_TakePictureParamsListLock) {
            s_TakePictureParamsList.clear();
            s_TakePictureUpdated = false;
        }
        
        synchronized(s_FlashModeParamsLock) {
            s_FlashModeParams = createGetEventFlashModeParams(null);
            s_FlashModeUpdated = false;
        }

        synchronized(s_ZoomInformationParamsLock) {
        	s_ZoomInformationParams = createGetEventZoomInformationParams(null);
            s_ZoomInformationUpdated = false;
        }
    }
    
    // //////////////////////////////////////////////////////
    // camera status
    static GetEventCameraStatusParams s_CameraStatusParams = createGetEventCameraStatusParams(null);
    static final Object s_CameraStatusParamsLock = new Object();
    
    private static GetEventCameraStatusParams createGetEventCameraStatusParams(GetEventCameraStatusParams original)
    {
        GetEventCameraStatusParams cameraStatusParams = new GetEventCameraStatusParams();
        cameraStatusParams.type = "cameraStatus";
        if (null != original)
        {
            cameraStatusParams.cameraStatus = original.cameraStatus;
        }
        else
        {
            cameraStatusParams.cameraStatus = ReceiveEventStatus.DUMMY;
        }
        return cameraStatusParams;
    }
    
    public static GetEventCameraStatusParams getServerStatus(boolean isPolling)
    {
        /*
         * if(SRCtrlConstants.SUPPORT_FULL_RCV_EVENT){
         * switch(StateController.getInstance().getServerStatus()){ case IDLE:
         * return ReceiveEventStatus.IDLE; case STILL_CAPTURING: return
         * ReceiveEventStatus.STILL_CAPTURING; case NOT_READY: return
         * ReceiveEventStatus.NOT_READY; default: return
         * ReceiveEventStatus.ERROR; } } else { return ReceiveEventStatus.DUMMY;
         * }
         */
        String serverStatus = ReceiveEventStatus.DUMMY;
        switch (StateController.getInstance().getServerStatus())
        {
        case IDLE:
            serverStatus = ReceiveEventStatus.IDLE;
            break;
        case STILL_CAPTURING:
            serverStatus = ReceiveEventStatus.STILL_CAPTURING;
            break;
        case NOT_READY:
            serverStatus = ReceiveEventStatus.NOT_READY;
            break;
        default:
            serverStatus = ReceiveEventStatus.ERROR;
            break;
        }
        if (!isPolling || !s_CameraStatusParams.cameraStatus.equals(serverStatus))
        {
            synchronized(s_CameraStatusParamsLock) {
                s_CameraStatusParams.cameraStatus = serverStatus;
                GetEventCameraStatusParams clone = createGetEventCameraStatusParams(s_CameraStatusParams);
                return clone;
            }
        }
        else
        {
            return null;
        }
    }
    
    // //////////////////////////////////////////////////////
    // liveview status
    static GetEventLiveviewStatusParams s_LiveviewStatusParams = null;
    static final Object s_LiveviewStatusParamsLock = new Object();
    private static boolean s_LiveviewStatusUpdated = false;
    
    private static GetEventLiveviewStatusParams createGetEventLiveviewStatusParams(GetEventLiveviewStatusParams original)
    {
        GetEventLiveviewStatusParams liveviewStatusParams = new GetEventLiveviewStatusParams();
        liveviewStatusParams.type = "liveviewStatus";
        if (null != original)
        {
            liveviewStatusParams.liveviewStatus = original.liveviewStatus;
        }
        else
        {
            liveviewStatusParams.liveviewStatus = false;
        }
        return liveviewStatusParams;
    }
    
    public static boolean updateLiveviewStatus()
    {
        synchronized(s_LiveviewStatusParamsLock) {
        if (s_LiveviewStatusUpdated)
        {
            return false;
        }
        s_LiveviewStatusUpdated = true;
        return true;
        }
    }
    
    public static GetEventLiveviewStatusParams getLiveviewStatus(boolean isPolling)
    {
        boolean liveviewStatus = false;
        if (!AppCondition.PREPARATION.equals(StateController.getInstance().getAppCondition()))
        {
            liveviewStatus = LiveviewLoader.isLoadingPreview();
        }
        
        synchronized(s_LiveviewStatusParamsLock) {
        if (!isPolling || s_LiveviewStatusUpdated || null == s_LiveviewStatusParams
                || liveviewStatus != s_LiveviewStatusParams.liveviewStatus)
        {
            s_LiveviewStatusUpdated = false;
            if (null == s_LiveviewStatusParams)
            {
                s_LiveviewStatusParams = createGetEventLiveviewStatusParams(null);
            }
            s_LiveviewStatusParams.liveviewStatus = liveviewStatus;
            
            GetEventLiveviewStatusParams clone = createGetEventLiveviewStatusParams(s_LiveviewStatusParams);
            return clone;
        }
        else
        {
            return null;
        }
        }
    }
    
    /*
     * public static AvailableParams getAvailableData(){ AvailableParams params
     * = new AvailableParams();
     * 
     * if(SRCtrlConstants.SUPPORT_FULL_RCV_EVENT){
     * if(ServerStatus.IDLE.equals(StateController
     * .getInstance().getServerStatus())){
     * if(AvailabilityDetector.isAvailable(Name.SET_EXPOSURE_COMPENSATION)){
     * params = addExposureCompensation(params); }
     * if(AvailabilityDetector.isAvailable(Name.SET_SELF_TIMER)){ params =
     * addSelfTimer(params); } } }
     * 
     * return params; } //
     */
    
    // //////////////////////////////////////////////////////
    // Postview Image Size
    private static boolean s_PostviewImageSizeUpdated = false;
    
    public static boolean updatePostviewImageSize()
    {
        synchronized(s_PostviewImageSizeParamsLock)
        {
        if (s_PostviewImageSizeUpdated)
        {
            return false;
        }
        s_PostviewImageSizeUpdated = true;
        return true;
        }
    }
    
    static GetEventPostviewImageSizeParams s_PostviewImageSizeParams = null;
    static final Object s_PostviewImageSizeParamsLock = new Object();
    
    private static GetEventPostviewImageSizeParams createGetEventPostviewImageSizeParams(
            GetEventPostviewImageSizeParams original)
    {
        GetEventPostviewImageSizeParams postviewImageSizeParams = new GetEventPostviewImageSizeParams();
        postviewImageSizeParams.type = "postviewImageSize";
        if (null != original)
        {
            postviewImageSizeParams.currentPostviewImageSize = original.currentPostviewImageSize;
            postviewImageSizeParams.postviewImageSizeCandidates = Arrays.copyOf(original.postviewImageSizeCandidates,
                    original.postviewImageSizeCandidates.length);
        }
        else
        {
            postviewImageSizeParams.currentPostviewImageSize = "";
            postviewImageSizeParams.postviewImageSizeCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        return postviewImageSizeParams;
    }
    
    public static GetEventPostviewImageSizeParams getPostviewImageSize(boolean isPolling)
    {
        String current = CameraProxyPostviewImageSize.get();
        String[] available = CameraProxyPostviewImageSize.getAvailable();
        synchronized(s_PostviewImageSizeParamsLock) {
        if (!isPolling || null == s_PostviewImageSizeParams || s_PostviewImageSizeUpdated)
        {
            s_PostviewImageSizeUpdated = false;
            if (null == s_PostviewImageSizeParams)
            {
                s_PostviewImageSizeParams = createGetEventPostviewImageSizeParams(null);
            }
            s_PostviewImageSizeParams.currentPostviewImageSize = current;
            s_PostviewImageSizeParams.postviewImageSizeCandidates = available;
            
            GetEventPostviewImageSizeParams clone = createGetEventPostviewImageSizeParams(s_PostviewImageSizeParams);
            return clone;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // exposure compensation
    /*
     * private static ArrayList<String> convertIntArrayToStringList(int[]
     * intArray){ ArrayList<String> strList = new ArrayList<String>(); for(int
     * value : intArray){ strList.add(Integer.toString(value)); } return
     * strList; } private static AvailableParams
     * addExposureCompensation(AvailableParams params){ ArrayList<String>
     * tmpList; tmpList = new ArrayList<String>(); int[] available =
     * AvailableManager.getExopsureCompensationAvailable(); if(null !=
     * available) { tmpList.add(Integer.toString(available.length));
     * tmpList.addAll(convertIntArrayToStringList(available)); } Integer current
     * = AvailableManager.getExposureCompensationCurrent(); if(null != current)
     * { params.addData( Name.SET_EXPOSURE_COMPENSATION, ResponseType.INT, true,
     * current.toString(), tmpList); } return params; } //
     */
    
    static GetEventExposureCompensationParams s_ExposureCompensationParams = createGetEventExposureCompensationParams(null);
    static final Object s_ExposureCompensationParamsLock = new Object();

    private static GetEventExposureCompensationParams createGetEventExposureCompensationParams(
            GetEventExposureCompensationParams original)
    {
        GetEventExposureCompensationParams exposureCompensationParams = new GetEventExposureCompensationParams();
        exposureCompensationParams.type = "exposureCompensation";
        if (null != original)
        {
            exposureCompensationParams.currentExposureCompensation = original.currentExposureCompensation;
            exposureCompensationParams.maxExposureCompensation = original.maxExposureCompensation;
            exposureCompensationParams.minExposureCompensation = original.minExposureCompensation;
            exposureCompensationParams.stepIndexOfExposureCompensation = original.stepIndexOfExposureCompensation;
        }
        else
        {
            exposureCompensationParams.currentExposureCompensation = s_INVALID_EVCOMPASATION_VALUE;
            exposureCompensationParams.maxExposureCompensation = s_INVALID_EVCOMPASATION_VALUE;
            exposureCompensationParams.minExposureCompensation = s_INVALID_EVCOMPASATION_VALUE;
            exposureCompensationParams.stepIndexOfExposureCompensation = s_INVALID_EVCOMPASATION_VALUE;
        }
        return exposureCompensationParams;
    }
    
    public static final int s_INVALID_EVCOMPASATION_VALUE = 0;
    public static boolean s_EXPOSURE_COMPENSATION_VALUE_INITIALIZED = false;
    
    public static GetEventExposureCompensationParams getExposureCompensation(boolean isPolling)
    {
        /*
         * if(SRCtrlConstants.SUPPORT_FULL_RCV_EVENT){
         * switch(StateController.getInstance().getServerStatus()){ case IDLE:
         * return ReceiveEventStatus.IDLE; case STILL_CAPTURING: return
         * ReceiveEventStatus.STILL_CAPTURING; case NOT_READY: return
         * ReceiveEventStatus.NOT_READY; default: return
         * ReceiveEventStatus.ERROR; } } else { return ReceiveEventStatus.DUMMY;
         * }
         */
        if (!ParamsGenerator.s_EXPOSURE_COMPENSATION_VALUE_INITIALIZED ||
                AppCondition.PREPARATION.equals(StateController.getInstance().getAppCondition()))
        {
            Log.e(tag, "Camera is not opened yet to get exposure compensation params.");
            return null;
        }
        
        synchronized(s_ExposureCompensationParamsLock){
            if (!isPolling || s_ExposureCompensationUpdated)
            {
            	s_ExposureCompensationUpdated = false;
            	GetEventExposureCompensationParams ref = s_ExposureCompensationParams;            
            	s_ExposureCompensationParams = createGetEventExposureCompensationParams(ref);
                return ref;
            }
            else
            {
                return null;
            }
        }
    }
    
    private static boolean s_ExposureCompensationUpdated = false;
    
    public static boolean updateExposureCompensationParams(int current, int maxAvailable, int minAvailable, int availableStep)
    {
        synchronized(s_ExposureCompensationParamsLock) {
	        if (s_ExposureCompensationUpdated 
	        		&& s_ExposureCompensationParams.currentExposureCompensation == current
                    && s_ExposureCompensationParams.maxExposureCompensation == maxAvailable
                    && s_ExposureCompensationParams.minExposureCompensation == minAvailable
                    && s_ExposureCompensationParams.stepIndexOfExposureCompensation == availableStep)
	        {
	            return false;
	        }
	        
	        s_ExposureCompensationParams.currentExposureCompensation = current;
	        
	        if(s_ExposureCompensationParams.maxExposureCompensation != maxAvailable 
	        		|| s_ExposureCompensationParams.minExposureCompensation != minAvailable
	        		|| s_ExposureCompensationParams.stepIndexOfExposureCompensation != availableStep) {
	        	
	        	s_ExposureCompensationParams.maxExposureCompensation = maxAvailable;
	            s_ExposureCompensationParams.minExposureCompensation = minAvailable;
	            s_ExposureCompensationParams.stepIndexOfExposureCompensation = availableStep;
	        }
            
	        if(!ParamsGenerator.s_EXPOSURE_COMPENSATION_VALUE_INITIALIZED) {
                ParamsGenerator.updateAvailableApiList();
                ParamsGenerator.s_EXPOSURE_COMPENSATION_VALUE_INITIALIZED = true;
            }
	        
	        s_ExposureCompensationUpdated = true;
        }

        return true;
    } 
    
    // //////////////////////////////////////////////////////
    // selftimer
    /*
     * private static AvailableParams addSelfTimer(AvailableParams params){
     * ArrayList<String> tmpList; tmpList = new ArrayList<String>(); int[]
     * available = CameraSettingSelftimer.getSelftimerAvailable(); if(null !=
     * available) { tmpList.add(Integer.toString(available.length));
     * tmpList.addAll(convertIntArrayToStringList(available)); } Integer current
     * = CameraSettingSelftimer.getSelftimerCurrent(); if(null != current) {
     * params.addData( Name.SET_SELF_TIMER, ResponseType.INT, false,
     * current.toString(), tmpList); } return params; } //
     */
    
    static GetEventSelfTimerParams s_SelfTimerParams = createGetEventSelftimerParams(null);
    static final Object s_SelfTimerParamsLock = new Object();

    private static GetEventSelfTimerParams createGetEventSelftimerParams(GetEventSelfTimerParams original)
    {
        GetEventSelfTimerParams selfTimerParams = new GetEventSelfTimerParams();
        selfTimerParams.type = "selfTimer";
        if (null != original)
        {
            selfTimerParams.currentSelfTimer = original.currentSelfTimer;
            selfTimerParams.selfTimerCandidates = Arrays.copyOf(original.selfTimerCandidates,
                    original.selfTimerCandidates.length);
        }
        else
        {
            selfTimerParams.currentSelfTimer = s_INVALID_SELFTIMER_VALUE;
            selfTimerParams.selfTimerCandidates = SRCtrlConstants.s_EMPTY_INT_ARRAY;
        }
        return selfTimerParams;
    }
    
    public static final int s_INVALID_SELFTIMER_VALUE = 0;
    public static boolean s_SELFTIMER_VALUE_INITIALIZED = false;
    
    public static GetEventSelfTimerParams getSelfTimer(boolean isPolling)
    {
        if (!ParamsGenerator.s_SELFTIMER_VALUE_INITIALIZED || 
                AppCondition.PREPARATION.equals(StateController.getInstance().getAppCondition()))
        {
            Log.e(tag, "Camera is not opened yet to get Selftimer params.");
            return null;
        }
        
        synchronized(s_SelfTimerParamsLock) {
            if (!isPolling || s_SelfTimerUpdated)
            {
            	s_SelfTimerUpdated = false;
            	GetEventSelfTimerParams ref = s_SelfTimerParams;            
            	s_SelfTimerParams = createGetEventSelftimerParams(ref);
                return ref;
            }
            else
            {
                return null;
            }
        }
    }
    
    private static boolean s_SelfTimerUpdated = false;
    
    public static boolean updateSelfTimerParams(int current, int[] candidates)
    {
        synchronized(s_SelfTimerParamsLock) {
	        if (s_SelfTimerUpdated && s_SelfTimerParams.currentSelfTimer == current
	                && Arrays.equals(s_SelfTimerParams.selfTimerCandidates, candidates))
	        {
	            // already updated and the new value is the same
	            return false;
	        }
	        s_SelfTimerParams.currentSelfTimer = current;
	        s_SelfTimerParams.selfTimerCandidates = Arrays.copyOf(candidates, candidates.length);
	        
	        if(!ParamsGenerator.s_SELFTIMER_VALUE_INITIALIZED) {
                ParamsGenerator.updateAvailableApiList();
                ParamsGenerator.s_SELFTIMER_VALUE_INITIALIZED = true;
            }
	        
	        s_SelfTimerUpdated = true;
        }

        return true;
    } 
    
    // ///////////////////////////////////////////
    // Notification Listeners
    public static class CameraSettingChangeListener implements NotificationListener
    {
        private String[] TAGS = new String[]
        { CameraNotificationManager.SHUTTER_SPEED, CameraNotificationManager.APERTURE,
                CameraNotificationManager.FOCUS_CHANGE, CameraNotificationManager.FOCUS_AREA_INFO,
                CameraNotificationManager.ZOOM_INFO_CHANGED,
                CameraNotificationManager.SCENE_MODE
        };
        
        @Override
        public String[] getTags()
        {
            return TAGS;
        }
        
        @Override
        public void onNotify(String tag)
        {
            if (tag.equals(CameraNotificationManager.SHUTTER_SPEED))
            {
                String shutterSpeed = ParamsGenerator.getShutterSpeed();
                if (null != shutterSpeed) {
	                String[] shutterSpeedCandidates = ParamsGenerator.getShutterSpeedAvailableCandidate();
	                ParamsGenerator.updateShutterSpeedParams(shutterSpeed, shutterSpeedCandidates);
                }
            }
            else if (tag.equals(CameraNotificationManager.APERTURE))
            {
                String fNumber = ParamsGenerator.getFNumber();
                if (null != fNumber) {
	                String[] fNumberCandidates = ParamsGenerator.getFNumberAvailableCandidate();
	                ParamsGenerator.updateFNumberParams(fNumber, fNumberCandidates);
                }
            }
            else if (tag.equals(CameraNotificationManager.FOCUS_CHANGE))
            {
                FocusModeController fmc = FocusModeController.getInstance();
                String focusModeInBase = fmc.getValue();
                String focusMode = CameraOperationFocusMode.getIdFromBase(focusModeInBase);
                List<String> availableFocusModeInBase = fmc.getAvailableValue();
                String[] availableFocusMode = CameraOperationFocusMode.getIdFromBase(availableFocusModeInBase);
                boolean updated = ParamsGenerator.updateFocusModeParams(focusMode,  availableFocusMode);
                if(updated)
                {
                    ParamsGenerator.updateAvailableApiList();
                }
            }
            else if (tag.equals(CameraNotificationManager.FOCUS_AREA_INFO) || tag.equals(CameraNotificationManager.ZOOM_INFO_CHANGED))
            {
                FocusAreaController fac = FocusAreaController.getInstance();
                String focusAreaInBase;
                try
                {
                    focusAreaInBase = fac.getValue();
                    String focusArea = CameraOperationFocusArea.getIdFromBase(focusAreaInBase);
                    List<String> availableFocusAreaInBase = fac.getAvailableValue();
                    String[] availableFocusArea = CameraOperationFocusArea.getIdFromBase(availableFocusAreaInBase);
                    boolean updated = ParamsGenerator.updateFocusAreaParams(focusArea,  availableFocusArea);
                    if(updated)
                    {
                        ParamsGenerator.updateAvailableApiList();
                    }
                }
                catch (NotSupportedException e)
                {
                    e.printStackTrace();
                }
            } 
            else
            {
                Log.e(tag, "Unknown camera change: " + tag);
            }
            ServerEventHandler.getInstance().onServerStatusChanged();
        }
    }
    
    private static WeakReference<CameraSettingChangeListener> s_cameraChangeListener;
    
    public static void startCameraSettingListener()
    {
        CameraSettingChangeListener cameraSettingChangeListener = null;
        if (null != s_cameraChangeListener)
        {
            cameraSettingChangeListener = s_cameraChangeListener.get();
        }
        if (null == cameraSettingChangeListener)
        {
            cameraSettingChangeListener = new CameraSettingChangeListener();
            s_cameraChangeListener = new WeakReference<CameraSettingChangeListener>(cameraSettingChangeListener);
        }

        reset();

        CameraNotificationManager notifier = CameraNotificationManager.getInstance();
        notifier.setNotificationListener(cameraSettingChangeListener);
        notifier.setNotificationListener(CameraOperationExposureMode.getNotificationListener());
        notifier.setNotificationListener(CameraOperationWhiteBalance.getNotificationListener());
        notifier.setNotificationListener(CameraOperationIsoNumber.getNotificationListener());
        notifier.setNotificationListener(CameraOperationProgramShift.getNotificationListener());
        notifier.setNotificationListener(CameraOperationFlashMode.getNotificationListener());
        notifier.setNotificationListener(CameraOperationZoom.getNotificationListener());
        notifier.setNotificationListener(CameraOperationSelfTimer.getNotificationListener());
        notifier.setNotificationListener(CameraOperationExposureCompensation.getNotificationListener());
    }
    
    public static void stopCameraSettingListener()
    {
        CameraSettingChangeListener cameraSettingChangeListener = null;
        if (null != s_cameraChangeListener)
        {
            cameraSettingChangeListener = s_cameraChangeListener.get();
        }
        if (null == cameraSettingChangeListener)
        {
            return;
        }
        CameraNotificationManager notifier = CameraNotificationManager.getInstance();
        notifier.removeNotificationListener(cameraSettingChangeListener);
        notifier.removeNotificationListener(CameraOperationExposureMode.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationWhiteBalance.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationIsoNumber.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationProgramShift.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationFlashMode.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationZoom.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationSelfTimer.getNotificationListener());
        notifier.removeNotificationListener(CameraOperationExposureCompensation.getNotificationListener());
    }
    
    // //////////////////////////////////////////////////////
    // getAvailableApiList
    private static boolean s_AvailableApiListUpdated = false;
    
    public static boolean updateAvailableApiList()
    {
        synchronized(s_AvailableApiListParamsLock) {
        if (s_AvailableApiListUpdated)
        {
            return false;
        }
        // Don't update API list HERE!
        // This API could be called by several thread contexts including UI
        // thread and web server thread.
        // AvailabilityDetector#getAvailables() may be switched to UI thread so
        // this might cause a deadlock.
        // So leave the update task of API list until it is needed in
        // ParamsGenerator#getAvailableApiList().
        s_AvailableApiListUpdated = true;
        return true;
        }
    }
    
    private static GetEventAvailableApiListParams s_AvailableApiListParams = null;
    static final Object s_AvailableApiListParamsLock = new Object();

    public static GetEventAvailableApiListParams getAvailableApiList(boolean isPolling)
    {
        boolean changed = false;
        synchronized(s_AvailableApiListParamsLock) {
        if (s_AvailableApiListUpdated || !isPolling)
        {
            changed = true;
        }
        }

        if(!changed) {
            return null;
        }

        synchronized(s_AvailableApiListParamsLock) {
            s_AvailableApiListUpdated = false;
        }

        // Obtaining API List should be done asynchronously because it accesses camera API and
        // causes deadlock with UI thread.
        String[] apiList = AvailabilityDetector.getAvailables(true);
        
        synchronized(s_AvailableApiListParamsLock) {
        if (null == s_AvailableApiListParams)
        {
            s_AvailableApiListParams = new GetEventAvailableApiListParams();
            s_AvailableApiListParams.type = "availableApiList";
            s_AvailableApiListParams.names = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        s_AvailableApiListParams.names = apiList;
            
        if (null == s_AvailableApiListParams.names)
        {
            Log.e(tag, "Available API was null.");
            s_AvailableApiListParams.names = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        return s_AvailableApiListParams;
        }
    }
    
    // //////////////////////////////////////////////////////
    // Shutter Speed
    private static String getShutterSpeed()
    {
        return CameraOperationShutterSpeed.get();
    }
    
    private static GetEventShutterSpeedParams s_ShutterSpeedParams = null;
    static final Object s_ShutterSpeedParamsLock = new Object();

    private static GetEventShutterSpeedParams createGetEventShutterSpeedParams(GetEventShutterSpeedParams original)
    {
        GetEventShutterSpeedParams params = new GetEventShutterSpeedParams();
        params.type = "shutterSpeed";
        if (null == original)
        {
            params.currentShutterSpeed = getShutterSpeed();
            params.shutterSpeedCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            params.currentShutterSpeed = original.currentShutterSpeed;
            params.shutterSpeedCandidates = Arrays.copyOf(original.shutterSpeedCandidates,
                    original.shutterSpeedCandidates.length);
        }
        return params;
    }
    
    private static boolean s_ShutterSpeedUpdated = false;
    
    private static String[] getShutterSpeedAvailableCandidate()
    {
        return CameraOperationShutterSpeed.getAvailable();
    }
    
    public static boolean updateShutterSpeedParams(String shutterSpeed, String[] shutterSpeedCandidates)
    {
        synchronized(s_ShutterSpeedParamsLock) {
        if (null == s_ShutterSpeedParams)
        {
            s_ShutterSpeedParams = createGetEventShutterSpeedParams(null);
        }
        
        if (null == shutterSpeedCandidates)
        {
            Log.e(tag, "Shutter speed candidates value was null.");
            shutterSpeedCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        if (s_ShutterSpeedUpdated && null != s_ShutterSpeedParams.currentShutterSpeed
                && s_ShutterSpeedParams.currentShutterSpeed.equals(shutterSpeed)
                && Arrays.equals(shutterSpeedCandidates, s_ShutterSpeedParams.shutterSpeedCandidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_ShutterSpeedParams.currentShutterSpeed = shutterSpeed;
        s_ShutterSpeedParams.shutterSpeedCandidates = Arrays.copyOf(shutterSpeedCandidates,
                shutterSpeedCandidates.length);
        
        s_ShutterSpeedUpdated = true;
        return true;
        }
    }
    
    public static GetEventShutterSpeedParams getShutterSpeed(boolean isPolling)
    {
        synchronized(s_ShutterSpeedParamsLock) {
        if (s_ShutterSpeedUpdated || !isPolling)
        {
            s_ShutterSpeedUpdated = false;
            GetEventShutterSpeedParams ref = s_ShutterSpeedParams;
            
            if (null == ref)
            {
                Log.e(tag, "<GETEVENT> GetEventShutterSpeedParams: not initialized yet.");
            }
            else
            {
                s_ShutterSpeedParams = createGetEventShutterSpeedParams(ref);
            }
            
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // F Number
    private static GetEventFNumberParams s_FNumberParams = null;
    static final Object s_FNumberParamsLock = new Object();

    private static String getFNumber()
    {
        return CameraOperationFNumber.get();
    }
    
    private static GetEventFNumberParams createGetEventFNumberParams(GetEventFNumberParams original)
    {
        GetEventFNumberParams params = new GetEventFNumberParams();
        params.type = "fNumber";
        if (null == original)
        {
            params.currentFNumber = getFNumber();
            params.fNumberCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            params.currentFNumber = original.currentFNumber;
            params.fNumberCandidates = Arrays.copyOf(original.fNumberCandidates, original.fNumberCandidates.length);
        }
        return params;
    }
    
    private static boolean s_FNumberUpdated = false;
    
    private static String[] getFNumberAvailableCandidate()
    {
        return CameraOperationFNumber.getAvailable();
    }
    
    public static boolean updateFNumberParams(String fNumber, String[] fNumberCandidates)
    {
        synchronized(s_FNumberParamsLock) {
        if (null == s_FNumberParams)
        {
            s_FNumberParams = createGetEventFNumberParams(null);
        }
        
        if (null == fNumberCandidates)
        {
            Log.e(tag, "FNumber candidates value was null.");
            fNumberCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        if (s_FNumberUpdated && null != s_FNumberParams.currentFNumber
                && s_FNumberParams.currentFNumber.equals(fNumber)
                && Arrays.equals(fNumberCandidates, s_FNumberParams.fNumberCandidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_FNumberParams.currentFNumber = fNumber;
        s_FNumberParams.fNumberCandidates = Arrays.copyOf(fNumberCandidates, fNumberCandidates.length);
        
        s_FNumberUpdated = true;
        return true;
        }
    }
    
    public static GetEventFNumberParams getFNumber(boolean isPolling)
    {
        synchronized(s_FNumberParamsLock) {
        if (s_FNumberUpdated || !isPolling)
        {
            s_FNumberUpdated = false;
            GetEventFNumberParams ref = s_FNumberParams;
            
            if (null == ref)
            {
                Log.e(tag, "<GETEVENT> GetEventFNumberParams: not initialized yet.");
            }
            else
            {
                s_FNumberParams = createGetEventFNumberParams(ref);
            }
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // Focus Mode
    private static GetEventFocusModeParams s_FocusModeParams = null;
    static final Object s_FocusModeParamsLock = new Object();

    private static String getFocusMode()
    {
        if(null == s_FocusModeParams)
        {
            return null;
        }
        return s_FocusModeParams.currentFocusMode;
    }
    
    private static GetEventFocusModeParams createGetEventFocusModeParams(GetEventFocusModeParams original)
    {
        GetEventFocusModeParams params = new GetEventFocusModeParams();
        params.type = "focusMode";
        if (null == original)
        {
            params.currentFocusMode = null;
            params.focusModeCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            params.currentFocusMode = original.currentFocusMode;
            params.focusModeCandidates = Arrays.copyOf(original.focusModeCandidates, original.focusModeCandidates.length);
        }
        return params;
    }
    
    private static boolean s_FocusModeUpdated = false;
    
    private static String[] getFocusModeAvailableCandidate()
    {
        if(null == s_FocusModeParams)
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        return s_FocusModeParams.focusModeCandidates;
    }
    
    public static boolean updateFocusModeParams(String focusMode, String[] focusModeCandidates)
    {
        synchronized(s_FocusModeParamsLock) {
        if (null == s_FocusModeParams)
        {
            s_FocusModeParams = createGetEventFocusModeParams(null);
        }
        
        if (null == focusModeCandidates)
        {
            Log.e(tag, "FocusMode candidates value was null.");
            focusModeCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        if (s_FocusModeUpdated && null != s_FocusModeParams.currentFocusMode
                && s_FocusModeParams.currentFocusMode.equals(focusMode)
                && Arrays.equals(focusModeCandidates, s_FocusModeParams.focusModeCandidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_FocusModeParams.currentFocusMode = focusMode;
        s_FocusModeParams.focusModeCandidates = Arrays.copyOf(focusModeCandidates, focusModeCandidates.length);
        
        s_FocusModeUpdated = true;
        return true;
        }
    }
    
    public static GetEventFocusModeParams peekFocusModeParamsSnapshot()
    {
        synchronized(s_FocusModeParamsLock) {
        GetEventFocusModeParams ref = s_FocusModeParams;
        return ref;
        }
    }

    public static GetEventFocusModeParams getFocusMode(boolean isPolling)
    {
        synchronized(s_FocusModeParamsLock) {
        if (s_FocusModeUpdated || !isPolling)
        {
            s_FocusModeUpdated = false;
            GetEventFocusModeParams ref = s_FocusModeParams;
            
            if (null == ref)
            {
                Log.e(tag, "<GETEVENT> GetEventFocusModeParams: not initialized yet.");
            }
            else
            {
                s_FocusModeParams = createGetEventFocusModeParams(ref);
            }
            return ref;
        }
        else
        {
            return null;
        }
        }
    }

    // //////////////////////////////////////////////////////
    // Focus Area
    public static class _GetEventFocusAreaParams
    {
        public String type;

        /* - - - */
        /* "FocusArea" */
        public String currentFocusArea;
        public String[] focusAreaCandidates;
    }
    private static _GetEventFocusAreaParams s_FocusAreaParams = null;
    static final Object s_FocusAreaParamsLock = new Object();

    private static String getFocusArea()
    {
        if(null == s_FocusAreaParams)
        {
            return null;
        }
        return s_FocusAreaParams.currentFocusArea;
    }
    
    private static _GetEventFocusAreaParams createGetEventFocusAreaParams(_GetEventFocusAreaParams original)
    {
        _GetEventFocusAreaParams params = new _GetEventFocusAreaParams();
        params.type = "focusArea";
        if (null == original)
        {
            params.currentFocusArea = null;
            params.focusAreaCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            params.currentFocusArea = original.currentFocusArea;
            params.focusAreaCandidates = Arrays.copyOf(original.focusAreaCandidates, original.focusAreaCandidates.length);
        }
        return params;
    }
    
    private static boolean s_FocusAreaUpdated = false;
    
    private static String[] getFocusAreaAvailableCandidate()
    {
        if(null == s_FocusAreaParams)
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        return s_FocusAreaParams.focusAreaCandidates;
    }
    
    public static boolean updateFocusAreaParams(String focusArea, String[] focusAreaCandidates)
    {
        synchronized(s_FocusAreaParamsLock) {
        if (null == s_FocusAreaParams)
        {
            s_FocusAreaParams = createGetEventFocusAreaParams(null);
        }
        
        if (null == focusAreaCandidates)
        {
            Log.e(tag, "FocusArea candidates value was null.");
            focusAreaCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        if (s_FocusAreaUpdated && null != s_FocusAreaParams.currentFocusArea
                && s_FocusAreaParams.currentFocusArea.equals(focusArea)
                && Arrays.equals(focusAreaCandidates, s_FocusAreaParams.focusAreaCandidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_FocusAreaParams.currentFocusArea = focusArea;
        s_FocusAreaParams.focusAreaCandidates = Arrays.copyOf(focusAreaCandidates, focusAreaCandidates.length);
        
        s_FocusAreaUpdated = true;
        return true;
        }
    }
    
    public static _GetEventFocusAreaParams peekFocusAreaParamsSnapshot()
    {
        synchronized(s_FocusAreaParamsLock) {
        _GetEventFocusAreaParams ref = s_FocusAreaParams;
        return ref;
        }
    }

    public static _GetEventFocusAreaParams getFocusArea(boolean isPolling)
    {
        synchronized(s_FocusAreaParamsLock) {
        if (s_FocusAreaUpdated || !isPolling)
        {
            s_FocusAreaUpdated = false;
            _GetEventFocusAreaParams ref = s_FocusAreaParams;
            
            if (null == ref)
            {
                Log.e(tag, "<GETEVENT> GetEventFocusAreaParams: not initialized yet.");
            }
            else
            {
                s_FocusAreaParams = createGetEventFocusAreaParams(ref);
            }
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // TOUCH AF
    private static GetEventTouchAFPositionParams s_TouchAFPositionParams = createGetEventTouchAFPositionParams(null);
    static final Object s_TouchAFPositionParamsLock = new Object();

    private static GetEventTouchAFPositionParams createGetEventTouchAFPositionParams(
            GetEventTouchAFPositionParams original)
    {
        GetEventTouchAFPositionParams param = new GetEventTouchAFPositionParams();
        param.type = "touchAFPosition";
        if (null == original)
        {
            param.currentSet = false;
            param.currentTouchCoordinates = new double[0];
        }
        else
        {
            param.currentSet = original.currentSet;
            param.currentTouchCoordinates = Arrays.copyOf(original.currentTouchCoordinates,
                    original.currentTouchCoordinates.length);
        }
        return param;
    }
    
    private static boolean s_TouchAFPositionUpdated = false;
    
    public static boolean updateTouchAFPostionParams(boolean bSet)
    {
        synchronized(s_TouchAFPositionParamsLock) {
        final TouchAFCurrentPositionParams tmp = CameraOperationTouchAFPosition.get();
        if (s_TouchAFPositionUpdated && s_TouchAFPositionParams.currentSet == bSet)
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_TouchAFPositionParams.currentSet = tmp.set;
        s_TouchAFPositionUpdated = true;
        return true;
        }
    }
    
    public static GetEventTouchAFPositionParams getTouchAFPosition(boolean isPolling)
    {
        synchronized(s_TouchAFPositionParamsLock) {
        if (s_TouchAFPositionUpdated || !isPolling)
        {
            s_TouchAFPositionUpdated = false;
            GetEventTouchAFPositionParams ref = s_TouchAFPositionParams;
            s_TouchAFPositionParams = createGetEventTouchAFPositionParams(ref);
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // White Balance
    private static GetEventWhiteBalanceParams s_WhiteBalanceParams = createGetEventWhiteBalanceParams(null);
    private static WhiteBalanceParamCandidate[] s_WhiteBalanceAvaialble = CameraOperationWhiteBalance.s_EMPTY_CANDIDATE;
    static final Object s_WhiteBalanceParamsLock = new Object();

    private static GetEventWhiteBalanceParams createGetEventWhiteBalanceParams(GetEventWhiteBalanceParams original)
    {
        GetEventWhiteBalanceParams param = new GetEventWhiteBalanceParams();
        param.type = "whiteBalance";
        if (null == original)
        {
            param.currentWhiteBalanceMode = "";
            param.checkAvailability = true;
            param.currentColorTemperature = 0;
        }
        else
        {
            param.currentWhiteBalanceMode = original.currentWhiteBalanceMode;
            param.checkAvailability = original.checkAvailability;
            param.currentColorTemperature = original.currentColorTemperature;
        }
        return param;
    }
    
    private static boolean s_WhiteBalanceUpdated = false;
    
    private static boolean compareWhiteBalanceCandidates(WhiteBalanceParamCandidate l, WhiteBalanceParamCandidate r) {
        return l.whiteBalanceMode == r.whiteBalanceMode && Arrays.equals(l.colorTemperatureRange, r.colorTemperatureRange);
    }
    private static boolean compareWhiteBalanceCandidates(WhiteBalanceParamCandidate[] l, WhiteBalanceParamCandidate[] r) {
        if(l.length != r.length)
        {
            return false;
        }
        else
        {
            for(int i = 0; i < l.length; i++)
            {
                if(!compareWhiteBalanceCandidates(l[i], r[i]))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean updateWhiteBalanceParams(String mode, int temperature, WhiteBalanceParamCandidate[] available)
    {
        synchronized(s_WhiteBalanceParamsLock) {
        boolean checkAvailability = !compareWhiteBalanceCandidates(available, s_WhiteBalanceAvaialble);
        if (s_WhiteBalanceUpdated && s_WhiteBalanceParams.checkAvailability == checkAvailability
                && s_WhiteBalanceParams.currentColorTemperature == temperature
                && s_WhiteBalanceParams.currentWhiteBalanceMode.equals(mode))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_WhiteBalanceParams.currentColorTemperature = temperature;
        if(checkAvailability)
        {
            s_WhiteBalanceParams.checkAvailability = true;
        }
        s_WhiteBalanceParams.currentWhiteBalanceMode = mode;
        s_WhiteBalanceAvaialble = available;
        s_WhiteBalanceUpdated = true;
        return true;
        }
    }
    
    public static GetEventWhiteBalanceParams getWhiteBalanceParams(boolean isPolling)
    {
        synchronized(s_WhiteBalanceParamsLock) {
        if (s_WhiteBalanceUpdated || !isPolling)
        {
            s_WhiteBalanceUpdated = false;
            GetEventWhiteBalanceParams ref = s_WhiteBalanceParams;
            s_WhiteBalanceParams = createGetEventWhiteBalanceParams(ref);
            
            if (!isPolling)
            {
                ref.checkAvailability = true;
            }
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    // //////////////////////////////////////////////////////
    // ShootMode
    private static GetEventShootModeParams s_ShootModeParams = null;
    static final Object s_ShootModeParamsLock = new Object();

    public static GetEventShootModeParams getShootModeParams(boolean isPolling)
    {
        boolean changed = false;
        synchronized(s_ShootModeParamsLock) {
        if (!isPolling || null == s_ShootModeParams)
        {
            changed = true;
        }
        }
        if(!changed)
        {
            return null;
        }
        
        String current = CameraProxyShootMode.get();
        String[] available = CameraProxyShootMode.getAvailable();
        synchronized(s_ShootModeParamsLock) {
            if (null == s_ShootModeParams)
            {
                s_ShootModeParams = new GetEventShootModeParams();
                s_ShootModeParams.type = "shootMode";
                s_ShootModeParams.currentShootMode = current; 
                if (null != available)
                {
                    s_ShootModeParams.shootModeCandidates = Arrays.copyOf(available, available.length);
                }
                else
                {
                    s_ShootModeParams.shootModeCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
                }
            }
            return s_ShootModeParams;
        }
    }
    
    // //////////////////////////////////////////////////////
    // Exposure Mode
    private static GetEventExposureModeParams s_ExposureModeParams = createGetEventExposureModeParams(null);
    static final Object s_ExposureModeParamsLock = new Object();

    private static GetEventExposureModeParams createGetEventExposureModeParams(GetEventExposureModeParams original)
    {
        GetEventExposureModeParams param = new GetEventExposureModeParams();
        param.type = "exposureMode";
        if (null == original)
        {
            param.currentExposureMode = null;
            param.exposureModeCandidates = null;
        }
        else
        {
            param.currentExposureMode = original.currentExposureMode;
            param.exposureModeCandidates = Arrays.copyOf(original.exposureModeCandidates,
                    original.exposureModeCandidates.length);
        }
        return param;
    }
    
    private static boolean s_ExposureModeUpdated = false;
    
    public static boolean updateExposureModeParams(String mode, String[] candidates)
    {
        synchronized(s_ExposureModeParamsLock) {
        if (s_ExposureModeUpdated && s_ExposureModeParams.currentExposureMode == mode
                && Arrays.equals(s_ExposureModeParams.exposureModeCandidates, candidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        s_ExposureModeParams.currentExposureMode = mode;
        s_ExposureModeParams.exposureModeCandidates = Arrays.copyOf(candidates, candidates.length);
        s_ExposureModeUpdated = true;
        }
        
        ParamsGenerator.updateAvailableApiList();
        CameraNotificationManager notifier = CameraNotificationManager.getInstance();
        notifier.requestNotify(CameraNotificationManager.WB_MODE_CHANGE);
        notifier.requestNotify(CameraNotificationManager.WB_DETAIL_CHANGE);
        return true;
    }
    
    public static GetEventExposureModeParams getExposureModeParams(boolean isPolling)
    {
        if (StateController.getInstance().getAppCondition().equals(StateController.AppCondition.PREPARATION) ||
            null == s_ExposureModeParams.currentExposureMode)
        {
            // force return because there is possibility camera can not allow to
            // get the exposure mode.
            Log.e(tag, "<GETEVENT> GetEventExposureModeParams: not initialized yet.");
            return null;
        }
        
        synchronized(s_ExposureModeParamsLock) {
        if (s_ExposureModeUpdated || !isPolling)
        {
            s_ExposureModeUpdated = false;
            GetEventExposureModeParams ref = s_ExposureModeParams;
            s_ExposureModeParams = createGetEventExposureModeParams(ref);
            
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    public static GetEventExposureModeParams peekExposureModeParamsSnapshot()
    {
        synchronized(s_ExposureModeParamsLock) {
        GetEventExposureModeParams ref = s_ExposureModeParams;
        return ref;
        }
    }
    
    // //////////////////////////////////////////////////////
    // Iso number
    private static GetEventIsoSpeedRateParams s_IsoSpeedRateParams = createGetEventIsoSpeedRateParams(null);
    static final Object s_IsoSpeedRateParamsLock = new Object();

    private static GetEventIsoSpeedRateParams createGetEventIsoSpeedRateParams(GetEventIsoSpeedRateParams original)
    {
        GetEventIsoSpeedRateParams param = new GetEventIsoSpeedRateParams();
        param.type = "isoSpeedRate";
        if (null == original)
        {
            param.currentIsoSpeedRate = null;
            param.isoSpeedRateCandidates = null;
        }
        else
        {
            param.currentIsoSpeedRate = original.currentIsoSpeedRate;
            param.isoSpeedRateCandidates = Arrays.copyOf(original.isoSpeedRateCandidates,
                    original.isoSpeedRateCandidates.length);
        }
        return param;
    }
    
    private static boolean s_IsoSpeedRateUpdated = false;
    
    public static boolean updateIsoSpeedRateParams(String iso, String[] candidates)
    {
        synchronized(s_IsoSpeedRateParamsLock) {
        if (s_IsoSpeedRateUpdated && s_IsoSpeedRateParams.currentIsoSpeedRate == iso
                && Arrays.equals(s_IsoSpeedRateParams.isoSpeedRateCandidates, candidates))
        {
            // already updated and the new value is the same
            return false;
        }
        
        String previousIso = s_IsoSpeedRateParams.currentIsoSpeedRate;
        s_IsoSpeedRateParams.currentIsoSpeedRate = iso;
        s_IsoSpeedRateParams.isoSpeedRateCandidates = Arrays.copyOf(candidates, candidates.length);
        s_IsoSpeedRateUpdated = true;
        
        if(!iso.equals(previousIso))
        {
            if(ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING.equals(iso) || ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING.equals(previousIso))
            {
                // Manual Exposure && ISO AUTO?
                String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
                if(null != exposureMode && CameraOperationExposureMode.EXPOSURE_MODE_MANUAL.equals(exposureMode))
                {
                    // Enable
                    ParamsGenerator.updateAvailableApiList();
                }
            }
        }
        return true;
        }
    }
    
    public static GetEventIsoSpeedRateParams getIsoSpeedRateParams(boolean isPolling)
    {
        if (null == s_IsoSpeedRateParams.currentIsoSpeedRate ||
                StateController.getInstance().getAppCondition().equals(StateController.AppCondition.PREPARATION))
        {
            // force return because there is possibility camera can not allow to
            // get the iso speed rate.
            Log.e(tag, "<GETEVENT> GetEventIsoSpeedRateParams: not initialized yet.");
            return null;
        }

        boolean firstGet = false;
        if (null == s_IsoSpeedRateParams.currentIsoSpeedRate)
        {
            // first get
            s_IsoSpeedRateParams.currentIsoSpeedRate = CameraProxyIsoNumber.get();
            s_IsoSpeedRateParams.isoSpeedRateCandidates = CameraProxyIsoNumber.getAvailable();
            firstGet = true;
        }
        synchronized(s_IsoSpeedRateParamsLock) {
        if (s_IsoSpeedRateUpdated || !isPolling || firstGet)
        {
            s_IsoSpeedRateUpdated = false;
            GetEventIsoSpeedRateParams ref = s_IsoSpeedRateParams;
            s_IsoSpeedRateParams = createGetEventIsoSpeedRateParams(ref);
            
            return ref;
        }
        else
        {
            return null;
        }
        }
    }

    public static GetEventIsoSpeedRateParams peekIsoSpeedRateParamsSnapshot()
    {
        synchronized(s_IsoSpeedRateParamsLock) {
        GetEventIsoSpeedRateParams ref = s_IsoSpeedRateParams;
        return ref;
        }
    }
    
    // //////////////////////////////////////////////////////
    // ProgramShift
    private static GetEventProgramShiftParams s_ProgramShiftParams = null;
    static final Object s_ProgramShiftParamsLock = new Object();

    private static GetEventProgramShiftParams createGetEventProgramShiftParams(GetEventProgramShiftParams original)
    {
        GetEventProgramShiftParams param = new GetEventProgramShiftParams();
        param.type = "programShift";
        if (null == original)
        {
            param.isShifted = false;
        }
        else
        {
            param.isShifted = original.isShifted;
        }
        return param;
    }
    
    private static boolean s_ProgramShiftUpdated = false;
    
    public static boolean updateProgramShiftParams(boolean isShifted)
    {
        synchronized(s_ProgramShiftParamsLock) {
        if (s_ProgramShiftUpdated && null != s_ProgramShiftParams && s_ProgramShiftParams.isShifted == isShifted)
        {
            // already updated and the new value is the same
            return false;
        }
        if (null == s_ProgramShiftParams)
        {
            s_ProgramShiftParams = createGetEventProgramShiftParams(null);
        }
        s_ProgramShiftParams.isShifted = isShifted;
        s_ProgramShiftUpdated = true;
        return true;
        }
    }
    
    public static GetEventProgramShiftParams getProgramShift(boolean isPolling)
    {
        if (StateController.getInstance().getAppCondition().equals(StateController.AppCondition.PREPARATION))
        {
            // force return because there is possibility camera can not allow to
            // get the iso speed rate.
            Log.e(tag, "<GETEVENT> GetEventProgramShiftParams: not initialized yet.");
            return null;
        }
        
        synchronized(s_ProgramShiftParamsLock) {
        if (null == s_ProgramShiftParams)
        {
            // first get
            s_ProgramShiftParams = createGetEventProgramShiftParams(null);
            s_ProgramShiftParams.isShifted = false;
            s_ProgramShiftUpdated = true;
        }
        
        if (s_ProgramShiftUpdated || !isPolling)
        {
            s_ProgramShiftUpdated = false;
            GetEventProgramShiftParams ref = s_ProgramShiftParams;
            s_ProgramShiftParams = createGetEventProgramShiftParams(ref);
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    // //////////////////////////////////////////////////////
    // TakePicture
    private static List<GetEventTakePictureParams> s_TakePictureParamsList = new ArrayList<GetEventTakePictureParams>();
    static final Object s_TakePictureParamsListLock = new Object();
    private static boolean s_TakePictureUpdated = false;
    private static GetEventTakePictureParams[] s_TakePictureParamsArray_EMPTY = new GetEventTakePictureParams[0]; 
    
    private static GetEventTakePictureParams createGetEventTakePictureParams(String[] urlArray)
    {
        GetEventTakePictureParams param = new GetEventTakePictureParams();
        param.type = "takePicture";
        //param.takePictureUrl = Arrays.copyOf(urlArray, urlArray.length);
        param.takePictureUrl = urlArray;
        return param;
    }
    
    public static void updateTakePictureParams(String[] urlArray)
    {
        if(0<urlArray.length)
        {
            synchronized(s_TakePictureParamsListLock) {
                GetEventTakePictureParams param = createGetEventTakePictureParams(urlArray);
                s_TakePictureUpdated = true;
                s_TakePictureParamsList.add(param);
            }
        }
    }
    
    public static GetEventTakePictureParams[] getTakePicture(boolean isPolling)
    {
        synchronized(s_TakePictureParamsListLock) {
            if(isPolling && !s_TakePictureUpdated) {
                return s_TakePictureParamsArray_EMPTY;
            }
            
            GetEventTakePictureParams[] takePictureArray = s_TakePictureParamsList.toArray(s_TakePictureParamsArray_EMPTY);
            s_TakePictureParamsList.clear();
            s_TakePictureUpdated = false;
            return takePictureArray;
        }
    }
    
    // //////////////////////////////////////////////////////
    // Flash Mode
    private static GetEventFlashModeParams s_FlashModeParams = null;
    static final Object s_FlashModeParamsLock = new Object();
    
    private static GetEventFlashModeParams createGetEventFlashModeParams(GetEventFlashModeParams original)
    {
    	GetEventFlashModeParams param = new GetEventFlashModeParams();
        param.type = "flashMode";
        if (null == original)
        {
            param.currentFlashMode = null;
            param.flashModeCandidates = SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            param.currentFlashMode = original.currentFlashMode;
            param.flashModeCandidates = Arrays.copyOf(original.flashModeCandidates,
                    original.flashModeCandidates.length);
        }
        return param;
    }
    
    private static boolean s_FlashModeUpdated = false;
    
    public static boolean updateFlashModeParams(String mode, String[] candidates)
    {
        synchronized(s_FlashModeParamsLock) {
        if (s_FlashModeUpdated && s_FlashModeParams.currentFlashMode == mode
                && Arrays.equals(s_FlashModeParams.flashModeCandidates, candidates))
        {
            // already updated and the new value is the same
            return false;
        }
        boolean updateAvailableApi = !(Arrays.equals(s_FlashModeParams.flashModeCandidates, candidates));
        
        s_FlashModeParams.currentFlashMode = mode;
        s_FlashModeParams.flashModeCandidates = Arrays.copyOf(candidates, candidates.length);
        s_FlashModeUpdated = true;
        
        if (updateAvailableApi) {
        	ParamsGenerator.updateAvailableApiList();
        }
        }

        return true;
    }
    
    public static GetEventFlashModeParams getFlashModeParams(boolean isPolling)
    {
        if (StateController.getInstance().getAppCondition().equals(StateController.AppCondition.PREPARATION) ||
       		null == s_FlashModeParams.currentFlashMode)
        {
            // force return because there is possibility camera can not allow to
            // get the flash mode.
            Log.e(tag, "<GETEVENT> GetEventFlashModeParams: not initialized yet.");
            return null;
        }
        
        synchronized(s_FlashModeParamsLock) {
        if (s_FlashModeUpdated || !isPolling)
        {
        	s_FlashModeUpdated = false;
        	GetEventFlashModeParams ref = s_FlashModeParams;
        	s_FlashModeParams = createGetEventFlashModeParams(ref);
            
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    public static GetEventFlashModeParams peekFlashModeParamsSnapshot()
    {
        synchronized(s_FlashModeParamsLock) {
        GetEventFlashModeParams ref = s_FlashModeParams;
        return ref;
        }
    }
    
    // //////////////////////////////////////////////////////
    // Zoom
    private static GetEventZoomInformationParams s_ZoomInformationParams = null;
    static final Object s_ZoomInformationParamsLock = new Object();
    private static boolean s_ZoomInformationUpdated = false;
    
    private static GetEventZoomInformationParams createGetEventZoomInformationParams(GetEventZoomInformationParams original)
    {
    	GetEventZoomInformationParams param = new GetEventZoomInformationParams();
        param.type = "zoomInformation";
        if (null == original) 
        {
            param.zoomPosition = -1;
            param.zoomNumberBox = -1;
            param.zoomIndexCurrentBox = -1;
            param.zoomPositionCurrentBox = -1;
        }
        else
        {
            param.zoomPosition = original.zoomPosition;
            param.zoomNumberBox = original.zoomNumberBox;
            param.zoomIndexCurrentBox = original.zoomIndexCurrentBox;
            param.zoomPositionCurrentBox = original.zoomPositionCurrentBox;
        }
        return param;
    }
    
    public static boolean updateZoomInformationParams(int position, int numberBox, int indexCurrentBox, int positionCurrentBox)
    {
        synchronized(s_ZoomInformationParamsLock) {
        //Log.v(tag, "updateZoomInformationParams: start");
        if (s_ZoomInformationUpdated 
        		&& s_ZoomInformationParams.zoomPosition == position
        		&& s_ZoomInformationParams.zoomNumberBox == numberBox
        		&& s_ZoomInformationParams.zoomIndexCurrentBox == indexCurrentBox
                && s_ZoomInformationParams.zoomPositionCurrentBox == positionCurrentBox )
        {
            // already updated and the new value is the same
            return false;
        }
        boolean updateAvailableApi = (s_ZoomInformationParams.zoomNumberBox != numberBox);
        //Log.v(tag, "updateZoomInformationParams: zoomNumberBox=" + s_ZoomInformationParams.zoomNumberBox + "  numberBox=" + numberBox);
        //Log.v(tag, "updateZoomInformationParams: updateAvailableApi=" + updateAvailableApi);
        
        s_ZoomInformationParams.zoomPosition = position;
        s_ZoomInformationParams.zoomNumberBox = numberBox;
        s_ZoomInformationParams.zoomIndexCurrentBox = indexCurrentBox;
        s_ZoomInformationParams.zoomPositionCurrentBox = positionCurrentBox;
        s_ZoomInformationUpdated = true;
        
        //if (updateAvailableApi) {
        	ParamsGenerator.updateAvailableApiList();
        //}
        }
        return true;
    }
    
    public static GetEventZoomInformationParams getZoomInformation(boolean isPolling)
    {
        if (StateController.getInstance().getAppCondition().equals(StateController.AppCondition.PREPARATION) )
        {
            // force return because there is possibility camera can not allow to
            // get the flash mode.
            Log.e(tag, "<GETEVENT> getZoomInformation: not initialized yet.");
            return null;
        }
        
        synchronized(s_ZoomInformationParamsLock) {
        if (s_ZoomInformationUpdated || !isPolling)
        {
        	s_ZoomInformationUpdated = false;
        	GetEventZoomInformationParams ref = s_ZoomInformationParams;
        	s_ZoomInformationParams = createGetEventZoomInformationParams(ref);
            return ref;
        }
        else
        {
            return null;
        }
        }
    }
    
    public static GetEventZoomInformationParams peekZoomInformationParamsSnapshot()
    {
        synchronized(s_ZoomInformationParamsLock) {
        	GetEventZoomInformationParams ref = s_ZoomInformationParams;
        return ref;
        }
    }
}
