package com.sony.scalar.webapi.service.camera.v1_2.touchafposition;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface GetTouchAFPosition extends Service
{
    public int getTouchAFPosition(GetTouchAFPositionCallback returnCb);
}
