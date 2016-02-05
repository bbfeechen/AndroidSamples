package com.sony.imaging.app.srctrl.util.cameraproxy;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.util.MediaObserverAggregator;
import com.sony.imaging.app.srctrl.util.MediaObserver.MediaType;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;

public class CameraProxyPostviewImageSize
{
    private static final String TAG = CameraProxyPostviewImageSize.class.getSimpleName();
    
    private static final String SIZE_2M = "2M";
    private static final String SIZE_ORIGINAL = "Original";
    
    private static final String[] SUPPORTED =
        { SIZE_ORIGINAL, SIZE_2M };

    private static final String[] SIZE_2M_ONLY =
        { SIZE_2M };

    private static String SIZE_CURRENT = SIZE_2M;
    
    public static boolean set(String size)
    {
        boolean ret = false;
        String[] available = getAvailable(); 
        for (String s : available)
        {
            if (s.equals(size))
            {
                if(!SIZE_CURRENT.equals(size))
                {
                    SIZE_CURRENT = size;
                    ParamsGenerator.updatePostviewImageSize();
                }
                ret = true;
                break;
            }
        }
        return ret;
    }
    
    public static String get()
    {
        if( isExternalMediaMounted() )
        {
            return SIZE_CURRENT;
        }
        return SIZE_2M;
    }
    
    private static boolean isExternalMediaMounted()
    {
        return MediaObserverAggregator.isExternalMediaMounted();
    }
    
    public static String[] getAvailable()
    {
        if( isExternalMediaMounted() ) {
            return SUPPORTED;
        }
        return SIZE_2M_ONLY;
    }
    
    public static String[] getSupported()
    {
        return SUPPORTED;
    }
    
    public static boolean isSizeOriginal() {
        return SIZE_ORIGINAL.equals(SIZE_CURRENT);
    }
}
