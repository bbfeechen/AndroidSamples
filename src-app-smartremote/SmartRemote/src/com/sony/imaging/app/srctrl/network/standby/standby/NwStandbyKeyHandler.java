package com.sony.imaging.app.srctrl.network.standby.standby;

import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.network.base.NwKeyHandlerBase;
import com.sony.imaging.app.util.BeepUtility;
import com.sony.imaging.app.util.BeepUtilityRsrcTable;

public class NwStandbyKeyHandler extends NwKeyHandlerBase {
	public static final String TAG = NwStandbyKeyHandler.class.getSimpleName();

	@Override
	public int pushedMenuKey() {
		BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
		((NetworkRootState)target).getActivity().finish();
		return HANDLED;
	}
}
