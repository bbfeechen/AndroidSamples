package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;

public class CameraOperationProgramShift
{
    private static final String TAG = CameraOperationProgramShift.class.getSimpleName();
    
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
                { CameraNotificationManager.PROGRAM_LINE_CHANGE };
            }
            
            @Override
            public void onNotify(String tag)
            {
                if (tag.equals(CameraNotificationManager.PROGRAM_LINE_CHANGE))
                {
                    boolean adjusted = isAdjusted();
                    boolean toBeNotified = ParamsGenerator.updateProgramShiftParams(adjusted);
                    if (toBeNotified)
                    {
                        ServerEventHandler.getInstance().onServerStatusChanged();
                    }
                }
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    public static boolean isAdjusted()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(!CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode)){
            Log.v(TAG, "Set FALSE to the adjusted value due to the exposure mode " + exposureMode);
            return false;
        }

        return CameraSetting.getInstance().isProgramLineAdjusted();
    }
    
    public static List<Boolean> set(int step)
    {
        List<Boolean> result = new ArrayList<Boolean>();
        boolean ret = false;
        try
        {
            CameraSetting camera_setting = CameraSetting.getInstance();
            if (0 < step)
            {
                for (int i = 0; i < step; i++)
                {
                    camera_setting.incrementProgramLine();
                    Thread.sleep(5); // magic sleep 5 msec
                }
            }
            else if (step < 0)
            {
                int r_step = step * -1;
                for (int i = 0; i < r_step; i++)
                {
                    camera_setting.decrementProgramLine();
                    Thread.sleep(5); // magic sleep 5 msec
                }
            }
            ret = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        result.add(ret);
        result.add(isAdjusted());
        return result;
    }
}
