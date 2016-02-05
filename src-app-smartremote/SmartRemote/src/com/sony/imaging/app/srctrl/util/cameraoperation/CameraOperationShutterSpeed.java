package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.util.Pair;

import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.srctrl.util.Fraction;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.scalar.hardware.CameraEx.ShutterSpeedInfo;

public class CameraOperationShutterSpeed
{
    private static final String TAG = CameraOperationShutterSpeed.class.getSimpleName();
    
    private static final int[][] SS_TABLE = SRCtrlConstants.getShutterSpeedTable();
    private static final Fraction[] SS_TABLE_FRACTION = new Fraction[SS_TABLE.length];
    static {
        for (int i = 0; i < SS_TABLE_FRACTION.length; i++)
        {
            SS_TABLE_FRACTION[i] = new Fraction(SS_TABLE[i][0], SS_TABLE[i][1]);
        }
    }
    

    public static boolean set(String speed)
    {
        boolean result = false;
        try
        {
            if("0.6\"".equals(speed)) {
                speed = "0.625\""; // 10/16
            }
            Fraction target = new Fraction(speed);
            CameraSetting camera_setting = CameraSetting.getInstance();
            Pair<Integer, Integer> ss = camera_setting.getShutterSpeed();
            Fraction current = new Fraction(ss);
            
            if (0 == current.compare(target))
            {
                // the same value - nothing to do
                result = true;
            }
            else
            {
                int targetIndex = Arrays.binarySearch(SS_TABLE_FRACTION, target, Fraction.COMPARATOR);
                int currentIndex = Arrays.binarySearch(SS_TABLE_FRACTION, current, Fraction.COMPARATOR);
                
                if (targetIndex < 0)
                {
                    Log.v(TAG, "[ShutterSpeed] Invalid parameters specified: " + target);
                }
                else if (currentIndex < 0)
                {
                    Log.v(TAG, "[ShutterSpeed] Current Shutter Speed is invalid: " + current);
                }
                else
                {
                    int diffOfIndex = targetIndex - currentIndex;
                    Log.v(TAG, "[ShutterSpeed] Index diff between " + current + " and " + target + " is " + diffOfIndex);
                    while (0 != diffOfIndex)
                    {
                        if (0 < diffOfIndex)
                        {
                            camera_setting.decrementShutterSpeed();
                            diffOfIndex--;
                        }
                        else
                        {
                            camera_setting.incrementShutterSpeed();
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
        CameraSetting cameraSetting = CameraSetting.getInstance();
        Pair<Integer, Integer> ss = cameraSetting.getShutterSpeed();
        if (null == ss) {	// IMDLAPP6-1301
            Log.v(TAG, "ShutterSpeed is null. ");
        	return null;
        }
        
        return getShutterSpeedStr(ss.first, ss.second);
    }
    
    public static String[] getAvailable()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_APERATURE.equals(exposureMode)
                ) {
            Log.v(TAG, "Set empty string array to the available value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        List<String> result = new ArrayList<String>();
        CameraSetting cameraSetting = CameraSetting.getInstance();
        
        ShutterSpeedInfo shutterSpeedInfo = cameraSetting.getShutterSpeedInfo();
        if (null == shutterSpeedInfo) {	// IMDLAPP6-1301
            Log.v(TAG, "ShutterSpeed Info is null. ");
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }
        
        final Fraction ssMIN = new Fraction(shutterSpeedInfo.currentAvailableMin_n, shutterSpeedInfo.currentAvailableMin_d);
        final Fraction ssMAX = new Fraction(shutterSpeedInfo.currentAvailableMax_n, shutterSpeedInfo.currentAvailableMax_d);

        int[][] shutterSpeedTable = SRCtrlConstants.getShutterSpeedTable();
        boolean maxValueFound = false;
        for(int i = 0; i < shutterSpeedTable.length; i++) {
            Integer ssNumerator = shutterSpeedTable[i][0];
            Integer ssDenominator = shutterSpeedTable[i][1];
            Fraction ssCUR = new Fraction(ssNumerator, ssDenominator);
            if(!maxValueFound) {
                if(0<=ssCUR.compare(ssMAX)) {
                    maxValueFound = true;
                    result.add(0, getShutterSpeedStr(ssNumerator, ssDenominator) );
                }
            } else {
                if(0<ssCUR.compare(ssMIN)) {
                    break; // end
                } else {
                    result.add(0, getShutterSpeedStr(ssNumerator, ssDenominator) );
                }
            }
        }
        
        return result.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
    
    public static String[] getSupportd()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if(null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode) ||
                CameraOperationExposureMode.EXPOSURE_MODE_APERATURE.equals(exposureMode)
                ) {
            Log.v(TAG, "Set empty string array to the supported value due to the exposure mode " + exposureMode);
            return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
        }

        return getAvailable(); // TODO temporary implementation
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Coppied from ShuterSpeedView
    private static final float THRESHODL_ONE_OR_SMALL = 0.4f;
    private static final float THRESHODL_BIG_OR_ONE = 10.0f;
    private static final String FORMAT_ONE_DIGIT = "%.1f\"";    //STRID_AMC_STR_06814
    private static final String FORMAT_BIG_DIGIT = "%.0f\"";    //There is no strid in diadem.
    private static String FORMAT_SMALL_DIGIT = "%s/%s";
    private static String getShutterSpeedStr(int numerator, int denominator)
    {
        String displayValue = null;
        float value=0;
        // QEMU is ss.first = 0 , ss.second = 0 !
        if(denominator != 0){
            value = numerator/(float)denominator;
            BigDecimal bi = new BigDecimal(String.valueOf(value));
            value = bi.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
        }

        if (value < THRESHODL_ONE_OR_SMALL) {
            displayValue = String.format(FORMAT_SMALL_DIGIT, numerator, denominator);
        }
        else if ((value < THRESHODL_BIG_OR_ONE) && (denominator != 1.0f)) {
            displayValue = String.format(FORMAT_ONE_DIGIT, value);
        }
        else {
            displayValue = String.format(FORMAT_BIG_DIGIT, value);
        }
        return displayValue;
    }
}
