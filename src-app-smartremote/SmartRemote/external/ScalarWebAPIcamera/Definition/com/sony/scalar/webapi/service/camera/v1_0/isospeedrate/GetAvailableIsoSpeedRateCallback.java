package com.sony.scalar.webapi.service.camera.v1_0.isospeedrate;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableIsoSpeedRateCallback extends Callbacks
{

    public void returnCb(String currentIso, String[] isoCandidates);

}
