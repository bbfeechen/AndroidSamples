package com.sony.imaging.app.srctrl.util.cameraproxy;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;

public class CameraProxyZoom
{
    private static String TAG = CameraProxyZoom.class.getSimpleName();
    
    public static boolean actZoom(String direction, String movement)
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.ZOOM, direction, movement);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
 }
