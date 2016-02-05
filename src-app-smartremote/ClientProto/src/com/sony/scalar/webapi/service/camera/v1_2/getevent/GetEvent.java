package com.sony.scalar.webapi.service.camera.v1_2.getevent;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface GetEvent extends Service
{
    public int getEvent(boolean polling, GetEventCallback returnCb);
}
