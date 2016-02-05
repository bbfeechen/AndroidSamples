package com.sony.scalar.webapi.service.camera.v1_0.pictureeffect;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetSupportedPictureEffect extends Service
{
    public int getSupportedPictureEffect(GetSupportedPictureEffectCallback returnCb);
}
