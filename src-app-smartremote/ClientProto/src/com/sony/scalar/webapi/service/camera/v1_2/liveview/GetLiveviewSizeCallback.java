package com.sony.scalar.webapi.service.camera.v1_2.liveview;

import com.sony.mexi.webapi.Callbacks;

public interface GetLiveviewSizeCallback extends Callbacks
{

    public void returnCb(String size);

}
