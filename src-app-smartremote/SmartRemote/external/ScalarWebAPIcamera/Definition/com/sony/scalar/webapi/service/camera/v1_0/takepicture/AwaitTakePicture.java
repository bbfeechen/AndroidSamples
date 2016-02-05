package com.sony.scalar.webapi.service.camera.v1_0.takepicture;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface AwaitTakePicture extends Service
{
    public int awaitTakePicture(AwaitTakePictureCallback returnCb);
}
