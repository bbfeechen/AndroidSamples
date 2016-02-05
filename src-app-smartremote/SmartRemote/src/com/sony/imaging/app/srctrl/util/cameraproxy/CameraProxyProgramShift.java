package com.sony.imaging.app.srctrl.util.cameraproxy;

import java.util.List;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;

public class CameraProxyProgramShift
{
    private static final int MAX_PROGRAM_SHIFT_STEP = 5;
    private static final int MIN_PROGRAM_SHIFT_STEP = -5;
    private static final int[] DEFAULT_SUPPORTED_RANGE =
            new int[] { MAX_PROGRAM_SHIFT_STEP, MIN_PROGRAM_SHIFT_STEP };
    
    private static String TAG = CameraProxyProgramShift.class.getSimpleName();
    
    public static boolean[] set(int step) throws IllegalArgumentException
    {
        if (step < MIN_PROGRAM_SHIFT_STEP || MAX_PROGRAM_SHIFT_STEP < step || 0 == step)
        {
            Log.v(TAG, "Invalid parameter was specified to Program Shift: " + step);
            return null;
        }
        List<Boolean> result = new OperationRequester<List<Boolean>>().request(HandoffOperationInfo.SET_CAMERA_SETTING,
                HandoffOperationInfo.CameraSettings.PROGRAM_SHIFT, step);
        if (null != result)
        {
            return OperationRequester.copy(result);
        }
        else
        {
            return null;
        }
    }
    
    public static int[] getSupported()
    {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode || 
                !CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode)
                ) {
            Log.v(TAG, "Set empty int array to the supported value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_INT_ARRAY;
        }

        return DEFAULT_SUPPORTED_RANGE;
    }
}
