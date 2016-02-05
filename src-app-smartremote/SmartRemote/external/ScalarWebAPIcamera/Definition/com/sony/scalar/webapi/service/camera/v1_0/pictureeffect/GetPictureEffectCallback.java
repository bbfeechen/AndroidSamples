package com.sony.scalar.webapi.service.camera.v1_0.pictureeffect;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.PictureEffectParams;

public interface GetPictureEffectCallback extends Callbacks
{
    public void returnCb(PictureEffectParams currentPictureEffect);
}
