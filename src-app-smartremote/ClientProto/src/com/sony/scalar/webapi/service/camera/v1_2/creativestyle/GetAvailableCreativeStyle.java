package com.sony.scalar.webapi.service.camera.v1_2.creativestyle;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface GetAvailableCreativeStyle extends Service
{
    public int getAvailableCreativeStyle(GetAvailableCreativeStyleCallback returnCb);
}
