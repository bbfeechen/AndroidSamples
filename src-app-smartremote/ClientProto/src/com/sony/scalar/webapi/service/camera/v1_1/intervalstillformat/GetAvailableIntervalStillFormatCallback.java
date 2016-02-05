package com.sony.scalar.webapi.service.camera.v1_1.intervalstillformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableIntervalStillFormatCallback extends Callbacks {

	void returnCb(String current, String[] format);

}
