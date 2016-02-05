package com.sony.scalar.webapi.service.camera.v1_0.cameraFunction;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedCameraFunctionCallback extends Callbacks {

	public void returnCb(String[] cameraFunctionCandidates);

}
