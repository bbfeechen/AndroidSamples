package com.sony.imaging.app.srctrl.network.wpspin.print;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.wifi.direct.DirectError;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * When a P2P device requested this camera to connect with WPS PIN as KEYPAD device.
 * If user input correct PIN code at P2P device, the registration will succeed.
 * @author 0000138134
 *
 */
public class NwWpsPinPrintState extends NetworkRootState {
	private final static String TAG = NwWpsPinPrintState.class.getSimpleName();
	
	public static final String ID_PIN_PRINT_LAYOUT = "PIN_PRINT_LAYOUT";
	
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;
    
	private Handler handler;
	private Runnable timeoutRunnable;
	
	@Override
	public void onResume(){	
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        setWpsPin(directManager.createWpsPin(true));
        
		openLayout(NwWpsPinPrintState.ID_PIN_PRINT_LAYOUT);
		
        directManager.startWpsPin(getWpsPin());
        
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
        
        handler = new Handler();			// use handler instead of timer in 1st update
        timeoutRunnable = new Runnable(){
			@Override
			public void run() {
    			Log.e(TAG, "Timeout: WPS PIN DISPLAY TIMEOUT");
    			StateController.getInstance().setIsErrorByTimeout(true);
    			setNextState(ID_WPS_ERROR, null);
			}        	
        };
        handler.postDelayed(timeoutRunnable, SRCtrlConstants.WPS_PIN_TIMEOUT);
	}
	
	@Override
	public void onPause(){
    	handler.removeCallbacks(timeoutRunnable);
    	bReceiver.unregisterThis(getActivity());
		closeLayout(ID_PIN_PRINT_LAYOUT);
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
    	setNextState(ID_WAITING, null);
    }
}
