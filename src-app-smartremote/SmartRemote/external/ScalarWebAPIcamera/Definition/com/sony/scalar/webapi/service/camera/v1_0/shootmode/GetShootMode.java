package com.sony.scalar.webapi.service.camera.v1_0.shootmode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetShootMode extends Service
{
    public int getShootMode(GetShootModeCallback returnCb);
}