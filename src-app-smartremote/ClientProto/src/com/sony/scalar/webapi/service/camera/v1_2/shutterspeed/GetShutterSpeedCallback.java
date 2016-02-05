package com.sony.scalar.webapi.service.camera.v1_2.shutterspeed;

import com.sony.mexi.webapi.Callbacks;

public interface GetShutterSpeedCallback extends Callbacks
{

    public void returnCb(String currentShutterSpeed);

}
