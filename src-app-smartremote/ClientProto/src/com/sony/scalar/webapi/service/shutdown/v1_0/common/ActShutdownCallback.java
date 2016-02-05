package com.sony.scalar.webapi.service.shutdown.v1_0.common;

import com.sony.mexi.webapi.Callbacks;



public interface ActShutdownCallback extends Callbacks {

	void returnCb(int ret);

}
