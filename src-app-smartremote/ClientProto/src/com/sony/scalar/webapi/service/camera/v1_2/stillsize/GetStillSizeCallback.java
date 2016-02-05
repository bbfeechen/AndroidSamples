package com.sony.scalar.webapi.service.camera.v1_2.stillsize;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.StillSizeParams;


public interface GetStillSizeCallback extends Callbacks {

	void returnCb(StillSizeParams currentStillSize);

}
