package com.sony.scalar.webapi.service.camera.v1_1.viewangle;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableViewAngleCallback extends Callbacks {

	void returnCb(int current, int[] angle);

}
