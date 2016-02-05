package ezremote.client.webapi.get;


import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractGetAvailableApiListCallback;

import ezremote.client.webapi.listener.GetAvailableApiListListener;

public class GetAvailableApiListCallback extends AbstractGetAvailableApiListCallback {

    private GetAvailableApiListListener listener;
    
    public GetAvailableApiListCallback(GetAvailableApiListListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void returnCb(String[] apis) {
        listener.onSuccessGetAvailableApiList(apis);
	}

	@Override
	public void handleStatus(int error, String message){
        listener.onFailureGetAvailableApiList(error);
	}	
}
