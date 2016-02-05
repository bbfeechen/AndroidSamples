package com.sony.scalar.webapi.service.camera.v1_2.isonumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableIsoNumberCallback extends Callbacks
{

    public void returnCb(String currentIso, String[] isoCandidates);

}
