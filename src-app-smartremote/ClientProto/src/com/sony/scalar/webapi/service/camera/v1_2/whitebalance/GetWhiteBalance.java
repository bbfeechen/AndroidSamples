package com.sony.scalar.webapi.service.camera.v1_2.whitebalance;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface GetWhiteBalance extends Service
{
    public int getWhiteBalance(GetWhiteBalanceCallback returnCb);
}
