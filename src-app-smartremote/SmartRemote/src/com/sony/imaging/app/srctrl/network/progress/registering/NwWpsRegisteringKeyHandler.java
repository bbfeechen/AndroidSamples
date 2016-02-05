package com.sony.imaging.app.srctrl.network.progress.registering;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;
import com.sony.wifi.direct.DirectManager;

public class NwWpsRegisteringKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwWpsRegisteringKeyHandler.class.getSimpleName();

	@Override
	public int pushedCenterKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		cancelAndMoveToWaitingState();
		return HANDLED;
	}
	
	private void cancelAndMoveToWaitingState(){
		NetworkRootState state = (NetworkRootState) target;
		DirectManager directManager = (DirectManager) state.getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		directManager.cancel();
		state.setNextState(NetworkRootState.ID_WAITING, null);
	}
}
