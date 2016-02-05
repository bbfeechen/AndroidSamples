package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventBracketShootModeParams
{
    public String type;
    public boolean checkAvailability = false;      // When true, to issue the getAvailable API ASAP is recommended.

    /* - - - */
    /* "BracketShootMode" */
    public String currentBracketShootMode;
    public String currentBracketShootModeOption;
    //public boolean checkAvailability;      // Cannot express complex candidates.

}
