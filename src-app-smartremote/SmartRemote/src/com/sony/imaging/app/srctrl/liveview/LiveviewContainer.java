package com.sony.imaging.app.srctrl.liveview;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.PictureSizeController;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

/**
 * 
 * This class contains liveview parameters.
 * 
 * @author 0000138134
 * 
 */
public class LiveviewContainer
{
	private static final String TAG = LiveviewContainer.class.getSimpleName();

    static private LiveviewContainer sState = new LiveviewContainer();
    private int frameRate;
    private String width;
    private int compressRate;
    private int maxFileSize;
    private String pictureAspectRatio;

    private int eeWidthSize = 0;

    private static final int LIVEVIEW_GENERATING_FRAMERATE = 30000;
    public static final String s_DEFAULT_LIVEVIEW_SIZE = "M";
    public static final String s_LARGE_LIVEVIEW_SIZE = "L";

    /* for XGA (Default) */
    private static final int LIVEVIEW_COMPRESS_RATE_FOR_DEFAULT = 15;
    private static final int LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_DEFAULT = 100;
    private static final int LIVEVIEW_COMPRESS_RATE_FOR_LARGE = 8;
    private static final int LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_LARGE = 100;
    private static final Map<String, Integer> supportedLiveviewSize = new HashMap<String, Integer>(2);
    static
    {
        supportedLiveviewSize.put(s_DEFAULT_LIVEVIEW_SIZE, 640); // Default and mandatory
        supportedLiveviewSize.put(s_LARGE_LIVEVIEW_SIZE, 1024);
    }

    /* for VGA */
    private static final int LIVEVIEW_COMPRESS_RATE_FOR_DEFAULT_VGA = 15;
    private static final int LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_DEFAULT_VGA = 100;
    private static final int LIVEVIEW_COMPRESS_RATE_FOR_LARGE_VGA = 5;
    private static final int LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_LARGE_VGA = 100;
    private static final Map<String, Integer> supportedLiveviewSizeVGA = new HashMap<String, Integer>(2);
    static
    {
    	supportedLiveviewSizeVGA.put(s_DEFAULT_LIVEVIEW_SIZE, 640); // Default and mandatory
    	supportedLiveviewSizeVGA.put(s_LARGE_LIVEVIEW_SIZE, 640);
    }

    public static final int PANEL_EE_SIZE_INVALID = -1;
    public static final int PANEL_EE_SIZE_XGA = 1;
    public static final int PANEL_EE_SIZE_VGA = 2;
    
    public static LiveviewContainer getInstance()
    {
        return sState;
    }
    
    public LiveviewContainer()
    {
        initState();
    }
    
    public void initState()
    {
        frameRate = LIVEVIEW_GENERATING_FRAMERATE;
        width = s_DEFAULT_LIVEVIEW_SIZE;
        compressRate = LIVEVIEW_COMPRESS_RATE_FOR_DEFAULT;
        maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_DEFAULT;
    }
    
    public boolean setFrameRate(int framerate)
    {
        this.frameRate = framerate;
        return true;
    }
    
    public int getFrameRate()
    {
        return frameRate;
    }
    
    public int getMaxFileSize()
    {
    	switch (getEESize())
    	{
	    	case PANEL_EE_SIZE_XGA:
	    	{
				if (this.width.equals(s_DEFAULT_LIVEVIEW_SIZE))
				{
					maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_DEFAULT;
				}
				else if (this.width.equals(s_LARGE_LIVEVIEW_SIZE))
				{
					maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_LARGE;
				}
	    		break;
	    	}
	    	case PANEL_EE_SIZE_VGA:
	    	{
	            if (this.width.equals(s_DEFAULT_LIVEVIEW_SIZE))
	            {	
	            	maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_DEFAULT_VGA;
	            }
	            else if (this.width.equals(s_LARGE_LIVEVIEW_SIZE))
	            {
	            	maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_LARGE_VGA;
	            }
	    		break;
	    	}
	    	default:
	    	{
	    		// EEサイズが決まっていない場合は、デフォルト値として最大サイズを返す
	    		maxFileSize = LIVEVIEW_MAXIMUM_SIZE_IN_KB_FOR_LARGE;
	    		break;
	    	}
	    }
        Log.v(TAG, "MaxFileSize size=" + maxFileSize);
        return maxFileSize;
    }
    
    public boolean setLiveviewSize(String size)
    {
    	if (s_DEFAULT_LIVEVIEW_SIZE.equals(size) || s_LARGE_LIVEVIEW_SIZE.equals(size)) {
			this.width = size;
			return true;
		} 
		else 
		{
			return false;
		}
    }
    
    public int getWidth()
    {
    	int retWidth = 0;
    	switch (getEESize())
    	{
	    	default:
	    	case PANEL_EE_SIZE_XGA:
	    	{
	    		retWidth =  supportedLiveviewSize.get(width).intValue();
	    		break;
	    	}
	    	case PANEL_EE_SIZE_VGA:
	    	{
	    		retWidth = supportedLiveviewSizeVGA.get(width).intValue();
	    		break;
	    	}
    	}    	
        Log.v(TAG, "Width=" + retWidth);
    	return retWidth;
    }
    
    public String getLiveviewSize()
    {
        return this.width;
    }
    
    public int getCompressRate()
    {
    	switch (getEESize())
    	{
	    	default:
	    	case PANEL_EE_SIZE_XGA:
	    	{
				if (this.width.equals(s_DEFAULT_LIVEVIEW_SIZE))
				{
					compressRate = LIVEVIEW_COMPRESS_RATE_FOR_DEFAULT;
				}
				else if (this.width.equals(s_LARGE_LIVEVIEW_SIZE))
				{
					compressRate = LIVEVIEW_COMPRESS_RATE_FOR_LARGE;
				}
	    		break;
	    	}
	    	case PANEL_EE_SIZE_VGA:
	    	{
	            if (this.width.equals(s_DEFAULT_LIVEVIEW_SIZE))
	            {
	            	compressRate = LIVEVIEW_COMPRESS_RATE_FOR_DEFAULT_VGA;
	            }
	            else if (this.width.equals(s_LARGE_LIVEVIEW_SIZE))
	            {
	            	compressRate = LIVEVIEW_COMPRESS_RATE_FOR_LARGE_VGA;
	            }
	    		break;
	    	}
    	}
        Log.v(TAG, "CompressRate=" + compressRate);
        return compressRate;
    }
    
    public void setPictureAspectRatio(String aspectRatio)
    {
        pictureAspectRatio = aspectRatio;
    }
    
    public String getPictureAspectRatio()
    {
        return pictureAspectRatio;
    }
    
    public int getHeight()
    {
        if (pictureAspectRatio != null)
        {
            if (PictureSizeController.ASPECT_3_2.equals(pictureAspectRatio))
            {
                float tmp = (float) ((float) (Integer.valueOf(supportedLiveviewSize.get(width))) * 2.0 / 3.0);
                return ((int) (tmp / 8) * 8);
            }
            else if (PictureSizeController.ASPECT_16_9.equals(pictureAspectRatio))
            {
                float tmp = (float) ((float) (Integer.valueOf(supportedLiveviewSize.get(width))) * 9.0 / 16.0);
                return ((int) (tmp / 8) * 8);
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return 0;
        }
    }
    
    public String[] getAvailableLiveviewSize()
    {
        return getSupportedLiveviewSize();
    }
    
    public String[] getSupportedLiveviewSize()
    {
        return supportedLiveviewSize.keySet().toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
    
    public boolean setEEWidthSize(int eeWidth)
    {
    	eeWidthSize = eeWidth;
        return true;
    }

    public int getEESize()
    {
    	if (1024 == eeWidthSize) {
            Log.v(TAG, "EE Size = XGA");
    		return PANEL_EE_SIZE_XGA;
    	} else if (640 == eeWidthSize) {
            Log.v(TAG, "EE Size = VGA");
    		return PANEL_EE_SIZE_VGA;
    	} else {
            Log.v(TAG, "EE Size = INVALID");
    		return PANEL_EE_SIZE_INVALID;
    	}
    }
}
