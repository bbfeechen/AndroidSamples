package com.sony.scalar.webapi.service.camera.v1_1.intervalstillquality;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableIntervalStillQualityCallback extends Callbacks {

	void returnCb(String current, String[] quality);

}
