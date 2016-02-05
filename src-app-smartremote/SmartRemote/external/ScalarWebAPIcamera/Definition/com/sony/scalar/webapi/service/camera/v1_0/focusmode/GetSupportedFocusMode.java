package com.sony.scalar.webapi.service.camera.v1_0.focusmode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedFocusMode extends Service
{
    public int getSupportedFocusMode(GetSupportedFocusModeCallback returnCb);
}
