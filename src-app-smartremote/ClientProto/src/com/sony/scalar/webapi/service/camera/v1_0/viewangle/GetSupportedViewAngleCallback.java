package com.sony.scalar.webapi.service.camera.v1_0.viewangle;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedViewAngleCallback extends Callbacks {

	void returnCb(int[] angle);

}
