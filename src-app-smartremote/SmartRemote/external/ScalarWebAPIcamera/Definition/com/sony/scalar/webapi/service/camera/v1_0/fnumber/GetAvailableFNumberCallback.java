package com.sony.scalar.webapi.service.camera.v1_0.fnumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableFNumberCallback extends Callbacks
{

    public void returnCb(String currentFNumber, String[] fNumberCandidates);

}
