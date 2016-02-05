package com.sony.scalar.webapi.service.camera.v1_2.selftimer;

import com.sony.mexi.webapi.Callbacks;


public interface SetSelfTimerCallback extends Callbacks {

    public void returnCb(int ret);

}
