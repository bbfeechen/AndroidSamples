package com.sony.scalar.webapi.service.camera.v1_2.isonumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetIsoNumberCallback extends Callbacks
{

    public void returnCb(String currentIso);

}
