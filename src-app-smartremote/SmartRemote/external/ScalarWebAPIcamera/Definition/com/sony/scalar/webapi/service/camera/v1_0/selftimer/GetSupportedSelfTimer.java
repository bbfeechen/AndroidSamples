package com.sony.scalar.webapi.service.camera.v1_0.selftimer;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.0.0
*
*/

public interface GetSupportedSelfTimer extends Service {

    public int getSupportedSelfTimer(GetSupportedSelfTimerCallback returnCb);
}
