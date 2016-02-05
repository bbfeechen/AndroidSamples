package com.sony.imaging.app.srctrl.network.base;

import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.fw.ContainerState;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwStateBase extends ContainerState {	
	protected NetworkRootState getContainer() {
		return (NetworkRootState) container;
	}
	
	protected NetworkRootState getRootContainer(){
		return (NetworkRootState) getData(NetworkRootState.PROP_ID_APP_ROOT);
	}
	
	protected String getLogTag() {
		return this.getClass().getSimpleName();
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
}
