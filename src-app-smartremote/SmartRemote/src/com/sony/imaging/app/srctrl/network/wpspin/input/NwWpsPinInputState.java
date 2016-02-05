package com.sony.imaging.app.srctrl.network.wpspin.input;

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
 * When a P2P device requested this camera to connect with WPS PIN as DISPLAY device, this state starts.
 * If user input correct PIN code at this camera, the registration will succeed.
 * @author 0000138134
 *
 */
public class NwWpsPinInputState extends NetworkRootState {
	private final static String TAG = NwWpsPinInputState.class.getSimpleName();
	
	public static final String ID_PIN_INPUT_LAYOUT = "PIN_INPUT_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		openLayout(NwWpsPinInputState.ID_PIN_INPUT_LAYOUT);
		
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        setWpsPin(directManager.createWpsPin(true));
        
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
		closeLayout(ID_PIN_INPUT_LAYOUT);
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
