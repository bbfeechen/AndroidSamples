package com.sony.scalar.webapi.service.camera.v1_0.selftimer;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.0.0
*
*/

public interface SetSelfTimer extends Service {

    public int setSelfTimer(int selfTimer, SetSelfTimerCallback returnCb);

}
