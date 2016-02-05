package ezremote.client.webapi.guide;

import com.sony.mexi.json.JsArray;
import com.sony.mexi.json.JsString;
import com.sony.mexi.orb.client.OrbAbstractMethodTypeHandler;

import ezremote.client.webapi.listener.MethodTypeListener;

public class GuideMethodTypeHandler extends OrbAbstractMethodTypeHandler {

    private MethodTypeListener listener;
    
    public GuideMethodTypeHandler(MethodTypeListener listener){
    	this.listener = listener;
    }
    
	@Override
	public void handleMethodType(String methodName, String[] parameterTypes, String[] resultTypes, String version) {
        JsString s = new JsString(methodName);
        JsArray pt = new JsArray(parameterTypes);
        JsArray rt = new JsArray(resultTypes);
        JsString v = new JsString(version);
        JsArray a = new JsArray();
        a.add(s);
        a.add(pt);
        a.add(rt);
        a.add(v);
        listener.onSuccessMethodTypeHandler(methodName, parameterTypes, resultTypes, version);
	}
    
	@Override
	public void handleStatus(int error, String message){
        listener.onHandledStatusOfMethodType(error);
	}


}
