package com.sony.scalar.webapi.service.camera.v1_1.steadymode;

import com.sony.mexi.webapi.Callbacks;


public interface GetSteadyModeCallback extends Callbacks {

	void returnCb(String steady);

}
