package com.sony.scalar.webapi.service.camera.v1_0.misc;

import com.sony.mexi.webapi.Callbacks;


public interface GetApplicationInfoCallback extends Callbacks {

	public void returnCb(String name, String version);

}
