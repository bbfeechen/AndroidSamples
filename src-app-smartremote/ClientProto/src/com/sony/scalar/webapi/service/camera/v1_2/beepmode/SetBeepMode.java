package com.sony.scalar.webapi.service.camera.v1_2.beepmode;

import com.sony.mexi.webapi.Service;

public interface SetBeepMode extends Service {
	public int setBeepMode(String BeepMode, SetBeepModeCallback returnCb);
}
