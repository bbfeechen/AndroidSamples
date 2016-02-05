package com.sony.scalar.webapi.service.camera.v1_2.takepicture;

import com.sony.mexi.webapi.Callbacks;

public interface AwaitTakePictureCallback extends Callbacks
{
    void returnCb(String[] url);
}
