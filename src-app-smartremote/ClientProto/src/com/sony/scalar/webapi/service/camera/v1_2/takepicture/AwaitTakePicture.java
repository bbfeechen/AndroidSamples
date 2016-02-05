package com.sony.scalar.webapi.service.camera.v1_2.takepicture;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface AwaitTakePicture extends Service
{
    int awaitTakePicture(AwaitTakePictureCallback returnCb);
}
