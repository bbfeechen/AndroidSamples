package com.sony.imaging.app.srctrl.menu.layout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sony.imaging.app.base.menu.layout.BaseMenuLayout;
import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.caution.IkeyDispatchEach;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.fw.AppRoot.USER_KEYCODE;
import com.sony.imaging.app.base.menu.layout.LastBastionMenuLayout;

/**
 * This layout will be displayed when {@link EachShootingMenuState#canRemoveState} returns {@code false}, when menu state is closing.<br>
 * <p>
 * This is sample implementation.<br>
 * You should implement this layout for your own application.
 * <p>
 * <b>Explanation of sample implementation</b><br>
 * <ul>
 * <li>At {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, this layout creates empty view.
 * <li>At {@link #onResume()}, this layout shows a caution screen of {@link Caution.CAUTION_ID_INVALID_FUNCTION}.<br>
 * <li>In order to limit key event handling, an implementation of {@link CautionUtilityClass.IkeyDispatchEach} is set to {@link CautionUtilityClass}.<br>
 * The Other way to limit key event is to overide {@link #onKeyDown(int, KeyEvent)} and {@link #onKeyUp(int, KeyEvent)}.
 * </ul>
 *
 */
public class LastBastionMenuLayoutEx extends LastBastionMenuLayout {
	private static final String TAG = LastBastionMenuLayoutEx.class.getName();

	//TODO key trigger for PJOne
	protected IkeyDispatchEach getKeyHandler() {
    	final IkeyDispatchEach changeModeKeyHandler = super.getKeyHandler();
		IkeyDispatchEach myChangeModeKeyHandler = new IkeyDispatchEach(null, null) {

			@Override
			public int pushedPlayBackKey() {
				Log.i(TAG, "pushedPlayBackKey -> INVALID");				
				return THROUGH;
			}

			@Override
			public int pushedMovieRecKey() {
				return changeModeKeyHandler.pushedMovieRecKey();
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
			public int pushedS1Key() {
				return changeModeKeyHandler.pushedS1Key();
			}

			@Override
			public int releasedS1Key() {
				return changeModeKeyHandler.releasedS1Key();
			}
			
			// IMDLAPP6-1452
			@Override
			public int turnedEVDial(){
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
