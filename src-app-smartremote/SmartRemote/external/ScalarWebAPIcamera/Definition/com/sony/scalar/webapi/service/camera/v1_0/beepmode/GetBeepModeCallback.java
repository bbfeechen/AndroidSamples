package com.sony.scalar.webapi.service.camera.v1_0.beepmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetBeepModeCallback extends Callbacks {

	public void returnCb(String currentBeepMode);

}
