package com.sony.scalar.webapi.service.camera.v1_1.movieformat;

import com.sony.mexi.webapi.Callbacks;


public interface GetAvailableMovieFormatCallback extends Callbacks {

	void returnCb(String current, String[] format);

}
