package com.sony.scalar.webapi.service.camera.v1_2.touchafposition;

import com.sony.mexi.webapi.Callbacks;

public interface GetTouchAFPositionCallback extends Callbacks
{
    public void returnCb(boolean set, double[] touchCordinate);
}
