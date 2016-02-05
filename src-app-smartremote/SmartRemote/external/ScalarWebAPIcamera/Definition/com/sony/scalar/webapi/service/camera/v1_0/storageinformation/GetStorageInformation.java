package com.sony.scalar.webapi.service.camera.v1_0.storageinformation;

import com.sony.mexi.webapi.Service;

public interface GetStorageInformation extends Service 
{
	public int getStorageInformation(GetStorageInformationCallback returnCb);
}
