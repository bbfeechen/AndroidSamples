package com.sony.scalar.webapi.service.camera.v1_1.recmode;

import com.sony.mexi.webapi.Callbacks;


public interface StartRecModeCallback extends Callbacks {

	void returnCb(int ret);

}