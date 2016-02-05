package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;

public class CameraOperationExposureMode
{
    private static final String TAG = CameraOperationExposureMode.class.getSimpleName();
    
    public static final String EXPOSURE_MODE_PROGRAM_AUTO = "Program Auto";
    public static final String EXPOSURE_MODE_APERATURE = "Aperture";
    public static final String EXPOSURE_MODE_SHUTTER = "Shutter";
    public static final String EXPOSURE_MODE_MANUAL = "Manual";
    public static final String EXPOSURE_MODE_INTELLIGENT_AUTO = "Intelligent Auto";
    
    private static String getBaseIdStr(String param)
    {
        if (param.equals(EXPOSURE_MODE_PROGRAM_AUTO))
        {
            return ExposureModeControllerEx.PROGRAM_AUTO_MODE;
        }
        else if (param.equals(EXPOSURE_MODE_APERATURE))
        {
            return ExposureModeControllerEx.APERATURE_MODE;
        }
        else if (param.equals(EXPOSURE_MODE_SHUTTER))
        {
            return ExposureModeControllerEx.SHUTTER_MODE;
        }
        else if (param.equals(EXPOSURE_MODE_MANUAL))
        {
            return ExposureModeControllerEx.MANUAL_MODE;
        }
        else if (param.equals(EXPOSURE_MODE_INTELLIGENT_AUTO))
        {
            return ExposureModeControllerEx.INTELLIGENT_AUTO_MODE;
        }
        else
        {
            Log.e(TAG, "Unknown parameter name: " + param);
            return null;
        }
    }
    
    private static List<String> getIdStrFromBase(List<String> baseIdList)
    {
        List<String> ret = new ArrayList<String>();
        for (String baseId : baseIdList)
        {
            String mode = getIdStrFromBase(baseId);
            if (null != mode)
            {
                if(!ExposureModeControllerEx.SCENE_SELECTION_MODE.equals(mode)) {
                        ret.add(mode);
                }
            }
        }
        return ret;
    }
    
    private static String getIdStrFromBase(String baseId)
    {
        if (baseId.equals(ExposureModeControllerEx.PROGRAM_AUTO_MODE))
        {
            return EXPOSURE_MODE_PROGRAM_AUTO;
        }
        else if (baseId.equals(ExposureModeControllerEx.APERATURE_MODE))
        {
            return EXPOSURE_MODE_APERATURE;
        }
        else if (baseId.equals(ExposureModeControllerEx.SHUTTER_MODE))
        {
            return EXPOSURE_MODE_SHUTTER;
        }
        else if (baseId.equals(ExposureModeControllerEx.MANUAL_MODE))
        {
            return EXPOSURE_MODE_MANUAL;
        }
        else if (baseId.equals(ExposureModeControllerEx.INTELLIGENT_AUTO_MODE))
        {
            return EXPOSURE_MODE_INTELLIGENT_AUTO;
        }
        else
        {
            Log.e(TAG, "Unknown parameter name: " + baseId);
            return null;
        }
    }
    
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
                {               CameraNotificationManager.SCENE_MODE };
            }
            
            @Override
            public void onNotify(String tag)
            {
                ExposureModeController emc = ExposureModeController.getInstance();
                String modeInBase = emc.getValue(ExposureModeController.EXPOSURE_MODE);
                String mode = getIdStrFromBase(modeInBase);
                String[] candidatesList = getAvailable();
                if(null != mode)
                {
                    boolean toBeNotified = ParamsGenerator.updateExposureModeParams(mode, candidatesList);
                    if (toBeNotified)
                    {
                        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.FOCUS_AREA_INFO); // to detect flex spot availability
                        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.EXPOSURE_COMPENSATION);
                        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.SHUTTER_SPEED);		// IMDLAPP6-1301
                        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.APERTURE);			// IMDLAPP6-1301
                        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.ISO_SENSITIVITY);	// IMDLAPP6-1301
                        ServerEventHandler.getInstance().onServerStatusChanged();
                    }
                } 
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }

    public static boolean set(String mode)
    {
        if(ModeDialDetector.hasModeDial())
        {
            return false;
        }

        String baseId = getBaseIdStr(mode);
        if (null == baseId)
        {
            return false;
        }
        
        ExposureModeControllerEx controller = (ExposureModeControllerEx) ExposureModeControllerEx.getInstance();
        controller.setValue(ExposureModeControllerEx.EXPOSURE_MODE, baseId);
        
        String check = controller.getValue(ExposureModeControllerEx.EXPOSURE_MODE);
        if (baseId.equals(check))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static String get()
    {
        ExposureModeControllerEx controller = (ExposureModeControllerEx) ExposureModeControllerEx.getInstance();
        String baseId = controller.getValue(ExposureModeControllerEx.EXPOSURE_MODE);
        
        String mode = getIdStrFromBase(baseId);
        return mode;
    }
    
    public static String[] getAvailable()
    {
        if(ModeDialDetector.hasModeDial())
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        ExposureModeControllerEx controller = (ExposureModeControllerEx) ExposureModeControllerEx.getInstance();
        List<String> availableList = controller.getAvailableValue(ExposureModeControllerEx.EXPOSURE_MODE);
        
        List<String> ret = getIdStrFromBase(availableList);
        return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
    
    public static String[] getSupportd()
    {
        if(ModeDialDetector.hasModeDial())
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        ExposureModeControllerEx controller = (ExposureModeControllerEx) ExposureModeControllerEx.getInstance();
        List<String> supportedList = controller.getSupportedValue(ExposureModeControllerEx.EXPOSURE_MODE);
        
        List<String> ret = getIdStrFromBase(supportedList);
        return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
}
