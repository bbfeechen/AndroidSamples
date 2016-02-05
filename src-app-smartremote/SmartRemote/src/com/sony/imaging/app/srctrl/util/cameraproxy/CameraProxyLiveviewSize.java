package com.sony.imaging.app.srctrl.util.cameraproxy;

import com.sony.imaging.app.srctrl.liveview.LiveviewContainer;
import com.sony.imaging.app.srctrl.liveview.LiveviewLoader;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

public class CameraProxyLiveviewSize
{
    private static String TAG = CameraProxyLiveviewSize.class.getSimpleName();
    
    public static boolean set(String size)
    {
        boolean ret = false;
        LiveviewContainer container = LiveviewContainer.getInstance();
        ret = container.setLiveviewSize(size);
        return ret;
    }
    
    public static String get()
    {
        LiveviewContainer container = LiveviewContainer.getInstance();
        return container.getLiveviewSize();
    }
    
    public static String[] getAvailable()
    {
        if (LiveviewLoader.isLoadingPreview())
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        else
        {
            LiveviewContainer container = LiveviewContainer.getInstance();
            return container.getAvailableLiveviewSize();
        }
    }
    
    public static String[] getSupported()
    {
        LiveviewContainer container = LiveviewContainer.getInstance();
        return container.getSupportedLiveviewSize();
    }
}
