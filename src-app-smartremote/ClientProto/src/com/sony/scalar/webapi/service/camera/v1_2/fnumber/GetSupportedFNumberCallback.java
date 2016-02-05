package com.sony.scalar.webapi.service.camera.v1_2.fnumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetSupportedFNumberCallback extends Callbacks
{

    public void returnCb(String[] fNumberCandidates);

}
