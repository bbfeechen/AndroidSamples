package com.sony.scalar.webapi.service.camera.v1_2.postviewimage;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetPostviewImageSize extends Service
{
    public int setPostviewImageSize(String size, SetPostviewImageSizeCallback returnCb);
}
