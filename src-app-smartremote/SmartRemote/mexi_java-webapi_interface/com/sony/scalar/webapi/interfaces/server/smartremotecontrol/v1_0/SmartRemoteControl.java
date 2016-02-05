package com.sony.scalar.webapi.interfaces.server.smartremotecontrol.v1_0;

import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetAvailableExposureCompensation;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetExposureCompensation;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetSupportedExposureCompensation;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.SetExposureCompensation;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetAvailableExposureMode;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetExposureMode;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetSupportedExposureMode;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.SetExposureMode;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetAvailableFlashMode;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetFlashMode;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetSupportedFlashMode;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.SetFlashMode;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetAvailableFNumber;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetFNumber;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetSupportedFNumber;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.SetFNumber;
import com.sony.scalar.webapi.service.camera.v1_0.getevent.GetEvent;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetAvailableIsoSpeedRate;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetIsoSpeedRate;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetSupportedIsoSpeedRate;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.SetIsoSpeedRate;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetAvailableLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetSupportedLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.StartLiveview;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.StartLiveviewWithSize;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.StopLiveview;
import com.sony.scalar.webapi.service.camera.v1_0.misc.GetApplicationInfo;
import com.sony.scalar.webapi.service.camera.v1_0.misc.GetAvailableApiList;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetAvailablePostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetSupportedPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.SetPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_0.programshift.GetSupportedProgramShift;
import com.sony.scalar.webapi.service.camera.v1_0.programshift.SetProgramShift;
import com.sony.scalar.webapi.service.camera.v1_0.recmode.StartRecMode;
import com.sony.scalar.webapi.service.camera.v1_0.recmode.StopRecMode;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetAvailableSelfTimer;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetSelfTimer;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetSupportedSelfTimer;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.SetSelfTimer;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetAvailableShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetSupportedShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.SetShootMode;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetAvailableShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetSupportedShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.SetShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_0.takepicture.ActTakePicture;
import com.sony.scalar.webapi.service.camera.v1_0.takepicture.AwaitTakePicture;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.CancelTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.GetTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.SetTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetAvailableWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetSupportedWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.SetWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_0.zoom.ActZoom;

/**
 * 
 * @version 1.0.0
 */

public interface SmartRemoteControl extends
        /* takePicture */
        ActTakePicture,
        AwaitTakePicture,

        /* exposure compensation */
        SetExposureCompensation,
        GetExposureCompensation,
        GetAvailableExposureCompensation,
        GetSupportedExposureCompensation,
        
        /* liveview */
        StartLiveview,
        StopLiveview,

        /* liveview with size */
        GetLiveviewSize,
        GetAvailableLiveviewSize,
        GetSupportedLiveviewSize,
        StartLiveviewWithSize,
        
        /* rec mode */
        StartRecMode,
        StopRecMode,
        
        /* self timer */
        SetSelfTimer,
        GetSelfTimer,
        GetAvailableSelfTimer,
        GetSupportedSelfTimer,
        
        /* misc */
        GetApplicationInfo,
        GetAvailableApiList,
        
        /* white balance */
        SetWhiteBalance,
        GetWhiteBalance,
        GetAvailableWhiteBalance,
        GetSupportedWhiteBalance,
        
        /* GetEvent */
        GetEvent,
        
        /* touch af */
        SetTouchAFPosition,
        GetTouchAFPosition,
        CancelTouchAFPosition,
        
        /* F number */
        SetFNumber,
        GetFNumber,
        GetAvailableFNumber,
        GetSupportedFNumber,
        
        /* shutter speed */
        SetShutterSpeed,
        GetShutterSpeed,
        GetAvailableShutterSpeed,
        GetSupportedShutterSpeed,
        
        /* iso */
        SetIsoSpeedRate,
        GetIsoSpeedRate,
        GetAvailableIsoSpeedRate,
        GetSupportedIsoSpeedRate,
        
        /* exposure mode */
        SetExposureMode,
        GetExposureMode,
        GetAvailableExposureMode,
        GetSupportedExposureMode,
        
        /* postview image size */
        SetPostviewImageSize,
        GetPostviewImageSize,
        GetAvailablePostviewImageSize,
        GetSupportedPostviewImageSize,
        
        /* program shift */
        SetProgramShift,
        GetSupportedProgramShift,

        /* ShootMode */
        SetShootMode,
        GetShootMode,
        GetAvailableShootMode,
        GetSupportedShootMode,
        
        /* flash mode */
        SetFlashMode,
        GetFlashMode,
        GetAvailableFlashMode,
        GetSupportedFlashMode,
       
        /* zoom */
        ActZoom
{
    /**
     * SmartRemoteControl Camera service version 1.0
     * 
     * 
     */

}
