package com.sony.scalar.webapi.service.camera.v1_0.common;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableApiListCallback extends Callbacks {

	void returnCb(String[] names);

}
