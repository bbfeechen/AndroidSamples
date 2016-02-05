package com.sony.imaging.app.srctrl.shooting.keyhandler;

import android.util.Log;

import com.sony.imaging.app.base.shooting.trigger.MfAssistKeyHandler;

public class MfAssistKeyHandlerEx extends MfAssistKeyHandler {
	private static final String TAG = MfAssistKeyHandlerEx.class.getName();
	
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

}
