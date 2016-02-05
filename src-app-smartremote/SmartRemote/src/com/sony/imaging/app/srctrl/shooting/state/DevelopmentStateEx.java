package com.sony.imaging.app.srctrl.shooting.state;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.sony.imaging.app.base.shooting.DevelopmentState;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.util.MediaObserver;
import com.sony.imaging.app.srctrl.util.MediaObserver.MediaType;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyPostviewImageSize;
import com.sony.imaging.app.srctrl.util.MediaObserverAggregator;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.imaging.app.util.PTag;
import com.sony.scalar.hardware.CameraEx.ReviewInfo;

/**
 * This class represents status about developing image stage in the camera.
 *
 */
public class DevelopmentStateEx extends DevelopmentState {
	private static final String TAG = DevelopmentStateEx.class.getName();
	
	/**
	 * The current state name is used for returning from captureState.
	 */
	protected static final String CURRENT_STATE_NAME = "DevelopmentEx";

	private CameraNotificationManager mCamNtfy = CameraNotificationManager.getInstance();
	public static ReviewInfo s_ReviewInfo;

    private NotificationListener mReviewInfoListener = new NotificationListener() {
        private String[] TAGS = {CameraNotificationManager.PICTURE_REVIEW_INFO};
        @Override
        public String[] getTags()
        {
            return TAGS;
        }
        @Override
        public void onNotify(String tag)
        {
            // Notified twice
            ReviewInfo reviewInfo = (ReviewInfo)mCamNtfy.getValue(CameraNotificationManager.PICTURE_REVIEW_INFO);
            if(null == s_ReviewInfo)
            {
                s_ReviewInfo = reviewInfo;
            }
            else
            {
                if(null == reviewInfo.hist)
                {
                    s_ReviewInfo.photo = reviewInfo.photo;
                }
                else
                {
                    s_ReviewInfo.hist = reviewInfo.hist;
                }
            }
        }
    };
    @Override
    public void onResume() {
        s_ReviewInfo = null;
        mCamNtfy.setNotificationListener(mReviewInfoListener);
        
        super.onResume();
    }
	        
	@Override
	public void onPause() {
	    super.onPause();
	    
        mCamNtfy.removeNotificationListener(mReviewInfoListener);
        try{
            ShootingExecutor.setJpegListener(null);
        } catch (RuntimeException e){
            Log.e(TAG, "RuntimeException at setJpegListner(null)");
        }
        
        getHandler().removeCallbacks(waitFlushFileRunnable);
        
        removeData(CaptureStateUtil.KEY_NUMOFBURSTPICTURES);
        removeData(CaptureStateUtil.KEY_NUMOFFINISHEDPICTURES);
        removeData(CaptureStateUtil.KEY_USEFINALPICTURE);
	}
	
    @Override
	protected void onPreviewStart(){
	    //setNextState(NEXT_EE_STATE, null);
	    PTag.end(PTAG_EE_START);
        waitFlushFileRunnable.postToHandler(NEXT_EE_STATE, 0);
	}

    @Override
	protected void onPictureReveiwStart(){
	    //setNextState(NEXT_AUTOREVIEW_STATE, null);
	    PTag.end(PTAG_DEVELOP_END);
        waitFlushFileRunnable.run();
	}

    //////////////////////////////////////////////////////////////////////////////////////
	private static final int WAIT_FLUSH_FILE_DELAY_MSEC = 100;
	private class WaitFlushFileRunnable implements Runnable{
        String nextState = NEXT_AUTOREVIEW_STATE;
        Bundle returnBundle = null;
        public void run()
        {
            ShootingHandler shootingHandler = ShootingHandler.getInstance();
            if(ShootingStatus.PROCESSING == shootingHandler.getShootingStatus()) {
                Log.v(TAG, "Shooting status is during processing yet.  Wait for the completion...");
                postToHandler(nextState, WAIT_FLUSH_FILE_DELAY_MSEC);
                notifyProgressStatus(0.1);
                return;
            }
            if(false == CaptureStateUtil.jpeg_generation_finished) {
                Log.v(TAG, "onPictureTaken() of JpegListener has not been called during processing yet.  Wait for the completion...");
                postToHandler(nextState, WAIT_FLUSH_FILE_DELAY_MSEC);
                notifyProgressStatus(0.2);
                return;
            }
            if(ShootingStatus.DEVELOPING != shootingHandler.getShootingStatus()) {
                Log.e(TAG, "Invalid state.  ShootingStatus might have been in error already: " + shootingHandler.getShootingStatus().name());
                DevelopmentStateEx.this.setNextState(nextState, returnBundle);
                return;
            }
            
            // check if the process should use actual file or not.
            if(!isPostviewActualFile())
            {
                Log.v(TAG, "No need to wait for the file flush.");
                shootingHandler.setShootingStatus(ShootingStatus.FINISHED);
                DevelopmentStateEx.this.setNextState(nextState, returnBundle);
                if(!CaptureStateUtil.remote_shooting_mode) {
                    shootingHandler.notifyPictureUrl();
                }
                return;
            }
            
            // check if the process should wait for flushing the picture file.
            if(shouldWaitForFlusingFile()) {
                Log.v(TAG, "Need to retry flushing the file.");
                postToHandler(nextState, WAIT_FLUSH_FILE_DELAY_MSEC);
                notifyProgressStatus(0.75);
                return;
            }

            Log.v(TAG, "Complete flushing the file.");
            DevelopmentStateEx.this.setNextState(nextState, returnBundle);
            if(!CaptureStateUtil.remote_shooting_mode) {
                shootingHandler.notifyPictureUrl();
            }
            return;
        }

        void postToHandler(String nextState, long delay) {
            this.nextState = nextState;
            Handler h = getHandler();
            h.removeCallbacks(this);
            h.postDelayed(this, delay); // throw again
        }
	}
	private WaitFlushFileRunnable waitFlushFileRunnable = new WaitFlushFileRunnable();
	
	private boolean shouldWaitForFlusingFile()
	{
        ShootingHandler shootingHandler = ShootingHandler.getInstance();
	    MediaObserverAggregator mediaObservers = shootingHandler.getMediaObserverAggregator();
	    if(null == mediaObservers) {
            Log.e(TAG, "MediaObserverAggregator are needed, but it has gone (=null).");
            shootingHandler.setShootingStatus(ShootingStatus.FAILED);
            return false;
        }

        String mediaId = SRCtrlExecutorCreator.getRecordingMedia();
        mediaObservers.invokeFlushingMediaDatabase(mediaId);
        
        int initialRecordingCount = mediaObservers.getInitialContentsCount(mediaId);
        int currentRecordingCount = mediaObservers.getCurrentContentsCount(mediaId);
        int expectedStoredPictures = mediaObservers.getExpectedStoredPictures(mediaId);
        Log.v(TAG, "CONDITION: Current pics="+currentRecordingCount+", Initial pics=" + initialRecordingCount + ", Expected pics=" + expectedStoredPictures);
        if(MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED == currentRecordingCount ||
                MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED == expectedStoredPictures) {
            // error
            Log.e(TAG, "Media may have been unmounted.");
            shootingHandler.setShootingStatus(ShootingStatus.FAILED);
            return false;
        }
        
        if(currentRecordingCount - initialRecordingCount == expectedStoredPictures) {
            // File flushed.
            Log.v(TAG, "File flushing was done.");
            List<String> files = mediaObservers.getImageFileList(mediaId, CaptureStateUtil.numOfBurstPictures);
            
            if(null == files) {
                // error
                Log.e(TAG, "Cannot retrieve stored files.  Media may have been unmounted.");
                shootingHandler.setShootingStatus(ShootingStatus.FAILED);
                return false;
            }
                
            // clean already added screennail urls
            shootingHandler.clearUrlList();

            // add original file path(s)
            if(CaptureStateUtil.useFinalPicture) {
                shootingHandler.addToUrlList(SRCtrlConstants.POSTVIEW_DIRECTORY_ON_MEMORY + files.get(0));
            }
            for(String file : files) {
                String url = SRCtrlConstants.POSTVIEW_DIRECTORY_ON_MEMORY + file;
                if(!CaptureStateUtil.useFinalPicture) {
                    shootingHandler.addToUrlList(url);
                }
                // backup all pics
                shootingHandler.addToUrlListOnMemory(url);
            }
            shootingHandler.setShootingStatus(ShootingStatus.FINISHED);
            return false;
        } else {
            // retry
            return true;
        }
	}
    
    private boolean isPostviewActualFile() {
        String mediaId = SRCtrlExecutorCreator.getRecordingMedia();

        MediaObserverAggregator mediaObservers = ShootingHandler.getInstance().getMediaObserverAggregator();
        if(null != mediaObservers) {
            MediaType mediaType = mediaObservers.getMediaType(mediaId);
            if (MediaType.INTERNAL == mediaType || MediaType.EXTERNAL == mediaType) {
                if(CameraProxyPostviewImageSize.isSizeOriginal()) {
                    // Furthermore, checking media-rw and media-not_full might be needed.
                    return true;
                } else {
                    Log.e(TAG, "Can't produce the specified size.  Use the default size.");
                }
            }
        }
        return false;
    }
}
