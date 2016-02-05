package com.sony.imaging.app.srctrl.util.cameraproxy;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationWhiteBalance;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParamCandidate;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParams;

public class CameraProxyWhiteBalance
{
    private static String TAG = CameraProxyWhiteBalance.class.getSimpleName();
    
    public static boolean set(WhiteBalanceParams param, boolean colorTemperatureEnabled)
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.WHITE_BALANCE, param, new Boolean(colorTemperatureEnabled));
        if(null != result) {
            return result.booleanValue();
        } else {
            return false;
        }
    }
    
    public static WhiteBalanceParams get()
    {
        WhiteBalanceParams result = new OperationRequester<WhiteBalanceParams>().request(
                HandoffOperationInfo.GET_CAMERA_SETTING, HandoffOperationInfo.CameraSettings.WHITE_BALANCE);
        return result;
    }
    
    public static WhiteBalanceParamCandidate[] getAvailable()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode ||
             CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)
         ) {
            Log.v(TAG, "Set empty array to the available value due to the exposure mode: " + exposureMode);
            return CameraOperationWhiteBalance.s_EMPTY_CANDIDATE;
        }

        WhiteBalanceParamCandidate[] result = new OperationRequester<WhiteBalanceParamCandidate[]>().request(
                HandoffOperationInfo.GET_CAMERA_SETTING_AVAILABLE, HandoffOperationInfo.CameraSettings.WHITE_BALANCE);
        return result;
    }
    
    public static WhiteBalanceParamCandidate[] getSupported()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode ||
             CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)
         ) {
            Log.v(TAG, "Set empty array to the supported value due to the exposure mode " + exposureMode);
            return CameraOperationWhiteBalance.s_EMPTY_CANDIDATE;
        }

        WhiteBalanceParamCandidate[] result = new OperationRequester<WhiteBalanceParamCandidate[]>().request(
                HandoffOperationInfo.GET_CAMERA_SETTING_SUPPORTED, HandoffOperationInfo.CameraSettings.WHITE_BALANCE);
        return result;
    }
}
