package com.sony.scalar.webapi.service.camera.v1_0.liveview;

import com.sony.mexi.webapi.Callbacks;

public interface GetSupportedLiveviewSizeCallback extends Callbacks
{

    public void returnCb(String[] candidates);

}