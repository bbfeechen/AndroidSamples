package com.sony.scalar.webapi.service.camera.v1_0.selftimer;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableSelfTimerCallback extends Callbacks {

	void returnCb(int current, int[] timer);

}
