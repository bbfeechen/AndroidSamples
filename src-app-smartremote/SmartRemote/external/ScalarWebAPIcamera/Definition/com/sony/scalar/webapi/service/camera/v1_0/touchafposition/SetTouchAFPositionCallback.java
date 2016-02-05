package com.sony.scalar.webapi.service.camera.v1_0.touchafposition;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFPositionParams;

public interface SetTouchAFPositionCallback extends Callbacks
{
    public void returnCb(int ret, TouchAFPositionParams position);
}
