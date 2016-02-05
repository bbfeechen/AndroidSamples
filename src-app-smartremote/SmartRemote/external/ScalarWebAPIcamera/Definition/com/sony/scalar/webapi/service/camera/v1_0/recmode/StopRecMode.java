package com.sony.scalar.webapi.service.camera.v1_0.recmode;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.0.0
*
*/

public interface StopRecMode extends Service {

	public int stopRecMode(StopRecModeCallback returnCb);


}
