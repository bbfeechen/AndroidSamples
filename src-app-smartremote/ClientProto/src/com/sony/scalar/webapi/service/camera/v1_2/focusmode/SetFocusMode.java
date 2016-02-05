package com.sony.scalar.webapi.service.camera.v1_2.focusmode;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetFocusMode extends Service
{
    public int setFocusMode(String focusMode, SetFocusModeCallback returnCb);
}
