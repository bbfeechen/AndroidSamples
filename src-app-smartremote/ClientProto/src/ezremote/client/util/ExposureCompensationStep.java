package ezremote.client.util;

import java.math.BigDecimal;

import android.util.Log;

public class ExposureCompensationStep {
	private static final String TAG = ExposureCompensationStep.class.getSimpleName();
	public static enum EVStep {
		EV_UNKNOWN, EV_1_3, EV_1_2 
	}
	
	public static EVStep getStepIndex(float exposure){
		BigDecimal bigDecimal;
		bigDecimal = new BigDecimal(exposure);
		float scaledExposure = bigDecimal.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
		Log.i(TAG, Float.toString(scaledExposure));
		if(Float.compare(scaledExposure, 0.50f) == 0){
			return EVStep.EV_1_2;
		} else if(Float.compare(scaledExposure, 0.333f) == 0){
			return EVStep.EV_1_3;
		} else {
			return EVStep.EV_UNKNOWN;
		}
	}
	
	public static float getStepFloat(int stepIndex){
		if(EVStep.EV_1_2.equals(EVStep.values()[stepIndex])){
			return 0.50f;
		} else if(EVStep.EV_1_3.equals(EVStep.values()[stepIndex])){
			return 0.333f;
		} else {
			return 0;
		}
	}
}
