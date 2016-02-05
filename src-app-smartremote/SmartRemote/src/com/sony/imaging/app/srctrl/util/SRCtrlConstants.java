package com.sony.imaging.app.srctrl.util;

import com.sony.imaging.app.srctrl.webapi.definition.ServiceType;

/**
 * 
 * Almost all constants are contained in this class.
 * @author 0000138134
 *
 */
public class SRCtrlConstants {
	public static final String SERVER_NAME = "Smart Remote Control";
    public static final String SERVER_KEYWORD = "__MKN__";
	public static final String SERVER_VERSION = "2.0.0";

	public static final int KIKILOG_ID_APP_LAUNCH = 0x00105;
	public static final int KIKILOG_ID_REMOTE_SHOOTING = 0x01029;
	public static final int KIKILOG_ID_LOCAL_SHOOTING = 0x0102A;
	
	public static final int BROADCAST_RECEIVER_TIMEOUT = 8000;
	
    public static final String DEVICE_DISCOVERY_UNIQUE_SERVICE_ID = "000000001000";	// APPLICATION UNIQUE VALUE 1000-1fff
    public static final String DEVICE_DISCOVERY_ACTION_LIST_PATH = "/sony";
        
    public static final boolean SUPPORT_FULL_RCV_EVENT = false;			//  TODO Deferred for next update.
    
    public static final int DELAY_WIFI_ENABLE = 5000;
    
    public static final int CHANGE_MODE_TIMEOUT = 20000;
    
    public static final int RECEIVE_EVENT_TIMEOUT = 15000;
    public static final int RECEIVE_EVENT_WAIT_PREVIOUS_TIMEOUT = 5000;

    public static final int TAKE_PICTURE_TIMEOUT = 15000;
    
    public static final int FOCUS_EVENT_TIMEOUT = 15*1000;
    
    public static final String URL_HTTP_PREFIX = "http://";
    public static final String MY_IP_ADDRESS = "192.168.122.1";			// defined by framework
    public static final int HTTP_PORT_INT = 8080;
    public static final int NUM_OF_SERVER_THREADS = 7;
    public static final int NUM_OF_SERVER_BACKLOGS = 10;

    public static final String SERVLETS = "servlets";
    public static final String PORT = "port";
    public static final String NUM_OF_THREADS = "num_of_threads";
    public static final String NUM_OF_BACKLOGS = "backlog";
    
    public static final String SERVLET_ROOT_PATH_WEBAPI = DEVICE_DISCOVERY_ACTION_LIST_PATH + "/";
    public static final String SERVLET_ROOT_PATH_WEBAPI_GUIDE = SERVLET_ROOT_PATH_WEBAPI + ServiceType.GUIDE;
    public static final String SERVLET_ROOT_PATH_WEBAPI_CAMERA = SERVLET_ROOT_PATH_WEBAPI + ServiceType.CAMERA;
    public static final String SERVLET_ROOT_PATH_WEBAPI_ACCESS_CONTROL = SERVLET_ROOT_PATH_WEBAPI + ServiceType.ACCESS_CONTROL;
    public static final String SERVLET_ROOT_PATH_LIVEVIEW = "/liveview/";
    public static final String SERVLET_ROOT_PATH_POSTVIEW = "/postview/";
    public static final String SERVLET_ROOT_PATH_POSTVIEW_ON_MEMORY = "/postview/memory";
    
    public static final String WEBAPI_BASE_URL = URL_HTTP_PREFIX + MY_IP_ADDRESS + ":" + HTTP_PORT_INT + DEVICE_DISCOVERY_ACTION_LIST_PATH;
    public static final String WEBAPI_ROOT_FILENAME = "index.html";
    
    public static final String LIVEVIEW_FILENAME = "liveviewstream";
    public static final String LIVEVIEW_URL = URL_HTTP_PREFIX + MY_IP_ADDRESS + ":" + HTTP_PORT_INT + SERVLET_ROOT_PATH_LIVEVIEW + LIVEVIEW_FILENAME;
    
    public static final String POSTVIEW_FILENAME_PREFIX = "pict";
    public static final String POSTVIEW_FILENAME_EXTENTION = ".JPG";
    public static final String POSTVIEW_DIRECTORY = URL_HTTP_PREFIX + MY_IP_ADDRESS + ":" + HTTP_PORT_INT + SERVLET_ROOT_PATH_POSTVIEW;
    public static final String POSTVIEW_DIRECTORY_ON_MEMORY = URL_HTTP_PREFIX + MY_IP_ADDRESS + ":" + HTTP_PORT_INT + SERVLET_ROOT_PATH_POSTVIEW_ON_MEMORY;
    
    public static final String TEXT_ENCODING_UTF8 = "UTF-8";
    public static final String FILE_EXTENTION_HTML = "html";
    public static final String CONTENT_TYPE_APP_OCTET_STREAM = "application/octet-stream";
    
    public static final int WPS_PIN_TIMEOUT = 120000;	// defined by framework
    public static final int WPS_PIN_CHARACTERS_LONG = 8;	// WPS rule
    public static final int WPS_PIN_CHARACTERS_SHORT = 4;	// WPS rule
    
    public static final float EXPOSURE_COMPENSATION_STEP_1_2 = 0.50f;
    public static final float EXPOSURE_COMPENSATION_STEP_1_3 = 0.333f;
    
    public static final int JPEG_QUALITY_FINE = 50;

    private static final float[] s_FNUMBER_TABLE= {
        1.0f
        , 1.1f
        , 1.3f
        , 1.4f
        , 1.6f
        , 1.7f
        , 1.8f
        , 2.0f
        , 2.2f
        , 2.5f
        , 2.8f
        , 3.2f
        , 3.5f
        , 4.0f
        , 4.5f
        , 5.0f
        , 5.6f
        , 6.3f
        , 7.1f
        , 8.0f
        , 9.0f
        , 10.0f
        , 11.0f
        , 13.0f
        , 14.0f
        , 16.0f
        , 18.0f
        , 20.0f
        , 22.0f
        , 25.0f
        , 29.0f
        , 32.0f
        , 36.0f
        , 40.0f
        , 45.0f
        , 51.0f
        , 57.0f
        , 64.0f
        , 72.0f
        , 81.0f
        , 90.0f
    };
    public static final float[] getFNumberTable()
    {
        return s_FNUMBER_TABLE;
    }
    private static final int[][] s_SHUTTER_SPEED_TABLE= {
        {1,12000}
        ,{1,8000}
        ,{1,6400}
        ,{1,5000}
        ,{1,4000}
        ,{1,3200}
        ,{1,2500}
        ,{1,2000}
        ,{1,1600}
        ,{1,1250}
        ,{1,1000}
        ,{1,800}
        ,{1,640}
        ,{1,500}
        ,{1,400}
        ,{1,320}
        ,{1,250}
        ,{1,200}
        ,{1,160}
        ,{1,125}
        ,{1,100}
        ,{1,80}
        ,{1,60}
        ,{1,50}
        ,{1,40}
        ,{1,30}
        ,{1,25}
        ,{1,20}
        ,{1,15}
        ,{1,13}
        ,{1,10}
        ,{1,8}
        ,{1,6}
        ,{1,5}
        ,{1,4}
        ,{1,3}
        ,{4,10}
        ,{5,10}
        ,{10,16} // 0.6 = 6/10 <= 10/16
        ,{8,10}
        ,{1,1}
        ,{13,10}
        ,{16,10}
        ,{2,1}
        ,{25,10}
        ,{32,10}
        ,{4,1}
        ,{5,1}
        ,{6,1}
        ,{8,1}
        ,{10,1}
        ,{13,1}
        ,{15,1}
        ,{20,1}
        ,{25,1}
        ,{30,1}
    };
    public static final int[][] getShutterSpeedTable()
    {
        return s_SHUTTER_SPEED_TABLE;
    }
    
    public static final String[] s_EMPTY_STRING_ARRAY = new String[0];
    public static final int[] s_EMPTY_INT_ARRAY = new int[0];
}
