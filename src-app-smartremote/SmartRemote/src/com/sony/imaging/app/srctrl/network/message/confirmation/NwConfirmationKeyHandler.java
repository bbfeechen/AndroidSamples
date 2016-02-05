package com.sony.imaging.app.srctrl.network.message.confirmation;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

public class NwConfirmationKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwConfirmationKeyHandler.class.getSimpleName();

	@Override
	public int pushedMenuKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		moveToConnectedState();
		return HANDLED;
	}

	@Override
	public int pushedCenterKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		moveToChangingPwState();
		return HANDLED;
	}
	
	private void moveToConnectedState(){
		NetworkRootState state = (NetworkRootState) target;
		state.setNextState(NetworkRootState.ID_CONNECTED, null);
	}
	
	private void moveToChangingPwState(){
		NetworkRootState state = (NetworkRootState) target;
		StateController.getInstance().setIsRestartForNewConfig(true);
		state.setNextState(NetworkRootState.ID_RESTARTING, null);
	}
}
