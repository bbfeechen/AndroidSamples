package com.sony.scalar.webapi.service.camera.v1_2.shutterspeed;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetAvailableShutterSpeed extends Service
{
    public int getAvailableShutterSpeed(GetAvailableShutterSpeedCallback returnCb);
}
