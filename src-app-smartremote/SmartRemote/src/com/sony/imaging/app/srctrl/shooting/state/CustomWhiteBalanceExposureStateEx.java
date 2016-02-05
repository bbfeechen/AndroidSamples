package com.sony.imaging.app.srctrl.shooting.state;

import com.sony.imaging.app.base.shooting.CustomWhiteBalanceExposureState;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 * 
 * Forced APO OFF.
 * @author 0000138134
 *
 */
public class CustomWhiteBalanceExposureStateEx extends CustomWhiteBalanceExposureState {
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
}
