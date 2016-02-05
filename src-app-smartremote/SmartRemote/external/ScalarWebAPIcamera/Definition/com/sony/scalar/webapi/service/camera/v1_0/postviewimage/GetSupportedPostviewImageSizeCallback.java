package com.sony.scalar.webapi.service.camera.v1_0.postviewimage;

import com.sony.mexi.webapi.Callbacks;

public interface GetSupportedPostviewImageSizeCallback extends Callbacks
{

    public void returnCb(String[] candidates);

}
