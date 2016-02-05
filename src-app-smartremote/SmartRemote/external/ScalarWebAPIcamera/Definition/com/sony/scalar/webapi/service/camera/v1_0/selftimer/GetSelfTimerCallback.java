package com.sony.scalar.webapi.service.camera.v1_0.selftimer;

import com.sony.mexi.webapi.Callbacks;


public interface GetSelfTimerCallback extends Callbacks {

    public void returnCb(int currentSelfTimer);

}
