package ezremote.client.webapi.get.exposure;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetSupportedExposureCompensationCallback;

import ezremote.client.webapi.listener.GetSupportedExposureCompensationListener;

public class GetSupportedExposureCompensationCallback extends AbstractGetSupportedExposureCompensationCallback {

	private GetSupportedExposureCompensationListener listener;
	
	public GetSupportedExposureCompensationCallback(GetSupportedExposureCompensationListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(int[] max, int[] min, int[] step) {
		listener.onSuccessGetSupportedExposureCompensation(max, min, step);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetSupportedExposureCompensation(error);
	}

}
