package com.sony.imaging.app.srctrl.shooting.state;

import com.sony.imaging.app.base.shooting.ExposureModeCheckState;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.common.EVDialDetector;
import com.sony.imaging.app.base.common.EventParcel;
import com.sony.imaging.app.base.shooting.camera.ExposureCompensationController;
import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.caution.IkeyDispatchEach;
import com.sony.imaging.app.base.caution.Info;
import com.sony.imaging.app.util.BeepUtility;


/**
 * This class representing Each's force setting.
 * 
 */
public class ExposureModeCheckStateEx extends ExposureModeCheckState {
	private static final String TAG = ExposureModeCheckStateEx.class.getName();
	
	@Override
	public int pushedPlayBackKey() {
		Log.i(TAG, "pushedPlayBackKey -> INVALID");
		return INVALID;
	}

	@Override
	public int pushedPlayIndexKey() {
		Log.i(TAG, "pushedPlayIndexKey -> INVALID");
		return INVALID;
	}

    /**
     * Return KeyDispatcher that is used by Caution displaying on-boot timing.
     * If return Null, default key dispatcher is worked.
     * @return key dispatcher
     */
    protected IkeyDispatchEach getKeyHandler() {
    	final IkeyDispatchEach changeModeKeyHandler = super.getKeyHandler();
    	IkeyDispatchEach myChangeModeKeyHandler = new IkeyDispatchEach() {

			@Override
			public int pushedPlayBackKey() {
				BeepUtility.getInstance().playBeep("BEEP_ID_SELECT");
				return INVALID;
			}

			@Override
			public int pushedMovieRecKey() {
				return changeModeKeyHandler.pushedMovieRecKey();
			}

			@Override
			public int pushedFnKey() {
				return changeModeKeyHandler.pushedFnKey();
			}

			@Override
			public int turnedModeDial() {
				return changeModeKeyHandler.turnedModeDial();
			}

			@Override
			public int pushedMenuKey() {
				return changeModeKeyHandler.pushedMenuKey();
			}
			
			@Override
			public int turnedEVDial() {
				return changeModeKeyHandler.turnedEVDial();
			}

			// IMDLAPP6-1452
			@Override
			public int turnedFocusModeDial() {
				return changeModeKeyHandler.turnedFocusModeDial();
			}
    	};    	
    	return myChangeModeKeyHandler;
    }
}
