package com.sony.imaging.app.srctrl.network.wpspbc.pbc;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;
import com.sony.wifi.direct.DirectManager;

public class NwWpsPbcKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwWpsPbcKeyHandler.class.getSimpleName();

	@Override
	public int pushedMenuKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		cancelAndMoveToWaitingState();
		return HANDLED;
	}
	
	@Override
	public int pushedCenterKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		tryPbcAndMoveToRegisteringState();
		return HANDLED;
	}
	
	private void cancelAndMoveToWaitingState(){
		NetworkRootState state = (NetworkRootState) target;
		DirectManager directManager = (DirectManager) state.getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		directManager.cancel();
		state.setNextState(NetworkRootState.ID_WAITING, null);		
	}
	
	private void tryPbcAndMoveToRegisteringState(){
		NetworkRootState state = (NetworkRootState) target;
		DirectManager directManager = (DirectManager) state.getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		directManager.startWpsPbc(NetworkRootState.getDirectTargetDeviceAddress());		
		state.setNextState(NetworkRootState.ID_REGISTERING, null);
	}
}
