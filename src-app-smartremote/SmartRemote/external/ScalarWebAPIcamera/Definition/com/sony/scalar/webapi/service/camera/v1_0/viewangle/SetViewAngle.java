package com.sony.scalar.webapi.service.camera.v1_0.viewangle;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */
public interface SetViewAngle extends Service {

	public int setViewAngle(int angle, SetViewAngleCallback returnCb);

}
