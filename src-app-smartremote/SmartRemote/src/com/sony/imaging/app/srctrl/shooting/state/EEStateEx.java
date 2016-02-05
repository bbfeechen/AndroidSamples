package com.sony.imaging.app.srctrl.shooting.state;

import android.util.Log;

import com.sony.imaging.app.base.shooting.EEState;
import com.sony.imaging.app.srctrl.shooting.ModeDialCautionCaller;
import com.sony.imaging.app.srctrl.shooting.ModeDialCautionCaller.CautionType;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;

/**
 * 
 * Added mode dial caution displaying function.
 * @author 0000138134
 *
 */
public class EEStateEx extends EEState {
    private static final String tag = EEStateEx.class.getName();
    public static final String STATE_NAME = "EE";
	@Override
	public void onResume(){
		super.onResume();

/*		ModeDialCautionCaller cautionCaller = new ModeDialCautionCaller();
		cautionCaller.checkModeDialCautionRequirement();		
		if(cautionCaller.requireModeDialCaution()){
			if(StateController.getInstance().hasModeDial()){
				cautionCaller.showChangeModeDialCaution(getActivity(), CautionType.ON_EE);
			}
		}
*/	}
	
	@Override
	public void onPause(){
		super.onPause();
	}

    @Override
    protected String getNextChildState()
    {
        StateController stateController = StateController.getInstance();
        AppCondition appLastCondition = stateController.getLastAppConditionBeforeCapturing();
        if(AppCondition.SHOOTING_REMOTE_TOUCHAF == appLastCondition ||
                AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST == appLastCondition
                ) {
            Log.v(tag, "Next state screen is " + AppCondition.SHOOTING_REMOTE_TOUCHAF.name());
            stateController.setAppCondition(AppCondition.SHOOTING_REMOTE_TOUCHAF);
            stateController.setLastAppConditionBeforeCapturing(AppCondition.SHOOTING_REMOTE); // reset
            return S1OnEEStateForTouchAF.STATE_NAME;
        }
        return super.getNextChildState();
    }
}
