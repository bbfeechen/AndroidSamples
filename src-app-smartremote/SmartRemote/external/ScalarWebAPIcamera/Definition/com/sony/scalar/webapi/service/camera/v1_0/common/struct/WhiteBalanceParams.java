package com.sony.scalar.webapi.service.camera.v1_0.common.struct;

public class WhiteBalanceParams
{
    public String whiteBalanceMode;
    public int colorTemperature = -1; // -1 is the substitution of 'null'
}
