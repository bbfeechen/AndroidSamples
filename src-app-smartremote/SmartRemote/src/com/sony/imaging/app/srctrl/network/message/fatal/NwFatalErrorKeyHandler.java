package com.sony.imaging.app.srctrl.network.message.fatal;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

public class NwFatalErrorKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwFatalErrorKeyHandler.class.getSimpleName();
	@Override
	public int pushedCenterKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		((BaseApp)target.getActivity()).finish(false);
		return HANDLED;
	}
}
