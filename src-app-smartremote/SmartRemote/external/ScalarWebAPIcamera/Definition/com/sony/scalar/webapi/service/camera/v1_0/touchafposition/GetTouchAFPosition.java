package com.sony.scalar.webapi.service.camera.v1_0.touchafposition;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetTouchAFPosition extends Service
{
    public int getTouchAFPosition(GetTouchAFPositionCallback returnCb);
}
