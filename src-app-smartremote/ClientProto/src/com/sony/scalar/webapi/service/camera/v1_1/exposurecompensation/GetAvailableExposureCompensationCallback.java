package com.sony.scalar.webapi.service.camera.v1_1.exposurecompensation;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableExposureCompensationCallback extends Callbacks {

	void returnCb(int current, int max, int min, int step);

}
