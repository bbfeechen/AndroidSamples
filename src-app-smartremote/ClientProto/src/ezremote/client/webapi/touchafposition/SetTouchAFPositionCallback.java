package ezremote.client.webapi.touchafposition;

import com.sony.mexi.webapi.DefaultCallbacks;

import ezremote.client.webapi.listener.touchafposition.SetTouchAFPositionListener;

public class SetTouchAFPositionCallback
extends DefaultCallbacks
implements com.sony.scalar.webapi.service.camera.v1_2.touchafposition.SetTouchAFPositionCallback
{

    private SetTouchAFPositionListener listener;
    
    public SetTouchAFPositionCallback(SetTouchAFPositionListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(boolean AFResult, String AFType,
			double[] touchCordinate, double[] AFBoxLeftTop,
			double[] AFBoxRightBottom) {
		listener.onSuccessSetTouchAFPosition(AFResult, AFType, touchCordinate, AFBoxLeftTop, AFBoxRightBottom);
		
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureSetTouchAFPosition(error);
	}
}
