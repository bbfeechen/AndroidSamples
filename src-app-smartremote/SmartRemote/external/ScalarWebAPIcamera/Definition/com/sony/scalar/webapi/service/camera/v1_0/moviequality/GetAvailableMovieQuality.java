package com.sony.scalar.webapi.service.camera.v1_0.moviequality;

import com.sony.mexi.webapi.Service;

/**
 * 
 * @version 1.0.0
 * 
 */

public interface GetAvailableMovieQuality extends Service {

	public int getAvailableMovieQuality(GetAvailableMovieQualityCallback returnCb);

}
