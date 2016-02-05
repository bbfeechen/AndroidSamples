package com.sony.imaging.app.srctrl.network.message.confirmation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This state asks user whether to restart Wi-Fi Direct GO with new configuration or not.
 * @author 0000138134
 *
 */
public class NwConfirmationState extends NetworkRootState {
	private final static String TAG = NwConfirmationState.class.getSimpleName();
	
	public static final String ID_CONFIRM_CHANGE_CONFIG_LAYOUT = "CONFIRM_CHANGE_CONFIG_LAYOUT";

    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		openLayout(NwConfirmationState.ID_CONFIRM_CHANGE_CONFIG_LAYOUT);

        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.STA_DISCONNECTED_ACTION);
        
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
	    closeLayout(ID_CONFIRM_CHANGE_CONFIG_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.STA_DISCONNECTED_ACTION.equals(action)) {
            handleStationDisconnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
        }
    }
    
    private void handleStationDisconnected(String addr) {
    	setNextState(ID_WAITING, null);
    }

}
