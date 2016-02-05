package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.FocusAreaController;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

public class CameraOperationFocusArea
{
    private static final String TAG = CameraOperationFocusArea.class.getSimpleName();
    
    //public static final String FOCUS_AREA_MULTI = FocusAreaController.MULTI;
    //public static final String FOCUS_AREA_SEMI_MULTI = FocusAreaController.SEMI_MULTI;
    public static final String FOCUS_AREA_FLEX = FocusAreaController.FLEX;
    //public static final String FOCUS_AREA_CENTER_WEIGHTED = FocusAreaController.CENTER_WEIGHTED;
    //public static final String FOCUS_AREA_WIDE = FocusAreaController.WIDE;
    //public static final String FOCUS_AREA_SEMI_WIDE = FocusAreaController.SEMI_WIDE;
    //public static final String FOCUS_AREA_LOCAL = FocusAreaController.LOCAL;
    //public static final String FOCUS_AREA_FIX_CENTER = FocusAreaController.FIX_CENTER;

    public static String getBaseIdStr(String param)
    {
        String baseId = null;
        if (FocusAreaController.FLEX.equals(param))
        {
            baseId = FocusAreaController.FLEX;
        }
        else
        {
            Log.e(TAG, "Unknown focus area parameter name: " + param);
        }
        return baseId;
    }
    
    public static String getIdFromBase(String baseId)
    {
        String id = null;
        if (FocusAreaController.FLEX.equals(baseId))
        {
            id = FOCUS_AREA_FLEX;
        }
        else
        {
            Log.e(TAG, "Unknown BaseApp focus area parameter name: " + baseId);
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
        for(String focusAreaInBase : baseIdList)
        {
            String mode = getIdFromBase(focusAreaInBase);
            if(null != mode)
            {
                ret.add(mode);
            }
        }
        return ret.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
}
