package ezremote.client.webapi;

import com.sony.mexi.orb.client.serviceguide.v1_0.ServiceGuideClient;


public class GuideForegroundOperator {
	private static final String TAG = GuideForegroundOperator.class.getSimpleName();
	private ServiceGuideClient webApiClient;
	
	public GuideForegroundOperator(ServiceGuideClient webApiClient){
		this.webApiClient = webApiClient;
	}
	
	/* Common */
	/*
	public void getVersions(){
		webApiClient.getVersions(GuideCallbacksContainer.getVersionHandler());
	}
	public void getMethodTypes(String version){
		webApiClient.getMethodTypes(version, GuideCallbacksContainer.getMethodTypeHandler());
	}
	*/
	public void getServiceProtocols(){
		webApiClient.getServiceProtocols(GuideCallbacksContainer.getProtocolHandler());
	}
}
