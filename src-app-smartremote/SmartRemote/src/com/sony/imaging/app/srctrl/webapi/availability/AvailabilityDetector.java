package com.sony.imaging.app.srctrl.webapi.availability;

import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.FocusModeController;
import com.sony.imaging.app.base.shooting.widget.ISOSensitivityView;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationExposureMode;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFocusArea;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationFocusMode;
import com.sony.imaging.app.srctrl.webapi.definition.Name;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;

/**
 * Judges available APIs according to the states.
 * @author 0000138134
 *
 */
public class AvailabilityDetector {

	private static final String TAG = AvailabilityDetector.class.getSimpleName();
	private static final String IS_AVAILABLE = "isAvailable ";
	private static final String TRUE_STR = ": true";
	private static final String FALSE_STR = ": false";

	/**
	 * Returns currently available WebAPI list in array form.
	 * @param includeSetter
	 * @return
	 */
	public static synchronized String[] getAvailables(boolean includeSetter){
	    AvailableApiFilterFactory.IAvailableApiFilter apiFilter = AvailableApiFilterFactory.createApiFilter();
	    if(null == apiFilter)
	    {
	        return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
	    }
	    
        String[] result = null;
        AppCondition condition  = AppCondition.PREPARATION;

        condition = StateController.getInstance().getAppCondition();
        ArrayList<String> availableList = new ArrayList<String>();
        availableList.add(Name.GET_VERSIONS);
        availableList.add(Name.GET_METHOD_TYPES);
        availableList.add(Name.GET_APPLICATION_INFO);
        availableList.add(Name.GET_AVAILABLE_API_LIST);
        availableList.add(Name.GET_EVENT);
		switch(condition){
			case PREPARATION:
			    result = getAvailablesInPreparation(availableList, false);
                break;
			case SHOOTING_EE:
			    result = getAvailablesInShootingEe(availableList, includeSetter);
                break;
			case SHOOTING_MENU:
			case DIAL_INHIBIT:
			    result = getAvailablesInShootingMenu(availableList, false);
                break;
			case SHOOTING_INHIBIT:
			case SHOOTING_REMOTE:
			case SHOOTING_LOCAL:
			    result = getAvailablesInShootingInhibit(availableList, false);
                break;
			case SHOOTING_REMOTE_TOUCHAF:
                result = getAvailablesInShootingRemoteTouchAf(availableList, includeSetter);
                break;
			case SHOOTING_REMOTE_TOUCHAFASSIST:
                result = getAvailablesInShootingRemoteTouchAfAssist(availableList, includeSetter);
			    break;
			default:
				Log.e(TAG, "unknown state: " + condition.name());
				return null;
		}
		AppCondition currentCondition = StateController.getInstance().getAppCondition();
		if(condition != currentCondition) {
		    Log.v(TAG, "Condition was changed while obtaining API list from " + condition.name() + " to " + currentCondition.name() + "...  Try again.");
		    ParamsGenerator.updateAvailableApiList();
		}
		
		String[] ret = apiFilter.squeezeApiList(result);
		Log.v(TAG, "Available API List in " + condition.name() + " mode: " + Arrays.toString(ret));
		return ret;
	}
	
	private static String[] getAvailablesInPreparation(ArrayList<String> availableList, boolean includeSetter){
		availableList.add(Name.START_REC_MODE);
		availableList.add(Name.STOP_REC_MODE);
		
		return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
	}
	
	private static String[] getAvailablesInShootingEe(ArrayList<String> availableList, boolean includeSetter){
		availableList.add(Name.ACT_TAKE_PICTURE);
		//availableList.add(Name.START_REC_MODE);
        availableList.add(Name.STOP_REC_MODE);
        availableList.add(Name.START_LIVEVIEW);
		availableList.add(Name.STOP_LIVEVIEW);
        availableList.add(Name.START_LIVEVIEW_WITH_SIZE);
		addActZoom(availableList);
		addAwaitTakePicture(availableList);
		
        addSetSelfTimer(includeSetter, availableList);
		
        addExposureMode(includeSetter, availableList);
        addExposureCompensation(includeSetter, availableList);
	    addFNumber(includeSetter, availableList);
	    addIsoNumber(includeSetter, availableList);
	    addLiveviewSize(includeSetter, availableList);
        addPostviewImageSize(includeSetter, availableList);
	    addProgramShift(includeSetter, availableList);
        addShootMode(includeSetter, availableList);
	    addShutterSpeed(includeSetter, availableList);
	    addTouchAFPosition(includeSetter, availableList);
	    addWhiteBalance(includeSetter, availableList);
	    addFlashMode(includeSetter, availableList);

	    return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
	}
	
	private static String[] getAvailablesInShootingMenu(ArrayList<String> availableList, boolean includeSetter){
//		availableList.add(Name.START_REC_MODE);
		availableList.add(Name.STOP_REC_MODE);
		availableList.add(Name.START_LIVEVIEW);
		availableList.add(Name.STOP_LIVEVIEW);
        availableList.add(Name.START_LIVEVIEW_WITH_SIZE);
		
		addAwaitTakePicture(availableList);
		
        addSetSelfTimer(false, availableList);
        addExposureMode(false, availableList);
        addExposureCompensation(false, availableList);
	    addFNumber(false, availableList);
	    addIsoNumber(false, availableList);
	    addLiveviewSize(false, availableList);
	    addPostviewImageSize(false, availableList);
	    addProgramShift(false, availableList);
        addShootMode(false, availableList);
	    addShutterSpeed(false, availableList);
	    addWhiteBalance(false, availableList);
	    addFlashMode(false, availableList);

	    return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
	}
	
	private static String[] getAvailablesInShootingInhibit(ArrayList<String> availableList, boolean includeSetter){
//        availableList.add(Name.START_REC_MODE);
        availableList.add(Name.STOP_REC_MODE);
		availableList.add(Name.START_LIVEVIEW);
		availableList.add(Name.STOP_LIVEVIEW);
        availableList.add(Name.START_LIVEVIEW_WITH_SIZE);
        
		//addActZoom(availableList);
        addAwaitTakePicture(availableList);
        
        addSetSelfTimer(false, availableList);
        addExposureMode(false, availableList);
        addExposureCompensation(false, availableList);
        addFNumber(false, availableList);
        addIsoNumber(false, availableList);
        addLiveviewSize(false, availableList);
        addPostviewImageSize(false, availableList);
        addProgramShift(false, availableList);
        addShootMode(false, availableList);
        addShutterSpeed(false, availableList);
        addWhiteBalance(false, availableList);
	    addFlashMode(false, availableList);

		return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
	}
	
	private static String[] getAvailablesInShootingRemoteTouchAf(ArrayList<String> availableList, boolean includeSetter){
        availableList.add(Name.START_LIVEVIEW);
        availableList.add(Name.STOP_LIVEVIEW);
        availableList.add(Name.START_LIVEVIEW_WITH_SIZE);
		
		availableList.add(Name.ACT_TAKE_PICTURE);

		addActZoom(availableList);
		addAwaitTakePicture(availableList);

		addTouchAFPosition(includeSetter, availableList);

        addPostviewImageSize(false, availableList);
        addSetSelfTimer(false, availableList);
        addExposureMode(false, availableList);
        addExposureCompensation(false, availableList);
        addFNumber(false, availableList);
        addIsoNumber(false, availableList);
        addLiveviewSize(false, availableList);
        addPostviewImageSize(false, availableList);
        addProgramShift(false, availableList);
        addShootMode(false, availableList);
        addShutterSpeed(false, availableList);
        addWhiteBalance(false, availableList);
	    addFlashMode(false, availableList);

        availableList.add(Name.CANCEL_TOUCHAFPOSITION);

        return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
	}

    private static String[] getAvailablesInShootingRemoteTouchAfAssist(ArrayList<String> availableList, boolean includeSetter){
        availableList.add(Name.START_LIVEVIEW);
        availableList.add(Name.STOP_LIVEVIEW);
        availableList.add(Name.START_LIVEVIEW_WITH_SIZE);
        
        availableList.add(Name.ACT_TAKE_PICTURE);
		addActZoom(availableList);
        addAwaitTakePicture(availableList);

        addPostviewImageSize(false, availableList);
        addSetSelfTimer(false, availableList);
        addExposureMode(false, availableList);
        addExposureCompensation(false, availableList);
        addFNumber(false, availableList);
        addIsoNumber(false, availableList);
        addLiveviewSize(false, availableList);
        addPostviewImageSize(false, availableList);
        addProgramShift(false, availableList);
        addShootMode(false, availableList);
        addShutterSpeed(false, availableList);
        addWhiteBalance(false, availableList);
	    addFlashMode(false, availableList);

        availableList.add(Name.CANCEL_TOUCHAFPOSITION);

        return availableList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
    }
	
    private static void addExposureCompensation(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }
        
        if(includeSetter) {
            boolean bSet = false;
            if(CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode) ||
                    CameraOperationExposureMode.EXPOSURE_MODE_APERATURE.equals(exposureMode) ||
                    CameraOperationExposureMode.EXPOSURE_MODE_SHUTTER.equals(exposureMode))
            {
                // {PROGRAM-AUTO, A, S} Exposure
                bSet = true;
            }
            else if(CameraOperationExposureMode.EXPOSURE_MODE_MANUAL.equals(exposureMode))
            {
                String isoNumber = ParamsGenerator.peekIsoSpeedRateParamsSnapshot().currentIsoSpeedRate;
                if(null != isoNumber && ISOSensitivityView.AUTO_ISO_SENSITIVITY_STRING.equals(isoNumber))
                {
                    // Manual Exposure && ISO=AUTO
                    bSet = true;
                }
            }
            else
            {
                // NG
            }
            
            if(bSet)
            {
                availableList.add(Name.SET_EXPOSURE_COMPENSATION);
            }
        }
        availableList.add(Name.GET_EXPOSURE_COMPENSATION);
        availableList.add(Name.GET_AVAILABLE_EXPOSURE_COMPENSATION);
        availableList.add(Name.GET_SUPPORTED_EXPOSURE_COMPENSATION);
    }

    private static void addSetSelfTimer(boolean includeSetter, ArrayList<String> availableList){
		if(ParamsGenerator.s_SELFTIMER_VALUE_INITIALIZED){
		    if(includeSetter) {
		        availableList.add(Name.SET_SELF_TIMER);
		    }
            availableList.add(Name.GET_SELF_TIMER);
            availableList.add(Name.GET_AVAILABLE_SELF_TIMER);
		}
        availableList.add(Name.GET_SUPPORTED_SELF_TIMER);
	}
	
	
	private static void addAwaitTakePicture(ArrayList<String> availableList){
		ShootingStatus status = ShootingHandler.getInstance().getShootingStatus();
		if(ShootingStatus.FINISHED.equals(status)
		        || ShootingStatus.PROCESSING.equals(status)
		        || ShootingStatus.DEVELOPING.equals(status)
				){
			availableList.add(Name.AWAIT_TAKE_PICTURE);
		}
	}
	
    private static void addExposureMode(boolean includeSetter, ArrayList<String> availableList) {
        boolean hasModeDial = StateController.getInstance().hasModeDial();
        if(!hasModeDial)
        {
            if(includeSetter) {
                availableList.add(Name.SET_EXPOSURE_MODE);
            }
            availableList.add(Name.GET_AVAILABLE_EXPOSURE_MODE);
    	}
    	availableList.add(Name.GET_EXPOSURE_MODE);
    	availableList.add(Name.GET_SUPPORTED_EXPOSURE_MODE);
    }
    
    private static void addFNumber(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }
        
    	if(includeSetter) {
            if(CameraOperationExposureMode.EXPOSURE_MODE_APERATURE.equals(exposureMode) ||
                    CameraOperationExposureMode.EXPOSURE_MODE_MANUAL.equals(exposureMode))
            {
                availableList.add(Name.SET_FNUMBER);
            }
    	}
    	availableList.add(Name.GET_FNUMBER);
    	availableList.add(Name.GET_AVAILABLE_FNUMBER);
    	availableList.add(Name.GET_SUPPORTED_FNUMBER);
    }
    
    private static void addIsoNumber(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }

        if(includeSetter) {
            if(!CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode))
            {
                availableList.add(Name.SET_ISOSPEEDRATE);
            }
    	}
        availableList.add(Name.GET_ISOSPEEDRATE);
        availableList.add(Name.GET_AVAILABLE_ISOSPEEDRATE);
        availableList.add(Name.GET_SUPPORTED_ISOSPEEDRATE);
    }
    
    private static void addLiveviewSize(boolean includeSetter, ArrayList<String> availableList) {
    	if(includeSetter) {
    		//availableList.add(Name.SET_LIVEVIEW_SIZE);
    	}
        availableList.add(Name.GET_LIVEVIEW_SIZE);
        availableList.add(Name.GET_AVAILABLE_LIVEVIEW_SIZE);
        availableList.add(Name.GET_SUPPORTED_LIVEVIEW_SIZE);
    }
    
    private static void addPostviewImageSize(boolean includeSetter, ArrayList<String> availableList) {
    	if(includeSetter) {
    		availableList.add(Name.SET_POSTVIEWIMAGE_SIZE);
    	}
        availableList.add(Name.GET_POSTVIEWIMAGE_SIZE);
        availableList.add(Name.GET_AVAILABLE_POSTVIEWIMAGE_SIZE);
        availableList.add(Name.GET_SUPPORTED_POSTVIEWIMAGE_SIZE);
    }
    
    private static void addProgramShift(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }
        
    	if(includeSetter) {
    	    if(CameraOperationExposureMode.EXPOSURE_MODE_PROGRAM_AUTO.equals(exposureMode))
    	    {
    	        availableList.add(Name.SET_PROGRAMSHIFT);
    	    }
    	}
        availableList.add(Name.GET_SUPPORTED_PROGRAMSHIFT);
    }
    
    private static void addShootMode(boolean includeSetter, ArrayList<String> availableList) {
        if(includeSetter) {
            availableList.add(Name.SET_SHOOTMODE);
        }
        availableList.add(Name.GET_SHOOTMODE);
        availableList.add(Name.GET_AVAILABLE_SHOOTMODE);
        availableList.add(Name.GET_SUPPORTED_SHOOTMODE);
    }

    private static void addShutterSpeed(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }
        
        if(includeSetter) {
            if(CameraOperationExposureMode.EXPOSURE_MODE_SHUTTER.equals(exposureMode) ||
                    CameraOperationExposureMode.EXPOSURE_MODE_MANUAL.equals(exposureMode))
            {
                availableList.add(Name.SET_SHUTTERSPEED);
            }
        }
        availableList.add(Name.GET_SHUTTERSPEED);
        availableList.add(Name.GET_AVAILABLE_SHUTTERSPEED);
        availableList.add(Name.GET_SUPPORTED_SHUTTERSPEED);
    }
    
    private static void addTouchAFPosition(boolean includeSetter, ArrayList<String> availableList) {
        // whether FlexSpot is available or not
        boolean matched = false;
        String[] availableAfArea = ParamsGenerator.peekFocusAreaParamsSnapshot().focusAreaCandidates;
        for(String available : availableAfArea)
        {
            if(CameraOperationFocusArea.FOCUS_AREA_FLEX.equals(available)) {
                matched = true;
                break;
            }
        }
        if(!matched) { 
            Log.e(TAG, "The flexible spot af is not available.");
            return;
        }
        
        // MF or AF?
        String focusMode = ParamsGenerator.peekFocusModeParamsSnapshot().currentFocusMode;
        if(null == focusMode) {
            Log.e(TAG, "The current focus mode is unknown.");
            return;
        }
        if(CameraOperationFocusMode.FOCUS_MODE_MF.equals(focusMode)) {
            Log.v(TAG, "MF disables Touch AF.");
            return;
        }
        
        FocusModeController fmc = FocusModeController.getInstance();
        if( fmc.isFocusHeld() ) {
            Log.v(TAG, "FoucusHeld disables Touch AF.");
            return;
        }

        if(includeSetter) {
    		availableList.add(Name.SET_TOUCHAFPOSITION);
    	}
        availableList.add(Name.GET_TOUCHAFPOSITION);
    }
    
    private static void addWhiteBalance(boolean includeSetter, ArrayList<String> availableList) {
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }

        if(includeSetter) {
            if(!CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode))
            {
                availableList.add(Name.SET_WHITEBALANCE);
            }
    	}
        availableList.add(Name.GET_WHITEBALANCE);
        availableList.add(Name.GET_SUPPORTED_WHITEBALANCE);
        availableList.add(Name.GET_AVAILABLE_WHITEBALANCE);
    }

    private static void addFlashMode(boolean includeSetter, ArrayList<String> availableList){
        String exposureMode = ParamsGenerator.peekExposureModeParamsSnapshot().currentExposureMode;
        if(null == exposureMode) {
            Log.e(TAG, "The current exposure mode is unknown.");
            return;
        }

        String[] flashModeCandidates = ParamsGenerator.peekFlashModeParamsSnapshot().flashModeCandidates;
        if(flashModeCandidates != null && flashModeCandidates.length > 0) {
	        if(includeSetter) {
	            availableList.add(Name.SET_FLASH_MODE);
	        }
	        availableList.add(Name.GET_FLASH_MODE);
	        availableList.add(Name.GET_AVAILABLE_FLASH_MODE);
        }
        availableList.add(Name.GET_SUPPORTED_FLASH_MODE);
    }
    
	private static void addActZoom(ArrayList<String> availableList){
        int zoomNumberBox = ParamsGenerator.peekZoomInformationParamsSnapshot().zoomNumberBox;
        if (zoomNumberBox > 0) {
			availableList.add(Name.ACT_ZOOM);
        }
	}    
    
	/**
	 * Returns whether the WebAPI is currently available or not.
	 * @param name
	 * @return
	 */
	public static synchronized boolean isAvailable(String name){
		String[] availables;
		if(name.startsWith(Name.PREFIX_SET) || name.startsWith(Name.PREFIX_GET_AVAILABLE)){
			availables = getAvailables(true);
		} else {
			availables = getAvailables(false);
		}
		
		for(String value : availables){
			if(name.equals(value)){
				Log.v(TAG, IS_AVAILABLE + name + " in " + StateController.getInstance().getAppCondition() + TRUE_STR);
				return true;
			}
		}
		Log.v(TAG, IS_AVAILABLE + name + " in " + StateController.getInstance().getAppCondition()  + FALSE_STR);
		return false;
	}
	
    // guide/getServiceProtocols:camera/getApplicationInfo:camera/getAvailableApiList:camera/setShootMode:camera/getShootMode:camera/getSupportedShootMode:camera/getAvailableShootMode:camera/setSelfTimer:camera/getSelfTimer:camera/getSupportedSelfTimer:camera/getAvailableSelfTimer:camera/setPostviewImageSize:camera/getPostviewImageSize:camera/getSupportedPostviewImageSize:camera/getAvailablePostviewImageSize:camera/startRecMode:camera/stopRecMode:camera/startLiveview:camera/stopLiveview:camera/actTakePicture:camera/awaitTakePicture:camera/startMovieRec:camera/stopMovieRec:camera/actZoom:camera/getEvent:camera/actZoom:accessControl/actEnableMethods
    private static final String[] PUBLIC_API = {
        "guide/getServiceProtocols"
        , "guide/getVersions"
        , "guide/getMethodTypes"
        , "camera/getVersions"
        , "camera/getMethodTypes"
        , "accessControl/getVersions"
        , "accessControl/getMethodTypes"
        , "camera/getApplicationInfo"
        , "camera/getAvailableApiList"
        , "camera/setShootMode"
        , "camera/getShootMode"
        , "camera/getSupportedShootMode"
        , "camera/getAvailableShootMode"
        , "camera/setSelfTimer"
        , "camera/getSelfTimer"
        , "camera/getSupportedSelfTimer"
        , "camera/getAvailableSelfTimer"
        , "camera/setPostviewImageSize"
        , "camera/getPostviewImageSize"
        , "camera/getSupportedPostviewImageSize"
        , "camera/getAvailablePostviewImageSize"
        , "camera/startRecMode"
        , "camera/stopRecMode"
        , "camera/startLiveview"
        , "camera/stopLiveview"
        , "camera/actTakePicture"
        , "camera/awaitTakePicture"
        , "camera/getEvent"
        , "camera/actZoom"
        , "accessControl/actEnableMethods"};
    static
    {
        Arrays.sort(PUBLIC_API);
    }
	public static ArrayList<String> getPrivateApiList() {
	    ArrayList<String> privateApiList = new ArrayList<String>();
	    for(String api : Name.s_DEFAULT_API_LIST)
	    {
	        String apiValue = "camera/"+api;
	        int result = Arrays.binarySearch(PUBLIC_API, apiValue, Name.s_COMP);
	        if(result < 0)
	        {
	            privateApiList.add(apiValue);
	        }
	    }
	    return privateApiList;
	}
}
