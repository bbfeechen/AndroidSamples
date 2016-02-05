package com.sony.scalar.webapi.service.camera.v1_1.movierec;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableMovieRecQualityCallback extends Callbacks {

	void returnCb(String current, String[] quality);

}
