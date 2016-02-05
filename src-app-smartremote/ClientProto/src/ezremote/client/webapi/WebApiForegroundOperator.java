package ezremote.client.webapi;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.SmartRemoteControlTestClientClient;
import com.sony.scalar.webapi.interfaces.client.srctestclient.v1_2.SmartRemoteControlTestClient;

import android.util.Log;

public class WebApiForegroundOperator {
	private static final String TAG = WebApiForegroundOperator.class.getSimpleName();
	private SmartRemoteControlTestClientClient webApiClient;
	
	public WebApiForegroundOperator(SmartRemoteControlTestClientClient webApiClient){
		this.webApiClient = webApiClient;
	}
	
	/* Common */
	public void getApplicationInfo(){
		webApiClient.getApplicationInfo(WebApiCallbacksContainer.getGetApplicationInfoCallback());
	}
	public void getVersions(){
		webApiClient.getVersions(WebApiCallbacksContainer.getVersionHandler());
	}
	public void getMethodTypes(String version){
		webApiClient.getMethodTypes(version, WebApiCallbacksContainer.getMethodTypeHandler());
	}	
	public void getAvailableApiList(){
		webApiClient.getAvailableApiList(WebApiCallbacksContainer.getGetAvailableApiListCallback());
	}
	/*
	public void receiveEvent(boolean bool){
		Log.i(TAG, "receiveEvent");
		webApiClient.receiveEvent(bool, WebApiCallbacksContainer.getReceiveEventCallback());
	}
	*/
	public void getEvent(boolean bool){
	    Log.i(TAG, "getEvent");
	    webApiClient.getEvent(bool, WebApiCallbacksContainer.getGetEventCallback());
	}
	
	/* Take Picture */
	
	public void actTakePicture(){
		webApiClient.actTakePicture(WebApiCallbacksContainer.getActTakePictureCallback());
	}
	public void awaitTakePicture(){
		webApiClient.awaitTakePicture(WebApiCallbacksContainer.getAwaitTakePictureCallback());
	}	
	
	/* REC Mode */
	
	public void startRecMode(){
		webApiClient.startRecMode(WebApiCallbacksContainer.getStartRecModeCallback());
	}	
	public void stopRecMode(){
		webApiClient.stopRecMode(WebApiCallbacksContainer.getStopRecModeCallback());
	}

	
	/* Liveview */
	
	public void startLiveview(){
		webApiClient.startLiveview(WebApiCallbacksContainer.getStartLiveviewCallback());
	}	
	public void stopLiveview(){
			webApiClient.stopLiveview(WebApiCallbacksContainer.getStopLiveviewCallback());
	}
	
	
	/* Exposure Compensation */
	
	public void getExposureCompensation(){
		webApiClient.getExposureCompensation(WebApiCallbacksContainer.getGetExposureCompensationCallback());
	}
	public void getAvailableExposureCompensation(){
		webApiClient.getAvailableExposureCompensation(WebApiCallbacksContainer.getGetAvailableExposureCompensationCallback());
	}
	public void getSupportedExposureCompensation(){
		webApiClient.getSupportedExposureCompensation(WebApiCallbacksContainer.getGetSupportedExposureCompensationCallback());
	}
	public void setExposureCompensation(final int exposure){
		webApiClient.setExposureCompensation(exposure, WebApiCallbacksContainer.getSetExposureCompensationCallback());
	}
	

	/* Self Timer */
	
	public void getSelfTimer(){
		webApiClient.getSelfTimer(WebApiCallbacksContainer.getGetSelfTimerCallback());
	}
	public void getAvailableSelfTimer(){
		webApiClient.getAvailableSelfTimer(WebApiCallbacksContainer.getGetAvailableSelfTimerCallback());
	}	
	public void getSupportedSelfTimer(){
		webApiClient.getSupportedSelfTimer(WebApiCallbacksContainer.getGetSupportedSelfTimerCallback());
	}	
	public void setSelfTimer(final int timer){
		webApiClient.setSelfTimer(timer, WebApiCallbacksContainer.getSetSelfTimerCallback());
	}
}
