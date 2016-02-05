package ezremote.client.webapi.set;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractSetSelfTimerCallback;

import ezremote.client.webapi.listener.SetSelfTimerListener;

public class SetSelfTimerCallback extends AbstractSetSelfTimerCallback {
	
    private SetSelfTimerListener listener;
    
    public SetSelfTimerCallback(SetSelfTimerListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(int ret) {
    	listener.onSuccessSetSelfTimer(ret);
	}

	@Override
	public void handleStatus(int error, String message){
		listener.onFailureSetSelfTimer(error);
	}

}
