package com.sony.imaging.app.srctrl.shooting.state;

import com.sony.imaging.app.base.shooting.S1OnEEState;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;

/**
 * 
 * Added Wi-Fi icon layout opener.
 * @author 0000138134
 *
 */
public class S1OnEEStateEx extends S1OnEEState {	
    public static final String STATE_NAME="S1OnEE";
	public static final String WIFI_ICON_LAYOUT = "Wifi Icon";
	
    protected void setAppCondition() {
        StateController.getInstance().setAppCondition(AppCondition.SHOOTING_INHIBIT);
    }
	
	@Override
	public void onResume(){
		super.onResume();
		setAppCondition();
		openLayout(WIFI_ICON_LAYOUT);
	}
	
	@Override
	public void onPause(){
		closeLayout(WIFI_ICON_LAYOUT);
		super.onPause();
	}
}
