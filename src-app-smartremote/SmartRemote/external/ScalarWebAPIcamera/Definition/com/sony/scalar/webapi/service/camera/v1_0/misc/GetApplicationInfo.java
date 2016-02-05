package com.sony.scalar.webapi.service.camera.v1_0.misc;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetApplicationInfo extends Service
{
    public int getApplicationInfo(GetApplicationInfoCallback returnCb);
}
