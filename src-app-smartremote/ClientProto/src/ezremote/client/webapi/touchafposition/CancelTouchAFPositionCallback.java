package ezremote.client.webapi.touchafposition;

import com.sony.mexi.webapi.DefaultCallbacks;

import ezremote.client.webapi.listener.touchafposition.CancelTouchAFPositionListener;

public class CancelTouchAFPositionCallback
extends DefaultCallbacks
implements com.sony.scalar.webapi.service.camera.v1_2.touchafposition.CancelTouchAFPositionCallback
{
	public CancelTouchAFPositionListener listener;
	
	public CancelTouchAFPositionCallback(CancelTouchAFPositionListener listener){
		this.listener = listener;
	}
	@Override
	public void returnCb() {
		listener.onSuccessCancelTouchAFPosition();
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureCancelTouchAFPosition(error);
	}
}
