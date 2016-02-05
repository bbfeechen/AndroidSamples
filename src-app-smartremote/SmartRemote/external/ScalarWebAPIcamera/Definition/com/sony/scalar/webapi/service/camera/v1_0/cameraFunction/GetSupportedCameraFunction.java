package com.sony.scalar.webapi.service.camera.v1_0.cameraFunction;

import com.sony.mexi.webapi.Service;

public interface GetSupportedCameraFunction extends Service {
	public int getSupportedCameraFunction(GetSupportedCameraFunctionCallback returnCb);
}
