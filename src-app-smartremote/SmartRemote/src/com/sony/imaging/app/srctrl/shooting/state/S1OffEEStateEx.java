package com.sony.imaging.app.srctrl.shooting.state;

import android.util.Log;

import com.sony.imaging.app.base.shooting.S1OffEEState;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OffEEStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 * 
 * Added state reporting function and Wi-Fi icon layout opener.
 * @author 0000138134
 *
 */
public class S1OffEEStateEx extends S1OffEEState {
    public static final String tag = S1OffEEState.class.getName();
    public static final String STATE_NAME = "S1OffEE";
	
	public static final String WIFI_ICON_LAYOUT = "Wifi Icon";
	
	@Override
	public void onResume(){
	    S1OffEEStateKeyHandlerEx.s_pushed_S2 = false;
        StateController stateController = StateController.getInstance();
        stateController.setState(this);
        if(!AppCondition.DIAL_INHIBIT.equals(stateController.getAppCondition())){
                stateController.setAppCondition(AppCondition.SHOOTING_EE);
        }

        super.onResume();
		openLayout(WIFI_ICON_LAYOUT);
		
		if(stateController.isWaitingBackTransition()){
		    Log.v(tag, "Change to network state because of the back transition flag.");
			stateController.changeToNetworkState();
			stateController.setGoBackFlag(false);
		}
	}
	
	@Override
	public void onPause(){
	    AppCondition appCondition= StateController.getInstance().getAppCondition();
		if(!AppCondition.DIAL_INHIBIT.equals(appCondition)
				&& !AppCondition.SHOOTING_REMOTE.equals(appCondition)
				&& !AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(appCondition)){
			StateController.getInstance().setAppCondition(AppCondition.SHOOTING_INHIBIT);
		}
		closeLayout(WIFI_ICON_LAYOUT);
		super.onPause();
	}
	
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
}
