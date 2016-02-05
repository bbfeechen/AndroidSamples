package com.sony.scalar.webapi.service.camera.v1_2.touchafposition;

import com.sony.mexi.webapi.Callbacks;

public interface SetTouchAFPositionCallback extends Callbacks
{
    public void returnCb(boolean AFResult, String AFType, double[] touchCordinate, double[] AFBoxLeftTop,
            double[] AFBoxRightBottom);
}
