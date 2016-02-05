package com.sony.scalar.webapi.service.camera.v1_2.programshift;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedProgramShiftCallback extends Callbacks {

	public void returnCb(int[] stepRange);

}
