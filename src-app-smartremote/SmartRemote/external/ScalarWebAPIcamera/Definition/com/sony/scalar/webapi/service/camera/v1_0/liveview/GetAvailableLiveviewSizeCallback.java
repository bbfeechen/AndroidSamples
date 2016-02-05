package com.sony.scalar.webapi.service.camera.v1_0.liveview;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableLiveviewSizeCallback extends Callbacks
{

    public void returnCb(String size, String[] candidates);

}
