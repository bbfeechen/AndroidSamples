package com.sony.scalar.webapi.service.camera.v1_0.liveview;

import com.sony.mexi.webapi.Callbacks;


public interface StartLiveviewCallback extends Callbacks {

	void returnCb(String url);

}
