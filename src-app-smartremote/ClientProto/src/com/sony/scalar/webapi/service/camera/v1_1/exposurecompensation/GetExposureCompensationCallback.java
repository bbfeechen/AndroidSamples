package com.sony.scalar.webapi.service.camera.v1_1.exposurecompensation;

import com.sony.mexi.webapi.Callbacks;


public interface GetExposureCompensationCallback extends Callbacks {

	void returnCb(int exposure);

}
