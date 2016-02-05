package ezremote.client.scanner;

import android.content.Context;
import ezremote.client.data.ApContainer;
import ezremote.client.data.ap.APInfo;

public class ScannedAPAdapter extends APAdapter{
	
	public ScannedAPAdapter(Context context, int resourceId) {
		super(context, resourceId);
	}
	
	protected APInfo getThisAPInfo(){
		return ApContainer.getInstance().getAPInfo(position);
	}
}
