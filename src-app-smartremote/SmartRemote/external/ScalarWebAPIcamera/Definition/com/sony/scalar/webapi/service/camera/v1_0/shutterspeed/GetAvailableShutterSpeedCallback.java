package com.sony.scalar.webapi.service.camera.v1_0.shutterspeed;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableShutterSpeedCallback extends Callbacks
{

    public void returnCb(String currentShutterSpeed, String[] shutterSpeedCandidates);

}
