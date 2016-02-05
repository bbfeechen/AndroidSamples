package com.sony.imaging.app.srctrl.network.waiting.waiting;

import java.util.BitSet;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.wifi.direct.DirectConfiguration;
import com.sony.wifi.direct.DirectDevice;
import com.sony.wifi.direct.DirectManager;
import com.sony.wifi.direct.DirectConfiguration.ConfigurationMethod;

/**
 * 
 * This state is stable state that shows SSID, password and device name.
 * Waiting connection from Legacy devices with password.
 * Waiting connection from P2P devices with 3 pattern WPS connection.
 * @author 0000138134
 *
 */
public class NwWaitingState extends NetworkRootState {
	private final static String TAG = NwWaitingState.class.getSimpleName();
	
	public static final String ID_WAITING_LAYOUT = "WAITING_LAYOUT";
	
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;

	private DirectManager directManager;
	
	@Override
	public void onResume() {
		StateController.getInstance().setInitialWifiSetup(false);

		setDirectTargetDeviceName(null);
		setDirectTargetDeviceAddress(null);
		openLayout(ID_WAITING_LAYOUT);

		directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
		bReceiver = null;
		
		if (directManager.getStationList().size() != 0) {
			setNextState(ID_CONNECTED, null);
		} else {
			iFilter = new IntentFilter();
			iFilter.addAction(DirectManager.STA_CONNECTED_ACTION);
			iFilter.addAction(DirectManager.PROVISIONING_REQUEST_ACTION);
			iFilter.addAction(DirectManager.GROUP_CREATE_SUCCESS_ACTION);

			bReceiver = new SafeBroadcastReceiver() {
				@Override
				public void checkedOnReceive(Context context, Intent intent) {
					handleEvent(intent);
				}
			};
			bReceiver.registerThis(getActivity(), iFilter);
		}

	}
	
	@Override
	public void onPause(){
		if(bReceiver != null) {
			bReceiver.unregisterThis(getActivity());
		}
	    directManager = null;
		closeLayout(ID_WAITING_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.STA_CONNECTED_ACTION.equals(action)) {
            handleStationConnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
        } else if (DirectManager.PROVISIONING_REQUEST_ACTION.equals(action)) {
        	handleProvisioningRequest((DirectDevice)intent.getParcelableExtra(DirectManager.EXTRA_P2P_DEVICE));
        } else if (DirectManager.GROUP_CREATE_SUCCESS_ACTION.equals(action)) {
            handleGroupCreateSuccess((DirectConfiguration)intent.getParcelableExtra(DirectManager.EXTRA_DIRECT_CONFIG));
        }
    }
    
    private void handleGroupCreateSuccess(DirectConfiguration config) {
        if(directManager.getStationList().size() != 0){
			setNextState(ID_CONNECTED, null);
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
    		setNextState(ID_WPS_PBC, null);
     	} else if(confMethod.get(ConfigurationMethod.KEYPAD)) {
    		Log.v(TAG, "Provisioning Request: WPS PIN DISPLAY");
    		setNextState(ID_WPS_PIN_PRINT, null);
    	} else if (confMethod.get(ConfigurationMethod.DISPLAY)) {
    		Log.v(TAG, "Provisioning Request: WPS PIN INPUT");
    		setNextState(ID_WPS_PIN_INPUT, null);
    	}
    }
}
