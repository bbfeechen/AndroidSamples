package com.sony.scalar.webapi.service.camera.v1_0.fnumber;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetAvailableFNumber extends Service
{
    public int getAvailableFNumber(GetAvailableFNumberCallback returnCb);
}
