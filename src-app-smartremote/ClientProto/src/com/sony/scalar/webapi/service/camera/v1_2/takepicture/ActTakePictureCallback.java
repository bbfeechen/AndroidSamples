package com.sony.scalar.webapi.service.camera.v1_2.takepicture;

import com.sony.mexi.webapi.Callbacks;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface ActTakePictureCallback extends Callbacks
{
    void returnCb(String[] url);
}
