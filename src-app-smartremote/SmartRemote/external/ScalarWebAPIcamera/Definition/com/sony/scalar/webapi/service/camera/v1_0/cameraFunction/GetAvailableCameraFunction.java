package com.sony.scalar.webapi.service.camera.v1_0.cameraFunction;

import com.sony.mexi.webapi.Service;

public interface GetAvailableCameraFunction extends Service {
	public int getAvailableCameraFunction(GetAvailableCameraFunctionCallback returnCb);
}
