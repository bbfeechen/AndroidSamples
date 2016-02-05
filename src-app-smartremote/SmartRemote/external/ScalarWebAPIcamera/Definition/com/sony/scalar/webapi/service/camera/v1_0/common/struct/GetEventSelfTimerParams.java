package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventSelfTimerParams
{
    public String type;

    /* "SelfTimer" */
    public int currentSelfTimer = 0;
    public int[] selfTimerCandidates;
}
