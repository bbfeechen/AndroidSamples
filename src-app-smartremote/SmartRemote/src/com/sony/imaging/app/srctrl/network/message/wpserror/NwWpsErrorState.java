package com.sony.imaging.app.srctrl.network.message.wpserror;

import java.util.BitSet;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.wifi.direct.DirectDevice;
import com.sony.wifi.direct.DirectManager;
import com.sony.wifi.direct.DirectConfiguration.ConfigurationMethod;

/**
 * 
 * This state tells user that Wi-Fi Direct WPS Error happened.
 * Includes registration error and timeout.
 * @author 0000138134
 *
 */
public class NwWpsErrorState extends NetworkRootState {
	private final static String TAG = NwWpsErrorState.class.getSimpleName();
	
	public static final String ID_INVALID_LAYOUT = "ERROR_LAYOUT";
	public static final String ID_TIMEOUT_LAYOUT = "TIMEOUT_LAYOUT";
	
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		if(StateController.getInstance().isErrorByTimeout()){
			openLayout(NwWpsErrorState.ID_TIMEOUT_LAYOUT);
		} else {
			openLayout(NwWpsErrorState.ID_INVALID_LAYOUT);
		}
		
		setDirectTargetDeviceName(null);
		setDirectTargetDeviceAddress(null);
        
        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.STA_CONNECTED_ACTION);
        iFilter.addAction(DirectManager.PROVISIONING_REQUEST_ACTION);
        
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
	    if(StateController.getInstance().isErrorByTimeout()){
			closeLayout(NwWpsErrorState.ID_TIMEOUT_LAYOUT);
		} else {
			closeLayout(NwWpsErrorState.ID_INVALID_LAYOUT);
		}
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.STA_CONNECTED_ACTION.equals(action)) {
            handleStationConnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
        } else if (DirectManager.PROVISIONING_REQUEST_ACTION.equals(action)) {
        	handleProvisioningRequest((DirectDevice)intent.getParcelableExtra(DirectManager.EXTRA_P2P_DEVICE));
        }
    }
    
    private void handleStationConnected(String addr) {
        setNextState(ID_CONNECTED, null);
    }
    
    private void handleProvisioningRequest(DirectDevice device){
    	setDirectTargetDeviceName(device.getName());
    	setDirectTargetDeviceAddress(device.getP2PDevAddress());
    	
    	Log.v(TAG, "TargetDeviceName: " + getDirectTargetDeviceName());
    	Log.v(TAG, "TargetDeviceAddress: " + getDirectTargetDeviceAddress());
    	
    	BitSet confMethod = device.getConfigMethod();
    	Log.v(TAG, "Bitset of ConfigMethod: " + confMethod.toString());
    	
    	if (confMethod.get(ConfigurationMethod.PUSH_BUTTON)) {
     		Log.v(TAG, "Provisioning Request: WPS PUSH BUTTON");
     	} else if(confMethod.get(ConfigurationMethod.KEYPAD)) {
    		Log.v(TAG, "Provisioning Request: WPS PIN DISPLAY");
    		setNextState(ID_WPS_PIN_PRINT, null);
    	} else if (confMethod.get(ConfigurationMethod.DISPLAY)) {
    		Log.v(TAG, "Provisioning Request: WPS PIN INPUT");
    		setNextState(ID_WPS_PIN_INPUT, null);
    	}
    }
}
