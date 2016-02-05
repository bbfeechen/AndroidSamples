package com.sony.scalar.webapi.service.camera.v1_0.postviewimage;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedPostviewImageSize extends Service
{
    public int getSupportedPostviewImageSize(GetSupportedPostviewImageSizeCallback returnCb);
}
