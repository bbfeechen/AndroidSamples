package com.sony.scalar.webapi.service.camera.v1_0.movierec;

import com.sony.mexi.webapi.Callbacks;


public interface GetMovieRecQualityCallback extends Callbacks {

	void returnCb(String quality);

}
