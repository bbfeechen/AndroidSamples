package ezremote.client.webapi.act.liveview;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractStopLiveviewCallback;

import ezremote.client.webapi.listener.StopLiveviewListener;

public class StopLiveviewCallback extends AbstractStopLiveviewCallback {
	
    private StopLiveviewListener listener;
    
    public StopLiveviewCallback(StopLiveviewListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(int ret) {
        listener.onSuccessStopLiveview(ret);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureStopLiveview(error);
	}
}
