package com.sony.scalar.webapi.service.camera.v1_1.intervalstillrec;

import com.sony.mexi.webapi.Callbacks;


public interface StopIntervalStillRecCallback extends Callbacks {

	void returnCb(int ret);

}