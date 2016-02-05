package com.sony.scalar.webapi.service.camera.v1_0.moviequality;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedMovieQualityCallback extends Callbacks {

	void returnCb(String[] quality);

}
