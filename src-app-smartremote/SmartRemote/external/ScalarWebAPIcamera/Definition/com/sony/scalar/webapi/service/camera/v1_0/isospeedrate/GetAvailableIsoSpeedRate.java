package com.sony.scalar.webapi.service.camera.v1_0.isospeedrate;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetAvailableIsoSpeedRate extends Service
{
    public int getAvailableIsoSpeedRate(GetAvailableIsoSpeedRateCallback returnCb);
}
