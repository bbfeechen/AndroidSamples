package com.sony.scalar.webapi.service.camera.v1_0.stillsize;

import com.sony.mexi.webapi.Service;

public interface SetStillSize extends Service
{
    public int setStillSize(String aspect, String size, SetStillSizeCallback returnCb);
}
