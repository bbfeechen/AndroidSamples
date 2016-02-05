package com.sony.scalar.webapi.service.camera.v1_2.beepmode;

import com.sony.mexi.webapi.Service;

public interface GetAvailableBeepMode extends Service {
	public int getAvailableBeepMode(GetAvailableBeepModeCallback returnCb);
}
