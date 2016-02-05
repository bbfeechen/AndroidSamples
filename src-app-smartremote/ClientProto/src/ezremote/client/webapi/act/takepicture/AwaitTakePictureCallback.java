package ezremote.client.webapi.act.takepicture;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractAwaitTakePictureCallback;

import ezremote.client.webapi.listener.AwaitTakePictureListener;

public class AwaitTakePictureCallback extends AbstractAwaitTakePictureCallback {

	private AwaitTakePictureListener listener;
	
	public AwaitTakePictureCallback(AwaitTakePictureListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(String[] urls) {
		listener.onSuccessAwaitTakePicture(urls);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureAwaitTakePicture(error);
	}

}
