package com.sony.scalar.webapi.service.camera.v1_0.stillsize;

import com.sony.mexi.webapi.Service;

public interface GetSupportedStillSize extends Service
{
    public int getSupportedStillSize(GetSupportedStillSizeCallback returnCb);
}
