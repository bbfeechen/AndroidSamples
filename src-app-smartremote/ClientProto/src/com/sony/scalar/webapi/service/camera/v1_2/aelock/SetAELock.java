package com.sony.scalar.webapi.service.camera.v1_2.aelock;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetAELock extends Service
{
    public int setAELock(boolean aELock, SetAELockCallback returnCb);
}
