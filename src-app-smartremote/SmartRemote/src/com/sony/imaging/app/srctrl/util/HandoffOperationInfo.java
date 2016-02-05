package com.sony.imaging.app.srctrl.util;

/**
 * 
 * Definition of Main thread operation. These will be used in {@link OperationReceiver#operate(int, Object...)}
 * @author 0000138134
 *
 */
public class HandoffOperationInfo {
	public static final int IS_AVAILABLE_EXPOSURE_COMPENSATION = 0;
	public static final int SET_EXPOSURE_COMPENSATION = 1;
	public static final int GET_EXPOSURE_COMPENSATION_INDEX = 2;
	public static final int GET_EXPOSURE_COMPENSATION_STEP = 3;
	public static final int GET_SUPPORTED_EXPOSURE_COMPENSATION = 4;
	public static final int GET_AVAILABLE_EXPOSURE_COMPENSATION = 5;
	public static final int IS_AVAILABLE_DRIVE_MODE = 6;
	public static final int SET_DRIVE_MODE = 7;
	public static final int GET_DRIVE_MODE = 8;
	public static final int GET_SUPPORTED_DRIVE_MODE = 9;
	public static final int GET_AVAILABLE_DRIVE_MODE = 10;
	public static final int IS_AVAILABLE_SPECIFIC_DRIVE_MODE = 11;
	public static final int SET_SELF_TIMER = 12;
	public static final int GET_SELF_TIMER = 13;
	public static final int GET_SUPPORTED_SELF_TIMER = 14;
	public static final int GET_AVAILABLE_SELF_TIMER = 15;
	public static final int GET_EXPOSURE_MODE = 16;
	public static final int GET_SELECTED_SCENE = 17;
	public static final int IS_SELF_TIMER = 18;
	public static final int MOVE_TO_SHOOTING_STATE = 19;
	public static final int MOVE_TO_NETWORK_STATE = 20;
	public static final int MOVE_TO_CAPTURE_STATE = 21;
	public static final int EXCUTE_TERMINATE_CAUTION = 22;
    public static final int GET_CAMERA_SETTING = 23;
    public static final int GET_CAMERA_SETTING_AVAILABLE = 24;
    public static final int GET_CAMERA_SETTING_SUPPORTED = 25;
    public static final int SET_CAMERA_SETTING = 26;
    public static final int MOVE_TO_S1ON_STATE_FOR_TOUCH_AF = 27;
	
	/** IDs used for 
	 * <ul> 
	 *   <li>check of the camera setting availability with IS_AVAILABLE_CAMERA_SETTING</li>
	 *   <li>retrieval of the camera setting with GET_CAMERA_SETTING</li>
	 * </ul>
	 * 
	 * @see #GET_CAMERA_SETTING
	 * @see #IS_CAMERA_SETTING_AVAILABLE
	 */
	public enum CameraSettings {
	    TOUCH_AF,
	    F_NUMBER,
	    SHUTTER_SPEED,
	    ISO_NUMBER,
	    EXPOSURE_MODE,
	    WHITE_BALANCE,
	    LIVEVIEW_SIZE,
	    POSTVIEW_IMAGE_SIZE,
	    PROGRAM_SHIFT,
	    FLASH_MODE,
	    ZOOM,
        // EXPOSURE_COMPENSATION,
        // SELF_TIMER,
	}
}
