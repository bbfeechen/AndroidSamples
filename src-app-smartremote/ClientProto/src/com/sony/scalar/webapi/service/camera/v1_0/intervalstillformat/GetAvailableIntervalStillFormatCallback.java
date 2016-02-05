package com.sony.scalar.webapi.service.camera.v1_0.intervalstillformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableIntervalStillFormatCallback extends Callbacks {

	void returnCb(String current, String[] format);

}
