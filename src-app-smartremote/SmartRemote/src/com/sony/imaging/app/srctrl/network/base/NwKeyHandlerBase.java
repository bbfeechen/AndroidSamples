package com.sony.imaging.app.srctrl.network.base;

import com.sony.imaging.app.fw.BaseInvalidKeyHandler;
import com.sony.imaging.app.fw.ICustomKey;


public class NwKeyHandlerBase extends BaseInvalidKeyHandler {

	@Override
	public String getKeyConvCategory() {
		// TODO Auto-generated method stub
		return ICustomKey.CATEGORY_MENU;
	}
}
