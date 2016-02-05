package ezremote.client.webapi.guide;

import com.sony.mexi.orb.client.serviceguide.v1_0.AbstractProtocolHandler;

import android.util.Log;


import ezremote.client.webapi.listener.guide.GuideProtocolHandlerListener;

public class GuideProtocolHandler extends AbstractProtocolHandler {

	private GuideProtocolHandlerListener listener;
	
	public GuideProtocolHandler(GuideProtocolHandlerListener listener){
		this.listener = listener;
	}
	
	@Override
	public void handleProtocols(String service, String[] protocols) {
		listener.onSuccessGuideProtocolHandler(service, protocols);
	}
	
	@Override
	public void handleStatus(int status, String message){
		Log.d("GuideProtocolHandler", "handleStatus" + status + ": " + message);
        listener.onHandledGuideProtocol(status);
	}
}
