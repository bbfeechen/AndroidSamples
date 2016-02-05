package ezremote.client.webapi.common;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetApplicationInfoCallback;

import ezremote.client.webapi.listener.GetApplicationInfoListener;

public class GetApplicationInfoCallback extends AbstractGetApplicationInfoCallback {

	private GetApplicationInfoListener listener;
	
	public GetApplicationInfoCallback(GetApplicationInfoListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(String name, String version) {
		listener.onSuccessGetApplicationInfo(name, version);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureGetApplicationInfo(error);
	}
}
