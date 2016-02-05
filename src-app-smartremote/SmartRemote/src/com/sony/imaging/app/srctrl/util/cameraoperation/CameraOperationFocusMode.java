package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.FocusModeController;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

public class CameraOperationFocusMode
{
    private static final String TAG = CameraOperationFocusMode.class.getSimpleName();
    
    private static final String AF_SELECT_AFA = "AF-A";
    public static final String AF_SELECT_AFC = "AF-C";
    public static final String AF_SELECT_AFS = "AF-S";
    public static final String FOCUS_MODE_MF = "MF";
    public static final String FOCUS_MODE_DMF = "DMF";

    public static String getBaseIdStr(String param)
    {
        String baseId = null;
        if (FOCUS_MODE_DMF.equals(param))
        {
            baseId = FocusModeController.DMF;
        }
        else if (AF_SELECT_AFC.equals(param))
        {
            baseId = FocusModeController.AF_C;
        }
        else if (AF_SELECT_AFS.equals(param))
        {
            baseId = FocusModeController.AF_S;
        }
        else if (FOCUS_MODE_MF.equals(param))
        {
            baseId = FocusModeController.MANUAL;
        }
//tama
/*
        else if (AF_SELECT_AFA.equals(param))
        {
            baseId = FocusModeController.AF_A;
        }
*/
        else
        {
            Log.e(TAG, "Unknown focus mode parameter name: " + param);
        }
        return baseId;
    }
    
    public static String getIdFromBase(String baseId)
    {
        String id = null;
        if (FocusModeController.DMF.equals(baseId))
        {
            id = FOCUS_MODE_DMF;
        }
        else if (FocusModeController.AF_C.equals(baseId))
        {
            id = AF_SELECT_AFC;
        }
        else if (FocusModeController.AF_S.equals(baseId))
        {
            id = AF_SELECT_AFS;
        }
        else if (FocusModeController.MANUAL.equals(baseId))
        {
            id = FOCUS_MODE_MF;
        }
//tama
/*
        else if (FocusModeController.AF_A.equals(baseId))
        {
            id = AF_SELECT_AFA;
        }
*/
        else
        {
            Log.e(TAG, "Unknown BaseApp focus mode parameter name: " + baseId);
        }
        return id;
    }
    
    public static String[] getIdFromBase(List<String> baseIdList)
    {
        if(null == baseIdList)
        {
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        ArrayList<String> ret = new ArrayList<String>();
        for(String focusModeInBase : baseIdList)
        {
            String mode = getIdFromBase(focusModeInBase);
            if(null != mode)
            {
                ret.add(mode);
            }
        }
        return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
}
