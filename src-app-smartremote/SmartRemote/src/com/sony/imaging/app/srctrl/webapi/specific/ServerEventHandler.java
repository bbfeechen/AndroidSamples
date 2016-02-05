package com.sony.imaging.app.srctrl.webapi.specific;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;

import android.util.Log;

/**
 * 
 * This class will just wait for 15 seconds.
 * If server status changed or 15 seconds taken, finish waiting..
 * @author 0000138134
 * 
 */
public class ServerEventHandler {
	private static final String TAG = ServerEventHandler.class.getSimpleName();
	
	private static ServerEventHandler sHandler = new ServerEventHandler();
	
	public static ServerEventHandler getInstance(){
		return sHandler;
	}
	
	/**
	 * 
	 * receiveEvent server status
	 * @author 0000138134
	 *
	 */
	public enum Status {
		SUCCESS, CANCELED, FAILED, WAITING
	}
	
	private Status status, returnStatus;
	private Object sync;
	private boolean isStatusChanged;
		
	public ServerEventHandler(){
		sync = new Object();
		status = Status.SUCCESS;
	}
	
	/**
	 * Start waiting until Change of server status or timeout.
	 * @return
	 */
	public Status startWaiting(){		
        Log.v(TAG, "Start waiting for the event update...");
        synchronized(sync){
		if(Status.WAITING.equals(status)){
			Log.v(TAG, "Wait until previous one is stopped");
				try {
					onDoublePollingHappened();
					sync.wait(SRCtrlConstants.RECEIVE_EVENT_WAIT_PREVIOUS_TIMEOUT);
					Log.v(TAG, "Previous one is finished: " + status.toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		setStatusChanged(false);

			try {
                Log.v(TAG, "Event update status is " + Status.WAITING + " changed from " + status);
				status = Status.WAITING;
				Log.v(TAG, "Waiting for event update....");
				sync.wait(SRCtrlConstants.RECEIVE_EVENT_TIMEOUT);
				Log.v(TAG, "Finish waiting for event update.");
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException when waiting.");
                Log.v(TAG, "Event update status is " + Status.FAILED + " changed from " + status);
				status = Status.FAILED;
				return status;
			}
			if(Status.WAITING.equals(status)){
                Log.v(TAG, "Event update status is " + Status.SUCCESS + " changed from " + Status.WAITING);
				setReturnStatus(Status.SUCCESS);
			}
		
        setStatusChanged(false);
		
			sync.notifyAll();
		
		Log.v(TAG, returnStatus.toString());
		
		return returnStatus;
        }
	}
	
	public void onDoublePollingHappened() {
		synchronized(sync){
			Log.v(TAG, "detected double polling");
			setReturnStatus(Status.CANCELED);
			sync.notifyAll();
		}
	}

	public void onServerStatusChanged() {
		// MK141H-15247
		Log.v(TAG, "server status. " + StateController.getInstance().getServerStatus().toString());
			synchronized(sync){
				// MK141H-15247
//				Log.v(TAG, "detected change of server status. " + StateController.getInstance().getServerStatus().toString());
				Log.v(TAG, "detected change of server status.");

				setReturnStatus(Status.SUCCESS);
		        setStatusChanged(true);
				sync.notify();
			}
	}

	public void onPauseCalled() {
		synchronized(sync){
			Log.v(TAG, "detected onPause.");
			setReturnStatus(Status.FAILED);
			sync.notify();
		}
	}	
    
	/*
	 * Returns current receiveEvent status.
	 */
    public Status getStatus(){
    	return status;
    }
    
    /*
     * Returns whether server status is changed after previous receiveEvent.
     */
    public boolean isStatusChanged(){
    	return isStatusChanged;
    }
    
    /*
     * Set server status is changed or not.
     */
    public void setStatusChanged(boolean bool){
        Log.v(TAG, "The updated flag for GetEvent is set to " + bool);
    	isStatusChanged = bool;
    }
    
    private void setReturnStatus(Status status){
        Log.v(TAG, "The return status for GetEvent is set to " + status.name());
    	this.status = status;
    	returnStatus = status;
    }
}
