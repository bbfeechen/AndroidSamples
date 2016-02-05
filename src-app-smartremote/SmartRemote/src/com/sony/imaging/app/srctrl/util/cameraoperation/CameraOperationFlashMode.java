package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.FlashController;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;

public class CameraOperationFlashMode
{
    private static final String TAG = CameraOperationFlashMode.class.getSimpleName();
    
    private static final String FLASH_MODE_OFF = "off";
    private static final String FLASH_MODE_AUTO = "auto";
    private static final String FLASH_MODE_ON = "on";
    private static final String FLASH_MODE_SLOWSYNC = "slowSync";
    private static final String FLASH_MODE_REARSYNC = "rearSync";
    private static final String FLASH_MODE_WIRELESS = "wireless";
    
    // Basically, the menu UI order should be taken care of by the platform.
    // Now the order is unique over several models so this app rearranges
    // items following the unique order.
    // However, the order varies over the models, this ordering should be
    // handled by PF connected to a model tightly.
    private static final String[] FLASH_MODE_MENU_ORDER =
        { FLASH_MODE_OFF
        , FLASH_MODE_AUTO
        , FLASH_MODE_ON
        , FLASH_MODE_SLOWSYNC
        , FLASH_MODE_REARSYNC
        , FLASH_MODE_WIRELESS
        };
        
    private static WeakReference<NotificationListener> s_NotificationListenerRef = new WeakReference<NotificationListener>(
            null);
    
    public static NotificationListener getNotificationListener()
    {
        NotificationListener notificationListener = s_NotificationListenerRef.get();
        if (null != notificationListener)
        {
            return notificationListener;
        }
        notificationListener = new NotificationListener()
        {
            @Override
            public String[] getTags()
            {
                return new String[]
                { CameraNotificationManager.FLASH_CHANGE,
                   	CameraNotificationManager.DEVICE_EXTERNAL_FLASH_ONOFF,	// update Available.
           			CameraNotificationManager.DEVICE_INTERNAL_FLASH_ONOFF,	// update Available.
               		CameraNotificationManager.SCENE_MODE,					// update Available.
               		CameraNotificationManager.REC_MODE_CHANGED};			// update Available.
            }
            
            @Override
            public void onNotify(String tag)
            {
            	if (CameraNotificationManager.FLASH_CHANGE.equals(tag) 
            			|| CameraNotificationManager.DEVICE_EXTERNAL_FLASH_ONOFF.equals(tag)
            			|| CameraNotificationManager.DEVICE_INTERNAL_FLASH_ONOFF.equals(tag)
            			|| CameraNotificationManager.SCENE_MODE.equals(tag)
            			|| CameraNotificationManager.REC_MODE_CHANGED.equals(tag) ) 
            	{
	            	String mode = get();
	                String[] candidatesList = getAvailable();
	                if(null != mode)
	                {
	                    boolean toBeNotified = ParamsGenerator.updateFlashModeParams(mode, candidatesList);
	                    if (toBeNotified)
	                    {
	                        ServerEventHandler.getInstance().onServerStatusChanged();
	                    }
	                }
            	}
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    private static String getBaseIdStr(String param)
    {
        String baseId = null;
        if (FLASH_MODE_OFF.equals(param))
        {
            baseId = FlashController.OFF;
        }
        else if (FLASH_MODE_AUTO.equals(param))
        {
            baseId = FlashController.AUTO;
        }
        else if (FLASH_MODE_ON.equals(param))
        {
            baseId = FlashController.FRONTSYNC;
        }
        else if (FLASH_MODE_SLOWSYNC.equals(param))
        {
            baseId = FlashController.SLOWSYN;
        }
        else if (FLASH_MODE_REARSYNC.equals(param))
        {
        	FlashController controller = FlashController.getInstance();
            List<String> supportedList = controller.getSupportedValue(FlashController.FLASHMODE);
        	if (supportedList != null) {
        		for (String s : supportedList) {
        			if (FlashController.REARSYN.equals(s) || FlashController.SLOWREARSYN.equals(s)) 
        			{
        				baseId = s;
        				break;
        			}
        		}
        	} 
        	if(baseId == null) {
                Log.e(TAG, "flashmode REARSYNC not supported: " + param);
        	}
        }
        else if (FLASH_MODE_WIRELESS.equals(param))
        {
            baseId = FlashController.WIRELESS;
        }
        else
        {
            Log.e(TAG, "Unknown flashmode parameter name: " + param);
        }
        return baseId;
    }
    
    private static List<String> getIdStrFromBase(List<String> baseIdList)
    {
        List<String> tmp = new ArrayList<String>();
        for (final String baseId : baseIdList)
        {
            String mode = getIdStrFromBase(baseId);
            if (null != mode)
            {
            	tmp.add(mode);
            }
        }
        
        // Sort
        List<String> ret = new ArrayList<String>();
        if (tmp != null)
        {
	        for(final String orderMode : FLASH_MODE_MENU_ORDER)
	        {
	        	for(final String mode : tmp)
	        	{
	        		if(orderMode.equals(mode))
	        		{
	        			ret.add(orderMode);
	        			break;
	        		}
	        	}
	        }
        }
        return ret;
    }
    
    private static String getIdStrFromBase(String baseId)
    {
        String id = null;
        if (FlashController.OFF.equals(baseId))
        {
            id = FLASH_MODE_OFF;
        }
        else if (FlashController.AUTO.equals(baseId))
        {
            id = FLASH_MODE_AUTO;
        }
        else if (FlashController.FRONTSYNC.equals(baseId))
        {
            id = FLASH_MODE_ON;
        }
        else if (FlashController.SLOWSYN.equals(baseId))
        {
            id = FLASH_MODE_SLOWSYNC;
        }
        else if (FlashController.REARSYN.equals(baseId))
        {
            id = FLASH_MODE_REARSYNC;
        }
        else if (FlashController.SLOWREARSYN.equals(baseId))
        {
            id = FLASH_MODE_REARSYNC;
        }
        else if (FlashController.WIRELESS.equals(baseId))
        {
            id = FLASH_MODE_WIRELESS;
        }
        else
        {
            Log.e(TAG, "Unknown BaseApp flashmode parameter name: " + baseId);
        }
        return id;
    }

    public static boolean set(String mode)
    {
        String baseId = getBaseIdStr(mode);
        if (null == baseId)
        {
            return false;
        }
        
        FlashController controller = FlashController.getInstance();
        controller.setValue(FlashController.FLASHMODE, baseId);
        
        return true;
    }
    
    public static String get()
    {
    	FlashController controller = FlashController.getInstance();
        String baseId = controller.getValue(FlashController.FLASHMODE);
        
        String mode = getIdStrFromBase(baseId);
        return mode;
    }
    
    public static String[] getAvailable()
    {
    	FlashController controller = FlashController.getInstance();
    	if (!controller.isAvailable(FlashController.FLASHMODE)) 
    	{
            Log.v(TAG, "getAvailable: Not Available");
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
    	}
    	
    	CameraSetting cameraSetting = CameraSetting.getInstance();
    	if (cameraSetting.getFlashExternalEnable() || cameraSetting.getFlashInternalEnable()) 
    	{
            List<String> availableList = controller.getAvailableValue(FlashController.FLASHMODE);
            List<String> ret = getIdStrFromBase(availableList);
            return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    	}
    	else
    	{
            Log.v(TAG, "getAvailable: Flash Not Enabled");
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
    	}
    	
    }
    
    public static String[] getSupportd()
    {
    	FlashController controller = FlashController.getInstance();
        List<String> supportedList = controller.getSupportedValue(FlashController.FLASHMODE);
        
        List<String> ret = getIdStrFromBase(supportedList);
        return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
}
