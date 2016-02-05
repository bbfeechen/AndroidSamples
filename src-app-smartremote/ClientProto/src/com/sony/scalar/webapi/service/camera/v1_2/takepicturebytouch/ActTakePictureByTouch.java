package com.sony.scalar.webapi.service.camera.v1_2.takepicturebytouch;

import com.sony.mexi.webapi.Service;
import com.sony.scalar.webapi.service.camera.v1_2.takepicture.ActTakePictureCallback;

/**
 * @version 1.2.0
 * 
 */
public interface ActTakePictureByTouch extends Service
{
    public int actTakePictureByTouch(double x, double y, ActTakePictureCallback returnCb);
}
