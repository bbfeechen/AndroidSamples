package com.sony.scalar.webapi.service.camera.v1_0.cameraFunction;

import com.sony.mexi.webapi.Service;

public interface SetCameraFunction extends Service {
	public int setCameraFunction(String cameraFunction, SetCameraFunctionCallback returnCb);
}
