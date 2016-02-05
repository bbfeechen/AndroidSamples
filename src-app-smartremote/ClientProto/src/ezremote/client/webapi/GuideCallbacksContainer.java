package ezremote.client.webapi;

import ezremote.client.webapi.guide.GuideProtocolHandler;

public class GuideCallbacksContainer {
	private static final String TAG = GuideCallbacksContainer.class.getSimpleName();
	
	private static boolean isInitialized = false;
	
    // private static GuideVersionHandler versionHandler;
    // private static GuideMethodTypeHandler methodTypeHandler;
    private static GuideProtocolHandler protocolHandler;
    
    public static boolean isInitialized(){
    	return isInitialized;
    }
    public static void setInitializedFlag(boolean bool){
    	isInitialized = bool;
    }
    
    
    /* Common */
    
    /*
    public static void setVersionHandler(GuideVersionHandler handler){
    	versionHandler = handler;
    }
    public static GuideVersionHandler getVersionHandler(){
    	return versionHandler;
    }
    
    public static void setMethodTypeHandler(GuideMethodTypeHandler handler){
    	methodTypeHandler = handler;
    }
    public static GuideMethodTypeHandler getMethodTypeHandler(){
    	return methodTypeHandler;
    }
    */
    
    public static void setProtocolHandler(GuideProtocolHandler handler){
    	protocolHandler = handler;
    }
    public static GuideProtocolHandler getProtocolHandler(){
    	return protocolHandler;
    }
}
