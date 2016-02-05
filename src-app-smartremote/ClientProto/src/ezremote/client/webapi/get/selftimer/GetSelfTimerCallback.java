package ezremote.client.webapi.get.selftimer;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetSelfTimerCallback;

import ezremote.client.webapi.listener.GetSelfTimerListener;

public class GetSelfTimerCallback extends AbstractGetSelfTimerCallback {

    private GetSelfTimerListener listener;
    
    public GetSelfTimerCallback(GetSelfTimerListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(int timer) {
        listener.onSuccessGetSelfTimer(timer);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureGetSelfTimer(error);
	}
}
