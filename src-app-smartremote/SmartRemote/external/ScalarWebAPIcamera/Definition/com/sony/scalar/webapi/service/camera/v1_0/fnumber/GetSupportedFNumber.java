package com.sony.scalar.webapi.service.camera.v1_0.fnumber;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedFNumber extends Service
{
    public int getSupportedFNumber(GetSupportedFNumberCallback returnCb);
}
