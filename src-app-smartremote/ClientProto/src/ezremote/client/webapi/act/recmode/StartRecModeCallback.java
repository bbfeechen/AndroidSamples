package ezremote.client.webapi.act.recmode;
import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.AbstractStartRecModeCallback;

import ezremote.client.webapi.listener.StartRecModeListener;

public class StartRecModeCallback extends AbstractStartRecModeCallback {

    private StartRecModeListener listener;

    public StartRecModeCallback(StartRecModeListener listener) {
        this.listener = listener;
    }
	
	@Override
	public void returnCb(int ret) {
        listener.onSuccessStartRecMode(ret);
	}
	
	@Override
	public void handleStatus(int error, String message){
        listener.onFailureStartRecMode(error);
	}
}
