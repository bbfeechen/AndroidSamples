package ezremote.client.webapi.common;

import com.sony.mexi.orb.client.OrbAbstractVersionHandler;

import ezremote.client.webapi.listener.VersionHandlerListener;

public class VersionHandler extends OrbAbstractVersionHandler {

	private VersionHandlerListener listener;
	
    public VersionHandler(VersionHandlerListener listener){
    	this.listener = listener;
    }
    
    @Override
    public void handleVersions(String[] versions) {
        listener.onSuccessVersionHandler(versions);
    }
    
	@Override
	public void handleStatus(int status, String message){
        listener.onHandledStatusOfVersion(status);
	}
}
