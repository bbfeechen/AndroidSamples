package ezremote.client.webapi.act.liveview;



import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractStartLiveviewCallback;

import ezremote.client.webapi.listener.StartLiveviewListener;

public class StartLiveviewCallback extends AbstractStartLiveviewCallback {
    private StartLiveviewListener listener;
    
    public StartLiveviewCallback(StartLiveviewListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(String ret) {
        listener.onSuccessStartLiveview(ret);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureStartLiveview(error);
	}
}
