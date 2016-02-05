package com.sony.scalar.webapi.service.camera.v1_0.steadymode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSteadyMode extends Service {

	public int getSteadyMode(GetSteadyModeCallback returnCb);

}
