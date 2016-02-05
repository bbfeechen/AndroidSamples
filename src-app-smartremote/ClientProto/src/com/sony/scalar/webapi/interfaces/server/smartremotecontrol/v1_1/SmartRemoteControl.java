package com.sony.scalar.webapi.interfaces.server.smartremotecontrol.v1_1;

import com.sony.scalar.webapi.service.camera.v1_1.flashmode.FlashMode;
import com.sony.scalar.webapi.service.camera.v1_1.intervalstillrec.IntervalStillRec;
import com.sony.scalar.webapi.service.camera.v1_1.moviequality.MovieQuality;
import com.sony.scalar.webapi.service.camera.v1_1.movierec.MovieRec;
import com.sony.scalar.webapi.service.camera.v1_1.shootmode.ShootMode;
import com.sony.scalar.webapi.service.camera.v1_1.steadymode.SteadyMode;
import com.sony.scalar.webapi.service.camera.v1_1.viewangle.ViewAngle;
import com.sony.scalar.webapi.service.camera.v1_1.zoom.Zoom;
import com.sony.scalar.webapi.service.camera.v1_1.common.Information;
import com.sony.scalar.webapi.service.camera.v1_1.common.ReceiveEvent;
import com.sony.scalar.webapi.service.camera.v1_1.common.TakePicture;
import com.sony.scalar.webapi.service.camera.v1_1.exposurecompensation.ExposureCompensation;
import com.sony.scalar.webapi.service.camera.v1_1.liveview.Liveview;
import com.sony.scalar.webapi.service.camera.v1_1.recmode.RecMode;
import com.sony.scalar.webapi.service.camera.v1_1.selftimer.SelfTimer;
import com.sony.scalar.webapi.service.camera.v1_1.common.AwaitTakePicture;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
*
*/


public interface SmartRemoteControl extends Liveview, IntervalStillRec, MovieRec, ShootMode, MovieQuality,
									SteadyMode, ViewAngle, Information, ReceiveEvent, TakePicture,
									AwaitTakePicture, ExposureCompensation, RecMode, SelfTimer,
									FlashMode, Zoom {
	/**
	 * SmartRemoteControl Camera service version 1.1
	 *
	 *
	 */

}
