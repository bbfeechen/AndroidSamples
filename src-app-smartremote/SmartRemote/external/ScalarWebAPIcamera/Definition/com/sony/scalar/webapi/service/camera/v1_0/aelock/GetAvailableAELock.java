package com.sony.scalar.webapi.service.camera.v1_0.aelock;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetAvailableAELock extends Service
{
    public int getAvailableAELock(GetAvailableAELockCallback returnCb);
}
