package ezremote.client.webapi.get.selftimer;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetSupportedSelfTimerCallback;

import ezremote.client.webapi.listener.GetSupportedSelfTimerListener;

public class GetSupportedSelfTimerCallback extends AbstractGetSupportedSelfTimerCallback {

	public GetSupportedSelfTimerListener listener;
	
	public GetSupportedSelfTimerCallback(GetSupportedSelfTimerListener listener){
		this.listener = listener;
	}
	@Override
	public void returnCb(int[] supported) {
		listener.onSuccessGetSupportedSelfTimer(supported);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetSupportedSelfTimer(error);
	}
}
