package com.sony.scalar.webapi.interfaces.server.smartremotecontrol.v1_1;

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


public interface SRCtrlInterfaces extends TakePicture, AwaitTakePicture, ExposureCompensation, Liveview, RecMode, SelfTimer, Information, ReceiveEvent {
	/**
	 * SRCtrlInterfaces Camera service version 1.1
	 *
	 *
	 */

}
