package com.sony.scalar.webapi.service.camera.v1_1.flashmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetFlashModeCallback extends Callbacks {

	void returnCb(String flash);

}
