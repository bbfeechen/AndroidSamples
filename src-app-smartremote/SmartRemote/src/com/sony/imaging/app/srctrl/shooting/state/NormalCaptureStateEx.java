package com.sony.imaging.app.srctrl.shooting.state;

import android.util.Log;

import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.shooting.NormalCaptureState;
import com.sony.imaging.app.srctrl.caution.InfoEx;
import com.sony.imaging.app.srctrl.shooting.state.CaptureStateUtil.SRCtrlKeyDispatchForDLT06;
import com.sony.imaging.app.srctrl.util.SRCtrlKikiLogUtil;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;
import com.sony.imaging.app.srctrl.webapi.specific.ShutterListenerNotifier;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.ShutterListener;

/**
 * 
 * Added state reporting function and ShutterListener notifying function.
 * @author 0000138134
 *
 */
public class NormalCaptureStateEx extends NormalCaptureState {
    private static final String TAG = NormalCaptureStateEx.class.getName();

    private CaptureStateUtil mUtil = new CaptureStateUtil();
	
	@Override
	public void onResume() {
	    if(!mUtil.isReadyToTakePicture()) {
	        setNextState(EEStateEx.STATE_NAME, null);
            CautionUtilityClass.getInstance().setDispatchKeyEvent(InfoEx.CAUTION_ID_SMART_REMOTE_RSRC_ID_STRID_MSG_NO_MEMCARD, new SRCtrlKeyDispatchForDLT06(null));
            CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_RSRC_ID_STRID_MSG_NO_MEMCARD);
            ShootingHandler.getInstance().setShootingStatus(ShootingStatus.FAILED);
	        return;
	    }
	    
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
	}

    private void createShutterListener() {
	    shutterListener = new ShutterListener() {		
		@Override
		public void onShutter(int status, CameraEx cam) {
		    mCaptureStatus = status;
		    
		    onShuttered(status, cam);

			ShutterListenerNotifier notifier = mUtil.getNotifier();
			if(notifier != null){
				notifier.onShutterNotify(status, cam);
                SRCtrlKikiLogUtil.logRemoteShooting();
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
        Log.v(TAG, "setNotifier in NomalCaptureStateEx");
        mUtil.setNotifier(notifier);
    }
    
    @Override
    protected boolean isTakePictureByRemote()
    {
        return CaptureStateUtil.remote_shooting_mode;
    }
}
