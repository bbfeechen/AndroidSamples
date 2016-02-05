package com.sony.scalar.webapi.service.camera.v1_0.movieformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetSupportedMovieFormatCallback extends Callbacks {

	void returnCb(String[] format);

}
