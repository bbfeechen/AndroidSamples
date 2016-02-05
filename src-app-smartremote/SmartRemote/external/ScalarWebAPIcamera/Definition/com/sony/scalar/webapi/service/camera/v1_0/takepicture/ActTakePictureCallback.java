package com.sony.scalar.webapi.service.camera.v1_0.takepicture;

import com.sony.mexi.webapi.Callbacks;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface ActTakePictureCallback extends Callbacks
{
    public void returnCb(String[] url);
}
