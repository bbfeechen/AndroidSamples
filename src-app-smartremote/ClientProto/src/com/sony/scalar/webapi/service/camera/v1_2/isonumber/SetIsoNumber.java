package com.sony.scalar.webapi.service.camera.v1_2.isonumber;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetIsoNumber extends Service
{
    public int setIsoNumber(String iso, SetIsoNumberCallback returnCb);
}
