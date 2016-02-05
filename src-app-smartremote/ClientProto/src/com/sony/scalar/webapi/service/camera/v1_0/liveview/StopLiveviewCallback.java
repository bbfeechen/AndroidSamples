package com.sony.scalar.webapi.service.camera.v1_0.liveview;

import com.sony.mexi.webapi.Callbacks;


public interface StopLiveviewCallback extends Callbacks {

	void returnCb(int ret);

}
