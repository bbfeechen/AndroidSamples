package com.sony.imaging.app.srctrl.network.progress.connected;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.wifi.direct.DirectDevice;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This state tells user that a station device is connected to this camera.
 * @author 0000138134
 *
 */
public class NwConnectedState extends NetworkRootState {
	private final static String TAG = NwConnectedState.class.getSimpleName();
	
	public static final String ID_CONNECTED_LAYOUT = "CONNECTED_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        List<DirectDevice> stationList = directManager.getStationList();
        bReceiver = null;
        if(stationList.size() == 0){
        	setNextState(ID_WAITING, null);
        } else {        	
            DirectDevice dDevice = stationList.get(0);
            setDirectTargetDeviceName(dDevice.getName());  // In case of Legacy device, set empty string
            Log.v(TAG, dDevice.toString());

    		openLayout(ID_CONNECTED_LAYOUT);
            
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
	}
	
	@Override
	public void onPause(){
		if(bReceiver != null){
		    bReceiver.unregisterThis(getActivity());
			closeLayout(ID_CONNECTED_LAYOUT);
		}
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
