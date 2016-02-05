package ezremote.client.webapi;

import com.sony.scalar.webapi.service.camera.v1_2.getevent.GetEventCallback;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.CancelTouchAFPositionCallback;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.GetTouchAFPositionCallback;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.SetTouchAFPositionCallback;

import ezremote.client.webapi.act.liveview.StartLiveviewCallback;
import ezremote.client.webapi.act.liveview.StopLiveviewCallback;
import ezremote.client.webapi.act.recmode.StartRecModeCallback;
import ezremote.client.webapi.act.recmode.StopRecModeCallback;
import ezremote.client.webapi.act.takepicture.ActTakePictureCallback;
import ezremote.client.webapi.act.takepicture.AwaitTakePictureCallback;
import ezremote.client.webapi.common.GetApplicationInfoCallback;
import ezremote.client.webapi.common.MethodTypeHandler;
import ezremote.client.webapi.common.VersionHandler;
import ezremote.client.webapi.get.GetAvailableApiListCallback;
import ezremote.client.webapi.get.exposure.GetAvailableExposureCompensationCallback;
import ezremote.client.webapi.get.exposure.GetExposureCompensationCallback;
import ezremote.client.webapi.get.exposure.GetSupportedExposureCompensationCallback;
import ezremote.client.webapi.get.selftimer.GetAvailableSelfTimerCallback;
import ezremote.client.webapi.get.selftimer.GetSelfTimerCallback;
import ezremote.client.webapi.get.selftimer.GetSupportedSelfTimerCallback;
import ezremote.client.webapi.set.SetExposureCompensationCallback;
import ezremote.client.webapi.set.SetSelfTimerCallback;

public class WebApiCallbacksContainer {
	private static final String TAG = WebApiCallbacksContainer.class.getSimpleName();
	
	private static boolean isInitialized = false;
	
    private static VersionHandler versionHandler;
    private static MethodTypeHandler methodTypeHandler;
    private static GetApplicationInfoCallback getApplicationInfoCb;
    
    private static ActTakePictureCallback actTakePictureCb;
    private static AwaitTakePictureCallback awaitTakePictureCb;
    private static StartRecModeCallback startRecModeCb;
    private static StopRecModeCallback stopRecModeCb;
    private static StartLiveviewCallback startLiveviewCb;
    private static StopLiveviewCallback stopLiveviewCb;
    
    private static SetSelfTimerCallback setSelfTimerCb;
    private static GetSelfTimerCallback getSelfTimerCb;
    private static GetAvailableSelfTimerCallback getAvailableSelfTimerCb;
    private static GetSupportedSelfTimerCallback getSupportedSelfTimerCb;
    
    private static SetExposureCompensationCallback setExposureCompensationCb;
    private static GetExposureCompensationCallback getExposureCompensationCb;
    private static GetAvailableExposureCompensationCallback getAvailableExposureCompensationCb;
    private static GetSupportedExposureCompensationCallback getSupportedExposureCompensationCb;
    
    private static GetAvailableApiListCallback getAvailableApiListCb;
    //private static ReceiveEventCallback receiveEventCb;
    private static GetEventCallback getEventCb;

    private static CancelTouchAFPositionCallback cancelTouchAFPositionCallback;
    private static GetTouchAFPositionCallback getTouchAFPositionCallback;
    private static SetTouchAFPositionCallback setTouchAFPositionCallback;
    
    public static boolean isInitialized(){
    	return isInitialized;
    }
    public static void setInitializedFlag(boolean bool){
    	isInitialized = bool;
    }
    
    
    /* Common */
    
    public static void setVersionHandler(VersionHandler handler){
    	versionHandler = handler;
    }
    public static VersionHandler getVersionHandler(){
    	return versionHandler;
    }
    
    public static void setMethodTypeHandler(MethodTypeHandler handler){
    	methodTypeHandler = handler;
    }
    public static MethodTypeHandler getMethodTypeHandler(){
    	return methodTypeHandler;
    }

    public static void setGetApplicationInfoCallback(GetApplicationInfoCallback cb){
    	getApplicationInfoCb = cb;
    }
    public static GetApplicationInfoCallback getGetApplicationInfoCallback(){
    	return getApplicationInfoCb;
    }
    
    /*
    public static void setReceiveEventCallback(ReceiveEventCallback cb){
        receiveEventCb = cb;
    }
    public static ReceiveEventCallback getReceiveEventCallback(){
        return receiveEventCb;
    }
    */
    public static void setGetEventCallback(GetEventCallback cb){
        getEventCb = cb;
    }
    public static GetEventCallback getGetEventCallback(){
        return getEventCb;
    }

    
    /* Available API List */
    
    public static void setGetAvailableApiListCallback(GetAvailableApiListCallback cb){
    	getAvailableApiListCb = cb;
    }
    public static GetAvailableApiListCallback getGetAvailableApiListCallback(){
    	return getAvailableApiListCb;
    }

    /* Take Picture */
    
    public static void setActTakePictureCallback(ActTakePictureCallback cb){
    	actTakePictureCb = cb;
    }
    public static ActTakePictureCallback getActTakePictureCallback(){
    	return actTakePictureCb;
    }
    
    public static void setAwaitTakePictureCallback(AwaitTakePictureCallback cb){
    	awaitTakePictureCb = cb;
    }
    public static AwaitTakePictureCallback getAwaitTakePictureCallback(){
    	return awaitTakePictureCb;
    }
    
    /* REC Mode */
    
    public static void setStartRecModeCallback(StartRecModeCallback cb){
    	startRecModeCb = cb;
    }    
    public static StartRecModeCallback getStartRecModeCallback(){
    	return startRecModeCb;
    }
    
    public static void setStopRecModeCallback(StopRecModeCallback cb){
    	stopRecModeCb = cb;
    }
    public static StopRecModeCallback getStopRecModeCallback(){
    	return stopRecModeCb;
    }
    
    
    /* Liveview */
    
    public static void setStartLiveviewCallback(StartLiveviewCallback cb){
    	startLiveviewCb = cb;
    }    
    public static StartLiveviewCallback getStartLiveviewCallback(){
    	return startLiveviewCb;
    }
    
    public static void setStopLiveviewCallback(StopLiveviewCallback cb){
    	stopLiveviewCb = cb;
    }
    public static StopLiveviewCallback getStopLiveviewCallback(){
    	return stopLiveviewCb;
    }

    
    /* Exposure Compensation */
    
    public static void setSetExposureCompensationCallback(SetExposureCompensationCallback cb){
    	setExposureCompensationCb = cb;
    }
    public static SetExposureCompensationCallback getSetExposureCompensationCallback(){
    	return setExposureCompensationCb;
    }
    
    public static void setGetExposureCompensationCallback(GetExposureCompensationCallback cb){
    	getExposureCompensationCb = cb;
    }
    public static GetExposureCompensationCallback getGetExposureCompensationCallback(){
    	return getExposureCompensationCb;
    }
    
    public static void setGetSupportedExposureCompensationCallback(GetSupportedExposureCompensationCallback cb){
    	getSupportedExposureCompensationCb = cb;
    }
    public static GetSupportedExposureCompensationCallback getGetSupportedExposureCompensationCallback(){
    	return getSupportedExposureCompensationCb;
    }
    
    public static void setGetAvailableExposureCompensationCallback(GetAvailableExposureCompensationCallback cb){
    	getAvailableExposureCompensationCb = cb;
    }
    public static GetAvailableExposureCompensationCallback getGetAvailableExposureCompensationCallback(){
    	return getAvailableExposureCompensationCb;
    }
    
    
    /* Self Timer */
    
    public static void setSetSelfTimerCallback(SetSelfTimerCallback cb){
    	setSelfTimerCb = cb;
    }
    public static SetSelfTimerCallback getSetSelfTimerCallback(){
    	return setSelfTimerCb;
    }
    
    public static void setGetSelfTimerCallback(GetSelfTimerCallback cb){
    	getSelfTimerCb = cb;
    }
    public static GetSelfTimerCallback getGetSelfTimerCallback(){
    	return getSelfTimerCb;
    }
    
    public static void setGetSupportedSelfTimerCallback(GetSupportedSelfTimerCallback cb){
    	getSupportedSelfTimerCb = cb;
    }
    public static GetSupportedSelfTimerCallback getGetSupportedSelfTimerCallback(){
    	return getSupportedSelfTimerCb;
    }
    
    public static void setGetAvailableSelfTimerCallback(GetAvailableSelfTimerCallback cb){
    	getAvailableSelfTimerCb = cb;
    }
    public static GetAvailableSelfTimerCallback getGetAvailableSelfTimerCallback(){
    	return getAvailableSelfTimerCb;
    }
    public static CancelTouchAFPositionCallback getCancelTouchAFPositionCallback() {
    	return cancelTouchAFPositionCallback;
 	}
	public static SetTouchAFPositionCallback getSetTouchAFPositionCallback() {
		return setTouchAFPositionCallback;
	}
	public static GetTouchAFPositionCallback getGetTouchAFPositionCallback() {
		return getTouchAFPositionCallback;
	}
	public static void setCancelTouchAFPositionCallback(CancelTouchAFPositionCallback cb) {
		cancelTouchAFPositionCallback = cb;
    }
    public static void setSetTouchAFPositionCallback(SetTouchAFPositionCallback cb) {
        setTouchAFPositionCallback = cb;
    }
    public static void setGetTouchAFPositionCallback(GetTouchAFPositionCallback cb) {
       getTouchAFPositionCallback = cb;
    }
 }
