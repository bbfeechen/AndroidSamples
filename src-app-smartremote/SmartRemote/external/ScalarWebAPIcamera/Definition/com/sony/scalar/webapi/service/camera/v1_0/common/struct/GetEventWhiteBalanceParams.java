package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventWhiteBalanceParams
{
    public String type;
    public boolean checkAvailability = false;      // When true, to issue the getAvailable API ASAP is recommended.

    /* - - - */
    /* "WhiteBalance" */
    public String currentWhiteBalanceMode;
    public int currentColorTemperature = -1;
    //public boolean checkAvailability;      // Cannot express complex candidates.
}
