package ezremote.client.webapi.touchafposition;

import com.sony.mexi.webapi.DefaultCallbacks;

import ezremote.client.webapi.listener.touchafposition.GetTouchAFPositionListener;

public class GetTouchAFPositionCallback
extends DefaultCallbacks
implements com.sony.scalar.webapi.service.camera.v1_2.touchafposition.GetTouchAFPositionCallback
{

	private GetTouchAFPositionListener listener;
	
	public GetTouchAFPositionCallback(GetTouchAFPositionListener listener){
		this.listener = listener;
	}
	
	@Override
	public void returnCb(boolean set, double[] touchCordinate) {
		listener.onSuccessGetlTouchAFPosition(set, touchCordinate);
	}
	
	@Override
	public void handleStatus(int error, String message){
		listener.onFailureGetTouchAFPosition(error);
	}
}
