package com.sony.scalar.webapi.service.camera.v1_0.stillsize;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.StillSizeParams;


public interface GetAvailableStillSizeCallback extends Callbacks {

	void returnCb(StillSizeParams currentStillSize, StillSizeParams[] stillSizeCandidates);

}
