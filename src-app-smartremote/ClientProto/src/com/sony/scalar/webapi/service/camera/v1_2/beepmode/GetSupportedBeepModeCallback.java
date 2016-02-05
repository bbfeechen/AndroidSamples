package com.sony.scalar.webapi.service.camera.v1_2.beepmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedBeepModeCallback extends Callbacks {

	public void returnCb(String[] mode);

}
