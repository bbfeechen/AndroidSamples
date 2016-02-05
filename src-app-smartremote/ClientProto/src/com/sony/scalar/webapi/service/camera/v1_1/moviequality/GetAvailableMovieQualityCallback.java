package com.sony.scalar.webapi.service.camera.v1_1.moviequality;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableMovieQualityCallback extends Callbacks {

	void returnCb(String current, String[] quality);

}
