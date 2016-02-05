package com.sony.scalar.webapi.interfaces.server.diadem.v1_0;

import com.sony.scalar.webapi.service.camera.v1_0.common.Information;
import com.sony.scalar.webapi.service.camera.v1_0.common.ReceiveEvent;
import com.sony.scalar.webapi.service.camera.v1_0.common.TakePicture;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.FlashMode;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.Liveview;
import com.sony.scalar.webapi.service.camera.v1_0.movierec.MovieRec;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.SelfTimer;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.ShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.zoom.Zoom;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
*
*/


public interface DSCRemote extends TakePicture, Liveview,
									FlashMode, MovieRec, ShootMode, SelfTimer,
									Information, Zoom, ReceiveEvent {
	/**
	 * DSCRemote
	 *
	 *
	 */

}
