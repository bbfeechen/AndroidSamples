package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventStorageInformationParams
{
    public String type;

    /* - - - */
    /* "StorageInformation" */
    public String storageID;
    public boolean recordTarget = false;
    public int numberOfRecordableImages = -1;
    public int recordableTime = -1;
    public String storageDescription;
}
