package com.sony.imaging.app.srctrl.util;


import java.util.Arrays;
import java.util.List;

import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.base.shooting.camera.ExposureCompensationController;
import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.fw.RunStatus;
import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFNumber;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFlashMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationIsoNumber;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationProgramShift;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationShutterSpeed;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationTouchAFPosition;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationWhiteBalance;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationZoom;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShutterListenerEx;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParams;

public class OperationReceiver {
    private static final String TAG = OperationReceiver.class.getSimpleName();
    
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Object[] data = (Object[])msg.obj;
			OperationRequester<?> c = (OperationRequester<?>)data[0];
			synchronized(c.mSync) {
				c.mResult = operate(msg.what, (Object[])data[1]);
				c.mSync.notify();				
			}
		}
	};

	synchronized public void terminate() {
		mHandler = null;
	}

	synchronized public boolean isAlive() {
		return null != mHandler;
	}
	
    /**
     * This API will be executed in the UI thread of Android.
     * 
     * Web server thread can call this API through Requester#getRequester().
     * 
     * @param requestID requestID defined by user.
     * @param params arguments for the operation if needed.
     * @return Result of the operation
     */
    protected Object operate(int requestID, Object... params) {
        switch(requestID){
            case HandoffOperationInfo.IS_AVAILABLE_EXPOSURE_COMPENSATION:
                return ExposureCompensationController.getInstance().isAvailable(ExposureCompensationController.EXPOSURE_COMPENSATION);
            case HandoffOperationInfo.SET_EXPOSURE_COMPENSATION:
                if(params.length == 1){
                    ExposureCompensationController.getInstance().setValue(null, (String)params[0]);
                    return true;
                } else {
                    return false;
                }
            case HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_INDEX:
                return ExposureCompensationController.getInstance().getExposureCompensationIndex();
            case HandoffOperationInfo.GET_SUPPORTED_EXPOSURE_COMPENSATION:
            	// AVAILABLE と同様に RunStatus.RUNNING　のチェックが必要ですが、動作検証できないためこのままとしています
           		return ExposureCompensationController.getInstance().getSupportedValue(null);
            case HandoffOperationInfo.GET_AVAILABLE_EXPOSURE_COMPENSATION:
               	if ((RunStatus.getStatus() == RunStatus.RUNNING) && (null != CameraSetting.getInstance().getCamera()))
            	{
            		return ExposureCompensationController.getInstance().getAvailableValue(null);
            	} else {
                    //Log.v(TAG, "operate = GET_AVAILABLE_EXPOSURE_COMPENSATION : RunStatus is Not Running.");
            		return null;
            	}
            case HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_STEP:
                return ExposureCompensationController.getInstance().getExposureCompensationStep();
            case HandoffOperationInfo.IS_AVAILABLE_DRIVE_MODE:
                return DriveModeController.getInstance().isAvailable(DriveModeController.DRIVEMODE);
            case HandoffOperationInfo.SET_DRIVE_MODE:
                if(params.length == 1){
                    DriveModeController.getInstance().setValue(DriveModeController.DRIVEMODE, (String)params[0]);
                    return true;
                } else {
                    return false;
                }
            case HandoffOperationInfo.GET_DRIVE_MODE:
                return DriveModeController.getInstance().getValue(DriveModeController.DRIVEMODE);
            case HandoffOperationInfo.GET_SUPPORTED_DRIVE_MODE:
                return DriveModeController.getInstance().getSupportedValue(DriveModeController.DRIVEMODE);
            case HandoffOperationInfo.GET_AVAILABLE_DRIVE_MODE:
                return DriveModeController.getInstance().getAvailableValue(DriveModeController.DRIVEMODE);
            case HandoffOperationInfo.IS_AVAILABLE_SPECIFIC_DRIVE_MODE:
                if(params.length == 1){
                    return DriveModeController.getInstance().isAvailable((String)params[0]);
                }
                return false;
            case HandoffOperationInfo.SET_SELF_TIMER:
                if(params.length == 1){
                    DriveModeController.getInstance().setValue(DriveModeController.SELF_TIMER, (String)params[0]);
                    return true;
                } else {
                    return false;
                }
            case HandoffOperationInfo.GET_SELF_TIMER:
                return CameraSetting.getInstance().getParameters().second.getSelfTimer();
            case HandoffOperationInfo.GET_SUPPORTED_SELF_TIMER:
                return DriveModeController.getInstance().getSupportedValue(DriveModeController.SELF_TIMER);
            case HandoffOperationInfo.GET_AVAILABLE_SELF_TIMER:
                return DriveModeController.getInstance().getAvailableValue(DriveModeController.SELF_TIMER);
            case HandoffOperationInfo.GET_EXPOSURE_MODE:
                return ExposureModeControllerEx.getInstance().getValue(ExposureModeControllerEx.EXPOSURE_MODE);
            case HandoffOperationInfo.GET_SELECTED_SCENE:
                return ExposureModeControllerEx.getInstance().getValue(ExposureModeControllerEx.SCENE_SELECTION_MODE);
            case HandoffOperationInfo.IS_SELF_TIMER:
                return DriveModeController.getInstance().isSelfTimer();
            case HandoffOperationInfo.MOVE_TO_SHOOTING_STATE:
                return changeToShootingState();
            case HandoffOperationInfo.MOVE_TO_NETWORK_STATE:
                return StateController.getInstance().changeToNetworkState();
            case HandoffOperationInfo.MOVE_TO_CAPTURE_STATE:
                return StateController.getInstance().changeToCaptureState((ShutterListenerEx)params[0]);
            case HandoffOperationInfo.MOVE_TO_S1ON_STATE_FOR_TOUCH_AF:
                return changeToS1OnStateForTouchAF();
            case HandoffOperationInfo.EXCUTE_TERMINATE_CAUTION:
                CautionUtilityClass.getInstance().executeTerminate();
                return true;
            case HandoffOperationInfo.GET_CAMERA_SETTING:
            {
                HandoffOperationInfo.CameraSettings settingName = (HandoffOperationInfo.CameraSettings)params[0];
                return getCameraSetting(settingName);
            }
            case HandoffOperationInfo.GET_CAMERA_SETTING_AVAILABLE:
            {
                HandoffOperationInfo.CameraSettings settingName = (HandoffOperationInfo.CameraSettings)params[0];
                return getCameraSettingAvailable(settingName);
            }
            case HandoffOperationInfo.GET_CAMERA_SETTING_SUPPORTED:
            {
                HandoffOperationInfo.CameraSettings settingName = (HandoffOperationInfo.CameraSettings)params[0];
                return getCameraSettingSupported(settingName);
            }
            case HandoffOperationInfo.SET_CAMERA_SETTING:
            {
                HandoffOperationInfo.CameraSettings settingName = (HandoffOperationInfo.CameraSettings)params[0];
                return setCameraSetting(settingName, Arrays.copyOfRange(params, 1, params.length));
            }
            default:
                return null;
        }
    }
    
    private Object getCameraSetting(HandoffOperationInfo.CameraSettings settingName) {
        switch(settingName) {
        case TOUCH_AF:
        {
            return CameraOperationTouchAFPosition.get();
        }
        case F_NUMBER:
        {
            return CameraOperationFNumber.get();
        }
        case SHUTTER_SPEED:
        {
            return CameraOperationShutterSpeed.get();
        }
        case ISO_NUMBER:
        {
            return CameraOperationIsoNumber.get();
        }
        case EXPOSURE_MODE:
        {
            return CameraOperationExposureMode.get();
        }
        case WHITE_BALANCE:
        {
            return CameraOperationWhiteBalance.get();
        }
        /*
        case LIVEVIEW_SIZE:
        {
            return null;
        }
        */
        /*
        case POSTVIEW_IMAGE_SIZE:
        {
            return null;
        }
        */
        case FLASH_MODE:
        {
            return CameraOperationFlashMode.get();
        }
        // EXPOSURE_COMPENSATION,
        // SELF_TIMER,
        default:
        {
            Log.e(TAG, "Invalid camera setting name: " + settingName.name());
            return null;
        }
        }
    }
    private Object setCameraSetting(HandoffOperationInfo.CameraSettings settingName, Object... params) {
        switch(settingName) {
        case TOUCH_AF:
        {
            return CameraOperationTouchAFPosition.set((Double)params[0], (Double)params[1], (CameraOperationTouchAFPosition.CameraNotificationListener)params[2]);
        }
        case F_NUMBER:
        {
            return CameraOperationFNumber.set((String)params[0]);
        }
        case SHUTTER_SPEED:
        {
            return CameraOperationShutterSpeed.set((String)params[0]);
        }
        case ISO_NUMBER:
        {
            return CameraOperationIsoNumber.set((String)params[0]);
        }
        case EXPOSURE_MODE:
        {
            return CameraOperationExposureMode.set((String)params[0]);
        }
        case WHITE_BALANCE:
        {
            return CameraOperationWhiteBalance.set((WhiteBalanceParams)params[0], (Boolean)params[1]);
        }
        /*
        case LIVEVIEW_SIZE:
        {
            return null;
        }
        */
        /*
        case POSTVIEW_IMAGE_SIZE:
        {
            return null;
        }
        */
        case PROGRAM_SHIFT:
        {
            List<Boolean> result = CameraOperationProgramShift.set(((Integer)params[0]).intValue());
            return result;
        }
        case FLASH_MODE:
        {
            return CameraOperationFlashMode.set((String)params[0]);
        }
        case ZOOM:
        {
            return CameraOperationZoom.set((String)params[0], (String)params[1]);
        }
        // EXPOSURE_COMPENSATION,
        // SELF_TIMER,
        default:
        {
            Log.e(TAG, "Invalid camera setting name: " + settingName.name());
            return null;
        }
        }
    }
    private Object[] getCameraSettingAvailable(HandoffOperationInfo.CameraSettings settingName) {
        switch(settingName) {
        case TOUCH_AF:
        {
            return null;
        }
        case F_NUMBER:
        {
            return CameraOperationFNumber.getAvailable();
        }
        case SHUTTER_SPEED:
        {
            return CameraOperationShutterSpeed.getAvailable();
        }
        case ISO_NUMBER:
        {
            return CameraOperationIsoNumber.getAvailable();
        }
        case EXPOSURE_MODE:
        {
            return CameraOperationExposureMode.getAvailable();
        }
        case WHITE_BALANCE:
        {
            return CameraOperationWhiteBalance.getAvailable();
        }
        /*
        case LIVEVIEW_SIZE:
        {
            return null;
        }
        */
        /*
        case POSTVIEW_IMAGE_SIZE:
        {
            return null;
        }
        */
        case FLASH_MODE:
        {
            return CameraOperationFlashMode.getAvailable();
        }
        // EXPOSURE_COMPENSATION,
        // SELF_TIMER,
        default:
        {
            Log.e(TAG, "Invalid camera setting name: " + settingName.name());
            return null;
        }
        }
    }
    private Object[] getCameraSettingSupported(HandoffOperationInfo.CameraSettings settingName) {
        switch(settingName) {
        case TOUCH_AF:
        {
            return null;
        }
        case F_NUMBER:
        {
            return CameraOperationFNumber.getSupportd();
        }
        case SHUTTER_SPEED:
        {
            return CameraOperationShutterSpeed.getSupportd();
        }
        case ISO_NUMBER:
        {
            return CameraOperationIsoNumber.getSupportd();
        }
        case EXPOSURE_MODE:
        {
            return CameraOperationExposureMode.getSupportd();
        }
        case WHITE_BALANCE:
        {
            return CameraOperationWhiteBalance.getSupportd();
        }
        /*
        case LIVEVIEW_SIZE:
        {
            return null;
        }
        */
        /*
        case POSTVIEW_IMAGE_SIZE:
        {
            return null;
        }
        */
/*        case BRACKET_SHOOT_MODE:
        {
            return CameraOperationBracketShootMode.getSupportd();
        }
*/
        case FLASH_MODE:
        {
            return CameraOperationFlashMode.getSupportd();
        }
        // EXPOSURE_COMPENSATION,
        // SELF_TIMER,
        default:
        {
            Log.e(TAG, "Invalid camera setting name: " + settingName.name());
            return null;
        }
        }
    }
    
    public static boolean changeToShootingState() {
        return StateController.getInstance().changeToShootingState();
    }

    public static boolean changeToS1OffState() {
        return StateController.getInstance().changeToS1OffEEState();
    }

    public static boolean changeToS1OnStateForTouchAF() {
        return StateController.getInstance().changeToS1OnEEStateForTouchAF();
    }
}
