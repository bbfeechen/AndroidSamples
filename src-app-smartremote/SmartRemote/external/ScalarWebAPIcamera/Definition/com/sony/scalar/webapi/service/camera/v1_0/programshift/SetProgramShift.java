package com.sony.scalar.webapi.service.camera.v1_0.programshift;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface SetProgramShift extends Service
{
    public int setProgramShift(int step, SetProgramShiftCallback returnCb);
}
