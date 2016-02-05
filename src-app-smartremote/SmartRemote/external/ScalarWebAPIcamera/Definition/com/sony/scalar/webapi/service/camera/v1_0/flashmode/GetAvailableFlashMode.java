package com.sony.scalar.webapi.service.camera.v1_0.flashmode;

import com.sony.mexi.webapi.Service;

public interface GetAvailableFlashMode extends Service {

	int getAvailableFlashMode(GetAvailableFlashModeCallback returnCb);

}
