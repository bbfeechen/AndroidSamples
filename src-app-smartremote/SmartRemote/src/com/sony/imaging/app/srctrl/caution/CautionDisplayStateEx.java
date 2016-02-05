package com.sony.imaging.app.srctrl.caution;

import com.sony.imaging.app.base.caution.CautionDisplayState;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 * 
 * This class makes APO disabled while Caution is displayed.
 * @author 0000138134
 *
 */
public class CautionDisplayStateEx extends CautionDisplayState {
	@Override
	public APO_TYPE getApoType() {
	  return APO_TYPE.NONE;
	}
}
