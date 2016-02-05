package com.sony.scalar.webapi.service.camera.v1_0.programshift;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetSupportedProgramShift extends Service
{
    public int getSupportedProgramShift(GetSupportedProgramShiftCallback returnCb);
}
