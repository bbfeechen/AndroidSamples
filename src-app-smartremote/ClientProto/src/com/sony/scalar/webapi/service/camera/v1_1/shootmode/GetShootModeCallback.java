package com.sony.scalar.webapi.service.camera.v1_1.shootmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetShootModeCallback extends Callbacks {

	void returnCb(String mode);

}
