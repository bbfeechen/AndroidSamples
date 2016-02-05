package ezremote.client.webapi.get.exposure;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetExposureCompensationCallback;

import ezremote.client.webapi.listener.GetExposureCompensationListener;

public class GetExposureCompensationCallback extends AbstractGetExposureCompensationCallback {

	private GetExposureCompensationListener listener;
	
	public GetExposureCompensationCallback(GetExposureCompensationListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(int exposure) {
		listener.onSuccessGetExposureCompensation(exposure);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetExposureCompensation(error);
	}

}
