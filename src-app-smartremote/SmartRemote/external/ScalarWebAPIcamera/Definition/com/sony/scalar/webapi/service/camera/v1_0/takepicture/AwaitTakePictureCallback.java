package com.sony.scalar.webapi.service.camera.v1_0.takepicture;

import com.sony.mexi.webapi.Callbacks;

public interface AwaitTakePictureCallback extends Callbacks
{
    public void returnCb(String[] url);
}
