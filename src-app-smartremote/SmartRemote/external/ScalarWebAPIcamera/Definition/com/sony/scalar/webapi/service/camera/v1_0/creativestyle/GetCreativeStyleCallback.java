package com.sony.scalar.webapi.service.camera.v1_0.creativestyle;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.CreativeStyleParams;

public interface GetCreativeStyleCallback extends Callbacks
{
    public void returnCb(CreativeStyleParams currentCreativeStyle);
}
