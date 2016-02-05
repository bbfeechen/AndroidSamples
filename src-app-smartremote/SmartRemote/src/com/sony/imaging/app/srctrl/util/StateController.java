package com.sony.imaging.app.srctrl.util;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.fw.RunStatus;
import com.sony.imaging.app.fw.State;
import com.sony.imaging.app.srctrl.SRCtrlRootState;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OffEEStateKeyHandlerEx;
import com.sony.imaging.app.srctrl.shooting.state.CaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.NormalCaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.S1OffEEStateEx;
import com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateForTouchAF;
import com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateForTouchAFAssist;
import com.sony.imaging.app.srctrl.shooting.state.SelfTimerCaptureStateEx;
import com.sony.imaging.app.srctrl.shooting.state.ShootingMenuStateEx;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationTouchAFPosition;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.RecModeTransitionHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShutterListenerEx;

/**
 * 
 * Manages the state of application.
 * Manages the state of WebAPI inhibit.
 * Controls state changing from WebAPI.
 * 
 * @author 0000138134
 *
 */
public class StateController {
	private static final String TAG = StateController.class.getSimpleName();
	private static StateController sStateController = new StateController();
	
	private State state;
	private WeakReference<Context> mApplicationContextRef;

	public enum AppCondition {PREPARATION, SHOOTING_EE, SHOOTING_MENU, SHOOTING_INHIBIT, SHOOTING_LOCAL, SHOOTING_REMOTE, DIAL_INHIBIT, SHOOTING_REMOTE_TOUCHAF, SHOOTING_REMOTE_TOUCHAFASSIST};
	public enum ServerStatus {IDLE, NOT_READY, STILL_CAPTURING};			// added [1st update]
	
	private AppCondition appCondition;
	private ServerStatus servStat;
	private boolean backTransitionFlag;

	private boolean isInitialWifiSetup;
	private boolean isFatalError;
	private boolean isDdServerReady;
	
	private boolean isShooting;
	private boolean isWaitingStatusChange;
	private boolean isWaitingRecModeChange;
	
	private boolean isClosingShootingState;
	
	private boolean isErrorByTimeout;
	private boolean isRestartForNewConfig;

	private boolean hasModeDial;
	
	private boolean isDuringCameraSetupRoutine = false;
		
	private ShutterListenerEx listener;
	
	private OperationReceiver receiver;
	
	/** A remembered condition that was before capturing.
	 * 
	 * This condition is either:
	 * <ul>
	 * <li> {@link AppCondition#SHOOTING_REMOTE}
	 * <li> or  {@link AppCondition#SHOOTING_REMOTE_TOUCHAF}
	 * </ul>
	 */
	private AppCondition mLastAppConditionBeforeCapturing;
	
	public static StateController getInstance(){
		return sStateController;
	}
	
	public StateController(){
		init();
	}
	
	public synchronized void init(){
		appCondition = AppCondition.PREPARATION;
		servStat = ServerStatus.NOT_READY;
		isDdServerReady = false;
		isInitialWifiSetup = true;
		
		isErrorByTimeout = false;
		isRestartForNewConfig = false;
		isFatalError = false;
		isShooting = false;
		isWaitingStatusChange = false;
		isWaitingRecModeChange = false;
		isClosingShootingState = false;
		hasModeDial = false;
		
		initOperationReceiver();
		mLastAppConditionBeforeCapturing = AppCondition.SHOOTING_REMOTE;
	}
	
	public synchronized void initOperationReceiver(){
		receiver = new OperationReceiver();
	}	
	
	public synchronized void setState (State state){
		this.state = state;
		Activity act = state.getActivity();
		if(null != act) {
		    this.mApplicationContextRef = new WeakReference<Context>(act.getApplicationContext());
		}
	}
	public synchronized State getState() {
		// This state could be null so caller should check it.
		return state;
	}
	public synchronized Context getApplicationContext() {
	    if(null == mApplicationContextRef) {
	        return null;
	    }
	    return mApplicationContextRef.get();
	}
	
	/**
	 * Finish ShootingState.
	 * @return
	 */
	public synchronized boolean changeToNetworkState(){
		if(checkState()){
			if(appCondition.equals(AppCondition.PREPARATION)){
				Log.v(TAG, "Already in NwState");
				return false;
			} else {
				Log.v(TAG, "Changing to Network State");
				setIsClosingShootingState(true);
				//SRCtrlRootState.wsController.unbind();        // Moved to SRCtrlRootState
				if(AppCondition.SHOOTING_EE.equals(appCondition)){
				    if(state instanceof S1OffEEStateEx) {
				        ((S1OffEEStateEx) state).setNextState(SRCtrlRootState.APP_NETWORK, null);
				    } else {
				        Log.e(TAG, "Inconsistent state, S1OffEEStateEx state was expected in SHOOTING_EE state.");
				    }
				} else if(AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(appCondition)) {
                    CameraOperationTouchAFPosition.leaveTouchAFMode(false);
                    ((S1OnEEStateForTouchAF) state).setNextState(SRCtrlRootState.APP_NETWORK, null);
				} else if(AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST.equals(appCondition)) {
                    CameraOperationTouchAFPosition.leaveTouchAFMode(false);
                    ((S1OnEEStateForTouchAFAssist) state).setNextState(SRCtrlRootState.APP_NETWORK, null);
				} else if(AppCondition.DIAL_INHIBIT.equals(appCondition)){
					((S1OffEEStateEx) state).setNextState(SRCtrlRootState.APP_NETWORK, null);
				} else if(AppCondition.SHOOTING_MENU.equals(appCondition)){
					((ShootingMenuStateEx) state).setNextState(SRCtrlRootState.APP_NETWORK, null);
				} else {
					return false;
				}				
                // Dismiss exit screen
                BaseApp act = (BaseApp)state.getActivity();
                if(null != act) {
                    act.dismissExitScreen();
                }
				return true;
			}
		} else {
			Log.v(TAG, "State is null");
			return false;
		}
	}
	
    /**
     * Launch ShootingState.
     * @return
     */
    public synchronized boolean changeToShootingState(){
        if(checkState()){
            if(AppCondition.PREPARATION.equals(appCondition)){
                Log.v(TAG, "Changing to Shooting State from " + AppCondition.PREPARATION.name());
                if(RunStatus.RUNNING != RunStatus.getStatus()) {
                    Log.e(TAG, "  RunStatus is not RUNNING.  State transition will be canceled.");
                    return false;
                }

                ((NetworkRootState)state).setNextState(SRCtrlRootState.APP_SHOOTING, null);
                // Dismiss exit screen
                BaseApp act = (BaseApp)state.getActivity();
                if(null != act) {
                    act.dismissExitScreen();
                }
                
                return true;
            } else if(AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(appCondition)) {
                if(state instanceof S1OnEEStateForTouchAF) {
                    Log.v(TAG, "Changing to Shoogint State from " + AppCondition.SHOOTING_REMOTE_TOUCHAF.name());
                    ((S1OnEEStateForTouchAF)state).setNextState(SRCtrlRootState.APP_SHOOTING, null);
                    return true;
                } else if(state instanceof S1OnEEStateForTouchAFAssist) {
                    Log.v(TAG, "Changing to Shoogint State from " + AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST.name());
                    ((S1OnEEStateForTouchAF)state).setNextState(SRCtrlRootState.APP_SHOOTING, null);
                    return true;
                } else {
                    Log.e(TAG, "State is incorrect: " + state.toString());
                }
            } else {
                Log.e(TAG, "Already in ShootingState");
            }
        } else {
            Log.e(TAG, "State is null");
        }
        return false;
    }

	/**
	 * Start still image capturing.
	 * @param listener
	 * @return
	 */
	public synchronized boolean changeToCaptureState(ShutterListenerEx listener){
		if(checkState()){
			this.listener = listener;
			if(AppCondition.SHOOTING_REMOTE.equals(appCondition)){
				Log.v(TAG, "Changing to Capture State");
				/*
				Instrumentation mInstrumentation = new Instrumentation();
				mInstrumentation.sendKeySync(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_UNKNOWN, 0, 0, 0, USER_KEYCODE.S2_ON));
				*/
			    if(true == S1OffEEStateKeyHandlerEx.s_pushed_S2) {
			        Log.e(TAG, "S2 has been pushed on the camera.");
			        return false;
			    } else if(!(state instanceof S1OffEEStateEx)&&
			            !(state instanceof S1OnEEStateForTouchAF)&&
			            !(state instanceof S1OnEEStateForTouchAFAssist)) {
                    Log.e(TAG, "The current state is not S1OffEEStateEx|S1OnEEStateForTouchAF|S1OnEEStateForTouchAFAssist.  State=" + state.toString());
                    return false;
				} else { 
                    state.setNextState(CaptureStateEx.STATE_NAME, null);
                    return true;                
				}
			} else {
				Log.e(TAG, "Shooting is Unavailable");
				return false;				
			}
		} else {
			Log.e(TAG, "State is null");
			return false;
		}
	}
	
    /**
     * Launch S1OffEEState
     * @return
     */
    public synchronized boolean changeToS1OffEEState(){
        if(checkState()){
            if(AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(appCondition) ||
                    AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST.equals(appCondition)) {
                if(state instanceof S1OnEEStateForTouchAF) {
                    Log.v(TAG, "Changing to Shoogint State from " + AppCondition.SHOOTING_REMOTE_TOUCHAF.name());
                    ((S1OnEEStateForTouchAF)state).setNextState(S1OffEEStateEx.STATE_NAME, null);
                    return true;
                }
                else if(state instanceof S1OnEEStateForTouchAFAssist) {
                    Log.v(TAG, "Changing to Shoogint State from " + AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST.name());
                    ((S1OnEEStateForTouchAFAssist)state).setNextState(S1OffEEStateEx.STATE_NAME, null);
                    return true;
                } else {
                    Log.e(TAG, "State is incorrect: " + state.toString());
                }
            } else {
                Log.e(TAG, "State is not touch AF: " + state.toString());
            }
        } else {
            Log.e(TAG, "State is null");
        }
        return false;
    }

	public synchronized boolean changeToS1OnEEStateForTouchAF() {
	    if(checkState()) {
	        if(AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(appCondition) || 
	           AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST.equals(appCondition) ) {
	            if(state instanceof S1OnEEStateForTouchAF) {
	                Log.v(TAG, "Already in S1OnEEStateForTouchAF...  Request ignored.");
	            } else {
	                Log.v(TAG, "Changing to " + S1OnEEStateForTouchAF.STATE_NAME);
	                state.setNextState(S1OnEEStateForTouchAF.STATE_NAME, null);
	            }
	            return true;
	        } else {
	            Log.e(TAG, "Touch AF is unnablable.");
	            return false;
	        }
	    } else {
	        Log.e(TAG, "State is null");
	        return false;
	    }
	}
	
	private synchronized boolean checkState(){
		if(state == null){
			return false;
		} else {
			return true;
		}
	}
	
	public synchronized void setAppCondition(AppCondition aAppCondition){
	    AppCondition currentAppCondition = this.appCondition;
		if((this.appCondition == AppCondition.PREPARATION && aAppCondition == AppCondition.SHOOTING_INHIBIT)
				|| aAppCondition == AppCondition.PREPARATION
				){
			RecModeTransitionHandler.getInstance().onChangedOperatingMode();
		}
		if(!this.appCondition.equals(aAppCondition)){
			this.appCondition = aAppCondition;
			Log.v(TAG, "Switched AppCondition from "+currentAppCondition.name()+" to " + aAppCondition.name());
            boolean toBeNotified = ParamsGenerator.updateAvailableApiList();
            if(toBeNotified)
            {
                ServerEventHandler.getInstance().onServerStatusChanged();
            }
		}
		setServerStatus(aAppCondition);									// added [1st update]
	}
	
	private synchronized void setServerStatus(AppCondition appCondition){			// added [1st update]
		ServerStatus currentServStat = servStat;
		switch(appCondition){
			case SHOOTING_EE:
            case SHOOTING_REMOTE_TOUCHAF:
            case SHOOTING_REMOTE_TOUCHAFASSIST:
            case SHOOTING_INHIBIT:
            case SHOOTING_MENU:
				servStat = ServerStatus.IDLE;
				break;
			case SHOOTING_REMOTE:
				servStat = ServerStatus.STILL_CAPTURING;
				break;
            case SHOOTING_LOCAL:        // TODO TBD
			case PREPARATION:
			case DIAL_INHIBIT:
			default:
				servStat = ServerStatus.NOT_READY;
				break;
		}
		if(!servStat.equals(currentServStat)){
			Log.v(TAG, "Switched ServerStatus from " + currentServStat.name() + " to " + servStat.name());
            ParamsGenerator.updateAvailableApiList();
            ServerEventHandler.getInstance().onServerStatusChanged();
		}
	}
	
	public synchronized AppCondition getAppCondition(){
		return appCondition;
	}
	
	public synchronized ServerStatus getServerStatus(){
		return servStat;
	}
	
	public synchronized void setNotifierOnCaptureState(){
		if(listener != null){
			Log.v(TAG, "setNotifierOnCaptureState");
			if(DriveModeController.getInstance().isSelfTimer()){
				((SelfTimerCaptureStateEx) state).setNotifier(listener);
			} else {
				((NormalCaptureStateEx)state).setNotifier(listener);
			}
			listener = null;
		}
	}
	
	public synchronized void setGoBackFlag(boolean bool){
        Log.v(TAG, "StateController#setGoBackFlag(bool="+bool+") was called.");
		backTransitionFlag = bool;
	}	
	public synchronized boolean isWaitingBackTransition(){
		return backTransitionFlag;
	}
	
	public synchronized  void setInitialWifiSetup(boolean bool){
		isInitialWifiSetup = bool;
	}	
	public synchronized boolean isInitialWifiSetup(){
		return isInitialWifiSetup;
	}
	
	public synchronized void setFatalError(boolean bool){
		isFatalError = bool;
	}	
	public synchronized boolean isFatalError(){
		return isFatalError;
	}
	
	public synchronized void setDdServerReady(boolean bool){
		isDdServerReady = bool;
	}	
	public synchronized boolean isDdServerReady(){
		return isDdServerReady;
	}
	
	public synchronized void setShooting(boolean bool){
		isShooting = bool;
	}	
	public synchronized boolean isShooting(){
		return isShooting;
	}

	public synchronized void setWaitingStatusChange(boolean bool){
		isWaitingStatusChange = bool;
	}	
	public synchronized boolean isWaitingStatusChange(){
		return isWaitingStatusChange;
	}

	public synchronized void setWaitingRecModeChange(boolean bool){
		isWaitingRecModeChange = bool;
	}	
	public synchronized boolean isWaitingRecModeChange(){
		return isWaitingRecModeChange;
	}
	
	public synchronized void setIsClosingShootingState(boolean bool){
		isClosingShootingState = bool;
	}	
	public synchronized boolean isClosingShootingState(){
		return isClosingShootingState;
	}	
	
	public synchronized void setIsErrorByTimeout(boolean bool){
		isErrorByTimeout = bool;
	}
	public synchronized boolean isErrorByTimeout(){
		return isErrorByTimeout;
	}
	
	public synchronized void setIsRestartForNewConfig(boolean bool){
		isRestartForNewConfig = bool;
	}
	public synchronized boolean isRestartForNewConfig(){
		return isRestartForNewConfig;
	}
	
	public synchronized void onPauseCalled(){
		ServerEventHandler.getInstance().onPauseCalled();
		ShootingHandler.getInstance().onPauseCalled();
		RecModeTransitionHandler.getInstance().onPauseCalled();
	}
	
	public synchronized OperationReceiver getReceiver(){
		return receiver;
	}
	
	public synchronized void setHasModeDial(boolean bool){
		hasModeDial = bool;
	}
	
	public synchronized boolean hasModeDial(){
		return hasModeDial;
	}
	
	public synchronized boolean isDuringCameraSetupRoutine() {
	    return isDuringCameraSetupRoutine;
	}
	
	public synchronized void setDuringCameraSetupRoutine(boolean duringCameraSetupRoutine) {
	    isDuringCameraSetupRoutine = duringCameraSetupRoutine;
	}
	
	public synchronized AppCondition getLastAppConditionBeforeCapturing() {
        return mLastAppConditionBeforeCapturing;
	}
	
	public synchronized void setLastAppConditionBeforeCapturing(AppCondition appCondition) {
	    if(AppCondition.SHOOTING_REMOTE == appCondition ||
	           AppCondition.SHOOTING_REMOTE_TOUCHAF == appCondition ||
	           AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST == appCondition) {
	        mLastAppConditionBeforeCapturing = appCondition;
	    } else {
	        Log.e(TAG, "Invalid parameter: " + appCondition.name());
	    }
	}
}
