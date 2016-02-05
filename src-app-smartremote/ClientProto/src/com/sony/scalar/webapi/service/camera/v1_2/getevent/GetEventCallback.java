package com.sony.scalar.webapi.service.camera.v1_2.getevent;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.GetEventParams;

public interface GetEventCallback extends Callbacks
{
    public void returnCb(GetEventParams[] params);
}
