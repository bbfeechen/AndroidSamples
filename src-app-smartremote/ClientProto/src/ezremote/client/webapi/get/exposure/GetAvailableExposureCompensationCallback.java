package ezremote.client.webapi.get.exposure;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetAvailableExposureCompensationCallback;

import android.util.Log;


import ezremote.client.webapi.listener.GetAvailableExposureCompensationListener;

public class GetAvailableExposureCompensationCallback extends AbstractGetAvailableExposureCompensationCallback {

	private GetAvailableExposureCompensationListener listener;
	
	public GetAvailableExposureCompensationCallback(GetAvailableExposureCompensationListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(int current, int max, int min, int step) {
		Log.i("GetAvailableExposureCompensation", "returnCb - max:" + max + " min:" + min + " step:" + step);
		listener.onSuccessGetAvailableExposureCompensation(current, max, min, step);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetAvailableExposureCompensation(error);
	}
}
