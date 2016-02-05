package com.sony.imaging.app.srctrl.util.cameraproxy;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.webapi.availability.AvailableManager;
import com.sony.imaging.app.srctrl.webapi.definition.StatusCode;

public class CameraProxySelftimer
{
    private static String TAG = CameraProxySelftimer.class.getSimpleName();
    
    public static int setSelfTimer(int timer)
    {
        switch (timer)
        {
        case 0:
            if (AvailableManager.isAvailableSingle())
            {
                new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_DRIVE_MODE,
                        DriveModeController.SINGLE);
                return 0;
            }
            else if (AvailableManager.isAvailableBurst())
            {
                new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_DRIVE_MODE,
                        DriveModeController.BURST);
                return 0;
            }
            else
            {
                Log.e(TAG, "setDriveMode SINGLE is not Available now.");
                return StatusCode.ILLEGAL_ARGUMENT.toInt();
            }
        case 2:
            if (AvailableManager.isAvailableSelfTimer())
            {
                new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_SELF_TIMER,
                        DriveModeController.SELF_TIMER_2S);
                return 0;
            }
            else
            {
                Log.e(TAG, "setDriveMode SELF_TIMER is not Available now.");
                return StatusCode.ILLEGAL_ARGUMENT.toInt();
            }
            /*
             * case 10: if(isAvailableSelfTimer()){
             * StateController.getInstance().getRequester()
             * .requestBoolean(HandoffOperationInfo.SET_SELF_TIMER,
             * DriveModeController.SELF_TIMER_10S); return 0; } else {
             * Log.e(TAG, "setDriveMode SELF_TIMER is not Available now.");
             * return StatusCode.ILLEGAL_ARGUMENT.toInt(); }
             */
        default:
            Log.e(TAG, "TimerValue:" + timer + " is not supported.");
            return StatusCode.ILLEGAL_ARGUMENT.toInt();
        }
    }
    
    public static Integer getSelftimerCurrent()
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.IS_SELF_TIMER, (Object) null);
        if (null != result)
        {
            if (result.booleanValue())
            {
                return CameraSetting.getInstance().getParameters().second.getSelfTimer();
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return null;
        }
    }
    
    public static int[] getSelftimerSupported()
    {
        // return SupportedValueCache.getInstance().getSupportedSelfTimer(); //
        // Deferred.
        int[] supportedSelfTimer;
        ArrayList<Integer> supported = new ArrayList<Integer>();
        boolean isTimerSupported = false;
        
        List<String> dModeList = new OperationRequester<List<String>>().request(
                HandoffOperationInfo.GET_SUPPORTED_DRIVE_MODE, (Object) null);
        if (null != dModeList)
        {
            for (String str : dModeList)
            {
                if (DriveModeController.SINGLE.equals(str))
                {
                    supported.add(0);
                }
                else if (DriveModeController.SELF_TIMER.equals(str))
                {
                    isTimerSupported = true;
                }
            }
        }
        if (isTimerSupported)
        {
            List<String> timerList = new OperationRequester<List<String>>().request(
                    HandoffOperationInfo.GET_SUPPORTED_SELF_TIMER, (Object) null);
            if (null != timerList)
            {
                for (String str : timerList)
                {
                    if (DriveModeController.SELF_TIMER_2S.equals(str))
                    {
                        supported.add(2);
                    }
                }
            }
        }
        supportedSelfTimer = new int[supported.size()];
        int i = 0;
        for (Integer value : supported)
        {
            supportedSelfTimer[i++] = value;
        }
        return supportedSelfTimer;
    }
    
    public static int[] getSelftimerAvailable()
    {
        // return SupportedValueCache.getInstance().getAvailableSelfTimer(); //
        // Deferred.
        ArrayList<Integer> available = new ArrayList<Integer>();
        boolean isSingleAvailable = true;
        String tmpMode = new OperationRequester<String>()
                .request(HandoffOperationInfo.GET_EXPOSURE_MODE, (Object) null);
        if (null != tmpMode && ExposureModeControllerEx.SCENE_SELECTION_MODE.equals(tmpMode))
        {
            tmpMode = new OperationRequester<String>().request(HandoffOperationInfo.GET_SELECTED_SCENE, (Object) null);
            if (ExposureModeControllerEx.SPORTS.equals(tmpMode))
            {
                isSingleAvailable = false;
            }
        }
        boolean isTimerAvailable = false;
        List<String> list = new OperationRequester<List<String>>().request(
                HandoffOperationInfo.GET_AVAILABLE_DRIVE_MODE, (Object) null);
        if (isSingleAvailable)
        {
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.SINGLE.equals(str))
                    {
                        available.add(0);
                    }
                    else if (DriveModeController.SELF_TIMER.equals(str))
                    {
                        isTimerAvailable = true;
                    }
                }
            }
        }
        else
        {
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.BURST.equals(str))
                    {
                        available.add(0);
                    }
                    else if (DriveModeController.SELF_TIMER.equals(str))
                    {
                        isTimerAvailable = true;
                    }
                }
            }
        }
        if (isTimerAvailable)
        {
            list = new OperationRequester<List<String>>().request(HandoffOperationInfo.GET_AVAILABLE_SELF_TIMER,
                    (Object) null);
            if (null != list)
            {
                for (String str : list)
                {
                    if (DriveModeController.SELF_TIMER_2S.equals(str))
                    {
                        available.add(2);
                    }
                }
            }
        }
        int[] availableArray = new int[available.size()];
        int i = 0;
        for (Integer value : available)
        {
            availableArray[i++] = value;
        }
        return availableArray;
    }
}
