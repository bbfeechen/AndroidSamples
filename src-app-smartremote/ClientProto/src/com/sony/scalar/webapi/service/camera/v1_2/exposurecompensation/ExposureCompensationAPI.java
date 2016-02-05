package com.sony.scalar.webapi.service.camera.v1_2.exposurecompensation;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.2.0
*
*/

public interface ExposureCompensationAPI extends Service {

    public int setExposureCompensation(int exposureCompensation, SetExposureCompensationCallback returnCb);


	public int getExposureCompensation(GetExposureCompensationCallback returnCb);


	public int getSupportedExposureCompensation(GetSupportedExposureCompensationCallback returnCb);


	public int getAvailableExposureCompensation(GetAvailableExposureCompensationCallback returnCb);

}
