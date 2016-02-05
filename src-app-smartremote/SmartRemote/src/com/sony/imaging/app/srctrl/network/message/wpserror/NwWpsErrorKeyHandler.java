package com.sony.imaging.app.srctrl.network.message.wpserror;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;


public class NwWpsErrorKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwWpsErrorKeyHandler.class.getSimpleName();
	@Override
	public int pushedCenterKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		moveToWaitingState();
		return HANDLED;
	}
	
	private void moveToWaitingState(){
		NetworkRootState state = (NetworkRootState) target;
		state.setNextState(NetworkRootState.ID_WAITING, null);
	}
}
