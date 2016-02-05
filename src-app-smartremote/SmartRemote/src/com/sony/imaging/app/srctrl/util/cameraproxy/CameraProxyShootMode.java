package com.sony.imaging.app.srctrl.util.cameraproxy;

import android.util.Log;

public class CameraProxyShootMode
{
    private static final String TAG = CameraProxyShootMode.class.getSimpleName();
    
    private static final String SHOOT_MODE_STILL = "still";
    
    public static boolean set(String shoot_mode)
    {
        if (SHOOT_MODE_STILL.equals(shoot_mode))
        {
            return true;
        }
        else
        {
            Log.v(TAG, "Invalid shoot mode specified: " + shoot_mode);
            return false;
        }
    }
    
    public static String get()
    {
        return SHOOT_MODE_STILL;
    }
    
    public static String[] getAvailable()
    {
        return new String[]
        { SHOOT_MODE_STILL };
    }
    
    public static String[] getSupported()
    {
        return new String[]
        { SHOOT_MODE_STILL };
    }
}
