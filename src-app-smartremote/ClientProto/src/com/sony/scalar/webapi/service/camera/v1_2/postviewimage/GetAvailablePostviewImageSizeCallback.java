package com.sony.scalar.webapi.service.camera.v1_2.postviewimage;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailablePostviewImageSizeCallback extends Callbacks
{

    public void returnCb(String size, String[] candidates);

}
