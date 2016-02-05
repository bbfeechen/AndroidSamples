package com.sony.scalar.webapi.service.camera.v1_2.selftimer;

import com.sony.mexi.webapi.Service;

/**
*
* @version 1.2.0
*
*/

public interface SelfTimerAPI extends Service {

    public int setSelfTimer(int selfTimer, SetSelfTimerCallback returnCb);

    public int getSelfTimer(GetSelfTimerCallback returnCb);

    public int getSupportedSelfTimer(GetSupportedSelfTimerCallback returnCb);

    public int getAvailableSelfTimer(GetAvailableSelfTimerCallback returnCb);

}
