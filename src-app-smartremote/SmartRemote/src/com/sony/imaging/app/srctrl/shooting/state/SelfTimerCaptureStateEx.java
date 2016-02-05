package com.sony.imaging.app.srctrl.shooting.state;

import android.util.Log;

import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.common.MediaNotificationManager;
import com.sony.imaging.app.base.shooting.SelfTimerCaptureState;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.srctrl.caution.InfoEx;
import com.sony.imaging.app.srctrl.shooting.state.CaptureStateUtil.SRCtrlKeyDispatchForDLT06;
import com.sony.imaging.app.srctrl.util.SRCtrlKikiLogUtil;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;
import com.sony.imaging.app.srctrl.webapi.specific.ShutterListenerNotifier;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.ShutterListener;

/**
 * 
 * Added state reporting function and ShutterListener notifying function.
 * @author 0000138134
 *
 */
public class SelfTimerCaptureStateEx extends SelfTimerCaptureState {
	private static final String TAG = SelfTimerCaptureStateEx.class.getSimpleName();

	private CaptureStateUtil mUtil = new CaptureStateUtil();
	
	private NotificationListener mMediaNotificationListener = new NotificationListener() {

        @Override
        public String[] getTags()
        {
            return new String[]{MediaNotificationManager.TAG_MEDIA_STATUS_CHANGE};
        }

        @Override
        public void onNotify(String tag)
        {
            if(MediaNotificationManager.getInstance().isNoCard()) {
                if(!mUtil.isReadyToTakePicture())
                {
                    abortSelfTimer();
                    MediaNotificationManager.getInstance().removeNotificationListener(mMediaNotificationListener);
                    mMediaNotificationListener = null;
                }
            }
        }};
        
    private void abortSelfTimer() {
        cancelSelfTimer(null);
        setNextState(EEStateEx.STATE_NAME, null);
        CautionUtilityClass.getInstance().setDispatchKeyEvent(InfoEx.CAUTION_ID_SMART_REMOTE_RSRC_ID_STRID_MSG_NO_MEMCARD, new SRCtrlKeyDispatchForDLT06(null));
        CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_RSRC_ID_STRID_MSG_NO_MEMCARD);
        ShootingHandler.getInstance().setShootingStatus(ShootingStatus.FAILED);
    }

	@Override
	public void onResume() {
	    createShutterListener();
        mUtil.init();
	    CaptureStateUtil.remote_shooting_mode = false;
		StateController stateController = StateController.getInstance();
		if(!AppCondition.SHOOTING_REMOTE.equals(stateController.getAppCondition())){
			stateController.setAppCondition(AppCondition.SHOOTING_LOCAL);
		}
		stateController.setState(this);
		stateController.setNotifierOnCaptureState();
		
        super.onResume();

		CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.SELFTIMER_COUNTDOWN_STATUS, true);
		
		if(!mUtil.isReadyToTakePicture())
		{
		    abortSelfTimer();
		    mMediaNotificationListener = null;
		}
		else {
		    MediaNotificationManager.getInstance().setNotificationListener(mMediaNotificationListener);
		}
	}

    @Override
    public void onPause() {
        if(null != mMediaNotificationListener) {
            MediaNotificationManager.getInstance().removeNotificationListener(mMediaNotificationListener);
        }
        CameraNotificationManager.getInstance().requestNotify(CameraNotificationManager.SELFTIMER_COUNTDOWN_STATUS, false);
        super.onPause();
    }

	private void createShutterListener() {
	    shutterListener = new ShutterListener() {
		@Override
		public void onShutter(int status, CameraEx cam) {
            mCaptureStatus = status;
            
            onShuttered(status, cam);
		    
			ShutterListenerNotifier notifier = mUtil.getNotifier();
			if(notifier != null){
                SRCtrlKikiLogUtil.logRemoteShooting();
				notifier.onShutterNotify(status, cam);
			} else {
			    SRCtrlKikiLogUtil.logLocalShooting();
                if(ShutterListener.STATUS_OK != status) {
    				cam.cancelTakePicture();
    				cam.getNormalCamera().cancelAutoFocus();
                    ShootingHandler.getInstance().setShootingStatus(ShootingStatus.FAILED);
                }
			}
			
            notifier = null;
		}
	    };
	}
	
    public void setNotifier(ShutterListenerNotifier notifier){
        Log.v(TAG, "setNotifier in SelfTimerCaptureStateEx");
        mUtil.setNotifier(notifier);
    }   

    @Override
    protected boolean isTakePictureByRemote()
    {
        return CaptureStateUtil.remote_shooting_mode;
    }
}
