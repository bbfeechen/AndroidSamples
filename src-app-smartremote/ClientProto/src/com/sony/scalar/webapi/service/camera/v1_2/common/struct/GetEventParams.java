package com.sony.scalar.webapi.service.camera.v1_2.common.struct;

public class GetEventParams
{
    public String type;
    public boolean checkAvailability = false;      // When true, to issue the getAvailable API ASAP is recommended.

    /************************************************************/
    
    /* - - - */
    /* "SelfTimer" */
    public int currentSelfTimer = 0;
    public int[] selfTimerCandidates;

    /* - - - */
    /* "ExposureCompensation" */
    public int currentExposureCompensation = 0;
    public int maxExposureCompensation = 0;
    public int minExposureCompensation = 0;
    public int stepIndexOfExposureCompensation = 0;

    /* - - - */
    /* "AELock" */
    public boolean currentAeLock;
    public boolean[] aeLockCandidates;

    /* - - - */
    /* "BracketShootMode" */
    public String currentBracketShootMode;
    public String currentBracketShootModeOption;
    //public boolean checkAvailability;      // Cannot express complex candidates.

    /* - - - */
    /* "CreativeStyle" */
    public String currentCreativeStyle;
    public int currentCreativeStyleContrast = 0;
    public int currentCreativeStyleSaturation = 0;
    public int currentCreativeStyleSharpness = 0;
    //public boolean checkAvailability;      // Cannot express complex candidates.

    /* - - - */
    /* "ExposureMode" */
    public String currentExposureMode;
    public String[] exposureModeCandidates;
    
    /* - - - */
    /* "FNumber" */
    public String currentFNumber;
    public String[] fNumberCandidates;
    
    /* - - - */
    /* "FocusMode" */
    public String currentFocusMode;
    public String[] focusModeCandidates;
    
    /* - - - */
    /* "IsoNumber" */
    public String currentIso;
    public String[] isoCandidates;
    
    /* - - - */
    /* "PictureEffect" */
    public String currentPictureEffect;
    public String currentPictureEffectOption;
    //public boolean checkAvailability;      // Cannot express complex candidates.
    
    /* - - - */
    /* "ProgramShift" */
    // none
    
    /* - - - */
    /* "ShutterSpeed" */
    public String currentShutterSpeed;
    public String[] shutterSpeedCandidates;

    /* - - - */
    /* "WhiteBalance" */
    public String currentWhiteBalanceMode;
    public int currentColorTemperature = -1;
    //public boolean checkAvailability;      // Cannot express complex candidates.
    
    
    /************************************************************/
    /* - - - */
    /* "cameraStatus" */
    public String currentCameraStatus;
    
    /* - - - */
    /* "errorStatus" */
    public String fatalErrorStatus;
    public String mediaRecoveryErrorStatus;
    public String MediaTroubleErrorStatus;
    public String LowBatteryErrorStatus;
    public String NoMediaErrorStatus;
    public String BatteryTroubleErrorStatus;
    public String ThermalErrorStatus;
    public String RecoveryFaildErrorStatus;
    public String CaptureImpossibleErrorStatus;
    public String CaptureImpossibleNumverPossibleShotsErrorStatus;
    public String CaptureImpossibleMediaFullErrorStatus;
    
    /* - - - */
    /* "liveViewStatus" */
    public boolean liveViewStatus;
    
    /* - - - */
    /* "zoomInfomation" */
    public int zoomPosition;
    public int zoomNumberBox;
    public int zoomIndexCurrentBox;
    public int zoomPositionCurrentBox;

    /* - - - */
    /* "sceneRecognition" */
    public String sceneRecogniton;
    public String steadyRecogniton;
    public String motionRecogniton;

    /************************************************************/
    
    /* - - - */
    /* "TakePicture" */
    public boolean[] storedResults;
    public String[] originals;
    public String[] screenails;
    public String[] thumbnails;
    public String[] raws;
}
