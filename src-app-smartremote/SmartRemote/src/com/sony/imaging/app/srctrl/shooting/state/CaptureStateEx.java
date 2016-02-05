package com.sony.imaging.app.srctrl.shooting.state;

import com.sony.imaging.app.base.shooting.CaptureState;
import com.sony.imaging.app.srctrl.shooting.keyhandler.S1OffEEStateKeyHandlerEx;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 * 
 * Forced APO OFF.
 * @author 0000138134
 *
 */
public class CaptureStateEx extends CaptureState {
    public static final String STATE_NAME = "Capture";

    @Override
    public void onResume() {
        S1OffEEStateKeyHandlerEx.s_pushed_S2 = false;
        super.onResume();
    }
    
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
}
