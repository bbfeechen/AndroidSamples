package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventPictureEffectParams
{
    public String type;
    public boolean checkAvailability = false;      // When true, to issue the getAvailable API ASAP is recommended.

    /* - - - */
    /* "PictureEffect" */
    public String currentPictureEffect;
    public String currentPictureEffectOption;
    //public boolean checkAvailability;      // Cannot express complex candidates.
}
