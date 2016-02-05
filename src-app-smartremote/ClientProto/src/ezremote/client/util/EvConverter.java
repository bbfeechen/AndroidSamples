package ezremote.client.util;

import java.math.BigDecimal;
import java.util.ArrayList;

public class EvConverter {
	public static float[] convertToValueArray(int max, int min, int step){
		BigDecimal bigDecimal;
		Float stepFloat = ExposureCompensationStep.getStepFloat(step);
		ArrayList<Float> list = new ArrayList<Float>();
		for(int i = min; i < max+1; i++){
			bigDecimal = new BigDecimal((double)i * stepFloat);
			list.add(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
		}
		float[] array = new float [list.size()];
		int i = 0;
		for(float value: list){
			array[list.size() -1 - (i++)] = value;
		}
		return array;
	}
}
