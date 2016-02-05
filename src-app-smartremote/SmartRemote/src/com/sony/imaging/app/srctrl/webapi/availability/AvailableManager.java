package com.sony.imaging.app.srctrl.webapi.availability;

import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.webapi.definition.StatusCode;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep.EVStep;

/**
 * 
 * getValue from XxxController in BaseShooting from outside Main thread.
 * 
 * @author 0000138134
 * 
 */
public class AvailableManager
{
    private static String TAG = AvailableManager.class.getSimpleName();
    
    public static boolean isAvailableSelfTimer()
    {
        Boolean result = new OperationRequester<Boolean>().request(
                HandoffOperationInfo.IS_AVAILABLE_SPECIFIC_DRIVE_MODE, DriveModeController.SELF_TIMER);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isAvailableSingle()
    {
        Boolean result = new OperationRequester<Boolean>().request(
                HandoffOperationInfo.IS_AVAILABLE_SPECIFIC_DRIVE_MODE, DriveModeController.SINGLE);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isAvailableBurst()
    {
        Boolean result = new OperationRequester<Boolean>().request(
                HandoffOperationInfo.IS_AVAILABLE_SPECIFIC_DRIVE_MODE, DriveModeController.BURST);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isAvailableExposureCompensation()
    {
        Boolean result = new OperationRequester<Boolean>().request(
                HandoffOperationInfo.IS_AVAILABLE_EXPOSURE_COMPENSATION, (Object) null);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isCameraSettingAvailable(HandoffOperationInfo.CameraSettings settingName)
    {
        List<Object> result = getCameraSettingAvailable(settingName);
        if (null != result && 0 != result.size())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static Object getCameraSetting(HandoffOperationInfo.CameraSettings settingName)
    {
        return new OperationRequester<String>().request(HandoffOperationInfo.GET_CAMERA_SETTING, settingName);
    }
    
    public static List<Object> getCameraSettingAvailable(HandoffOperationInfo.CameraSettings settingName)
    {
        return new OperationRequester<List<Object>>().request(HandoffOperationInfo.GET_CAMERA_SETTING_AVAILABLE,
                settingName);
    }
    
    public static List<Object> getCameraSettingSupported(HandoffOperationInfo.CameraSettings settingName)
    {
        return new OperationRequester<List<Object>>().request(HandoffOperationInfo.GET_CAMERA_SETTING_SUPPORTED,
                settingName);
    }
    
    public static boolean setCameraSetting(HandoffOperationInfo.CameraSettings settingName, Object[] settings)
    {
        Boolean result = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                settingName, settings);
        if (null != result)
        {
            return result.booleanValue();
        }
        else
        {
            return false;
        }
    }
    
    public static int setExposureCompensation(int exposure)
    {
        List<String> available = new OperationRequester<List<String>>().request(
                HandoffOperationInfo.GET_AVAILABLE_EXPOSURE_COMPENSATION, (Object) null);
        String exposureString = Integer.toString(exposure);
        if (null != available &&available.contains(exposureString))
        {
            try
            {
                Boolean booleanObj = new OperationRequester<Boolean>().request(HandoffOperationInfo.SET_EXPOSURE_COMPENSATION, exposureString); 
                if (null != booleanObj && booleanObj.booleanValue())
                {
                    return StatusCode.OK.toInt();
                }
                else
                {
                    return StatusCode.ANY.toInt();
                }
            }
            catch (IllegalArgumentException e)
            {
                Log.e(TAG, "IllegalArgumentException in setExposureCompensation");
                return StatusCode.ILLEGAL_ARGUMENT.toInt();
            }
        }
        else
        {
            Log.e(TAG, "EV:" + exposure + " is not available now");
            return StatusCode.ILLEGAL_ARGUMENT.toInt();
        }
    }
    
    private static int[] s_Zero3IntArray = new int[]{0, 0, 0};
    public static int[] getExopsureCompensationAvailable()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.e(TAG, "Set invalid array to the available value due to the exposure mode " + exposureMode);
            return s_Zero3IntArray;
        }
        // return
        // SupportedValueCache.getInstance().getAvailableExposureCompensation();
        // // Deferred.
        List<String> available = new OperationRequester<List<String>>().request(
                HandoffOperationInfo.GET_AVAILABLE_EXPOSURE_COMPENSATION, (Object) null);
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
            Float stepObj = new OperationRequester<Float>().request(HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_STEP,
                    (Object) null);
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
    
    private static int[][] s_Zero3x1IntArray = new int[][]{{0}, {0}, {0}}; 
    public static int[][] getExopsureCompensationSupported()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.e(TAG, "Set invalid array to the supported value due to the exposure mode " + exposureMode);
            return s_Zero3x1IntArray;
        }

        // return
        // SupportedValueCache.getInstance().getSupportedExposureCompensation();
        // // Deferred.
        int[][] supportedExposureCompensation;
        List<String> supported = new OperationRequester<List<String>>().request(
                HandoffOperationInfo.GET_SUPPORTED_EXPOSURE_COMPENSATION, (Object) null);
        if (null != supported && supported.size() != 0)
        {
            int[] max =
            { 0 }, min =
            { 0 };
            float[] step =
            { 0.0f };
            try
            {
                min[0] = Integer.parseInt(supported.get(0));
                max[0] = Integer.parseInt(supported.get(supported.size() - 1));
            }
            catch (NumberFormatException e)
            {
                Log.e(TAG, "NumberFormatException in getSupportedExposureCompensation");
            }
            Float floatObj = new OperationRequester<Float>().request(HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_STEP, (Object) null);
            if(null != floatObj) {
                step[0] = floatObj.floatValue();
            } else {
                return null;
            }
            EVStep[] stepIndex =
            { ExposureCompensationStep.getStepIndex(step[0]) };
            int[] stepIndexOrdinal =
            { stepIndex[0].ordinal() };
            supportedExposureCompensation = new int[3][];
            supportedExposureCompensation[0] = max;
            supportedExposureCompensation[1] = min;
            supportedExposureCompensation[2] = stepIndexOrdinal;
            return supportedExposureCompensation;
        }
        else
        {
            return null;
        }
    }
    
    public static Integer getExposureCompensationCurrent()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode)) {
            Log.v(TAG, "Set zero to the current value due to the exposure mode " + exposureMode);
            return 0;
        }

        Integer result = new OperationRequester<Integer>().request(
                HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_INDEX, (Object) null);
        if (null != result)
        {
            return result.intValue();
        }
        else
        {
            return null;
        }
    }
}
