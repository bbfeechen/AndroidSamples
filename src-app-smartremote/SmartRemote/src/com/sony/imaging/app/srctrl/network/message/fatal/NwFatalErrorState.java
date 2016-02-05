package com.sony.imaging.app.srctrl.network.message.fatal;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.StateController;

/**
 * 
 * This state tells user that unrecoverable error happened.
 * @author 0000138134
 *
 */
public class NwFatalErrorState extends NetworkRootState {
	
	public static final String ID_FATAL_LAYOUT = "FATAL_LAYOUT";
	
	@Override
	public void onResume(){
        StateController.getInstance().setFatalError(true);
		openLayout(ID_FATAL_LAYOUT);
		
		setDirectTargetDeviceName(null);
		setDirectTargetDeviceAddress(null);
	}
	
	@Override
	public void onPause(){
		throw new IllegalStateException();
		//closeLayout(ID_FATAL_LAYOUT);
	}
}
