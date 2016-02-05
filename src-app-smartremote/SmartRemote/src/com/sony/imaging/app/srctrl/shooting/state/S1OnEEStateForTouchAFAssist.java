package com.sony.imaging.app.srctrl.shooting.state;

import android.os.Message;

import com.sony.imaging.app.base.shooting.MfAssistState;
import com.sony.imaging.app.srctrl.util.OperationReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

public class S1OnEEStateForTouchAFAssist extends MfAssistState
{
    public static final String STATE_NAME = "S1OnEEStateForTouchAFAssist";
    private static final String tag = S1OnEEStateForTouchAF.class.getName();
    
    private static final String SWITCH_MODE_TO_TOUCHAF_EE = "com.sony.imaging.app.srctrl.shooting.state.S1OnEEStateForTouchAFAssist_TO_S1OnEEStateForTouchAF";
    
    @Override
    protected String getTranslationMode()
    {
        return SWITCH_MODE_TO_TOUCHAF_EE;
    }
    
    protected void setAppCondition()
    {
        StateController stateController = StateController.getInstance();
        stateController.setState(this);
        stateController.setAppCondition(AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        setAppCondition();
    }
    
    @Override
    public boolean handleMessage(Message msg)
    {
        if (SWITCH_MODE_TO_TOUCHAF_EE.equals(msg.obj))
        {
            return false; // why false? copied from EEState#handleMessage()
        }
        
        return super.handleMessage(msg);
    }
    
    /**
     * removes own state.
     */
    @Override
    public void removeState() {
        //container.removeChildState(this.handle);
        OperationReceiver.changeToS1OnStateForTouchAF();
    }
    
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
    
    
}
