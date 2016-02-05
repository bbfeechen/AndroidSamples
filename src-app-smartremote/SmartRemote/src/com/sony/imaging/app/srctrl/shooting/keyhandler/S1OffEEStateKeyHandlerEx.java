package com.sony.imaging.app.srctrl.shooting.keyhandler;

import com.sony.imaging.app.base.shooting.trigger.S1OffEEStateKeyHandler;

/**
 * 
 * Make invalid unsupported keys.
 * @author 0000138134
 *
 */
public class S1OffEEStateKeyHandlerEx extends S1OffEEStateKeyHandler {
    public static boolean s_pushed_S2 = false;
	@Override
	public int pushedPlayBackKey(){
		return INVALID;
	}
	
	@Override
	public int pushedIRShutterKey() {
		return INVALID;
	}

	@Override
	public int pushedIR2SecKey() {
		return INVALID;
	}

    @Override
    public int pushedS2Key()
    {
        if(false == s_pushed_S2) {
            s_pushed_S2 = true;
            return super.pushedS2Key();
        } else {
            return INVALID;
        }
    }
}
