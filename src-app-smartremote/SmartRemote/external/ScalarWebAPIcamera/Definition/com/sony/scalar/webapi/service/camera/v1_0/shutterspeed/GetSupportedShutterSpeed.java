package com.sony.scalar.webapi.service.camera.v1_0.shutterspeed;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedShutterSpeed extends Service
{
    public int getSupportedShutterSpeed(GetSupportedShutterSpeedCallback returnCb);
}
