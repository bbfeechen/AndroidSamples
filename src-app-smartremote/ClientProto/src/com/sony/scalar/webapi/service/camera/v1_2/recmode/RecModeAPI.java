package com.sony.scalar.webapi.service.camera.v1_2.recmode;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.2.0
*
*/

public interface RecModeAPI extends Service {

    public int startRecMode(StartRecModeCallback returnCb);

	public int stopRecMode(StopRecModeCallback returnCb);


}
