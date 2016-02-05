package com.sony.scalar.webapi.service.camera.v1_1.movierec;

import com.sony.mexi.webapi.Callbacks;


public interface StopMovieRecCallback extends Callbacks {

	void returnCb(String url);

}
