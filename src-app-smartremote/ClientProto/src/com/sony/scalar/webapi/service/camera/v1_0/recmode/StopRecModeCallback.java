package com.sony.scalar.webapi.service.camera.v1_0.recmode;

import com.sony.mexi.webapi.Callbacks;


public interface StopRecModeCallback extends Callbacks {

	void returnCb(int ret);

}