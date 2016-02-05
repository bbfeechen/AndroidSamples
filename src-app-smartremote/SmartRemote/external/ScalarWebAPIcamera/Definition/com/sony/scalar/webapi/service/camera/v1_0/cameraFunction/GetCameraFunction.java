package com.sony.scalar.webapi.service.camera.v1_0.cameraFunction;

import com.sony.mexi.webapi.Service;

public interface GetCameraFunction extends Service {
	public int getCameraFunction(GetCameraFunctionCallback returnCb);
}
