package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.ExposureCompensationController;
import com.sony.imaging.app.fw.RunStatus;
import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.definition.StatusCode;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep.EVStep;
import com.sony.imaging.app.util.NotificationListener;


/**
 * Added by Kailiang Chen
 * @author 0000131654
 *
 * To improve Zoom performance by using cache value in ParamGenerator
 * when PMM calls getEvent
 * 
 * refer to AvailableManager
 */
public class CameraOperationExposureCompensation
{
    private static final String TAG = CameraOperationExposureCompensation.class.getSimpleName();
    
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
                { 
                	CameraNotificationManager.EXPOSURE_COMPENSATION   
               	};			
            }
            
            @Override
            public void onNotify(String tag)
            {
            	if (CameraNotificationManager.EXPOSURE_COMPENSATION.equals(tag)) 
            	{
					Integer current = get();
					if (null == current) {
					    current = ParamsGenerator.s_INVALID_EVCOMPASATION_VALUE;
					}
					int[] available = getAvailable();
					
					int maxAvailable = ParamsGenerator.s_INVALID_EVCOMPASATION_VALUE;
					int minAvailable = ParamsGenerator.s_INVALID_EVCOMPASATION_VALUE;
					int availableStep = ParamsGenerator.s_INVALID_EVCOMPASATION_VALUE;
					if (null != available && 3 == available.length)
					{
					    maxAvailable = available[0];
					    minAvailable = available[1];
					    availableStep = available[2];
					}
	                
					boolean toBeNotified = ParamsGenerator.updateExposureCompensationParams(current, maxAvailable, minAvailable, availableStep);  
	                if (toBeNotified) {
	                    ServerEventHandler.getInstance().onServerStatusChanged();
	                }
            	}
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    private static int[] s_Zero3IntArray = new int[]{0, 0, 0};
    public static int[] getAvailable()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.e(TAG, "Set invalid array to the available value due to the exposure mode " + exposureMode);
            return s_Zero3IntArray;
        }
        // return
        // SupportedValueCache.getInstance().getAvailableExposureCompensation();
        // // Deferred.
        List<String> available = null;
        
        if (RunStatus.getStatus() == RunStatus.RUNNING)
    	{
        	available =  ExposureCompensationController.getInstance().getAvailableValue(null);
    	}
        
        if (null != available && available.size() != 0)
        {
            int max = 0, min = 0;
            try
            {
                min = Integer.parseInt(available.get(0));
                max = Integer.parseInt(available.get(available.size() - 1));
            }
            catch (NumberFormatException e)
            {
                Log.e(TAG, "NumberFormatException in getSupportedExposureCompensation");
                return null;
            }
            // TODO this is not "AVAILABLE" "Step".
            Float stepObj = ExposureCompensationController.getInstance().getExposureCompensationStep();
            
            if(null == stepObj)
            {
                Log.e(TAG, "Couldn't obtain a well-formed index value of exposure compensation.");
                return null;
            }
            float step = stepObj.floatValue();
            EVStep stepIndex = ExposureCompensationStep.getStepIndex(step);
            Log.v(TAG, "Exposure step " + step + " is converted to index " + stepIndex.ordinal());
            int[] availableArray =
            { max, min, stepIndex.ordinal() };
            return availableArray;
        }
        else
        {
            return null;
        }
    }
    
    public static Integer get()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.v(TAG, "Set zero to the current value due to the exposure mode " + exposureMode);
            return 0;
        }

        Integer result = ExposureCompensationController.getInstance().getExposureCompensationIndex();
        
        if (null != result)
        {
            return result.intValue();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * refer to AvailableManager
     * the common way is to use the following methods in OperateReceiver
     */    
//  public static int set(int exposure) {}
//  public static int[][] getSupported() {}
}
