package com.sony.imaging.app.srctrl.network.waiting.waiting;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

public class NwWaitingKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwWaitingKeyHandler.class.getSimpleName();

	@Override
	public int pushedMenuKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		((NetworkRootState)target).getActivity().finish();
		return HANDLED;
	}

}
