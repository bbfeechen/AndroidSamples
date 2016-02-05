package com.sony.scalar.webapi.service.camera.v1_0.whitebalance;

import com.sony.mexi.webapi.Callbacks;

public interface SetWhiteBalanceCallback extends Callbacks
{
    public void returnCb(int ret);
}
