package com.sony.scalar.webapi.service.camera.v1_1.stillformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetStillFormatCallback extends Callbacks {

	void returnCb(String format);

}
