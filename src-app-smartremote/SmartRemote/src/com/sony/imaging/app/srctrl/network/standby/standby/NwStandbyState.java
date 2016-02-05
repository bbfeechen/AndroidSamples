package com.sony.imaging.app.srctrl.network.standby.standby;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.wifi.direct.DirectConfiguration;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This state is the first state of this application.
 * Wi-Fi Direct GO and servers are starting.
 * @author 0000138134
 *
 */

public class NwStandbyState extends NetworkRootState{
	private final static String TAG = NwStandbyState.class.getSimpleName();
	
	public static final String ID_STANDBY_LAYOUT = "STANDBY_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		openLayout(ID_STANDBY_LAYOUT);		

        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        
        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.GROUP_CREATE_SUCCESS_ACTION);
        
        bReceiver = new SafeBroadcastReceiver() {
            @Override
            public void checkedOnReceive(Context context, Intent intent) {
                handleEvent(intent);
            }
        };
        bReceiver.registerThis(getActivity(), iFilter);
	}
	
	@Override
	public void onPause(){
		bReceiver.unregisterThis(getActivity());
	    closeLayout(ID_STANDBY_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.GROUP_CREATE_SUCCESS_ACTION.equals(action)) {
            handleGroupCreateSuccess((DirectConfiguration)intent.getParcelableExtra(DirectManager.EXTRA_DIRECT_CONFIG));
        }
    }
    
    private void handleGroupCreateSuccess(DirectConfiguration config) {
        directManager.saveConfiguration();
        setMySsid(config.getSsid());
        setMyPsk(config.getPreSharedKey());
	    setNextState(ID_WAITING, null);
    }
}


