package com.sony.scalar.webapi.service.camera.v1_0.fnumber;

import com.sony.mexi.webapi.Callbacks;

public interface GetFNumberCallback extends Callbacks
{

    public void returnCb(String currentFNumber);

}
