package com.sony.scalar.webapi.service.camera.v1_0.creativestyle;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetCreativeStyle extends Service
{
    public int getCreativeStyle(GetCreativeStyleCallback returnCb);
}
