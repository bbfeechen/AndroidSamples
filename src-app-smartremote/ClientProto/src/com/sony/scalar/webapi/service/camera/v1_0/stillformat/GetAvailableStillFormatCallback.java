package com.sony.scalar.webapi.service.camera.v1_0.stillformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableStillFormatCallback extends Callbacks {

	void returnCb(String current, String[] format);

}
