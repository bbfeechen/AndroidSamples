package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class GetEventTouchAFPositionParams
{
    public String type;

    /* "TouchAFPosition" */
    public boolean currentSet = false;
    public double[] currentTouchCoordinates;
}
