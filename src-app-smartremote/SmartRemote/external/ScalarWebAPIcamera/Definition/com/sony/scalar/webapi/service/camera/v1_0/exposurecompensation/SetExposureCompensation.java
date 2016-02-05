package com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation;

import com.sony.mexi.webapi.Service;

public interface SetExposureCompensation extends Service
{
    public int setExposureCompensation(int exposureCompensation, SetExposureCompensationCallback returnCb);
}
