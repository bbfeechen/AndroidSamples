package com.sony.imaging.app.srctrl.shooting.state;

import android.app.Activity;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.common.MediaNotificationManager;
import com.sony.imaging.app.base.menu.MenuTable;
import com.sony.imaging.app.base.shooting.ShootingState;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.DigitalZoomController;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.base.shooting.camera.ExposureCompensationController;
import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.base.shooting.camera.FlashController;
import com.sony.imaging.app.base.shooting.camera.FocusModeController;
import com.sony.imaging.app.base.shooting.camera.PictureSizeController;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.srctrl.liveview.LiveviewContainer;
import com.sony.imaging.app.srctrl.liveview.LiveviewLoader;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.shooting.camera.SRCtrlPictureQualityController;
import com.sony.imaging.app.srctrl.util.MediaObserverAggregator;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;
import com.sony.imaging.app.util.AppInfo;

/**
 * 
 * Change AppInfo to shooting mode.
 * State reporting function.
 * Start/Stop Liveview obtaining thread.
 * Force APO OFF.
 * 
 * @author 0000138134
 *
 */
public class ShootingStateEx extends ShootingState {
	private MediaObserverAggregator mMediaObservers;
	
	private static final String tag = ShootingStateEx.class.getName();
	
	class ExecutorInitThread extends Thread {
		@Override
		public void run() {
			SRCtrlExecutorCreator.getInstance().init();
		}
	}
	
	private IdleHandler mCameraParameterNotifier;
	private IdleHandler mMenuTreeCreator = new IdleHandler() {
        @Override
        public boolean queueIdle() {
            MenuTable.getInstance().createDefaultMenuTree(getActivity());
            return false;
        }
    };
	
	@Override
	public void onResume(){
        ParamsGenerator.startCameraSettingListener();

        LiveviewLoader.clean();

        StateController stateController = StateController.getInstance();
        stateController.setState(this);
        stateController.setAppCondition(AppCondition.SHOOTING_INHIBIT);
        stateController.setHasModeDial(ModeDialDetector.hasModeDial());

        super.onResume();

		StateController sc = StateController.getInstance();
		sc.setDuringCameraSetupRoutine(true); // Idle handler of SRCtrlStableLayout will cancel this flag.

		Activity activity = getActivity();
		AppInfo.notifyAppInfo(activity.getApplicationContext(), activity.getPackageName(), activity.getClass().getName(),
				AppInfo.LARGE_CATEGORY_REC, AppInfo.SMALL_CATEGORY_STILL,
				BaseApp.PULLING_BACK_KEYS_FOR_SHOOTING, BaseApp.RESUME_KEYS_FOR_SHOOTING);
		
		mCameraParameterNotifier = new IdleHandler() {
            private boolean mExposureModeNotified = false;
            private boolean mFocusModeNotified = false;
            private boolean mSelfTimerNotified = false;
            private boolean mExposureCompensationNotified = false;
            private boolean mFlashModeNotified = false;
            private boolean mZoomInformationNotified = false;
            
            @Override
            public boolean queueIdle() {
                CameraNotificationManager manager = CameraNotificationManager.getInstance();
                if(!mExposureModeNotified) {
                    ExposureModeController emc = ExposureModeController.getInstance();
                    String modeInBase = emc.getValue(ExposureModeController.EXPOSURE_MODE);
                    if(null == modeInBase) {
                        Log.v(tag, "Exposure Mode was not initilaized...  Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.SCENE_MODE);
                        mExposureModeNotified = true;
                    }
                }
                if(!mFocusModeNotified) {
                    FocusModeController fmc = FocusModeController.getInstance();
                    String focusModeInBase = fmc.getValue();
                    if(null == focusModeInBase) {
                        Log.v(tag, "Focus Mode was not initialized yet... Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.FOCUS_CHANGE);
                        mFocusModeNotified = true;
                    }
                }
                if(!mSelfTimerNotified) {
                    DriveModeController dmc = DriveModeController.getInstance();
                    String value = dmc.getValue(DriveModeController.SELF_TIMER);
                    if(null == value) {
                        Log.v(tag, "Selftimer was not initialized yet... Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.SELFTIMER_COUNTDOWN_STATUS);
                        mSelfTimerNotified = true;
                    }
                }
                if(!mExposureCompensationNotified) {
                    ExposureCompensationController ecc = ExposureCompensationController.getInstance();
                    String value = null;
                    try{
                        value = ecc.getValue(null);
                    } catch (Exception e) {
                        // ignore
                    }
                    if(null == value) {
                        Log.v(tag, "ExposureCompensation was not initialized yet... Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.EXPOSURE_COMPENSATION);
                        mExposureCompensationNotified = true;
                    }
                }
                if(!mFlashModeNotified) {
                	FlashController fmc = FlashController.getInstance();
                    String value = fmc.getValue(FlashController.FLASHMODE);
                    if(null == value) {
                        Log.v(tag, "Flash Mode was not initialized yet... Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.FLASH_CHANGE);
                        mFlashModeNotified = true;
                    }
                }
                if(!mZoomInformationNotified) {
                	DigitalZoomController dzc = DigitalZoomController.getInstance();
                	int optPositon =  dzc.getOpticalZoomPosition();
                	int digPositon =  dzc.getDigitalZoomPosition();
                	if ((optPositon == -1) && (digPositon == -1)) {
                        Log.v(tag, "Zoom Information was not initialized yet... Try again.");
                    } else {
                        manager.requestNotify(CameraNotificationManager.ZOOM_INFO_CHANGED);
                        mZoomInformationNotified = true;
                    }
                }
                
                return !(mExposureModeNotified && mFocusModeNotified && mSelfTimerNotified && mExposureCompensationNotified && mFlashModeNotified && mZoomInformationNotified);
            }
        };
		Looper.myQueue().addIdleHandler(mCameraParameterNotifier);

		Looper.myQueue().addIdleHandler(mMenuTreeCreator);
		
		LiveviewContainer.getInstance().setPictureAspectRatio(
				PictureSizeController.getInstance().getValue(PictureSizeController.MENU_ITEM_ID_ASPECT_RATIO));
		
		mMediaObservers = new MediaObserverAggregator();
		mMediaObservers.start(activity, getHandler());
		ShootingHandler.getInstance().setMediaObserverAggregator(mMediaObservers);
		
		// IMDLAPP6-1443
		MediaNotificationManager.getInstance().updateRemainingAmount();
	}
	
	@Override
	public void onPause(){
	    Looper.myQueue().removeIdleHandler(mCameraParameterNotifier);
	    mCameraParameterNotifier = null; // onetime
	    Looper.myQueue().removeIdleHandler(mMenuTreeCreator); // recycle
	    
        ParamsGenerator.stopCameraSettingListener();

	    ShootingHandler.getInstance().setMediaObserverAggregator(null);
		ShootingExecutor.setJpegListener(null);
		mMediaObservers.stop();
		mMediaObservers = null;
		
		CautionUtilityClass.getInstance().executeTerminate();

        LiveviewLoader.clean();
        super.onPause();
	}
	
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
}
