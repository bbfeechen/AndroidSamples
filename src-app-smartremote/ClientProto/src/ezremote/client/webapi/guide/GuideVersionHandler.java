package ezremote.client.webapi.guide;


import com.sony.mexi.orb.client.OrbAbstractVersionHandler;

import ezremote.client.webapi.listener.VersionHandlerListener;

public class GuideVersionHandler extends OrbAbstractVersionHandler {

	private VersionHandlerListener listener;
	
    public GuideVersionHandler(VersionHandlerListener listener){
    	this.listener = listener;
    }
    
    @Override
    public void handleVersions(String[] versions) {
        listener.onSuccessVersionHandler(versions);
    }
    
	@Override
	public void handleStatus(int error, String message){
        listener.onHandledStatusOfVersion(error);
	}
}
