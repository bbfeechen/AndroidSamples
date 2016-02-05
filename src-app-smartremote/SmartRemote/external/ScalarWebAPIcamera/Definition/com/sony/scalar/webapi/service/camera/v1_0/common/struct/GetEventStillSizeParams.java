package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventStillSizeParams
{
    public String type;
    public boolean checkAvailability = false;      // When true, to issue the getAvailable API ASAP is recommended.

    /* - - - */
    /* "StillSize" */
    public String currentAspect;
    public String currentSize;
    //public boolean checkAvailability;      // Cannot express complex candidates.
}
