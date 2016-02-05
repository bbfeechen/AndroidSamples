package com.sony.imaging.app.srctrl.webapi.specific;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.HandoffOperationInfo;
import com.sony.imaging.app.srctrl.util.MediaObserverAggregator;
import com.sony.imaging.app.srctrl.util.OperationRequester;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.ShutterListener;

/**
 * This class supports using shooting functions from WebAPI.
 * Transit from EE State to Capture State.
 * Wait for generating screennail images.
 * Store screennail images to PostviewResourceLoader.
 * @author 0000138134
 *
 */
public class ShootingHandler {
	private static final String TAG = ShootingHandler.class.getSimpleName();
	
	private static ShootingHandler sHandler = new ShootingHandler();
    public static ShootingHandler getInstance(){
		return sHandler;
	}
	
	private ShootingStatus shootingStatus = ShootingStatus.READY;
	private WaitingStatus waitingStatus = WaitingStatus.NOT_WAITING;
	private Object sync = new Object();
	
	/**
	 * 
	 * Remote shooting server status
	 * @author 0000138134
	 *
	 */
	public enum ShootingStatus {
		READY, PROCESSING, DEVELOPING, FINISHED, FAILED
	}
	
	/**
	 * 
	 * actTakePicture or awaitTakePicture's working status
	 * @author 0000138134
	 *
	 */
	public enum WaitingStatus {
		NOT_WAITING, WAITING, CANCELED
	}
	
	/**
	 * 
	 * Shooting Error status
	 * @author 0000138134
	 *
	 */
	public enum Error {
		UNKNOWN, CANCELED, JPEG_TIMEOUT
	}
	private Error error = Error.UNKNOWN;
	
    // Media observer aggregator will be set from ShootingStateEx.
    private WeakReference<MediaObserverAggregator> mediaObserverAggregatorRef;
    public void setMediaObserverAggregator(MediaObserverAggregator aggregator) {
        mediaObserverAggregatorRef = new WeakReference<MediaObserverAggregator>(aggregator);
    }
    public MediaObserverAggregator getMediaObserverAggregator() {
        if(null == mediaObserverAggregatorRef) {
            return null;
        }
        return mediaObserverAggregatorRef.get();
    }
    
	/**
	 * 
	 * <pre>
	 * Extended ShutterListener to inform Error of shutter.
	 * </pre>
	 * 
	 */
	public final class ShutterListenerEx implements ShutterListenerNotifier{

		@Override
		public void onShutterNotify(int status, CameraEx cam) {
			if (status == ShutterListener.STATUS_OK){
				Log.v(TAG, "onShutter: Success");
			} else {
				if (status == ShutterListener.STATUS_CANCELED){
					Log.v(TAG, "onShutter: Canceled");
					error = Error.CANCELED;
				} else if (status == ShutterListener.STATUS_ERROR){
					Log.v(TAG, "onShutter: Error");
					error = Error.UNKNOWN;
				}
				setShootingStatus(ShootingStatus.FAILED);
			}
		}
		
	}
    
    /**
     * 
     * Start capturing and waiting finish of capturing.
     * Or Start waiting finish of capturing.
     * @return　ShootingStatus after waiting is finished.
     */
    public ShootingStatus createStillPicture(){
		if(WaitingStatus.WAITING.equals(waitingStatus)){
			Log.v(TAG, "Wait until previous one is stopped");
			synchronized(sync){  // TODO sync is unlocked among several references(1)
				try {
					onDoublePolling();
					sync.wait(SRCtrlConstants.RECEIVE_EVENT_WAIT_PREVIOUS_TIMEOUT);  // TODO SHOULD_NOT wait 5 sec to ensure the state.
					Log.v(TAG, "Previous one is finished: " + waitingStatus.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
					setShootingStatus(ShootingStatus.FAILED);
					return shootingStatus;
				}
			}
		}
    	
        if(!ShootingStatus.PROCESSING.equals(shootingStatus) &&
                !ShootingStatus.DEVELOPING.equals(shootingStatus)
                ){ // TODO NO NEED to lock the shootingStatus variable? (1)
            error = Error.UNKNOWN;
    		takePicture();
    	}
    	
        long remainingWaitTime = SRCtrlConstants.TAKE_PICTURE_TIMEOUT;
        synchronized(sync){  // TODO sync is unlocked among several references(2)
            waitingStatus = WaitingStatus.WAITING;
            while(remainingWaitTime > 0 &&
                  (ShootingStatus.PROCESSING == shootingStatus ||
                   ShootingStatus.DEVELOPING == shootingStatus)) {
                try {
                    long startWaitTime = System.currentTimeMillis();
                    sync.wait(remainingWaitTime);
                    long endWaitTime = System.currentTimeMillis();
                    remainingWaitTime -= endWaitTime - startWaitTime;
                    if(remainingWaitTime < 0) {
                        remainingWaitTime = 0; // break
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    setShootingStatus(ShootingStatus.FAILED);
                    return shootingStatus;
                }
            }
    	}

        if(!WaitingStatus.CANCELED.equals(waitingStatus)){
            waitingStatus = WaitingStatus.NOT_WAITING;
        }

		synchronized(sync){  // TODO sync is unlocked among several references(3)
			sync.notifyAll();
		}
		
		Log.v(TAG, waitingStatus.toString());
		
		return shootingStatus;
    }
    
    private void takePicture(){
        setShootingStatus(ShootingStatus.PROCESSING);
		
		Boolean terminate_caution_result = new OperationRequester<Boolean>().request(HandoffOperationInfo.EXCUTE_TERMINATE_CAUTION, (Object)null); 
		
		Boolean move_capture_state_result = new OperationRequester<Boolean>()
                .request(HandoffOperationInfo.MOVE_TO_CAPTURE_STATE, new ShutterListenerEx()); 
		if(null == move_capture_state_result || !move_capture_state_result.booleanValue()){
		    setShootingStatus(ShootingStatus.FAILED);
		}
    }
    
    /**
     * Returns current shooting error status
     * @return
     */
    public Error getErrorStatus(){
    	return error;
    }
    
    /**
     * Returns current shooting status.
     * @return
     */
    public ShootingStatus getShootingStatus(){
    	return shootingStatus;
    }

    /**
     * Returns current actTakePicture/awaitTakePicture status.
     * @return
     */
    public WaitingStatus getWaitingStatus(){
    	return waitingStatus;
    }
    
	public void onPauseCalled() {
		Log.v(TAG, "detected onPause is called.");
		setShootingStatus(ShootingStatus.FAILED);
	}
	
	/**
	 * Use if awaitTakePicture is called while act/awaitTakePicture is working.
	 * Previous one will be returned with Error code 40402.
	 */
	public void onDoublePolling() {
		synchronized(sync){
			Log.v(TAG, "detected double Polling.");
			waitingStatus = WaitingStatus.CANCELED;
			sync.notifyAll();
		}
	}
	
	public void setShootingStatus(ShootingStatus status)
	{
	    synchronized(sync) {
	        Log.v(TAG, "Changing Shooting Status from " + shootingStatus.name() + " to + " + status.name() + ".");
	        shootingStatus = status;
	        sync.notify();
	    }
	}

    private ArrayList<String> urlList = new ArrayList<String>();
    private ArrayList<String> urlListOnMemory = new ArrayList<String>();

    /**
     * Returns Url array of stored postview images.
     * @return
     */
    public String[] getUrlArrayResult(){
        synchronized(urlList) {
            return (String[])urlList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
        }
    }
    
    public void addToUrlList(String url)
    {
        synchronized(urlList) {
            urlList.add(url);
        }
    }
    public void clearUrlList()
    {
        synchronized(urlList) {
            urlList.clear();
        }
    }
    
    public String[] getUrlArrayOnMemoryResult() {
        synchronized(urlListOnMemory)
        {
            return urlListOnMemory.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
        }
    }
    
    public void addToUrlListOnMemory(String url)
    {
        synchronized(urlListOnMemory) {
            urlListOnMemory.add(0, url);
        }
    }
    
    public void notifyPictureUrl()
    {
        synchronized(urlList) {
            ParamsGenerator.updateTakePictureParams(urlList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY));
        }
        ServerEventHandler.getInstance().onServerStatusChanged();
    }
}
