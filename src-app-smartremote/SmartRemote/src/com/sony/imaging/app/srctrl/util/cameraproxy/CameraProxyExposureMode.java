package com.sony.imaging.app.srctrl.util.cameraproxy;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;

public class CameraProxyExposureMode
{
    private static String TAG = CameraProxyExposureMode.class.getSimpleName();
    
    public static boolean set(String mode)
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.EXPOSURE_MODE, mode);
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
                HandoffOperationInfo.CameraSettings.EXPOSURE_MODE);
        return result;
    }
    
    public static String[] getAvailable()
    {
        String[] result = new OperationRequester<String[]>().request(HandoffOperationInfo.GET_CAMERA_SETTING_AVAILABLE,
                HandoffOperationInfo.CameraSettings.EXPOSURE_MODE);
        return result;
    }
    
    public static String[] getSupported()
    {
        String[] result = new OperationRequester<String[]>().request(HandoffOperationInfo.GET_CAMERA_SETTING_SUPPORTED,
                HandoffOperationInfo.CameraSettings.EXPOSURE_MODE);
        return result;
    }
}
