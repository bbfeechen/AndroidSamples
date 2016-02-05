package ezremote.client.webapi.act.takepicture;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractActTakePictureCallback;

import ezremote.client.webapi.listener.ActTakePictureListener;

public class ActTakePictureCallback extends AbstractActTakePictureCallback {
	
    private ActTakePictureListener listener;
    
    public ActTakePictureCallback(ActTakePictureListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(String[] urls) {
        listener.onSuccessActTakePicture(urls);
	}

	@Override
	public void handleStatus(int error, String message){
        listener.onFailureActTakePicture(error);
	}

}
