package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.AELController;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.ISOSensitivityController;
import com.sony.imaging.app.base.shooting.widget.ISOSensitivityView;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;

public class CameraOperationIsoNumber
{
    private static final String TAG = CameraOperationIsoNumber.class.getSimpleName();
    
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
                { CameraNotificationManager.ISO_SENSITIVITY, CameraNotificationManager.ISO_SENSITIVITY_AUTO,
                        CameraNotificationManager.AE_LOCK };
            }
            
            @Override
            public void onNotify(String tag)
            {
                if ((tag.equals(CameraNotificationManager.ISO_SENSITIVITY))
                        || (tag.equals(CameraNotificationManager.ISO_SENSITIVITY_AUTO))
                        || (tag.equals(CameraNotificationManager.AE_LOCK)))
                {
                    String value = get();
                    String[] available = getAvailable();
                    
                    if(null != value)
                    {
                        boolean toBeNotified = ParamsGenerator.updateIsoSpeedRateParams(value,  available);
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
    
    public static String get()
    {
        ISOSensitivityController iso = ISOSensitivityController.getInstance();
        String value = iso.getValue();
        if (null == value) {       	// IMDLAPP6-1301
            Log.v(TAG, "ISO Sensitivity Info is null. ");
        	return null;
        }
        
        if (value.equals(ISOSensitivityController.ISO_AUTO)) {
            if (AELController.getInstance().isAELock()) {
                value = iso.getAutoValue(null);
                if (value.equals(ISOSensitivityController.ISO_AUTO)) {
                    value = ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING;
                }
            } else {
                value = ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING;
            }
        }
        
        return value;
    }
    
    public static boolean set(String isoNumber)
    {
        if(ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING.equals(isoNumber)) {
            isoNumber = "0";
        }
        
        ISOSensitivityController iso_controller = ISOSensitivityController.getInstance();
        iso_controller.setValue(null, isoNumber);

        return true;
    }
    
    public static String[] getAvailable()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        ISOSensitivityController iso_controller = ISOSensitivityController.getInstance();
        List<String> ret = iso_controller.getAvailableValue(null);
        if(null == ret)
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        String[] array = ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
        return changeAutoValueInArray(array);
    }
    
    public static String[] getSupportd()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        ISOSensitivityController iso_controller = ISOSensitivityController.getInstance();
        List<String> ret = iso_controller.getSupportedValue(null);
        if(null == ret)
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        String[] array = ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
        return changeAutoValueInArray(array);
    }
    
    private static String[] changeAutoValueInArray(String[] array)
    {
        for(int i = 0; i < array.length; i++)
        {
            if(array[i].equals("0"))
            {
                array[i] = ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING;
            }
        }
        return array;
    }
}
