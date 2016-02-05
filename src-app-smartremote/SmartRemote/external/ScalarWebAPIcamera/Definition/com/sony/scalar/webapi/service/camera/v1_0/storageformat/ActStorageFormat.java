package com.sony.scalar.webapi.service.camera.v1_0.storageformat;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface ActStorageFormat extends Service
{
    public int actStorageFormat(String storageID, ActStorageFormatCallback returnCb);
}
