package com.sony.scalar.webapi.service.camera.v1_0.zoom;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface ActZoom extends Service {


	public int actZoom(String direction, String movement, ActZoomCallback returnCb);


}
