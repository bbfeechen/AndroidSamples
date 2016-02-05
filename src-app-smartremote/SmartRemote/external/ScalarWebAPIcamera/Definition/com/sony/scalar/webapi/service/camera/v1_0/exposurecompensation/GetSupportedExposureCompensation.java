package com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation;

import com.sony.mexi.webapi.Service;

public interface GetSupportedExposureCompensation extends Service
{
    public int getSupportedExposureCompensation(GetSupportedExposureCompensationCallback returnCb);
}
