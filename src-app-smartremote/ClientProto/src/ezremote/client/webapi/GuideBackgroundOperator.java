package ezremote.client.webapi;


import com.sony.mexi.orb.client.serviceguide.v1_0.ServiceGuideClient;

import android.os.Handler;


public class GuideBackgroundOperator {
	private static final String TAG = GuideBackgroundOperator.class.getSimpleName();
	private ServiceGuideClient webApiClient;
	private Handler handler;
	
	public GuideBackgroundOperator(ServiceGuideClient webApiClient, Handler handler){
		this.webApiClient = webApiClient;
		this.handler = handler;
	}
	
	/*
	public void getVersoins(){		
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getVersions(GuideCallbacksContainer.getVersionHandler());
					}
				});
		    }
		}).start();
	}
	
	public void getMethodTypes(final String version){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getMethodTypes(version, GuideCallbacksContainer.getMethodTypeHandler());
					}
				});
		    }
		}).start();
	}
	*/
	
	public void getServiceProtocols(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getServiceProtocols(GuideCallbacksContainer.getProtocolHandler());
					}
				});
		    }
		}).start();
	}
}
