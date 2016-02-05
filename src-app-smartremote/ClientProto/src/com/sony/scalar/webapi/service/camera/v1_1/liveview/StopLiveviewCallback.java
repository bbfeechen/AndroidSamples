package com.sony.scalar.webapi.service.camera.v1_1.liveview;

import com.sony.mexi.webapi.Callbacks;


public interface StopLiveviewCallback extends Callbacks {

	void returnCb(int ret);

}
