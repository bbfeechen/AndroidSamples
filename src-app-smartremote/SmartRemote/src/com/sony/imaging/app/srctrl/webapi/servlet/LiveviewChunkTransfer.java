package com.sony.imaging.app.srctrl.webapi.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.content.res.AssetManager;
import android.util.Log;

import com.sony.imaging.app.srctrl.liveview.LiveviewLoader;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.mexi.orb.servlet.OrbChunkTransfer;

public class LiveviewChunkTransfer extends OrbChunkTransfer /* implements Serializable */{
	private static final long SEND_BLOCKING_TIMEOUT = 10000; // [msec]
	private static final long IDLE_SLEEP_TIMEOUT = LiveviewLoader.LIVEVIEW_OBTAINING_INTERVAL; // 50 [msec]

	private OutputStream sendingStream = null;
	private LiveviewChunkTransfer sendingMutex = null;
	private volatile long latestSendingTime = -1;

	private static final String TAG = "LiveviewChunkTransfer";
	private static final long serialVersionUID = 1L;
	
	public LiveviewChunkTransfer() {
		sendingMutex = this;
	}

	public static void setAssertManager(AssetManager am) {
	}

	public void notifyGetScalarInfraIsKilled() {
		Log.v(TAG, "GetScalarInfra is killed.");
	}

	public String getRootPath() {
		return SRCtrlConstants.SERVLET_ROOT_PATH_LIVEVIEW;
	}

	@Override
	protected String getContentType(String uri) {
		return super.getContentType(uri);
	}
	
	public void forceDisconnect(){
		Log.v(TAG, "Force disconnect chunked transfer.");
		OutputStream os = sendingStream;
		if (os != null) {
			cancel();
			//Log.v(TAG, "forceDisconnect OutputStream.close.");
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
				Log.v(TAG, "forceDisconnect OutputStream.close(), IOException.");
			}
			
			//Log.v(TAG, "forceDisconnect closeConnection().");
			try {
				closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
				Log.v(TAG, "forceDisconnect closeConnection(), IOException.");
			}
			
		}
		//Log.v(TAG, "forceDisconnect end.");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Log.v(TAG, "Accepted HTTP GET for Liveview");
		if (sendingStream != null) {
			Log.v(TAG, "Force disconnect older HTTP GET.");
			forceDisconnect();
		}
		synchronized(sendingMutex) {
			Log.v(TAG, "Start chunked transfer.");

			sendingStream = res.getOutputStream();
			LiveviewLoader.setLiveviewChunkTransferInstance(this);

			try {
				super.doGet(req, res);
			} catch (Exception e) {
				e.printStackTrace();
				Log.v(TAG, "doGet() caught exception.");
			}

			LiveviewLoader.setLiveviewChunkTransferInstance(null);
			sendingStream = null;

			Log.v(TAG, "End chunked transfer.");
		}
	}

	@Override
	protected boolean doChunkTransfer(HttpServletRequest req, HttpServletResponse res) {
		OutputStream os = null;
		try {
			os = res.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
			Log.v(TAG, "HttpServletResponse.getOutputStream() IOException.");
			cancel();
			return false;
		} catch (IllegalStateException e2) {
			Log.v(TAG, "HttpServletResponse.getOutputStream() IllegalStateException.");
			cancel();
			return false;
		}
		
		LiveviewLoader.LiveviewData liveviewData = LiveviewLoader.getLiveviewData();
		if (liveviewData != null) {
		    latestSendingTime = System.currentTimeMillis();
			if (os != null) {
				try {
					os.write(liveviewData.headerData, 0, liveviewData.headerDataSize);
					os.write(liveviewData.jpegData, 0, liveviewData.jpegDataAndPaddingSize);
					res.flushBuffer();
				} catch (SocketException e1) {
					Log.v(TAG, "Sending JPEG data, SocketException.");
					e1.printStackTrace();
					cancel();
					latestSendingTime = -1;
					return false;
				} catch (IOException e2) {
					Log.v(TAG, "Sending JPEG data, IOException.");
					e2.printStackTrace();
					cancel();
					latestSendingTime = -1;
					return false;
				} catch (IllegalStateException e) {
					Log.v(TAG, "Liveview Loader is not generating liveview data.  Stop chunked transfer.");
					cancel();
					return false;
				}
			}
			
			long blockingTime = System.currentTimeMillis() - latestSendingTime;
			latestSendingTime = -1;
			if (blockingTime > 180) {
			    Log.v(TAG, "Sending was blocked " + blockingTime + " [msec]");
			}

			latestSendingTime = -1;
		} else {

            try {
		        Thread.sleep(LiveviewLoader.LIVEVIEW_OBTAINING_INTERVAL);
            } catch (InterruptedException e){
                Log.e(TAG, "sleep() InterruptedException.");
                cancel();
                return false;
            }
		}
		
        try {
            // Sleep when the camera or other status is busy.
            if(!sleepDuringCameraStartup()) {
                sleepByState();
            }
        } catch (InterruptedException e){
            Log.e(TAG, "sleep() for idle InterruptedException.");
            cancel();
            return false;
        }
            
	    return true;
	}
	
	private boolean sleepByState() throws InterruptedException {
        StateController state_controller = StateController.getInstance();
        AppCondition ac = state_controller.getAppCondition();
        if(AppCondition.SHOOTING_EE != ac && AppCondition.SHOOTING_REMOTE_TOUCHAF != ac && AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST != ac) {
            Thread.sleep(IDLE_SLEEP_TIMEOUT*3);
            return true;
        }
        return false;
	}
	
	private boolean sleepDuringCameraStartup() throws InterruptedException {
        StateController state_controller = StateController.getInstance();
        if(state_controller.isDuringCameraSetupRoutine()) {
            Thread.sleep(IDLE_SLEEP_TIMEOUT*5);
            return true;
        }
	    return false;
	}

	public boolean isSendBlocking() {
		long startSendingTime = latestSendingTime;
		if (startSendingTime > 0) {
			long blockingTime = System.currentTimeMillis() - startSendingTime;
			if (blockingTime > SEND_BLOCKING_TIMEOUT) {
				Log.v(TAG, "Sending is still blocked " + String.valueOf(blockingTime) + " msec.");
				return true;
			}
		}
		return false;
	}
}
