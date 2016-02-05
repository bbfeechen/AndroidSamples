package com.sony.imaging.app.srctrl.shooting;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.caution.IkeyDispatchEach;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.fw.AppRoot.USER_KEYCODE;
import com.sony.imaging.app.fw.CustomizableFunction;
import com.sony.imaging.app.fw.IKeyFunction;
import com.sony.imaging.app.srctrl.caution.InfoEx;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

/**
 * 
 * This class will provide functions related to the "Change Mode Dial to Another" caution.
 * @author 0000138134
 *
 */
public class ModeDialCautionCaller {
	private static final String TAG = ModeDialCautionCaller.class.getSimpleName();
	private boolean requireCaution;
	
	public static enum CautionType {
		UNKNOWN, ON_EE, ON_DIAL 
	}

	public ModeDialCautionCaller(){
		requireCaution = false;
	}
	
	/**
	 * 
	 * Checks Mode Dial position and returns whether the caution is required or not.
	 */
	public void checkModeDialCautionRequirement(){
		if(StateController.getInstance().hasModeDial()){		// show caution in case of invalid shoot-mode dial position
			switch(ModeDialDetector.getModeDialPosition()){
				case USER_KEYCODE.MODE_DIAL_AUTO:
				case USER_KEYCODE.MODE_DIAL_PROGRAM:
				case USER_KEYCODE.MODE_DIAL_AEA:
				case USER_KEYCODE.MODE_DIAL_AES:
				case USER_KEYCODE.MODE_DIAL_MANUAL:
				case USER_KEYCODE.MODE_DIAL_SCN:
					Log.v(TAG, "ModeDial: SUPPORTED");
					requireCaution = false;
					break;
				default:
					Log.v(TAG, "ModeDial: NOT SUPPORTED");
					requireCaution = true;
					break;
			}
		} else {		// It is impossible to detect the change to Auto-mode in w/o dial model.
			//showModeChangedCaution();
			requireCaution = false;
		}
	}
	
	/**
	 * 
	 * @return Whether ModeDial caution is required or not.
	 */
	public boolean requireModeDialCaution(){
		return requireCaution;
	}
	
	public void resetModeDialRequirement(){
		requireCaution = false;
	}
	
	/**
	 * 
	 * @param activity Activity instance to call {@link Activity#finish()} when SK1 is pushed.
	 * @param type CautionType whether on EE or ModeDialMenu.
	 */
	public void showChangeModeDialCaution(final Activity activity, CautionType type){
		if(CautionType.ON_EE.equals(type)){
			showChangeModeDialCautionOnEe(activity);
		} else if(CautionType.ON_DIAL.equals(type)){
			showChangeModeDialCautionOnDial();			
		} else {
			Log.e(TAG, "UNKNOWN CAUTION TYPE");
		}
	}
	
	/**
	 * 
	 * Supposed to be used to display Caution on EE screen.
	 * @param activity Activity instance to call {@link Activity#finish()} when SK1 is pushed.
	 */
	private void showChangeModeDialCautionOnEe(final Activity activity){
		Log.v(TAG, "showChangeModeDialCautionOnEE");
		IkeyDispatchEach changeModeKey = new IkeyDispatchEach(null, null) {
            @Override
            public int onConvertedKeyDown(KeyEvent event, IKeyFunction func)
            {
                int result = HANDLED;

                if(CustomizableFunction.Unchanged.equals(func)){
                    switch (event.getScanCode()) {
                    case USER_KEYCODE.MODE_DIAL_CHANGED:            // Caution will disappear, if user changed the location of Mode Dial
                        //CautionUtilityClass.getInstance().disapperTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_CHANGE_DIAL_TO_ANOTHER_EE);
                        CautionUtilityClass.getInstance().executeTerminate();
                        requireCaution = false;
                        result = INVALID;
                        break;
                    case USER_KEYCODE.SK1:          // Application will close, if user pushed SK1
                        BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
                        activity.finish();
                        break;
                    case USER_KEYCODE.S1_OFF:
                        result = INVALID;
                        break;
                    case USER_KEYCODE.S1_ON:
                        result = INVALID;
                        break;
                    default:        // Other Keys are disabled
                        break;
                    }
                }
                return result;
            }

            @Override
            public int onConvertedKeyUp(KeyEvent event, IKeyFunction func)
            {
                int result = HANDLED;
                int code = event.getScanCode();

                if(CustomizableFunction.Unchanged.equals(func)){
                    switch(code){
                    case USER_KEYCODE.S1_ON:
                        result = INVALID;
                        break;
                    default:
                        break;
                    }
                }
                return result;
            }
		};
		CautionUtilityClass.getInstance().setDispatchKeyEvent(InfoEx.CAUTION_ID_SMART_REMOTE_CHANGE_DIAL_TO_ANOTHER_EE, changeModeKey);
		CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_CHANGE_DIAL_TO_ANOTHER_EE);
		StateController.getInstance().setAppCondition(AppCondition.DIAL_INHIBIT);
	}
	
	/**
	 * Supposed to be used to display caution on Mode Dial Menu.
	 */
	private void showChangeModeDialCautionOnDial(){
		Log.v(TAG, "showChangeModeDialCautionOnDial");
		CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_CHANGE_DIAL_TO_ANOTHER_MENU);
	}
	
	/**
	 * Supposed to be used to display caution on EE for non ModeDial model.
	 */
	public void showModeChangedCaution(){
		CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_WILL_CHANGE_TO_AUTO);
	}
}
