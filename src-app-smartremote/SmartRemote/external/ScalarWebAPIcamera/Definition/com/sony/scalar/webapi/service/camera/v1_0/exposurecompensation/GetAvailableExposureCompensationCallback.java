package com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableExposureCompensationCallback extends Callbacks {

    public void returnCb(int currentExposureCompensation, int maxExposureCompensation, int minExposureCompensation, int stepIndexOfExposureCompensation);

}
