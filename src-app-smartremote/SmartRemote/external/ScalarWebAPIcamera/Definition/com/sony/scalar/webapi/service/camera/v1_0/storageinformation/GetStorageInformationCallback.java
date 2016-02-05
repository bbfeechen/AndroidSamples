package com.sony.scalar.webapi.service.camera.v1_0.storageinformation;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.StorageInformationParams;


public interface GetStorageInformationCallback extends Callbacks {

	public void returnCb(StorageInformationParams[] currentStorageInformation);

}
