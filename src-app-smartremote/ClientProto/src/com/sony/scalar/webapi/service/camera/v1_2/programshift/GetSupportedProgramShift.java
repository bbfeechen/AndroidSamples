package com.sony.scalar.webapi.service.camera.v1_2.programshift;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface GetSupportedProgramShift extends Service
{
    public int getSupportedProgramShift(GetSupportedProgramShiftCallback returnCb);
}
