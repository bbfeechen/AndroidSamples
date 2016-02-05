package com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation;

import com.sony.mexi.webapi.Service;

public interface GetExposureCompensation extends Service
{
    public int getExposureCompensation(GetExposureCompensationCallback returnCb);
}
