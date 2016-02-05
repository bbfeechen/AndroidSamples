package com.sony.scalar.webapi.service.camera.v1_0.shootmode;

import com.sony.mexi.webapi.Callbacks;

public interface GetSupportedShootModeCallback extends Callbacks
{
    public void returnCb(String[] mode);
}
