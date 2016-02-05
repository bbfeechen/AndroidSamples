package com.sony.scalar.webapi.service.camera.v1_2.programshift;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.2.0
 * 
 */

public interface SetProgramShift extends Service
{
    public int setProgramShift(int step, SetProgramShiftCallback returnCb);
}
