package com.sony.imaging.app.srctrl.network.wpspin.input;

import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.srctrl.caution.InfoEx;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;
import com.sony.wifi.direct.DirectManager;

import android.util.Log;

public class NwWpsPinInputKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwWpsPinInputKeyHandler.class.getSimpleName();
	
	@Override
	public int pushedMenuKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		cancelAndMoveToWaitingState();
		return HANDLED;
	}

	@Override
	public int pushedCenterKey() {
		int ret;
		if (NwWpsPinInputLayout.isEditTextFocused()) {
			ret = hadleEditBoxPushed();
		}
		else {
			ret = handleOkButtonPushed();
		}
		return ret;
	}
	
	private int handleOkButtonPushed() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		tryPinAndMoveToRegisteringState();
		return HANDLED;
	}

	private int hadleEditBoxPushed() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		openKeypad();
		return HANDLED;
	}
	
	private void cancelAndMoveToWaitingState(){
		NetworkRootState state = (NetworkRootState) target;
		DirectManager directManager = (DirectManager) state.getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		directManager.cancel();
		state.setNextState(NetworkRootState.ID_WAITING, null);		
	}
	
	private void tryPinAndMoveToRegisteringState(){
		NetworkRootState state = (NetworkRootState) target;
		DirectManager directManager = (DirectManager) state.getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		String pin = NwWpsPinInputLayout.getPin();
		
		if(pin.length() == 8 || pin.length() == 4){
			NwWpsPinInputLayout.clearPin();
			if(directManager.isValidWpsPin(pin)){
				Log.v(TAG, "PIN: " + pin);
				directManager.startWpsPin(pin);
				state.setNextState(NetworkRootState.ID_REGISTERING, null);
			} else {
				Log.v(TAG, "PIN: " + pin + " is Invalid");
				directManager.cancel();
				StateController.getInstance().setIsErrorByTimeout(false);
				state.setNextState(NetworkRootState.ID_WPS_ERROR, null);
			}
		} else {
			CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_INVALID_PIN);
			//state.setNextState(NwRootContainer.ID_INVALID_PIN, null);
		}
	}
	
	private void openKeypad(){
		NwWpsPinInputLayout.openKeypad();
	}
}
