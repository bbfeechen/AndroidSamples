package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.scalar.hardware.CameraEx.ApertureInfo;
import com.sony.scalar.hardware.CameraEx.LensInfo;

public class CameraOperationFNumber
{
    private static final String TAG = CameraOperationFNumber.class.getSimpleName();
    
    // Copied from ApertureView.java without "F" char.
    private static final float THRESHODL_BIG_OR_ONE = 10.0f;
    private static final String FORMAT_ONE_DIGIT = "%.1f"; //STRID_AMC_STR_06090 in Diadem
    private static final String FORMAT_BIG_DIGIT = "%.0f";
    private static final float INVALID_APERTURE_VALUE = 0.0f;
    private static final String INVALID_APERTURE_STRING = "--";
    
    private static String getFNumberString(float value) {
        String ret = INVALID_APERTURE_STRING;
        if(value == INVALID_APERTURE_VALUE) {
            // error
        }
        else if(value < THRESHODL_BIG_OR_ONE) {
            ret = String.format(FORMAT_ONE_DIGIT, value);
        }
        else {
            ret = String.format(FORMAT_BIG_DIGIT, value);
        }
        
        return ret;
    }
    
    public static boolean set(String fnumber)
    {
        boolean result = false;
        try
        {
            final float target = Float.parseFloat(fnumber);
            CameraSetting camera_setting = CameraSetting.getInstance();
            final float current = (float)camera_setting.getAperture() / 100;
            
            if (current == target)
            {
                // the same value - nothing to do
                result = true;
            }
            else
            {
                float[] F_TABLE = SRCtrlConstants.getFNumberTable();
                
                int targetIndex = Arrays.binarySearch(F_TABLE, target);
                int currentIndex = Arrays.binarySearch(F_TABLE, current);
                
                if (targetIndex < 0)
                {
                    Log.v(TAG, "[FNumber] Invalid parameters specified: " + fnumber);
                }
                else if (currentIndex < 0)
                {
                    Log.v(TAG, "[FNumber] Current F Number is invalid: " + current);
                }
                else
                {
                    int diffOfIndex = targetIndex - currentIndex;
                    Log.v(TAG, "[FNumber] Index diff between " + current + " and " + fnumber + " is " + diffOfIndex);
                    while (0 != diffOfIndex)
                    {
                        if (0 < diffOfIndex)
                        {
                            Log.v(TAG, "[FNumber] Incrementing FNumber...");
                            camera_setting.incrementAperture();
                            diffOfIndex--;
                        }
                        else
                        {
                            Log.v(TAG, "[FNumber] Decrementing FNumber...");
                            camera_setting.decrementAperture();
                            diffOfIndex++;
                        }
                        Thread.sleep(5); // magic sleep 5 msec
                    }
                    result = true;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String get()
    {
        CameraSetting setting = CameraSetting.getInstance();
        return getFNumberString((float)setting.getAperture()/100);
    }
    
    public static String[] getAvailable()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_SHUTTER.equals(exposureMode)
                ) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        CameraSetting cameraSetting = CameraSetting.getInstance();

        ApertureInfo fNumberInfo = cameraSetting.getApertureInfo();
        if (null == fNumberInfo) {		// IMDLAPP6-1301
            Log.v(TAG, "Aperture Info is null. ");
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        final float fnMIN = (float)fNumberInfo.currentAvailableMin/100;
        final float fnMAX = (float)fNumberInfo.currentAvailableMax/100;

        List<String> result = createFNumberList(fnMIN, fnMAX);
        
        return result.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
    
    public static String[] getSupportd()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_SHUTTER.equals(exposureMode)
                ) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        CameraSetting cameraSetting = CameraSetting.getInstance();
        LensInfo lensInfo = cameraSetting.getLensInfo();
        if(null == lensInfo)
        {
            Log.e(TAG, "Lens information is null.");
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        final float fnMIN = (float)Math.min(lensInfo.MinFValue.wide,lensInfo.MinFValue.tele)/10;
        final float fnMAX = (float)Math.max(lensInfo.MaxFValue.wide,lensInfo.MaxFValue.tele)/10;
        List<String> result = createFNumberList(fnMIN, fnMAX);
        
        return result.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
    
    private static List<String> createFNumberList(float min, float max)
    {
        List<String> result = new ArrayList<String>();
        float[] fNumberTable = SRCtrlConstants.getFNumberTable();
        boolean minValueFound = false;
        for(int i = 0; i < fNumberTable.length; i++) {
            float cur = fNumberTable[i];
            if(!minValueFound) {
                if(min <= cur) {
                    minValueFound = true;
                    result.add(getFNumberString(cur));
                }
            } else {
                if(max<cur) {
                    break; // end
                } else {
                    result.add(getFNumberString(cur));
                }
            }
        }
        return result;
    }
}
