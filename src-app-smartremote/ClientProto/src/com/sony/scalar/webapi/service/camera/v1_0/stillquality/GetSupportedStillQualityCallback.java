package com.sony.scalar.webapi.service.camera.v1_0.stillquality;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedStillQualityCallback extends Callbacks {

	void returnCb(String[] quality);

}
