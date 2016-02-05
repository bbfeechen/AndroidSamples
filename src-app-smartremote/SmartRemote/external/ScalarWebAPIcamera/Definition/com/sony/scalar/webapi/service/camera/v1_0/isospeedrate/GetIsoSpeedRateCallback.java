package com.sony.scalar.webapi.service.camera.v1_0.isospeedrate;

import com.sony.mexi.webapi.Callbacks;

public interface GetIsoSpeedRateCallback extends Callbacks
{

    public void returnCb(String currentIso);

}
