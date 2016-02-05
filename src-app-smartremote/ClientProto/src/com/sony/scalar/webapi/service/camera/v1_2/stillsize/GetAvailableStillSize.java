package com.sony.scalar.webapi.service.camera.v1_2.stillsize;

import com.sony.mexi.webapi.Service;

public interface GetAvailableStillSize extends Service
{
    public int getAvailableStillSize(GetAvailableStillSizeCallback returnCb);
}
