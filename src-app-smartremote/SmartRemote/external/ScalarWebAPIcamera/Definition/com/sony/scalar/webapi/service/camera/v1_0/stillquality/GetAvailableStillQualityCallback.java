package com.sony.scalar.webapi.service.camera.v1_0.stillquality;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableStillQualityCallback extends Callbacks {

	void returnCb(String current, String[] quality);

}
