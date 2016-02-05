package com.sony.scalar.webapi.service.camera.v1_0.takepicturebytouch;

import com.sony.mexi.webapi.Service;
import com.sony.scalar.webapi.service.camera.v1_0.takepicture.ActTakePictureCallback;

/**
 * @version 1.0.0
 * 
 */
public interface ActTakePictureByTouch extends Service
{
    public int actTakePictureByTouch(double x, double y, ActTakePictureCallback returnCb);
}
