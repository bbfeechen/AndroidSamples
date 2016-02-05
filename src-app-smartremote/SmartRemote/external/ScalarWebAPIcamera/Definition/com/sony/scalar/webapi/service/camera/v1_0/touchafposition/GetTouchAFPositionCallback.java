package com.sony.scalar.webapi.service.camera.v1_0.touchafposition;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;

public interface GetTouchAFPositionCallback extends Callbacks
{
    public void returnCb(TouchAFCurrentPositionParams position);
}
