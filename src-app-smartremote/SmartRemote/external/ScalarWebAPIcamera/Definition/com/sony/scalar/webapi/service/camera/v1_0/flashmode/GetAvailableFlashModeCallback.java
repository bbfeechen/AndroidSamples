package com.sony.scalar.webapi.service.camera.v1_0.flashmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableFlashModeCallback extends Callbacks {

	void returnCb(String current, String[] flash);

}
