package com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedExposureCompensationCallback extends Callbacks {

	void returnCb(int[] max, int[] min, int[] step);

}