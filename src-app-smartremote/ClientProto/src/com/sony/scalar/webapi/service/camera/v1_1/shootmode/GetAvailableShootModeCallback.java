package com.sony.scalar.webapi.service.camera.v1_1.shootmode;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableShootModeCallback extends Callbacks {

	void returnCb(String current, String[] mode);

}
