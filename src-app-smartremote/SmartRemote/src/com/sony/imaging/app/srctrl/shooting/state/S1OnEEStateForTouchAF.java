package com.sony.imaging.app.srctrl.shooting.state;

import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 */
public class S1OnEEStateForTouchAF extends S1OnEEStateEx {	
    public static final String STATE_NAME="S1OnEEStateForTouchAF";
    private static final String tag = S1OnEEStateForTouchAF.class.getName();
	
    @Override
    protected void setAppCondition() {
        StateController stateController = StateController.getInstance();
        stateController.setState(this);
        stateController.setAppCondition(AppCondition.SHOOTING_REMOTE_TOUCHAF);
    }
    
    @Override
    protected String getMfAssistStateName()
    {
        return S1OnEEStateForTouchAFAssist.STATE_NAME;
    }

	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
}
