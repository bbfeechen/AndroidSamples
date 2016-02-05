package com.sony.imaging.app.srctrl.util.cameraproxy;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;

public class CameraProxyIsoNumber
{
    private static String TAG = CameraProxyIsoNumber.class.getSimpleName();
    
    public static boolean set(String isonumber)
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.ISO_NUMBER, isonumber);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static String get()
    {
        String result = new OperationRequester<String>().request(HandoffOperationInfo.GET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.ISO_NUMBER);
        return result;
    }
    
    public static String[] getAvailable()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode ||
             CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)
         ) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        String[] result = new OperationRequester<String[]>().request(HandoffOperationInfo.GET_CAMERA_SETTING_AVAILABLE,
                HandoffOperationInfo.CameraSettings.ISO_NUMBER);
        return result;
    }
    
    public static String[] getSupported()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || 
             CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)
         ) {
            Log.v(TAG, "Set empty string array to the supported value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        String[] result = new OperationRequester<String[]>().request(HandoffOperationInfo.GET_CAMERA_SETTING_SUPPORTED,
                HandoffOperationInfo.CameraSettings.ISO_NUMBER);
        return result;
    }
}
