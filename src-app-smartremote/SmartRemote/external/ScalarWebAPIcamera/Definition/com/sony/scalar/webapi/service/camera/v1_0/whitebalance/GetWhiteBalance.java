package com.sony.scalar.webapi.service.camera.v1_0.whitebalance;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetWhiteBalance extends Service
{
    public int getWhiteBalance(GetWhiteBalanceCallback returnCb);
}
