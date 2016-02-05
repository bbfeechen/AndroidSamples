package com.sony.scalar.webapi.service.camera.v1_0.formatstorage;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface ActFormatStorage extends Service
{
    public int actFormatStorage(String storageID, ActFormatStorageCallback returnCb);
}
