package com.sony.scalar.webapi.service.camera.v1_2.aelock;

import com.sony.mexi.webapi.Callbacks;


public interface GetAELockCallback extends Callbacks {

	public void returnCb(boolean currentAELock);

}
