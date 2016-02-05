package com.sony.imaging.app.srctrl.network.wpspbc.pbc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.wifi.direct.DirectError;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * When a P2P device requested this camera to connect with WPS as PBC device, this state start.
 * Asking user whether allow connection from displayed P2P device or not.
 * @author 0000138134
 *
 */
public class NwWpsPbcState extends NetworkRootState {
	private final static String TAG = NwWpsPbcState.class.getSimpleName();
	
	public static final String ID_PBC_LAYOUT = "PBC_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		openLayout(NwWpsPbcState.ID_PBC_LAYOUT);
		
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        
        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.STA_CONNECTED_ACTION);
        iFilter.addAction(DirectManager.WPS_REG_FAILURE_ACTION);
        
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
		closeLayout(ID_PBC_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.STA_CONNECTED_ACTION.equals(action)) {
            handleStationConnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
        } else if (DirectManager.WPS_REG_FAILURE_ACTION.equals(action)) {
        	handleWpsRegFailure(intent.getIntExtra(DirectManager.EXTRA_ERROR_CODE, 0));
        }
    }
    
    private void handleStationConnected(String addr) {
        setNextState(ID_CONNECTED, null);
    }
    
    
    private void handleWpsRegFailure(int error){
    	Log.e(TAG, "WPS Error: " + DirectError.getErrorStr(error));
    	directManager.cancel();
    	setNextState(ID_WAITING, null);
    }
}
