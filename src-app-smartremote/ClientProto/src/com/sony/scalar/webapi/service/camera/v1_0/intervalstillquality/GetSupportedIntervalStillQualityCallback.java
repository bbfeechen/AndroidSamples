package com.sony.scalar.webapi.service.camera.v1_0.intervalstillquality;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedIntervalStillQualityCallback extends Callbacks {

	void returnCb(String[] quality);

}
