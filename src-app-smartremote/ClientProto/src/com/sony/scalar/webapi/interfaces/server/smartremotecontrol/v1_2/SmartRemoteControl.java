package com.sony.scalar.webapi.interfaces.server.smartremotecontrol.v1_2;

import com.sony.scalar.webapi.service.camera.v1_2.aelock.GetAELock;
import com.sony.scalar.webapi.service.camera.v1_2.aelock.GetAvailableAELock;
import com.sony.scalar.webapi.service.camera.v1_2.aelock.GetSupportedAELock;
import com.sony.scalar.webapi.service.camera.v1_2.aelock.SetAELock;
import com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode.GetAvailableBracketShootMode;
import com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode.GetBracketShootMode;
import com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode.GetSupportedBracketShootMode;
import com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode.SetBracketShootMode;
import com.sony.scalar.webapi.service.camera.v1_2.creativestyle.GetAvailableCreativeStyle;
import com.sony.scalar.webapi.service.camera.v1_2.creativestyle.GetCreativeStyle;
import com.sony.scalar.webapi.service.camera.v1_2.creativestyle.GetSupportedCreativeStyle;
import com.sony.scalar.webapi.service.camera.v1_2.creativestyle.SetCreativeStyle;
import com.sony.scalar.webapi.service.camera.v1_2.exposurecompensation.ExposureCompensationAPI;
import com.sony.scalar.webapi.service.camera.v1_2.exposuremode.GetAvailableExposureMode;
import com.sony.scalar.webapi.service.camera.v1_2.exposuremode.GetExposureMode;
import com.sony.scalar.webapi.service.camera.v1_2.exposuremode.GetSupportedExposureMode;
import com.sony.scalar.webapi.service.camera.v1_2.exposuremode.SetExposureMode;
import com.sony.scalar.webapi.service.camera.v1_2.fnumber.GetAvailableFNumber;
import com.sony.scalar.webapi.service.camera.v1_2.fnumber.GetFNumber;
import com.sony.scalar.webapi.service.camera.v1_2.fnumber.GetSupportedFNumber;
import com.sony.scalar.webapi.service.camera.v1_2.fnumber.SetFNumber;
import com.sony.scalar.webapi.service.camera.v1_2.focusmode.GetAvailableFocusMode;
import com.sony.scalar.webapi.service.camera.v1_2.focusmode.GetFocusMode;
import com.sony.scalar.webapi.service.camera.v1_2.focusmode.GetSupportedFocusMode;
import com.sony.scalar.webapi.service.camera.v1_2.focusmode.SetFocusMode;
import com.sony.scalar.webapi.service.camera.v1_2.getevent.GetEvent;
import com.sony.scalar.webapi.service.camera.v1_2.isonumber.GetAvailableIsoNumber;
import com.sony.scalar.webapi.service.camera.v1_2.isonumber.GetIsoNumber;
import com.sony.scalar.webapi.service.camera.v1_2.isonumber.GetSupportedIsoNumber;
import com.sony.scalar.webapi.service.camera.v1_2.isonumber.SetIsoNumber;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.GetAvailableLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.GetLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.GetSupportedLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.LiveviewAPI;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.SetLiveviewSize;
import com.sony.scalar.webapi.service.camera.v1_2.liveview.StartLiveviewWithSize;
import com.sony.scalar.webapi.service.camera.v1_2.misc.GetApplicationInfo;
import com.sony.scalar.webapi.service.camera.v1_2.misc.GetAvailableApiList;
import com.sony.scalar.webapi.service.camera.v1_2.pictureeffect.GetAvailablePictureEffect;
import com.sony.scalar.webapi.service.camera.v1_2.pictureeffect.GetPictureEffect;
import com.sony.scalar.webapi.service.camera.v1_2.pictureeffect.GetSupportedPictureEffect;
import com.sony.scalar.webapi.service.camera.v1_2.pictureeffect.SetPictureEffect;
import com.sony.scalar.webapi.service.camera.v1_2.postviewimage.GetAvailablePostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_2.postviewimage.GetPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_2.postviewimage.GetSupportedPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_2.postviewimage.SetPostviewImageSize;
import com.sony.scalar.webapi.service.camera.v1_2.programshift.GetSupportedProgramShift;
import com.sony.scalar.webapi.service.camera.v1_2.programshift.SetProgramShift;
import com.sony.scalar.webapi.service.camera.v1_2.recmode.RecModeAPI;
import com.sony.scalar.webapi.service.camera.v1_2.selftimer.SelfTimerAPI;
import com.sony.scalar.webapi.service.camera.v1_2.shutterspeed.GetAvailableShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_2.shutterspeed.GetShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_2.shutterspeed.GetSupportedShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_2.shutterspeed.SetShutterSpeed;
import com.sony.scalar.webapi.service.camera.v1_2.takepicture.ActTakePicture;
import com.sony.scalar.webapi.service.camera.v1_2.takepicture.AwaitTakePicture;
import com.sony.scalar.webapi.service.camera.v1_2.takepicturebytouch.ActTakePictureByTouch;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.CancelTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.GetTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_2.touchafposition.SetTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_2.whitebalance.GetAvailableWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_2.whitebalance.GetSupportedWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_2.whitebalance.GetWhiteBalance;
import com.sony.scalar.webapi.service.camera.v1_2.whitebalance.SetWhiteBalance;

/**
 * 
 * @version 1.2.0
 */

public interface SmartRemoteControl extends
        /* takePicture */
        ActTakePicture,
        AwaitTakePicture,

        /* exposure compensation (OLD style) */
        ExposureCompensationAPI,

        /* liveview (OLD style) */
        LiveviewAPI,

        /* liveview with size */
        SetLiveviewSize,
        GetLiveviewSize,
        GetAvailableLiveviewSize,
        GetSupportedLiveviewSize,
        StartLiveviewWithSize,
        
        /* rec mode (OLD style) */
        RecModeAPI,
        
        /* self timer (OLD style) */
        SelfTimerAPI,
        
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
        
        /* touch shutter */
        ActTakePictureByTouch,
        
        /* blacket shoot mode */
        SetBracketShootMode,
        GetBracketShootMode,
        GetAvailableBracketShootMode,
        GetSupportedBracketShootMode,
        
        /* focus mode */
        SetFocusMode,
        GetFocusMode,
        GetAvailableFocusMode,
        GetSupportedFocusMode,
        
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
        SetIsoNumber,
        GetIsoNumber,
        GetAvailableIsoNumber,
        GetSupportedIsoNumber,
        
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

        /* AELock */
        SetAELock,
        GetAELock,
        GetAvailableAELock,
        GetSupportedAELock,
        
        /* CreativeStyle */
        SetCreativeStyle,
        GetCreativeStyle,
        GetAvailableCreativeStyle,
        GetSupportedCreativeStyle,

        /* PictureEffect */
        SetPictureEffect,
        GetPictureEffect,
        GetAvailablePictureEffect,
        GetSupportedPictureEffect
{
    /**
     * SmartRemoteControl Camera service version 1.2
     * 
     * 
     */

}
