package com.sony.scalar.webapi.service.camera.v1_2.misc;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetAvailableApiList extends Service
{
    public int getAvailableApiList(GetAvailableApiListCallback returnCb);
}
