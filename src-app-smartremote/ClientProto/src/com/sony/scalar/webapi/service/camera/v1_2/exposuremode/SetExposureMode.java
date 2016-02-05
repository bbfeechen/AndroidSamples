package com.sony.scalar.webapi.service.camera.v1_2.exposuremode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetExposureMode extends Service
{
    public int setExposureMode(String exposureMode, SetExposureModeCallback returnCb);
}
