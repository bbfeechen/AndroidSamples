package com.sony.imaging.app.srctrl.webapi.util;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Convert supported EV from {max, min, step} to array of values.
 * @author 0000138134
 *
 */
public class EvConverter {
	public static float[] convertToValueArray(int max, int min, int step){
		BigDecimal bigDecimal;
		Float stepFloat = ExposureCompensationStep.getStepFloat(step);
		ArrayList<Float> list = new ArrayList<Float>();
		for(int i = min; i < max+1; i++){
			bigDecimal = new BigDecimal((double)i * stepFloat);
			list.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}
		return null;
	}
}
