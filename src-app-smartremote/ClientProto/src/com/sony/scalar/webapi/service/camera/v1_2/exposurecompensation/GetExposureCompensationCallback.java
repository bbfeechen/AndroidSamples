package com.sony.scalar.webapi.service.camera.v1_2.exposurecompensation;

import com.sony.mexi.webapi.Callbacks;


public interface GetExposureCompensationCallback extends Callbacks {

    public void returnCb(int exposureCompensation);

}
