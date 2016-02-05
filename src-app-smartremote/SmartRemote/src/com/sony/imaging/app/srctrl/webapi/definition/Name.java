package com.sony.imaging.app.srctrl.webapi.definition;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 
 * WebAPI names definition.
 * @author 0000138134
 *
 */
public class Name {
	
    public static final String PREFIX_ACT = "act";
    public static final String PREFIX_AWAIT = "await";
    public static final String PREFIX_START = "start";
    public static final String PREFIX_STOP = "stop";
    public static final String PREFIX_GET = "get";
    public static final String PREFIX_SET = "set";
    public static final String PREFIX_GET_AVAILABLE = PREFIX_GET + "Available";
    public static final String PREFIX_GET_SUPPORTED = PREFIX_GET + "Supported";

    private static final String PARAM_EXPOSURE_COMPENSATION = "ExposureCompensation";
    private static final String PARAM_SELF_TIMER = "SelfTimer";
    private static final String PARAM_REC_MODE = "RecMode";
    private static final String PARAM_LIVEVIEW = "Liveview";
    private static final String PARAM_TAKE_PICTURE = "TakePicture";
    
    public static final String GET_VERSIONS = "getVersions";
    public static final String GET_METHOD_TYPES = "getMethodTypes";
    
    public static final String GET_APPLICATION_INFO = PREFIX_GET + "ApplicationInfo";

    public static final String ACT_TAKE_PICTURE = PREFIX_ACT + PARAM_TAKE_PICTURE;
    public static final String AWAIT_TAKE_PICTURE = PREFIX_AWAIT + PARAM_TAKE_PICTURE;

    public static final String START_REC_MODE = PREFIX_START + PARAM_REC_MODE;
    public static final String STOP_REC_MODE = PREFIX_STOP + PARAM_REC_MODE;

    public static final String START_LIVEVIEW = PREFIX_START + PARAM_LIVEVIEW;
    public static final String STOP_LIVEVIEW = PREFIX_STOP + PARAM_LIVEVIEW;

    public static final String GET_AVAILABLE_API_LIST = PREFIX_GET_AVAILABLE + "ApiList";
    public static final String SET_EXPOSURE_COMPENSATION = PREFIX_SET + PARAM_EXPOSURE_COMPENSATION;
    public static final String GET_EXPOSURE_COMPENSATION = PREFIX_GET + PARAM_EXPOSURE_COMPENSATION;
    public static final String GET_AVAILABLE_EXPOSURE_COMPENSATION = PREFIX_GET_AVAILABLE + PARAM_EXPOSURE_COMPENSATION;
    public static final String GET_SUPPORTED_EXPOSURE_COMPENSATION = PREFIX_GET_SUPPORTED + PARAM_EXPOSURE_COMPENSATION;
    public static final String SET_SELF_TIMER = PREFIX_SET + PARAM_SELF_TIMER;
    public static final String GET_SELF_TIMER = PREFIX_GET + PARAM_SELF_TIMER;
    public static final String GET_AVAILABLE_SELF_TIMER = PREFIX_GET_AVAILABLE + PARAM_SELF_TIMER;
    public static final String GET_SUPPORTED_SELF_TIMER = PREFIX_GET_SUPPORTED + PARAM_SELF_TIMER;

    public static final String GET_EVENT = "getEvent";
    
    public static final String START_LIVEVIEW_WITH_SIZE= "startLiveviewWithSize";

    public static final String SET_EXPOSURE_MODE = "setExposureMode";
    public static final String GET_EXPOSURE_MODE = "getExposureMode";
    public static final String GET_AVAILABLE_EXPOSURE_MODE = "getAvailableExposureMode";
    public static final String GET_SUPPORTED_EXPOSURE_MODE = "getSupportedExposureMode";
    
    public static final String SET_FNUMBER = "setFNumber";
    public static final String GET_FNUMBER = "getFNumber";
    public static final String GET_AVAILABLE_FNUMBER = "getAvailableFNumber";
    public static final String GET_SUPPORTED_FNUMBER = "getSupportedFNumber";

    public static final String SET_ISOSPEEDRATE = "setIsoSpeedRate";
    public static final String GET_ISOSPEEDRATE = "getIsoSpeedRate";
    public static final String GET_AVAILABLE_ISOSPEEDRATE = "getAvailableIsoSpeedRate";
    public static final String GET_SUPPORTED_ISOSPEEDRATE = "getSupportedIsoSpeedRate";
    
    public static final String GET_LIVEVIEW_SIZE = "getLiveviewSize";
    public static final String GET_AVAILABLE_LIVEVIEW_SIZE = "getAvailableLiveviewSize";
    public static final String GET_SUPPORTED_LIVEVIEW_SIZE = "getSupportedLiveviewSize";

    public static final String SET_POSTVIEWIMAGE_SIZE = "setPostviewImageSize";
    public static final String GET_POSTVIEWIMAGE_SIZE = "getPostviewImageSize";
    public static final String GET_AVAILABLE_POSTVIEWIMAGE_SIZE = "getAvailablePostviewImageSize";
    public static final String GET_SUPPORTED_POSTVIEWIMAGE_SIZE = "getSupportedPostviewImageSize";

    public static final String SET_PROGRAMSHIFT = "setProgramShift";
    public static final String GET_SUPPORTED_PROGRAMSHIFT = "getSupportedProgramShift";

    public static final String SET_SHOOTMODE = "setShootMode";
    public static final String GET_SHOOTMODE = "getShootMode";
    public static final String GET_AVAILABLE_SHOOTMODE = "getAvailableShootMode";
    public static final String GET_SUPPORTED_SHOOTMODE = "getSupportedShootMode";

    public static final String SET_SHUTTERSPEED = "setShutterSpeed";
    public static final String GET_SHUTTERSPEED = "getShutterSpeed";
    public static final String GET_AVAILABLE_SHUTTERSPEED = "getAvailableShutterSpeed";
    public static final String GET_SUPPORTED_SHUTTERSPEED = "getSupportedShutterSpeed";

    public static final String SET_TOUCHAFPOSITION = "setTouchAFPosition";
    public static final String GET_TOUCHAFPOSITION = "getTouchAFPosition";
    public static final String CANCEL_TOUCHAFPOSITION = "cancelTouchAFPosition";

    public static final String SET_WHITEBALANCE = "setWhiteBalance";
    public static final String GET_WHITEBALANCE = "getWhiteBalance";
    public static final String GET_SUPPORTED_WHITEBALANCE = "getSupportedWhiteBalance";
    public static final String GET_AVAILABLE_WHITEBALANCE = "getAvailableWhiteBalance";

    public static final String SET_FLASH_MODE = "setFlashMode";
    public static final String GET_FLASH_MODE = "getFlashMode";
    public static final String GET_AVAILABLE_FLASH_MODE = "getAvailableFlashMode";
    public static final String GET_SUPPORTED_FLASH_MODE = "getSupportedFlashMode";

    public static final String ACT_ZOOM = "actZoom";
    
    public final static Comparator<String> s_COMP = new Comparator<String>()
    {
        @Override
        public int compare(String l, String r)
        {
            return l.compareTo(r);
        }
    };
    
    public static final String[] s_DEFAULT_API_LIST = new String[]
        { Name.GET_VERSIONS, Name.GET_METHOD_TYPES, 
        Name.GET_APPLICATION_INFO, Name.ACT_TAKE_PICTURE, Name.AWAIT_TAKE_PICTURE, 
                Name.SET_EXPOSURE_COMPENSATION, Name.GET_EXPOSURE_COMPENSATION, Name.GET_AVAILABLE_EXPOSURE_COMPENSATION,
                Name.GET_SUPPORTED_EXPOSURE_COMPENSATION, Name.START_LIVEVIEW, Name.STOP_LIVEVIEW, Name.GET_LIVEVIEW_SIZE,
                Name.GET_AVAILABLE_LIVEVIEW_SIZE, Name.GET_SUPPORTED_LIVEVIEW_SIZE, Name.START_LIVEVIEW_WITH_SIZE
                , Name.START_REC_MODE, Name.STOP_REC_MODE, Name.SET_SELF_TIMER, Name.GET_SELF_TIMER, Name.GET_AVAILABLE_SELF_TIMER,
                Name.GET_SUPPORTED_SELF_TIMER, Name.GET_AVAILABLE_API_LIST, Name.SET_WHITEBALANCE,
                Name.GET_WHITEBALANCE, Name.GET_AVAILABLE_WHITEBALANCE, Name.GET_SUPPORTED_WHITEBALANCE, Name.GET_EVENT,
                Name.SET_TOUCHAFPOSITION, Name.GET_TOUCHAFPOSITION, Name.CANCEL_TOUCHAFPOSITION, Name.SET_FNUMBER, Name.GET_FNUMBER,
                Name.GET_AVAILABLE_FNUMBER,
                Name.GET_SUPPORTED_FNUMBER,
                Name.SET_SHUTTERSPEED,
                Name.GET_SHUTTERSPEED,
                Name.GET_AVAILABLE_SHUTTERSPEED,
                Name.GET_SUPPORTED_SHUTTERSPEED
                , Name.SET_ISOSPEEDRATE
                , Name.GET_ISOSPEEDRATE
                , Name.GET_AVAILABLE_ISOSPEEDRATE
                , Name.GET_SUPPORTED_ISOSPEEDRATE
                , Name.SET_EXPOSURE_MODE
                , Name.GET_EXPOSURE_MODE
                , Name.GET_AVAILABLE_EXPOSURE_MODE
                , Name.GET_SUPPORTED_EXPOSURE_MODE
                , Name.SET_POSTVIEWIMAGE_SIZE, Name.GET_POSTVIEWIMAGE_SIZE, Name.GET_AVAILABLE_POSTVIEWIMAGE_SIZE,
                Name.GET_SUPPORTED_POSTVIEWIMAGE_SIZE
                , Name.SET_PROGRAMSHIFT, Name.GET_SUPPORTED_PROGRAMSHIFT
                ,Name.SET_SHOOTMODE, Name.GET_SHOOTMODE, Name.GET_AVAILABLE_SHOOTMODE, Name.GET_SUPPORTED_SHOOTMODE
                ,Name.SET_FLASH_MODE, Name.GET_FLASH_MODE, Name.GET_AVAILABLE_FLASH_MODE, Name.GET_SUPPORTED_FLASH_MODE
                ,Name.ACT_ZOOM
                };
    static {
        Arrays.sort(s_DEFAULT_API_LIST);
    }

    public static final String[] s_PREINSTALL_API_LIST = new String[]
    { Name.GET_VERSIONS, Name.GET_METHOD_TYPES, 
        Name.GET_APPLICATION_INFO, Name.ACT_TAKE_PICTURE,
            Name.AWAIT_TAKE_PICTURE, Name.SET_EXPOSURE_COMPENSATION,
            Name.GET_EXPOSURE_COMPENSATION, Name.GET_AVAILABLE_EXPOSURE_COMPENSATION,
            Name.GET_SUPPORTED_EXPOSURE_COMPENSATION, Name.START_LIVEVIEW, Name.STOP_LIVEVIEW,
            Name.START_REC_MODE, Name.STOP_REC_MODE, Name.SET_SELF_TIMER,
            Name.GET_SELF_TIMER, Name.GET_AVAILABLE_SELF_TIMER, Name.GET_SUPPORTED_SELF_TIMER,
            Name.GET_AVAILABLE_API_LIST, Name.GET_EVENT, Name.SET_SHOOTMODE, Name.GET_SHOOTMODE,
            Name.GET_AVAILABLE_SHOOTMODE, Name.GET_SUPPORTED_SHOOTMODE,
            Name.SET_FLASH_MODE, Name.GET_FLASH_MODE, Name.GET_AVAILABLE_FLASH_MODE, Name.GET_SUPPORTED_FLASH_MODE,
            Name.ACT_ZOOM };
    static
    {
        Arrays.sort(s_PREINSTALL_API_LIST);
    }
}
