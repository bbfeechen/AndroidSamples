package com.sony.imaging.app.srctrl.network.progress.restarting;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.wifi.direct.DirectConfiguration;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This state tells user that Wi-Fi Direct GO is restarting.
 * @author 0000138134
 *
 */
public class RestartingState extends NetworkRootState{
	private final static String TAG = RestartingState.class.getSimpleName();
	
	public static final String ID_CHANGING_CONFIG_LAYOUT = "CHANGING_CONFIG_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){
		openLayout(ID_CHANGING_CONFIG_LAYOUT);
		
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        directManager.removeGroup();
        
        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.GROUP_CREATE_SUCCESS_ACTION);
        iFilter.addAction(DirectManager.GROUP_TERMINATE_ACTION);
        iFilter.addAction(DirectManager.STA_CONNECTED_ACTION);
        
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
	    closeLayout(ID_CHANGING_CONFIG_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);
        if (DirectManager.GROUP_CREATE_SUCCESS_ACTION.equals(action)) {
            handleGroupCreateSuccess((DirectConfiguration)intent.getParcelableExtra(DirectManager.EXTRA_DIRECT_CONFIG));
        } else if (DirectManager.GROUP_TERMINATE_ACTION.equals(action)) {
            handleGroupTerminate();
        }
    }    
    
    private void handleGroupCreateSuccess(DirectConfiguration config) {
        directManager.saveConfiguration();
        setMySsid(config.getSsid());
        setMyPsk(config.getPreSharedKey());
	    setNextState(ID_WAITING, null);
    }
    
    private void handleGroupTerminate(){
		List<DirectConfiguration> confList = directManager.getConfigurations();
		if(StateController.getInstance().isRestartForNewConfig() || confList.size() == 0){
	    	directManager.startGo(DirectManager.PERSISTENT_GO);				// Start GO with new configuration   
    	} else {
    		directManager.startGo(confList.get(confList.size()-1).getNetworkId());			// Start GO with latest configuration 
    	    setNextState(ID_WAITING, null);
    	}
    }
}


