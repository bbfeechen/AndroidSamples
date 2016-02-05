package com.sony.scalar.webapi.service.camera.v1_2.postviewimage;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetSupportedPostviewImageSize extends Service
{
    public int getSupportedPostviewImageSize(GetSupportedPostviewImageSizeCallback returnCb);
}
