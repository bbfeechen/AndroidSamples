package com.sony.scalar.webapi.service.camera.v1_0.beepmode;

import com.sony.mexi.webapi.Service;

public interface GetBeepMode extends Service {
	public int getBeepMode(GetBeepModeCallback returnCb);
}
