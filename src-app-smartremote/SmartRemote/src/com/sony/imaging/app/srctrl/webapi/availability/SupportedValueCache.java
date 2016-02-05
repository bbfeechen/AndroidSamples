package com.sony.imaging.app.srctrl.webapi.availability;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep;
import com.sony.imaging.app.srctrl.webapi.util.ExposureCompensationStep.EVStep;

/**
 * Caches supported value for better performance.
 * @author 0000138134
 *
 */
public class SupportedValueCache {
	private static final String TAG = SupportedValueCache.class.getSimpleName();

	private static SupportedValueCache cache = new SupportedValueCache();
	
	public static SupportedValueCache getInstance(){
		return cache;
	}
	
	private int[][] supportedExposureCompensation;
	private int[] supportedSelfTimer;
	
	public SupportedValueCache(){
		supportedExposureCompensation = null;
		supportedSelfTimer = null;
	}
	
	/**
	 * Returns supported ExposureCompensation values.
	 * @return
	 */
	public int[][] getSupportedExposureCompensation(){
		if(supportedExposureCompensation == null){
			List<String> supported = new OperationRequester<List<String>>()
					.request(HandoffOperationInfo.GET_SUPPORTED_EXPOSURE_COMPENSATION, (Object)null);
	    	if(null != supported && supported.size() != 0){
		    	int[] max = {0}, min = {0};
		    	float[] step = {0.0f};
	    		try{
		    		min[0] = Integer.parseInt(supported.get(0));
		    		max[0] = Integer.parseInt(supported.get(supported.size()-1));
	    		} catch (NumberFormatException e){
	    			Log.e(TAG, "NumberFormatException in getSupportedExposureCompensation");
	    		}
	    		Float step_result = new OperationRequester<Float>()
	    				.request(HandoffOperationInfo.GET_EXPOSURE_COMPENSATION_STEP, (Object)null);
	    		if(null != step_result) {
	    		    step[0] = step_result.floatValue();
	    		    EVStep[] stepIndex = {ExposureCompensationStep.getStepIndex(step[0])};
	    		    int[] stepIndexOrdinal = {stepIndex[0].ordinal()};
	    		    supportedExposureCompensation = new int[3][];
	    		    supportedExposureCompensation[0] = max;
	    		    supportedExposureCompensation[1] = min;
	    		    supportedExposureCompensation[2] = stepIndexOrdinal;
	    		} else {
                    supportedExposureCompensation = null;
	    		}
	    	} else {
                supportedExposureCompensation = null;
	    	}
		}
		return supportedExposureCompensation;
	}
	
	/**
	 * Returns available ExposureCompensation values.
	 * @return
	 */
	public int[] getAvailableExposureCompensation(){
		//TODO Temporary implementation for performance. if EV range varies by some mode, this is not correct.
	    Boolean result = new OperationRequester<Boolean>()
                .request(HandoffOperationInfo.IS_AVAILABLE_EXPOSURE_COMPENSATION, (Object)null); 
		if(null != result && result.booleanValue()){
			if(supportedExposureCompensation == null){
				getSupportedExposureCompensation();
			}
			if(null == supportedExposureCompensation) {
			    return null;
			}
			
			int[] array = new int[3];
			for(int i=0; i<3; i++){
				// TODO Temporary implementation. if EV step changes, this is not correct.
				array[i] = supportedExposureCompensation[i][0];
			}
			return array;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns supported SelfTimer values.
	 * @return
	 */
	public int[] getSupportedSelfTimer(){
		if(supportedSelfTimer == null){
			ArrayList<Integer> supported = new ArrayList<Integer>();
	    	boolean isTimerSupported = false;
	    	
			List<String> dModeList = new OperationRequester<List<String>>()
					.request(HandoffOperationInfo.GET_SUPPORTED_DRIVE_MODE, (Object)null);
    		
			if(null != dModeList) {
	    	for(String str: dModeList){
	    		if(DriveModeController.SINGLE.equals(str)){
	    			supported.add(0);
	    		} else if(DriveModeController.SELF_TIMER.equals(str)){
	    			isTimerSupported = true;
	    		}
	    	}
			}
	    	if(isTimerSupported){
				List<String> timerList = new OperationRequester<List<String>>()
						.request(HandoffOperationInfo.GET_SUPPORTED_SELF_TIMER, (Object)null);
				if(null != timerList) {
		    	for(String str: timerList){
		    		if(DriveModeController.SELF_TIMER_2S.equals(str)){
		    			supported.add(2);
		    		}
		    	}
				}
	    	}
	    	supportedSelfTimer = new int[supported.size()];
	    	int i = 0;
	    	for(Integer value : supported){
	    		supportedSelfTimer[i++] = value;
	    	}
		}
		return supportedSelfTimer;
	}
	
	/**
	 * Returns available SelfTimer values.
	 * @return
	 */
	public int[] getAvailableSelfTimer(){
		//TODO Temporary implementation for performance. if EV range varies by some mode, this is not correct.
	    Boolean result = new OperationRequester<Boolean>()
                .request(HandoffOperationInfo.IS_AVAILABLE_SPECIFIC_DRIVE_MODE, DriveModeController.SELF_TIMER); 
		if(null != result && result.booleanValue()){
			return getSupportedSelfTimer();
		} else {
			return null;
		}
	}
}
