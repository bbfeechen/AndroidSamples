package ezremote.client.webapi.get.selftimer;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetAvailableSelfTimerCallback;

import ezremote.client.webapi.listener.GetAvailableSelfTimerListener;

public class GetAvailableSelfTimerCallback extends AbstractGetAvailableSelfTimerCallback {

	private GetAvailableSelfTimerListener listener;
	
	public GetAvailableSelfTimerCallback(GetAvailableSelfTimerListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(int current, int[] available) {
		listener.onSuccessGetAvailableSelfTimer(current, available);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetAvailableSelfTimer(error);
	}
}
