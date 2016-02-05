package com.sony.imaging.app.srctrl.webapi.specific;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;

/**
 * 
 * This class will wait for Completion of state transition from Network state to Shooting state.
 * @author 0000138134
 *
 */
public class RecModeTransitionHandler {
	private static final String TAG = RecModeTransitionHandler.class.getSimpleName();
	
	private static RecModeTransitionHandler transHandler = new RecModeTransitionHandler();
	
	public static RecModeTransitionHandler getInstance(){
		return transHandler;
	}
	
	public enum TransStatus{
		SUCCESS, FAILURE
	}
	
	private Object sync;
	private TransStatus status;
	
	public RecModeTransitionHandler(){
		sync = new Object();
		status = TransStatus.FAILURE;
	}
	
	public TransStatus goToShootingState(){
		StateController stateController = StateController.getInstance();
		status = TransStatus.FAILURE;
		
		Boolean result = new OperationRequester<Boolean>()
                .request(HandoffOperationInfo.MOVE_TO_SHOOTING_STATE, (Object)null); 
		if(null == result || !result.booleanValue()){
			return TransStatus.FAILURE;
		}
		
		stateController.setWaitingRecModeChange(true);
		
		synchronized(sync){
			try {
				sync.wait(SRCtrlConstants.CHANGE_MODE_TIMEOUT);
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException while waiting.");
			}
		}
		
		stateController.setWaitingRecModeChange(false);
		
		return status;
	}
	
	public TransStatus goToNetworkState(){
		StateController stateController = StateController.getInstance();
		status = TransStatus.FAILURE;
		
		Boolean result = new OperationRequester<Boolean>()
                .request(HandoffOperationInfo.MOVE_TO_NETWORK_STATE, (Object)null); 
		if(null == result || !result.booleanValue()){
			return TransStatus.FAILURE;
		}
		
		stateController.setWaitingRecModeChange(true);
		
		synchronized(sync){
			try {
				sync.wait(SRCtrlConstants.CHANGE_MODE_TIMEOUT);
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException while waiting.");
			}
		}
		
		stateController.setWaitingRecModeChange(false);
		
		return status;
	}
	
	public void onChangedOperatingMode() {
		Log.v(TAG, "detected changed to ShootingState/NetworkState");
		status = TransStatus.SUCCESS;
		synchronized(sync){
			sync.notify();
		}
	}

	public void onPauseCalled() {
		Log.v(TAG, "detected onPause is called");
		synchronized(sync){
			sync.notify();
		}
	}
}
