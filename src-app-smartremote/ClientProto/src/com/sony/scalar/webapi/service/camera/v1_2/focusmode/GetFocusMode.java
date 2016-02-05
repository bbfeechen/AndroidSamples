package com.sony.scalar.webapi.service.camera.v1_2.focusmode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetFocusMode extends Service
{
    public int getFocusMode(GetFocusModeCallback returnCb);
}
