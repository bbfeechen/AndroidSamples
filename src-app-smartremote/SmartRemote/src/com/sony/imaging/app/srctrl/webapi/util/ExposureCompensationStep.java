package com.sony.imaging.app.srctrl.webapi.util;

import java.math.BigDecimal;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

import android.util.Log;

/**
 * Convert ExposureCompensation step between float value and the definition.
 * @author 0000138134
 *
 */
public class ExposureCompensationStep {
	private static final String TAG = ExposureCompensationStep.class.getSimpleName();
	public static enum EVStep {
		EV_UNKNOWN, EV_1_3, EV_1_2 
	}
	
	public static EVStep getStepIndex(float exposure){
		BigDecimal bigDecimal;
		bigDecimal = new BigDecimal(exposure);
		float scaledExposure = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
		if(Float.compare(scaledExposure, SRCtrlConstants.EXPOSURE_COMPENSATION_STEP_1_2) == 0){
			return EVStep.EV_1_2;
		} else if(Float.compare(scaledExposure, SRCtrlConstants.EXPOSURE_COMPENSATION_STEP_1_3) == 0){
			return EVStep.EV_1_3;
		} else {
			return EVStep.EV_UNKNOWN;
		}
	}
	
	public static float getStepFloat(int stepIndex){
		if(EVStep.EV_1_2.equals(EVStep.values()[stepIndex])){
			return SRCtrlConstants.EXPOSURE_COMPENSATION_STEP_1_2;
		} else if(EVStep.EV_1_3.equals(EVStep.values()[stepIndex])){
			return SRCtrlConstants.EXPOSURE_COMPENSATION_STEP_1_3;
		} else {
			return 0;
		}
	}
}
