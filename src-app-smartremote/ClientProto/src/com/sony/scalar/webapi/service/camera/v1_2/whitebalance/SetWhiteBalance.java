package com.sony.scalar.webapi.service.camera.v1_2.whitebalance;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface SetWhiteBalance extends Service
{
    public int setWhiteBalance(String whiteBalanceMode, boolean colorTemperatureEnabled, int colorTemperature,
            SetWhiteBalanceCallback returnCb);
}
