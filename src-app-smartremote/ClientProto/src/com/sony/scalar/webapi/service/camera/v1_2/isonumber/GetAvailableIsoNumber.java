package com.sony.scalar.webapi.service.camera.v1_2.isonumber;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetAvailableIsoNumber extends Service
{
    public int getAvailableIsoNumber(GetAvailableIsoNumberCallback returnCb);
}
