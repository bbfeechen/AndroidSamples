package com.sony.scalar.webapi.service.camera.v1_0.aelock;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableAELockCallback extends Callbacks {

	public void returnCb(boolean currentAELock, boolean[] aeLockCandidates);

}
