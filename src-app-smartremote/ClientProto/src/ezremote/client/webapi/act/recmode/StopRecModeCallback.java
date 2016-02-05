package ezremote.client.webapi.act.recmode;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractStopRecModeCallback;

import ezremote.client.webapi.listener.StopRecModeListener;

public class StopRecModeCallback extends AbstractStopRecModeCallback {

    private StopRecModeListener listener;
    
    public StopRecModeCallback(StopRecModeListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(int ret) {
        listener.onSuccessStopRecMode(ret);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureStopRecMode(error);
	}
}
