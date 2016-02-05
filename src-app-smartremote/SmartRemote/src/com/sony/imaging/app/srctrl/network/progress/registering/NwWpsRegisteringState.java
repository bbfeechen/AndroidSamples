package com.sony.imaging.app.srctrl.network.progress.registering;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.wifi.direct.DirectError;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This state tells user that a P2P device is trying WPS registration to this camera.
 * @author 0000138134
 *
 */
public class NwWpsRegisteringState extends NetworkRootState {
	private final static String TAG = NwWpsRegisteringState.class.getSimpleName();
	
	public static final String ID_REGISTERING_LAYOUT = "REGISTERING_LAYOUT";
	
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
	
	@Override
	public void onResume(){		
		openLayout(ID_REGISTERING_LAYOUT);
        
        iFilter = new IntentFilter();
        iFilter.addAction(DirectManager.STA_CONNECTED_ACTION);
        iFilter.addAction(DirectManager.WPS_REG_SUCCESS_ACTION);
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
		closeLayout(ID_REGISTERING_LAYOUT);
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

        if (DirectManager.STA_CONNECTED_ACTION.equals(action)) {
            handleStationConnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
        } else if (DirectManager.WPS_REG_SUCCESS_ACTION.equals(action)) {
        	handleWpsRegisteringSuccess();
        } else if (DirectManager.WPS_REG_FAILURE_ACTION.equals(action)) {
	    	handleWpsRegisteringFailure(intent.getIntExtra(DirectManager.EXTRA_ERROR_CODE, -1));	  
        }
    }
    
    private void handleStationConnected(String addr) {
        setNextState(ID_CONNECTED, null);
    }
    
    private void handleWpsRegisteringSuccess(){
    	Log.v(TAG, "WPS Registering Success");
    }

    private void handleWpsRegisteringFailure(int error){
    	Log.v(TAG, "WPS Registering Failure. ErrorCode: " + DirectError.getErrorStr(error));
    	if(error == DirectError.USER_CANCELED){
    		Log.v(TAG, "WPS Registering canceled");
    	} else if(error == DirectError.TIMEOUT){
	    	StateController.getInstance().setIsErrorByTimeout(true);
	    	setNextState(ID_WPS_ERROR, null);    		
    	} else {
	    	StateController.getInstance().setIsErrorByTimeout(false);
	    	setNextState(ID_WPS_ERROR, null);
    	}
    }
}
