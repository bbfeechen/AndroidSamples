package ezremote.client.webapi.set;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractSetExposureCompensationCallback;

import ezremote.client.webapi.listener.SetExposureCompensationListener;

public class SetExposureCompensationCallback extends AbstractSetExposureCompensationCallback {
	
    private SetExposureCompensationListener listener;
    
    public SetExposureCompensationCallback(SetExposureCompensationListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(int ret) {
		listener.onSuccessSetExposureCompensation(ret);
	}

	@Override
	public void handleStatus(int error, String message){
		listener.onFailureSetExposureCompensation(error);
	}
	
}
