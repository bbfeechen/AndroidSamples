package com.sony.scalar.webapi.service.camera.v1_0.liveview;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedLiveviewSize extends Service
{
    public int getSupportedLiveviewSize(GetSupportedLiveviewSizeCallback returnCb);
}
