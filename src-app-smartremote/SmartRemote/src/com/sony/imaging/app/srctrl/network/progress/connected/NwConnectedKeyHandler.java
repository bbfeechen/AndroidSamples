package com.sony.imaging.app.srctrl.network.progress.connected;

import android.view.KeyEvent;

import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

public class NwConnectedKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwConnectedKeyHandler.class.getSimpleName();

	@Override
	public int pushedCenterKey() {
		moveToRestartingState();
		return HANDLED;
	}
	
	@Override
	public int onDeleteKeyPushed(KeyEvent event) {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		moveToConfirmState();
		return HANDLED;		
	}
	
	private void moveToConfirmState(){
		NetworkRootState state = (NetworkRootState) target;		
		state.setNextState(NetworkRootState.ID_CONFIRM, null);		
	}
	
	private void moveToRestartingState(){
		NetworkRootState state = (NetworkRootState) target;
		StateController.getInstance().setIsRestartForNewConfig(false);
		state.setNextState(NetworkRootState.ID_RESTARTING, null);
	}
	
	public void moveToShootingState(){
		NetworkRootState state = (NetworkRootState) target;
		((SRCtrl) state.getActivity()).changeApp(SRCtrl.APP_SHOOTING);		
	}
}
