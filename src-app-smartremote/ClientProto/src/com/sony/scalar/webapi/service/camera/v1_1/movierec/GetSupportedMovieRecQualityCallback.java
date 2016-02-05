package com.sony.scalar.webapi.service.camera.v1_1.movierec;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedMovieRecQualityCallback extends Callbacks {

	void returnCb(String[] quality);

}
