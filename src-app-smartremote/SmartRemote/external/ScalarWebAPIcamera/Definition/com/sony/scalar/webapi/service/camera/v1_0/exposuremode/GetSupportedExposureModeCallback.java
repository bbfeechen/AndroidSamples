package com.sony.scalar.webapi.service.camera.v1_0.exposuremode;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedExposureModeCallback extends Callbacks {

	public void returnCb(String[] exposureModeCandidates);

}
