package com.sony.scalar.webapi.service.camera.v1_0.creativestyle;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetSupportedCreativeStyle extends Service
{
    public int getSupportedCreativeStyle(GetSupportedCreativeStyleCallback returnCb);
}
