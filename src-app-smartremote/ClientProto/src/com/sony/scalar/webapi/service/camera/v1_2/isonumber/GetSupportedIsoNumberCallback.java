package com.sony.scalar.webapi.service.camera.v1_2.isonumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetSupportedIsoNumberCallback extends Callbacks
{

    public void returnCb(String[] isoCandidates);

}
