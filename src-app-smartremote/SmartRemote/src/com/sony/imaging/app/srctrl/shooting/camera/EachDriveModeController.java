package com.sony.imaging.app.srctrl.shooting.camera;

import com.sony.imaging.app.base.shooting.camera.DriveModeController;

public class EachDriveModeController extends DriveModeController {

	@Override
	protected boolean isSupported(String value) {
		if(BURST.equals(value)) return false;
		if(BURST_SPEED_HIGH.equals(value)) return false;
		if(BURST_SPEED_MID.equals(value)) return false;
		if(BURST_SPEED_LOW.equals(value)) return false;
		if(SPEED_PRIORITY_BURST.equals(value)) return false;
		if(SELF_TIMER_10S.equals(value)) return false;
		if(SELF_TIMER_BURST.equals(value)) return false;
		if(SELF_TIMER_BURST_10S_3SHOT.equals(value)) return false;
		if(SELF_TIMER_BURST_10S_5SHOT.equals(value)) return false;
		if(BRACKET.equals(value)) return false;

		return super.isSupported(value);
	}
	
}
