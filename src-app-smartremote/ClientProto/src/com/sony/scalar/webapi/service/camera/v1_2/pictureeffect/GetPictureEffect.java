package com.sony.scalar.webapi.service.camera.v1_2.pictureeffect;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface GetPictureEffect extends Service
{
    public int getPictureEffect(GetPictureEffectCallback returnCb);
}
