package com.sony.scalar.webapi.interfaces.server.diadem.v1_0;

import com.sony.scalar.webapi.service.camera.v1_0.common.Information;
import com.sony.scalar.webapi.service.camera.v1_0.common.ReceiveEvent;
import com.sony.scalar.webapi.service.camera.v1_0.intervalstillrec.IntervalStillRec;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.Liveview;
import com.sony.scalar.webapi.service.camera.v1_0.moviequality.MovieQuality;
import com.sony.scalar.webapi.service.camera.v1_0.movierec.MovieRec;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.ShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.steadymode.SteadyMode;
import com.sony.scalar.webapi.service.camera.v1_0.viewangle.ViewAngle;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
*
*/


public interface AKRemote extends Liveview, IntervalStillRec, MovieRec, ShootMode,
									MovieQuality, SteadyMode, ViewAngle, Information, ReceiveEvent {
	/**
	 * AKRemote
	 *
	 *
	 */

}
