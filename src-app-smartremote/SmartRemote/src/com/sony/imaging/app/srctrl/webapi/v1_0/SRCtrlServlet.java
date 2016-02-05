package com.sony.imaging.app.srctrl.webapi.v1_0;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.liveview.LiveviewContainer;
import com.sony.imaging.app.srctrl.liveview.LiveviewLoader;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyExposureMode;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyFNumber;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyFlashMode;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyIsoNumber;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyLiveviewSize;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyPostviewImageSize;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyProgramShift;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxySelftimer;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyShootMode;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyShutterSpeed;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyTouchAFPosition;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyWhiteBalance;
import com.sony.imaging.app.srctrl.util.cameraproxy.CameraProxyZoom;
import com.sony.imaging.app.srctrl.webapi.availability.AvailabilityDetector;
import com.sony.imaging.app.srctrl.webapi.availability.AvailableManager;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.definition.StatusCode;
import com.sony.imaging.app.srctrl.webapi.specific.RecModeTransitionHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.WaitingStatus;
import com.sony.imaging.app.srctrl.webapi.util.ApiCallLog;
import com.sony.mexi.orb.servlet.smartremotecontrol.v1_0.SmartRemoteControlServletBase;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventAELockParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventAvailableApiListParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventBeepModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventBracketShootModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventCameraFunctionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventCameraFunctionResultParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventCameraStatusParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventContinuousErrorParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventCreativeStyleParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventExposureCompensationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventExposureModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFNumberParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFlashModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFocusModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventFormatStatusParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventIsoSpeedRateParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventLiveviewOrientationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventLiveviewStatusParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventMovieQualityParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventPictureEffectParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventPostviewImageSizeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventProgramShiftParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventSceneRecognitionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventSelfTimerParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventShootModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventShutterSpeedParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventSteadyModeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventStillSizeParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventStorageInformationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventTakePictureParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventTouchAFPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventTriggeredErrorParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventViewAngleParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventWhiteBalanceParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.GetEventZoomInformationParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFPositionParams;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParamCandidate;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParams;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetAvailableExposureCompensationCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetExposureCompensationCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.GetSupportedExposureCompensationCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposurecompensation.SetExposureCompensationCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetAvailableExposureModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetExposureModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.GetSupportedExposureModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.exposuremode.SetExposureModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetAvailableFlashModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetFlashModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.GetSupportedFlashModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.flashmode.SetFlashModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetAvailableFNumberCallback;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetFNumberCallback;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.GetSupportedFNumberCallback;
import com.sony.scalar.webapi.service.camera.v1_0.fnumber.SetFNumberCallback;
import com.sony.scalar.webapi.service.camera.v1_0.getevent.GetEventCallback;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetAvailableIsoSpeedRateCallback;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetIsoSpeedRateCallback;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.GetSupportedIsoSpeedRateCallback;
import com.sony.scalar.webapi.service.camera.v1_0.isospeedrate.SetIsoSpeedRateCallback;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetAvailableLiveviewSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetLiveviewSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.GetSupportedLiveviewSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.liveview.StartLiveviewCallback;
import com.sony.scalar.webapi.service.camera.v1_0.misc.GetApplicationInfoCallback;
import com.sony.scalar.webapi.service.camera.v1_0.misc.GetAvailableApiListCallback;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetAvailablePostviewImageSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetPostviewImageSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.GetSupportedPostviewImageSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.postviewimage.SetPostviewImageSizeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.programshift.GetSupportedProgramShiftCallback;
import com.sony.scalar.webapi.service.camera.v1_0.programshift.SetProgramShiftCallback;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetAvailableSelfTimerCallback;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetSelfTimerCallback;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.GetSupportedSelfTimerCallback;
import com.sony.scalar.webapi.service.camera.v1_0.selftimer.SetSelfTimerCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetAvailableShootModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetShootModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.GetSupportedShootModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shootmode.SetShootModeCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetAvailableShutterSpeedCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetShutterSpeedCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.GetSupportedShutterSpeedCallback;
import com.sony.scalar.webapi.service.camera.v1_0.shutterspeed.SetShutterSpeedCallback;
import com.sony.scalar.webapi.service.camera.v1_0.takepicture.ActTakePictureCallback;
import com.sony.scalar.webapi.service.camera.v1_0.takepicture.AwaitTakePictureCallback;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.CancelTouchAFPositionCallback;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.GetTouchAFPositionCallback;
import com.sony.scalar.webapi.service.camera.v1_0.touchafposition.SetTouchAFPositionCallback;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetAvailableWhiteBalanceCallback;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetSupportedWhiteBalanceCallback;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.GetWhiteBalanceCallback;
import com.sony.scalar.webapi.service.camera.v1_0.whitebalance.SetWhiteBalanceCallback;
import com.sony.scalar.webapi.service.camera.v1_0.zoom.ActZoomCallback;

/**
 * This class will call camera functions according to the invoked WebAPI.
 * 
 * @author 0000138134
 * 
 */
public class SRCtrlServlet extends SmartRemoteControlServletBase {
	private static final String TAG = SRCtrlServlet.class.getSimpleName();
	private static final long serialVersionUID = 1L;
	
	@Override
	public int actTakePicture(ActTakePictureCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		ShootingHandler sHandler = ShootingHandler.getInstance();
		ShootingHandler.ShootingStatus currentStatus = sHandler.getShootingStatus();
		
		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			StateController stateController = StateController.getInstance();
			AppCondition appCondition = stateController.getAppCondition();
			if(AppCondition.SHOOTING_REMOTE_TOUCHAF == appCondition ||
			        AppCondition.SHOOTING_REMOTE_TOUCHAFASSIST == appCondition) {
			    stateController.setLastAppConditionBeforeCapturing(AppCondition.SHOOTING_REMOTE_TOUCHAF);
			} else {
			    stateController.setLastAppConditionBeforeCapturing(AppCondition.SHOOTING_REMOTE);
			}
			stateController.setAppCondition(AppCondition.SHOOTING_REMOTE);
			
			
			currentStatus = sHandler.createStillPicture();
			
			if (ShootingHandler.ShootingStatus.FINISHED.equals(currentStatus)) {
				String[] result = sHandler.getUrlArrayResult();
				if (result == null) {
					Log.v(TAG, "FAILURE: No url is available.");
					returnCb.handleStatus(StatusCode.FAILED_SHOOTING.toInt(), "Shooting Succeeded: No URL");
				}
				else {
					Log.v(TAG, "SUCCESS: Finished capturing.");
					returnCb.returnCb(result);
				}
			}
			else if (ShootingHandler.ShootingStatus.FAILED.equals(currentStatus)) {
				Log.v(TAG, "FAILURE: Shooting failed: " + sHandler.getErrorStatus().toString());
				returnCb.handleStatus(StatusCode.FAILED_SHOOTING.toInt(), "Shooting Failed");
			}
			else if (ShootingHandler.ShootingStatus.PROCESSING.equals(currentStatus) || 
			        ShootingHandler.ShootingStatus.DEVELOPING.equals(currentStatus) ) {
				Log.v(TAG, "PROCESSING: Long capturing is not finished yet.");
				returnCb.handleStatus(StatusCode.STILL_CAPTURING_NOT_FINISHED.toInt(), "Long shooting");
			}
			else if (ShootingHandler.ShootingStatus.READY.equals(currentStatus)) {
				Log.e(TAG, "FAILURE: Unexpected READY state after shooting.");
				returnCb.handleStatus(StatusCode.ANY.toInt(), "Unexpected state");
			}
		}
		else {
			Log.e(TAG, "Failed to start taking picture because of current application state");
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int awaitTakePicture(AwaitTakePictureCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init(false);
        //////

		ShootingHandler sHandler = ShootingHandler.getInstance();
		ShootingStatus currentStatus = sHandler.getShootingStatus();
		WaitingStatus waitingStatus = sHandler.getWaitingStatus();
		
		switch (waitingStatus) {
		case WAITING:
		case NOT_WAITING:
			if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
				String[] result;
				switch (currentStatus) {
				case READY:
				case FAILED:
					Log.v(TAG, "FAILURE: Remote shooting is not called.");
					returnCb.handleStatus(StatusCode.ANY.toInt(), "StillShootingNotCalled");
					break;
				case FINISHED:
					Log.v(TAG, "Capturing is finished after previous operation.");
					result = sHandler.getUrlArrayResult();
					if (result == null) {
						Log.v(TAG, "FAILURE: No url is available.");
						returnCb.handleStatus(StatusCode.FAILED_SHOOTING.toInt(), "No URL");
					}
					else {
						Log.v(TAG, "SUCCESS: Return previous pictures' url.");
						returnCb.returnCb(result);
					}
					break;
				case PROCESSING:
				case DEVELOPING:
					currentStatus = sHandler.createStillPicture();
					if (WaitingStatus.CANCELED.equals(sHandler.getWaitingStatus())) {
						Log.v(TAG, "CANCELED: DoublePolling");
						returnCb.handleStatus(StatusCode.ALREADY_RUNNING_POLLING.toInt(), "DoublePolling");
					}
					else {
						switch (currentStatus) {
						case FINISHED:
							result = sHandler.getUrlArrayResult();
							if (result == null) {
								Log.v(TAG, "FAILURE: No url is available.");
								returnCb.handleStatus(StatusCode.FAILED_SHOOTING.toInt(), "No URL");
							}
							else {
								Log.v(TAG, "SUCCESS: Finished capturing.");
								returnCb.returnCb(result);
							}
							break;
						case FAILED:
							Log.e(TAG, "FAILURE: Shooting failed: " + sHandler.getErrorStatus().toString());
							returnCb.handleStatus(StatusCode.FAILED_SHOOTING.toInt(), "Shooting Failed");
							break;
						case PROCESSING:
                        case DEVELOPING:
							Log.v(TAG, "PROCESSING: Long capturing is not finished yet.");
							returnCb.handleStatus(StatusCode.STILL_CAPTURING_NOT_FINISHED.toInt(), "Not Finished");
							break;
						default:
							Log.e(TAG, "FAILURE: Unexpected state after shooting.");
							returnCb.handleStatus(StatusCode.ANY.toInt(), "Unexpected state");
							break;
						}
					}
					break;
				default:
					Log.e(TAG, "FAILURE: Unknown Shooting Status.");
					returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown shooting status");
					break;
				}
			}
			else {
				Log.e(TAG, "Failed to await taking picture because of current application state");
				returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
			}
			break;
		default:
			Log.e(TAG, "FAILURE: Unknown Waiting Status.");
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown waiting status");
			break;
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int setExposureCompensation(int exposure, SetExposureCompensationCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			int ret = AvailableManager.setExposureCompensation(exposure);
			if (ret == 0) {
				returnCb.returnCb(0);
			}
			else {
				returnCb.handleStatus(ret, "failed");
			}
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getExposureCompensation(GetExposureCompensationCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
		    Integer current = AvailableManager.getExposureCompensationCurrent();
		    if(null != current) {
			returnCb.returnCb(current.intValue());
		    } else {
		        returnCb.handleStatus(StatusCode.ANY.toInt(), "Current value is not available");
		    }
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getSupportedExposureCompensation(GetSupportedExposureCompensationCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			int[][] supported = AvailableManager.getExopsureCompensationSupported();
			if (supported != null) {
				returnCb.returnCb(supported[0], supported[1], supported[2]);
			}
			else {
				returnCb.handleStatus(StatusCode.ANY.toInt(), "no value is supported");
			}
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getAvailableExposureCompensation(GetAvailableExposureCompensationCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			int[] available = AvailableManager.getExopsureCompensationAvailable();
			if (available != null) {
			    Integer current = AvailableManager.getExposureCompensationCurrent();
			    if(null != current) {
				returnCb.returnCb(current.intValue(), available[0], available[1],
						available[2]);
			    } else {
			        returnCb.handleStatus(StatusCode.ANY.toInt(), "Current value is not available");
			    }
			}
			else {
				returnCb.handleStatus(StatusCode.ANY.toInt(), "no value is available");
			}
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int startLiveview(com.sony.scalar.webapi.service.camera.v1_0.liveview.StartLiveviewCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

            if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
                boolean error = false;
                if (LiveviewLoader.isLoadingPreview()) {
                    Log.v(TAG, "Failed: Liveview already started.");
                    
                    String currentSize = CameraProxyLiveviewSize.get();
                    if(!currentSize.equals(LiveviewContainer.s_DEFAULT_LIVEVIEW_SIZE)) {
                        String message = "Default size(="+LiveviewContainer.s_DEFAULT_LIVEVIEW_SIZE+") differs from the current(="+currentSize+")."; 
                        Log.v(TAG, message);
                        returnCb.handleStatus(StatusCode.ILLEGAL_ARGUMENT.toInt(), message);
                        error = true;
                    }
                } else {
                    CameraProxyLiveviewSize.set(LiveviewContainer.s_DEFAULT_LIVEVIEW_SIZE);
                    boolean check = LiveviewLoader.startObtainingImages();
                    if(!check) {
                        returnCb.handleStatus(StatusCode.ANY.toInt(), "Liveview cannot start.");
                        error = true;
                    } else {
                        LiveviewLoader.setLoadingPreview(true);
                    }
                }

                if(!error) {
                    returnCb.returnCb(SRCtrlConstants.LIVEVIEW_URL);
                }
            }
            else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
            }

        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int stopLiveview(com.sony.scalar.webapi.service.camera.v1_0.liveview.StopLiveviewCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			LiveviewLoader.stopObtainingImages();
			returnCb.returnCb(0);
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int startRecMode(com.sony.scalar.webapi.service.camera.v1_0.recmode.StartRecModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		StateController stateController = StateController.getInstance();
		stateController.setGoBackFlag(false);
		
		if (AppCondition.PREPARATION.equals(StateController.getInstance().getAppCondition())) {
			if (!stateController.isWaitingRecModeChange()) {
				RecModeTransitionHandler.TransStatus status = RecModeTransitionHandler.getInstance()
						.goToShootingState();
				switch (status) {
				case SUCCESS:
					returnCb.returnCb(0);
					break;
				default:
					returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown");
					break;
				}
			}
			else {
				returnCb.returnCb(0);
			}
		}
		else {
			returnCb.returnCb(0);
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int stopRecMode(com.sony.scalar.webapi.service.camera.v1_0.recmode.StopRecModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		StateController stateController = StateController.getInstance();
		AppCondition condition = stateController.getAppCondition();
		if (AppCondition.SHOOTING_INHIBIT.equals(condition) || AppCondition.SHOOTING_LOCAL.equals(condition)
				|| AppCondition.SHOOTING_REMOTE.equals(condition)
				|| AppCondition.SHOOTING_REMOTE_TOUCHAF.equals(condition)
				) {
		    // Set a flag to make S1OffEEStateEx go back to the Network state automatically when it resumes.
			StateController.getInstance().setGoBackFlag(true);
			returnCb.returnCb(0);
		}
		else if (AppCondition.PREPARATION.equals(condition)) {
			returnCb.returnCb(0);
		}
		else {
			if (!stateController.isWaitingRecModeChange()) {
				RecModeTransitionHandler.TransStatus status = RecModeTransitionHandler.getInstance().goToNetworkState();
				switch (status) {
				case SUCCESS:
					returnCb.returnCb(0);
					break;
				default:
					returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown");
					break;
				}
			}
			else {
				returnCb.returnCb(0);
			}
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int setSelfTimer(int timer, SetSelfTimerCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			int ret = CameraProxySelftimer.setSelfTimer(timer);
			if (ret == 0) {
				returnCb.returnCb(0);
			}
			else {
				returnCb.handleStatus(ret, "failed");
			}
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "set Drive mode is not available now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getSelfTimer(GetSelfTimerCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            Integer current = CameraProxySelftimer.getSelftimerCurrent();
            if(null != current) {
            returnCb.returnCb(current.intValue());
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Current value is not available");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedSelfTimer(GetSupportedSelfTimerCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
		    int supported[] = CameraProxySelftimer.getSelftimerSupported();
		    if(null != supported) {
		        returnCb.returnCb(supported);
		    } else {
	            returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
		    }
		}
		else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getAvailableSelfTimer(GetAvailableSelfTimerCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            Integer current = CameraProxySelftimer.getSelftimerCurrent();
            if(null != current) {
            returnCb.returnCb(current.intValue(), CameraProxySelftimer.getSelftimerAvailable());
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Current value is not available");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	private String s_SERVER_NAME = null;
	private String getServerName() {
	    if(null != s_SERVER_NAME)
	    {
	        return s_SERVER_NAME;
	    }
	    StringBuffer buff = new StringBuffer(SRCtrlConstants.SERVER_NAME);
	    buff.append(" SR/");
	    boolean bReady = (null != SRCtrl.VERSION_NAME_STR); 
	    buff.append(bReady ? SRCtrl.VERSION_NAME_STR : "");
        buff.append(" ");
        buff.append(SRCtrlConstants.SERVER_KEYWORD);
	    String sRet = buff.toString();
	    
	    if(bReady) {
	        s_SERVER_NAME = sRet;
	    }
	    
	    return sRet;
	}
	
	@Override
	public int getApplicationInfo(GetApplicationInfoCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String sServerName = getServerName();
        Log.d(TAG, "["+apiCallLog.getMethodName()+"] NAME=" + sServerName + ", VERSION=" + SRCtrlConstants.SERVER_VERSION);
		returnCb.returnCb(sServerName, SRCtrlConstants.SERVER_VERSION);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getAvailableApiList(GetAvailableApiListCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
			String[] availables = AvailabilityDetector.getAvailables(true);
			if (availables != null) {
				returnCb.returnCb(availables);
                Log.e(TAG, "["+apiCallLog.getMethodName()+"] API List: " + Arrays.toString(availables));
			}
			else {
                Log.e(TAG, "["+apiCallLog.getMethodName()+"] API List is null.");
				returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown Error");
			}
		}
		else {
            Log.e(TAG, "["+apiCallLog.getMethodName()+"] The API call is forbidden now.");
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	private static void printGetEventData(
            GetEventAvailableApiListParams              availableApiList,
            GetEventCameraStatusParams                  cameraStatus,
            GetEventZoomInformationParams               zoomInformation,
            GetEventLiveviewStatusParams                liveviewStatus,
            GetEventLiveviewOrientationParams           liveviewOrientation,
            GetEventTakePictureParams[]                 takePicture,
            GetEventContinuousErrorParams[]             continuousError,
            GetEventTriggeredErrorParams                triggeredError,
            GetEventSceneRecognitionParams              sceneRecognition,
            GetEventFormatStatusParams                  formatStatus,
            GetEventStorageInformationParams[]          storageInformation,
            GetEventBeepModeParams                      beepMode,
            GetEventCameraFunctionParams                cameraFunction,
            GetEventMovieQualityParams                  movieQuality,
            GetEventStillSizeParams                     stillSize,
            GetEventCameraFunctionResultParams          cameraFunctionResult,
            GetEventSteadyModeParams                    steadyMode,
            GetEventViewAngleParams                     viewAngle,
            GetEventExposureModeParams                  exposureMode,
            GetEventPostviewImageSizeParams             postviewImageSize,
            GetEventSelfTimerParams                     selfTimer,
            GetEventShootModeParams                     shootMode,
            GetEventAELockParams                        aeLock,
            GetEventBracketShootModeParams              bracketShootMode,
            GetEventCreativeStyleParams                 creativeStyle,
            GetEventExposureCompensationParams          exposureCompensation,
            GetEventFlashModeParams                     flashMode,
            GetEventFNumberParams                       fNumber,
            GetEventFocusModeParams                     focusMode,
            GetEventIsoSpeedRateParams                  isoSpeedRate,
            GetEventPictureEffectParams                 pictureEffect,
            GetEventProgramShiftParams                  programShift,
            GetEventShutterSpeedParams                  shutterSpeed,
            GetEventWhiteBalanceParams                  whiteBalance,
            GetEventTouchAFPositionParams               touchAFPosition
            )
	{
        if(null == availableApiList)
        {
        } else {
            Log.v(TAG, "<GetEventAvailableApiListParams>");
            Log.v(TAG, "  --> type = " + availableApiList.type);
            Log.v(TAG, "  --> name = " + Arrays.toString(availableApiList.names));
        }

        if(null == cameraStatus)
        {
        } else {
            Log.v(TAG, "<GetEventCameraStatusParams>");
            Log.v(TAG, "  --> type = " + cameraStatus.type);
            Log.v(TAG, "  --> cameraStatus = " + cameraStatus.cameraStatus);
        }

        if(null == zoomInformation)
        {
        } else {
            Log.v(TAG, "<GetEventZoomInformationParams>");
            Log.v(TAG, "  --> type = " + zoomInformation.type);
            Log.v(TAG, "  --> zoomIndexCurrentBox =    " + zoomInformation.zoomIndexCurrentBox);
            Log.v(TAG, "  --> zoomNumberBox =          " + zoomInformation.zoomNumberBox);
            Log.v(TAG, "  --> zoomPoistion =           " + zoomInformation.zoomPosition);
            Log.v(TAG, "  --> zoomPositionCurrentBox = " + zoomInformation.zoomPositionCurrentBox);
        }

        if(null == liveviewStatus)
        {
        } else {
            Log.v(TAG, "<GetEventLiveviewStatusParams>");
            Log.v(TAG, "  --> type =           " + liveviewStatus.type);
            Log.v(TAG, "  --> liveviewStatus = " + liveviewStatus.liveviewStatus);
        }

        if(null == liveviewOrientation)
        {
        } else {
            Log.v(TAG, "<GetEventLiveviewOrientationParams>");
            Log.v(TAG, "  --> type =                " + liveviewOrientation.type);
            Log.v(TAG, "  --> liveviewOrientation = " + liveviewOrientation.liveviewOrientation);
        }

        if(null == takePicture)
        {
        } else {
            Log.v(TAG, "<GetEventTakePictureParams[]>");
            for(int i = 0; i < takePicture.length; i++) {
                Log.v(TAG, "  --> ["+i+"]\t type = " + takePicture[i].type);
                Log.v(TAG, "  --> \t takePictureUrl = " + Arrays.toString(takePicture[i].takePictureUrl));
            }
        }

        if(null == continuousError)
        {
        } else {
            Log.v(TAG, "<GetEventContinuousErrorParams[]>");
            for(int i = 0; i < continuousError.length; i++) {
                Log.v(TAG, "  --> ["+i+"]\t type = " + continuousError[i].type);
                Log.v(TAG, "  --> \t continuousError = " + continuousError[i].continuousError);
                Log.v(TAG, "  --> \t isContinued =     " + continuousError[i].isContinued);
            }
        }

        if(null == triggeredError)
        {
        } else {
            Log.v(TAG, "<GetEventTriggeredErrorParams>");
            Log.v(TAG, "  --> type = " + triggeredError.type);
            Log.v(TAG, "  --> triggeredError = " + Arrays.toString(triggeredError.triggeredError));
        }

        if(null == sceneRecognition)
        {
        } else {
            Log.v(TAG, "<GetEventSceneRecognitionParams>");
            Log.v(TAG, "  --> type = " + sceneRecognition.type);
            Log.v(TAG, "  --> motionRecognition = " + sceneRecognition.motionRecognition);
            Log.v(TAG, "  --> sceneRecognition  = " + sceneRecognition.sceneRecognition);
            Log.v(TAG, "  --> steadyRecognition = " + sceneRecognition.steadyRecognition);
        }

        if(null == formatStatus)
        {
        } else {
            Log.v(TAG, "<GetEventFormatStatusParams>");
            Log.v(TAG, "  --> type = " + formatStatus.type);
            Log.v(TAG, "  --> formatResult = " + formatStatus.formatResult);
        }

        if(null == storageInformation)
        {
        } else {
            Log.v(TAG, "<GetEventStorageInformationParams[]>");
            for(int i = 0; i < storageInformation.length; i++) {
                Log.v(TAG, "  --> ["+i+"]\t type = " + storageInformation[i].type);
                Log.v(TAG, "  --> \t continuousError =    " + storageInformation[i].numberOfRecordableImages);
                Log.v(TAG, "  --> \t recordableTime =     " + storageInformation[i].recordableTime);
                Log.v(TAG, "  --> \t storageDescription = " + storageInformation[i].storageDescription);
                Log.v(TAG, "  --> \t storageID =          " + storageInformation[i].storageID);
                Log.v(TAG, "  --> \t recordTarget =       " + storageInformation[i].recordTarget);
            }
        }

        if(null == beepMode)
        {
        } else {
            Log.v(TAG, "<GetEventBeepModeParams>");
            Log.v(TAG, "  --> type = " + beepMode.type);
            Log.v(TAG, "  --> currentBeepMode =    " + beepMode.currentBeepMode);
            Log.v(TAG, "  --> beepModeCandidates = " + Arrays.toString(beepMode.beepModeCandidates));
        }

        if(null == cameraFunction)
        {
        } else {
            Log.v(TAG, "<GetEventCameraFunctionParams>");
            Log.v(TAG, "  --> type = " + cameraFunction.type);
            Log.v(TAG, "  --> currentCameraFunction =    " + cameraFunction.currentCameraFunction);
            Log.v(TAG, "  --> cameraFunctionCandidates = " + Arrays.toString(cameraFunction.cameraFunctionCandidates));
        }

        if(null == movieQuality)
        {
        } else {
            Log.v(TAG, "<GetEventMovieQualityParams>");
            Log.v(TAG, "  --> type = " + movieQuality.type);
            Log.v(TAG, "  --> currentMovieQuality =    " + movieQuality.currentMovieQuality);
            Log.v(TAG, "  --> movieQualityCandidates = " + Arrays.toString(movieQuality.movieQualityCandidates));
        }

        if(null == stillSize)
        {
        } else {
            Log.v(TAG, "<GetEventStillSizeParams>");
            Log.v(TAG, "  --> type = " + stillSize.type);
            Log.v(TAG, "  --> currentAspect = " + stillSize.currentAspect);
            Log.v(TAG, "  --> currentSize = " + stillSize.currentSize);
            Log.v(TAG, "  --> checkAvailability = " + stillSize.checkAvailability);
            
        }

        if(null == cameraFunctionResult)
        {
        } else {
            Log.v(TAG, "<GetEventCameraFunctionResultParams>");
            Log.v(TAG, "  --> type = " + cameraFunctionResult.type);
            Log.v(TAG, "  --> cameraFunctionResult" + cameraFunctionResult.cameraFunctionResult);
        }

        if(null == steadyMode)
        {
        } else {
            Log.v(TAG, "<GetEventSteadyModeParams>");
            Log.v(TAG, "  --> type = " + steadyMode.type);
            Log.v(TAG, "  --> currentSteadyMode = " + steadyMode.currentSteadyMode);
        }

        if(null == viewAngle)
        {
        } else {
            Log.v(TAG, "<GetEventViewAngleParams>");
            Log.v(TAG, "  --> type = " + viewAngle.type);
            Log.v(TAG, "  --> currentViewAngle =           " + viewAngle.currentViewAngle);
            Log.v(TAG, "  --> currentViewAngleCandidates = " + Arrays.toString(viewAngle.viewAngleCandidates));
        }

        if(null == exposureMode)
        {
        } else {
            Log.v(TAG, "<GetEventExposureModeParams>");
            Log.v(TAG, "  --> type = " + exposureMode.type);
            Log.v(TAG, "  --> currentExposureMode =    " + exposureMode.currentExposureMode);
            Log.v(TAG, "  --> exposureModeCandidates = " + Arrays.toString(exposureMode.exposureModeCandidates));
        }

        if(null == postviewImageSize)
        {
        } else {
            Log.v(TAG, "<GetEventPostviewImageSizeParams>");
            Log.v(TAG, "  --> type = " + postviewImageSize.type);
            Log.v(TAG, "  --> currentPostviewImageSize =    " + postviewImageSize.currentPostviewImageSize);
            Log.v(TAG, "  --> postviewImageSizeCandidates = " + Arrays.toString(postviewImageSize.postviewImageSizeCandidates));
        }

        if(null == selfTimer)
        {
        } else {
            Log.v(TAG, "<GetEventSelfTimerParams>");
            Log.v(TAG, "  --> type = " + selfTimer.type);
            Log.v(TAG, "  --> currentSelfTimer =    " + selfTimer.currentSelfTimer);
            Log.v(TAG, "  --> selfTimerCandidates = " + Arrays.toString(selfTimer.selfTimerCandidates));
        }

        if(null == shootMode)
        {
        } else {
            Log.v(TAG, "<GetEventShootModeParams>");
            Log.v(TAG, "  --> type = " + shootMode.type);
            Log.v(TAG, "  --> currentShootMode =    " + shootMode.currentShootMode);
            Log.v(TAG, "  --> shootModeCandidates = " + Arrays.toString(shootMode.shootModeCandidates));
        }

        if(null == aeLock)
        {
        } else {
            Log.v(TAG, "<GetEventAELockParams>");
            Log.v(TAG, "  --> type = " + aeLock.type);
            Log.v(TAG, "  --> currentAELock =    " + aeLock.currentAELock);
            Log.v(TAG, "  --> aeLockCandidates = " + Arrays.toString(aeLock.aeLockCandidates));
        }

        if(null == bracketShootMode)
        {
        } else {
            Log.v(TAG, "<GetEventBracketShootModeParams>");
            Log.v(TAG, "  --> type = " + bracketShootMode.type);
            Log.v(TAG, "  --> currentBracketShootMode =       " + bracketShootMode.currentBracketShootMode);
            Log.v(TAG, "  --> currentBracketShootModeOption = " + bracketShootMode.currentBracketShootModeOption);
            Log.v(TAG, "  --> checkAvailability =             " + bracketShootMode.checkAvailability);
        }

        if(null == creativeStyle)
        {
        } else {
            Log.v(TAG, "<GetEventCreativeStyleParams>");
            Log.v(TAG, "  --> type = " + creativeStyle.type);
            Log.v(TAG, "  --> currentCreativeStyle = " + creativeStyle.currentCreativeStyle);
            Log.v(TAG, "  --> currentCreativeStyleContrast = " + creativeStyle.currentCreativeStyleContrast);
            Log.v(TAG, "  --> currentCreativeStyleSaturation = " + creativeStyle.currentCreativeStyleSaturation);
            Log.v(TAG, "  --> currentCreativeStyleSharpness = " + creativeStyle.currentCreativeStyleSharpness);
            Log.v(TAG, "  --> checkAvailability = " + creativeStyle.checkAvailability);
            
        }

        if(null == exposureCompensation)
        {
        } else {
            Log.v(TAG, "<GetEventExposureCompensationParams>");
            Log.v(TAG, "  --> type = " + exposureCompensation.type);
            Log.v(TAG, "  --> currentExposureCompensation =     " + exposureCompensation.currentExposureCompensation);
            Log.v(TAG, "  --> maxExposureCompensation =         " + exposureCompensation.maxExposureCompensation);
            Log.v(TAG, "  --> minExposureCompensation =         " + exposureCompensation.minExposureCompensation);
            Log.v(TAG, "  --> stepIndexOfExposureCompensation = " + exposureCompensation.stepIndexOfExposureCompensation);
            
        }

        if(null == flashMode)
        {
        } else {
            Log.v(TAG, "<GetEventFlashModeParams>");
            Log.v(TAG, "  --> type = " + flashMode.type);
            Log.v(TAG, "  --> currentFlashMode = " + flashMode.currentFlashMode);
            Log.v(TAG, "  --> flashModeCandidates = " + Arrays.toString(flashMode.flashModeCandidates));
        }

        if(null == fNumber)
        {
        } else {
            Log.v(TAG, "<GetEventFNumberParams>");
            Log.v(TAG, "  --> type = " + fNumber.type);
            Log.v(TAG, "  --> currentFNumber = " + fNumber.currentFNumber);
            Log.v(TAG, "  --> fNumberCandidates = " + Arrays.toString(fNumber.fNumberCandidates));
        }

        if(null == focusMode)
        {
        } else {
            Log.v(TAG, "<GetEventFocusModeParams>");
            Log.v(TAG, "  --> type = " + focusMode.type);
            Log.v(TAG, "  --> currentFocusMode = " + focusMode.currentFocusMode);
            Log.v(TAG, "  --> focusModeCandidates = " + Arrays.toString(focusMode.focusModeCandidates));
        }

        if(null == isoSpeedRate)
        {
        } else {
            Log.v(TAG, "<GetEventIsoSpeedRateParams>");
            Log.v(TAG, "  --> type = " + isoSpeedRate.type);
            Log.v(TAG, "  --> currentIsoSpeedRate =    " + isoSpeedRate.currentIsoSpeedRate);
            Log.v(TAG, "  --> isoSpeedRateCandidates = " + Arrays.toString(isoSpeedRate.isoSpeedRateCandidates));
        }

        if(null == pictureEffect)
        {
        } else {
            Log.v(TAG, "<GetEventPictureEffectParams>");
            Log.v(TAG, "  --> type = " + pictureEffect.type);
            Log.v(TAG, "  --> currentPictureEffect =       " + pictureEffect.currentPictureEffect);
            Log.v(TAG, "  --> currentPictureEffectOption = " + pictureEffect.currentPictureEffectOption);
            Log.v(TAG, "  --> checkAvailability =          " + pictureEffect.checkAvailability);
        }

        if(null == programShift)
        {
        } else {
            Log.v(TAG, "<GetEventProgramShiftParams>");
            Log.v(TAG, "  --> type = " + programShift.type);
            Log.v(TAG, "  --> isShifted = " + programShift.isShifted);
            
        }

        if(null == shutterSpeed)
        {
        } else {
            Log.v(TAG, "<GetEventShutterSpeedParams>");
            Log.v(TAG, "  --> type = " + shutterSpeed.type);
            Log.v(TAG, "  --> currentShutterSpeed =    " + shutterSpeed.currentShutterSpeed);
            Log.v(TAG, "  --> shutterSpeedCandidates = " + Arrays.toString(shutterSpeed.shutterSpeedCandidates));
        }

        if(null == whiteBalance)
        {
        } else {
            Log.v(TAG, "<GetEventWhiteBalanceParams>");
            Log.v(TAG, "  --> type = " + whiteBalance.type);
            Log.v(TAG, "  --> currentColorTemperature = " + whiteBalance.currentColorTemperature);
            Log.v(TAG, "  --> currentWhiteBalanceMode = " + whiteBalance.currentWhiteBalanceMode);
            Log.v(TAG, "  --> checkAvailability =       " + whiteBalance.checkAvailability);
        }

        if(null == touchAFPosition)
        {
        } else {
            Log.v(TAG, "<GetEventTouchAFPositionParams>");
            Log.v(TAG, "  --> type = " + touchAFPosition.type);
            Log.v(TAG, "  --> currentSet =              " + touchAFPosition.currentSet);
            Log.v(TAG, "  --> currentTouchCoordinates = " + Arrays.toString(touchAFPosition.currentTouchCoordinates));
        }
	}

	private static final GetEventContinuousErrorParams[] s_DUMMY_PARAM_ARRAY_CONTINUOUS_ERROR= new GetEventContinuousErrorParams[0];
	private static final GetEventStorageInformationParams[] s_DUMMY_PARAM_ARRAY_STORAGEINFOMATION = new GetEventStorageInformationParams[0];
	@Override
	public int getEvent(boolean isPolling, GetEventCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init(false);
        //////

	    String methodName = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+methodName+"(isPolling="+isPolling+")");
        ServerEventHandler.Status status = ServerEventHandler.Status.SUCCESS;
        
        if (isPolling) {
            ServerEventHandler eventHandler = ServerEventHandler.getInstance();
            if (!eventHandler.isStatusChanged()) {
                status = eventHandler.startWaiting();
            }
            else {
                eventHandler.setStatusChanged(false);
            }
        }
        
        if (ServerEventHandler.Status.SUCCESS.equals(status)) {
            //String serverStat = ParamsGenerator.getServerStatus();
            //boolean liveviewStat = ParamsGenerator.getLiveviewStatus();
            //AvailableParams params = ParamsGenerator.getAvailableData();
            
            GetEventAvailableApiListParams availableApiList = ParamsGenerator.getAvailableApiList(isPolling);
            GetEventCameraStatusParams cameraStatus = ParamsGenerator.getServerStatus(isPolling);
            GetEventLiveviewStatusParams liveviewStatus = ParamsGenerator.getLiveviewStatus(isPolling);
            GetEventTakePictureParams[] takePicture = ParamsGenerator.getTakePicture(isPolling);
            GetEventExposureModeParams exposureMode = ParamsGenerator.getExposureModeParams(isPolling);
            GetEventPostviewImageSizeParams postviewImageSize = ParamsGenerator.getPostviewImageSize(isPolling);
            GetEventSelfTimerParams selfTimer = ParamsGenerator.getSelfTimer(isPolling);
            GetEventShootModeParams shootMode = ParamsGenerator.getShootModeParams(isPolling);
            GetEventExposureCompensationParams exposureCompensation = ParamsGenerator.getExposureCompensation(isPolling);
            GetEventFNumberParams fNumber = ParamsGenerator.getFNumber(isPolling);
            GetEventIsoSpeedRateParams isoSpeedRate = ParamsGenerator.getIsoSpeedRateParams(isPolling);
            GetEventProgramShiftParams programShift = ParamsGenerator.getProgramShift(isPolling);
            GetEventShutterSpeedParams shutterSpeed = ParamsGenerator.getShutterSpeed(isPolling);
            GetEventWhiteBalanceParams whiteBalance = ParamsGenerator.getWhiteBalanceParams(isPolling);
            GetEventTouchAFPositionParams touchAFPosition = ParamsGenerator.getTouchAFPosition(isPolling);
            GetEventFlashModeParams flashMode = ParamsGenerator.getFlashModeParams(isPolling);
            GetEventZoomInformationParams zoomInformation = ParamsGenerator.getZoomInformation(isPolling);
            //
            GetEventContinuousErrorParams[] continuousError = s_DUMMY_PARAM_ARRAY_CONTINUOUS_ERROR;
            GetEventStorageInformationParams[] storageInformation = s_DUMMY_PARAM_ARRAY_STORAGEINFOMATION;
            //
            GetEventLiveviewOrientationParams liveviewOrientation = null;
            GetEventTriggeredErrorParams triggeredError = null;
            GetEventSceneRecognitionParams sceneRecognition = null;
            GetEventFormatStatusParams formatStatus = null;
            GetEventBeepModeParams beepMode = null;
            GetEventCameraFunctionParams cameraFunction = null;
            GetEventMovieQualityParams movieQuality = null;
            GetEventStillSizeParams stillSize = null;
            GetEventCameraFunctionResultParams cameraFunctionResult = null;
            GetEventSteadyModeParams steadyMode = null;
            GetEventViewAngleParams viewAngle = null;
            GetEventAELockParams aeLock = null;
            GetEventBracketShootModeParams bracketShootMode = null;
            GetEventCreativeStyleParams creativeStyle = null;
            GetEventFocusModeParams focusMode = null;
            GetEventPictureEffectParams pictureEffect = null;
            //
                                
            printGetEventData(
                    availableApiList,
                    cameraStatus,
                    zoomInformation,
                    liveviewStatus,
                    liveviewOrientation,
                    takePicture,
                    continuousError,
                    triggeredError,
                    sceneRecognition,
                    formatStatus,
                    storageInformation,
                    beepMode,
                    cameraFunction,
                    movieQuality,
                    stillSize,
                    cameraFunctionResult,
                    steadyMode,
                    viewAngle,
                    exposureMode,
                    postviewImageSize,
                    selfTimer,
                    shootMode,
                    aeLock,
                    bracketShootMode,
                    creativeStyle,
                    exposureCompensation,
                    flashMode,
                    fNumber,
                    focusMode,
                    isoSpeedRate,
                    pictureEffect,
                    programShift,
                    shutterSpeed,
                    whiteBalance,
                    touchAFPosition
                    );

            returnCb.returnCb(
                    availableApiList,
                    cameraStatus,
                    zoomInformation,
                    liveviewStatus,
                    liveviewOrientation,
                    takePicture,
                    continuousError,
                    triggeredError,
                    sceneRecognition,
                    formatStatus,
                    storageInformation,
                    beepMode,
                    cameraFunction,
                    movieQuality,
                    stillSize,
                    cameraFunctionResult,
                    steadyMode,
                    viewAngle,
                    exposureMode,
                    postviewImageSize,
                    selfTimer,
                    shootMode,
                    aeLock,
                    bracketShootMode,
                    creativeStyle,
                    exposureCompensation,
                    flashMode,
                    fNumber,
                    focusMode,
                    isoSpeedRate,
                    pictureEffect,
                    programShift,
                    shutterSpeed,
                    whiteBalance,
                    touchAFPosition
                    );
        }
        else if (ServerEventHandler.Status.CANCELED.equals(status)) {
            returnCb.handleStatus(StatusCode.ALREADY_RUNNING_POLLING.toInt(), "DoublePolling");
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown");
        }
        
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}

/*	
	public int receiveEvent(boolean isPolling, ReceiveEventCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		ServerEventHandler.Status status = ServerEventHandler.Status.SUCCESS;
		
		if (isPolling) {
			ServerEventHandler eventHandler = ServerEventHandler.getInstance();
			if (!eventHandler.isStatusChanged()) {
				status = eventHandler.startWaiting();
			}
			else {
				eventHandler.setStatusChanged(false);
			}
		}
		
		if (ServerEventHandler.Status.SUCCESS.equals(status)) {
			String serverStat = ParamsGenerator.getServerStatus();
			boolean liveviewStat = ParamsGenerator.getLiveviewStatus();
			AvailableParams params = ParamsGenerator.getAvailableData();
			Log.v(TAG, serverStat + ", " + liveviewStat);

			returnCb.returnCb(serverStat, liveviewStat, -1, -1, -1, -1, params.getNamesArray(), params.getTypesArray(),
					params.getRangeFlagsArray(), params.getCurrentsArray(), params.getAvailablesArray());
		}
		else if (ServerEventHandler.Status.CANCELED.equals(status)) {
			returnCb.handleStatus(StatusCode.ALREADY_RUNNING_POLLING.toInt(), "DoublePolling");
		}
		else {
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Unknown");
		}
		
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
*/
	
	@Override
	public int setTouchAFPosition(double x, double y, SetTouchAFPositionCallback returnCb) {
        int ret = 0; // 初期値はエラー値がいいが、その場合はなに？

        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setTouchAFPosition(x="+x+", y="+y+", callback)");

        StateController stateController = StateController.getInstance();
        AppCondition previousAppCondition = stateController.getAppCondition();
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
		    stateController.setAppCondition(AppCondition.SHOOTING_REMOTE_TOUCHAF);

			TouchAFPositionParams position = CameraProxyTouchAFPosition.set(x, y);
			if(null != position) {
	            Log.e("CHECK", "position: " + position.AFResult +","+ position.AFType
	                //+ ","+ position.touchCoordinates[0]+","+ position.touchCoordinates[1]+","+ position.AFBoxLeftTop[0]+","+ position.AFBoxLeftTop[1] +","+ position.AFBoxRightBottom[0]+","+ position.AFBoxRightBottom[1]
	                );
			    returnCb.returnCb(0, position);
			} else {
			    returnCb.handleStatus(StatusCode.ANY.toInt(), "Touch AF operation failed.");
                stateController.setAppCondition(previousAppCondition); // even if the previous condition is SHOOTING_REMOTE_TOUCHAF
			}
        } else {
            Log.v(TAG, "Failed to start touch AF because of current application state");
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
            stateController.setAppCondition(previousAppCondition); // even if the previous condition is SHOOTING_REMOTE_TOUCHAF
        }
        Log.e(TAG, "<=== setTouchAFPosition(x="+x+", y="+y+", callback)");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return ret;
	}
	
	@Override
	public int getTouchAFPosition(GetTouchAFPositionCallback returnCb) {
        int ret = 0; // 初期値はエラー値がいいが、その場合はなに？
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getTouchAFPosition()");

        StateController stateController = StateController.getInstance();
        AppCondition previousAppCondition = stateController.getAppCondition();
        TouchAFCurrentPositionParams pos = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            pos = CameraProxyTouchAFPosition.get();
            if(null != pos) {
                returnCb.returnCb(pos);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot get the touch AF position.");
            }
        } else {
            Log.v(TAG, "Failed to start touch AF because of current application state");
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
            stateController.setAppCondition(previousAppCondition); // even if the previous condition is SHOOTING_REMOTE_TOUCHAF
        }
        Log.e(TAG, "<=== getTouchAFPosition()");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return ret;
	}
	
	@Override
	public int cancelTouchAFPosition(CancelTouchAFPositionCallback returnCb) {
        int ret = 0; // 初期値はエラー値がいいが、その場合はなに？

        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

		Log.e(TAG, "---> cancelTouchAFPosition()");

		StateController stateController = StateController.getInstance();
		if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
	        CameraProxyTouchAFPosition.cancel();
			returnCb.returnCb();
		} else {
			Log.v(TAG, "Failed to cancel touch AF because of current application state");
			returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
		}
		Log.e(TAG, "<=== cancelTouchAFPosition()");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return ret;
	}
	
	@Override
	public int setFNumber(String fnumber, SetFNumberCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setFNumber(fnumber="+fnumber+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean check = CameraProxyFNumber.set(fnumber);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== setFNumber() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getFNumber(GetFNumberCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getFNumber()");
        boolean DEBUGSUCCEEDED = false;
        
        String fNumber=null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            fNumber = CameraProxyFNumber.get();
            if(null != fNumber) {
                returnCb.returnCb(fNumber);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getFNumber(fNumber="+ ((DEBUGSUCCEEDED && (null != fNumber)) ? fNumber : "n/a") + ")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailableFNumber(GetAvailableFNumberCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getAvailableFNumber()");
        boolean DEBUG_SUCCEEDED = false;
        
        String fNumber = null;
        String[] availableFNumber = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean error = true;
            fNumber = CameraProxyFNumber.get();
            if(null != fNumber) {
                availableFNumber = CameraProxyFNumber.getAvailable();
                if(null != availableFNumber) {
                    error = false;
                    returnCb.returnCb(fNumber, availableFNumber);
                }
            }
            if(error) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getAvailableFNumber(fNumber="
			  +((DEBUG_SUCCEEDED && (null != fNumber)) ? fNumber : "n/a") + ", available="
				+((DEBUG_SUCCEEDED && (null != availableFNumber)) ? availableFNumber.toString() : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedFNumber(GetSupportedFNumberCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getSupportedFNumber()");
        boolean DEBUG_SUCCEEDED = false;
        
        String[] supportedFNumber = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            supportedFNumber = CameraProxyFNumber.getSupported();
            if(null != supportedFNumber) {
                returnCb.returnCb(supportedFNumber);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getSupportedFNumber(supported="
			  +(DEBUG_SUCCEEDED && (null != supportedFNumber) ? supportedFNumber.toString() : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int setShutterSpeed(String shutterSpeed, SetShutterSpeedCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setShutterSpeed(shutterSpeed="+shutterSpeed+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean check = CameraProxyShutterSpeed.set(shutterSpeed);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== setShutterSpeed() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getShutterSpeed(GetShutterSpeedCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getShutterSpeed()");
        boolean DEBUGSUCCEEDED = false;
        
        String shutterSpeed=null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            shutterSpeed = CameraProxyShutterSpeed.get();
            if(null != shutterSpeed) {
                returnCb.returnCb(shutterSpeed);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getShutterSpeed(shutterSpeed="
			  + (DEBUGSUCCEEDED && (null != shutterSpeed) ? shutterSpeed : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailableShutterSpeed(GetAvailableShutterSpeedCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getAvailableShutterSpeed()");
        boolean DEBUG_SUCCEEDED = false;
        
        String shutterSpeed = null;
        String[] availableShutterSpeed = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean error = true;
            shutterSpeed = CameraProxyShutterSpeed.get();
            if(null != shutterSpeed) {
                availableShutterSpeed = CameraProxyShutterSpeed.getAvailable();
                if(null != availableShutterSpeed) {
                    error = false;
                    returnCb.returnCb(shutterSpeed, availableShutterSpeed);
                }
            }
            if(error) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getAvailableShutterSpeed(shutterSpeed="
			  +((DEBUG_SUCCEEDED && (null != shutterSpeed)) ? shutterSpeed : "n/a") + ", available="
			  +((DEBUG_SUCCEEDED && (null != availableShutterSpeed)) ? availableShutterSpeed.toString() : "n/a") + ")" );
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedShutterSpeed(GetSupportedShutterSpeedCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getSupportedShutterSpeed()");
        boolean DEBUG_SUCCEEDED = false;
        
        String[] supportedShutterSpeed = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            supportedShutterSpeed = CameraProxyShutterSpeed.getSupported();
            if(null != supportedShutterSpeed) {
                returnCb.returnCb(supportedShutterSpeed);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getSupportedShutterSpeed(supported="+((DEBUG_SUCCEEDED && (null != supportedShutterSpeed)) ? supportedShutterSpeed.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int setIsoSpeedRate(String iso, SetIsoSpeedRateCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.e(TAG, "---> "+thisMethod+"(iso="+iso+")");

        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            boolean check = CameraProxyIsoNumber.set(iso);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getIsoSpeedRate(GetIsoSpeedRateCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        String isoNumber = null;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            isoNumber = CameraProxyIsoNumber.get();
            if(null != isoNumber) {
                returnCb.returnCb(isoNumber);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(iso="+ ((null != isoNumber) ? isoNumber :"n/a") +") : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailableIsoSpeedRate(GetAvailableIsoSpeedRateCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String isoNumber = null;
        String[] availableIsoNumber = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            boolean error = true;
            isoNumber = CameraProxyIsoNumber.get();
            if(null != isoNumber) {
                availableIsoNumber = CameraProxyIsoNumber.getAvailable();
                if(null != availableIsoNumber) {
                    error = false;
                    returnCb.returnCb(isoNumber, availableIsoNumber);
                }
            }
            if(error) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(iso="+((DEBUG_SUCCEEDED && (null != isoNumber)) ? isoNumber:"n/a") + ", available="
											   +((DEBUG_SUCCEEDED && (null != availableIsoNumber)) ? availableIsoNumber.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedIsoSpeedRate(GetSupportedIsoSpeedRateCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");
        boolean DEBUG_SUCCEEDED = false;
        
        String[] supportedIsoNumber = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supportedIsoNumber = CameraProxyIsoNumber.getSupported();
            if(null != supportedIsoNumber) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            returnCb.returnCb(supportedIsoNumber);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+((DEBUG_SUCCEEDED && (null != supportedIsoNumber)) ? supportedIsoNumber.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int setExposureMode(String mode, SetExposureModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.e(TAG, "---> "+thisMethod+"(mode="+ ((null != mode) ? mode : "n/a")  +")");

        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            boolean check = CameraProxyExposureMode.set(mode);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getExposureMode(GetExposureModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        String mode = null;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            mode = CameraProxyExposureMode.get();
            if(null != mode) {
                returnCb.returnCb(mode);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot get camera settings.");
            }
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(mode="+mode+") : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailableExposureMode(GetAvailableExposureModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String mode = null;
        String[] available = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            boolean error = true;
            mode = CameraProxyExposureMode.get();
            if(null != mode) {
                available = CameraProxyExposureMode.getAvailable();
                if(null != available) {
                    error = false;
                    returnCb.returnCb(mode, available);
                }
            }
            if(error) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot get camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(mode="+((DEBUG_SUCCEEDED && (null != mode)) ? mode:"n/a") + ", available="
			  +((DEBUG_SUCCEEDED && (null != available)) ? available.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedExposureMode(GetSupportedExposureModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");
        boolean DEBUG_SUCCEEDED = false;
        
        String[] supported = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supported = CameraProxyExposureMode.getSupported();
            if(null != supported) {
                returnCb.returnCb(supported);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+((DEBUG_SUCCEEDED && (null != supported)) ? supported.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int startLiveviewWithSize(String size, StartLiveviewCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
            try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.e(TAG, "---> "+thisMethod+"(size="+size+")");
        boolean DEBUG_SUCCEEDED = false;
            
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            boolean error = false;
            if (LiveviewLoader.isLoadingPreview()) {
                Log.d(TAG, "Failed: Liveview already started.");
                
                String currentSize = CameraProxyLiveviewSize.get();
                if(!currentSize.equals(size)) {
                    String message = "New size(="+size+") differs from the current(="+currentSize+")."; 
                    Log.d(TAG, message);
                    returnCb.handleStatus(StatusCode.ILLEGAL_ARGUMENT.toInt(), message);
                    error = true;
                }
            } else {
                CameraProxyLiveviewSize.set(size);
                boolean check = LiveviewLoader.startObtainingImages();
                if(!check) {
                    returnCb.handleStatus(StatusCode.ANY.toInt(), "Liveview cannot start.");
                    error = true;
                } else {
                    LiveviewLoader.setLoadingPreview(true);
                }
            }

            if(!error) {
                returnCb.returnCb(SRCtrlConstants.LIVEVIEW_URL);
                DEBUG_SUCCEEDED = true;
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }
        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUG_SUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getLiveviewSize(GetLiveviewSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        String size = null;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            size = CameraProxyLiveviewSize.get();
            returnCb.returnCb(size);
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED +", size="+size);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailableLiveviewSize(GetAvailableLiveviewSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String size = null;
        String[] available = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            size = CameraProxyLiveviewSize.get();
            available = CameraProxyLiveviewSize.getAvailable();
            returnCb.returnCb(size, available);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(size="+((DEBUG_SUCCEEDED && (null != size)) ? size:"n/a")
			  + ", available="+((DEBUG_SUCCEEDED && (null != available)) ? Arrays.toString(available):"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedLiveviewSize(GetSupportedLiveviewSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String[] supported = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supported = CameraProxyLiveviewSize.getSupported();
            returnCb.returnCb(supported);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+((DEBUG_SUCCEEDED && (null != supported)) ? supported.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int setPostviewImageSize(String size, SetPostviewImageSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            boolean check = CameraProxyPostviewImageSize.set(size);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                    returnCb.handleStatus(StatusCode.ANY.toInt(), "Failed: couldn't set postview size.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(size="+size+") : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getPostviewImageSize(GetPostviewImageSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        String size = null;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            size = CameraProxyPostviewImageSize.get();
            returnCb.returnCb(size);
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED +", size="+((null != size) ? size : "n/a"));
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getAvailablePostviewImageSize(GetAvailablePostviewImageSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String size = null;
        String[] available = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            size = CameraProxyPostviewImageSize.get();
            available = CameraProxyPostviewImageSize.getAvailable();
            returnCb.returnCb(size, available);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(size="+((DEBUG_SUCCEEDED && (null != size)) ? size : "n/a") + ", available="
			  +((DEBUG_SUCCEEDED && (null != available)) ? available.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int getSupportedPostviewImageSize(GetSupportedPostviewImageSizeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String[] supported = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supported = CameraProxyPostviewImageSize.getSupported();
            returnCb.returnCb(supported);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+(DEBUG_SUCCEEDED?supported.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
	
	@Override
	public int setProgramShift(int step, SetProgramShiftCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setProgramShift(step="+step+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean[] result = CameraProxyProgramShift.set(step);
            if(null != result && true == result[0]) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        Log.e(TAG, "<=== setProgramShift() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
	@Override
	public int getSupportedProgramShift(GetSupportedProgramShiftCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getSupportedProgramShift()");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            int[] stepRange = CameraProxyProgramShift.getSupported();
            DEBUGSUCCEEDED = true;
            returnCb.returnCb(stepRange);
        }
        Log.e(TAG, "<=== getSupportedProgramShift() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
		return 0;
	}
	
    @Override
    public int setWhiteBalance(String whiteBalanceMode, boolean colorTemperatureEnabled, int colorTemperature,
            SetWhiteBalanceCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            WhiteBalanceParams param = new WhiteBalanceParams();
            param.whiteBalanceMode = whiteBalanceMode;
            param.colorTemperature = colorTemperature;
            
            boolean check = CameraProxyWhiteBalance.set(param, colorTemperatureEnabled);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                    returnCb.handleStatus(StatusCode.ANY.toInt(), "Failed: couldn't set white balance.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(mode="+((null != whiteBalanceMode) ? whiteBalanceMode : "n/a") +") : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }
    
    @Override
    public int getWhiteBalance(GetWhiteBalanceCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUGSUCCEEDED = false;
        WhiteBalanceParams param = null;
        if(AvailabilityDetector.isAvailable(thisMethod)) {
            param = CameraProxyWhiteBalance.get();
            if(null != param) {
                returnCb.returnCb(param);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED +", param="+param);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }
    
    @Override
    public int getAvailableWhiteBalance(GetAvailableWhiteBalanceCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        WhiteBalanceParams param = null;
        WhiteBalanceParamCandidate[] available = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            boolean error = true;
            param = CameraProxyWhiteBalance.get();
            if(null != param) {
                available = CameraProxyWhiteBalance.getAvailable();
                if(null != available) {
                    error = false;
                    returnCb.returnCb(param, available);
                }
            }
            if(error) {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }
        Log.v(TAG, "<=== "+thisMethod+"(supported="
			  +((DEBUG_SUCCEEDED && (null != param)) ? param.whiteBalanceMode : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }
    
    @Override
    public int getSupportedWhiteBalance(GetSupportedWhiteBalanceCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        WhiteBalanceParamCandidate[] supported = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supported = CameraProxyWhiteBalance.getSupported();
            if(null != supported) {
                returnCb.returnCb(supported);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+(DEBUG_SUCCEEDED?supported.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }

    @Override
    public int setShootMode(String mode, SetShootModeCallback returnCb)
    {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setShootMode(shootMode="+mode+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean check = CameraProxyShootMode.set(mode);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== setShootMode() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }

    @Override
    public int getShootMode(GetShootModeCallback returnCb)
    {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getShootMode()");
        boolean DEBUGSUCCEEDED = false;
        
        String shootMode = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            shootMode = CameraProxyShootMode.get();
            returnCb.returnCb(shootMode);
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getShootMode(ShootMode="+((DEBUGSUCCEEDED && (null != shootMode)) ? shootMode:"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }

    @Override
    public int getAvailableShootMode(GetAvailableShootModeCallback returnCb)
    {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getAvailableShootMode()");
        boolean DEBUG_SUCCEEDED = false;
        
        String shootMode = null;
        String[] availableShootModes = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            shootMode = CameraProxyShootMode.get();
            availableShootModes = CameraProxyShootMode.getAvailable();
            returnCb.returnCb(shootMode, availableShootModes);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getAvailableShootMode(shootMode="
			  +((DEBUG_SUCCEEDED && (null != shootMode )) ? shootMode:"n/a") + ", available="
			  +((DEBUG_SUCCEEDED && (null != availableShootModes)) ? availableShootModes.toString() : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }

    @Override
    public int getSupportedShootMode(GetSupportedShootModeCallback returnCb)
    {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getSupportedShootMode()");
        boolean DEBUG_SUCCEEDED = false;
        
        String[] supportedShootModes = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            supportedShootModes = CameraProxyShootMode.getSupported();
            returnCb.returnCb(supportedShootModes);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getSupportedShootMode(supported="
			  +(DEBUG_SUCCEEDED?supportedShootModes.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
    }

	@Override
	public int setFlashMode(String flash, SetFlashModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> setFlashMode(flashMode="+flash+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean check = CameraProxyFlashMode.set(flash);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== setFlashMode() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}

	@Override
	public int getFlashMode(GetFlashModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getFlashMode()");
        boolean DEBUGSUCCEEDED = false;
        
        String flashMode = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
        	flashMode = CameraProxyFlashMode.get();
            returnCb.returnCb(flashMode);
            DEBUGSUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getFlashMode(FlashMode="+((DEBUGSUCCEEDED && (null != flashMode)) ? flashMode:"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}

	@Override
	public int getAvailableFlashMode(GetAvailableFlashModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> getAvailableFlashMode()");
        boolean DEBUG_SUCCEEDED = false;
        
        String flashMode = null;
        String[] availableflashModes = null;
        if (AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
        	flashMode = CameraProxyFlashMode.get();
        	availableflashModes = CameraProxyFlashMode.getAvailable();
            returnCb.returnCb(flashMode, availableflashModes);
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== getAvailableFlashMode(flashMode="
			  +((DEBUG_SUCCEEDED && (null != flashMode )) ? flashMode:"n/a") + ", available="
			  +((DEBUG_SUCCEEDED && (null != availableflashModes)) ? availableflashModes.toString() : "n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}

	@Override
	public int getSupportedFlashMode(GetSupportedFlashModeCallback returnCb) {
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        String thisMethod = apiCallLog.getMethodName();
        Log.v(TAG, "---> "+thisMethod+"()");

        boolean DEBUG_SUCCEEDED = false;
        String[] supported = null;
        if (AvailabilityDetector.isAvailable(thisMethod)) {
            supported = CameraProxyFlashMode.getSupported();
            if(null != supported) {
                returnCb.returnCb(supported);
            } else {
                returnCb.handleStatus(StatusCode.ANY.toInt(), "Cannot obtain camera settings.");
            }
            DEBUG_SUCCEEDED = true;
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.v(TAG, "<=== "+thisMethod+"(supported="+(DEBUG_SUCCEEDED?supported.toString():"n/a")+")");
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}

	@Override
	public int actZoom(String direction, String movement,
			ActZoomCallback returnCb) 
	{
        //////
        ApiCallLog apiCallLog = null;
        try {
            apiCallLog = new ApiCallLog();
            apiCallLog.init();
        //////

        Log.e(TAG, "---> actZoom(direction="+direction+", movement="+movement+")");
        boolean DEBUGSUCCEEDED = false;
        if(AvailabilityDetector.isAvailable(apiCallLog.getMethodName())) {
            boolean check = CameraProxyZoom.actZoom(direction, movement);
            if(check) {
                returnCb.returnCb(0);
                DEBUGSUCCEEDED = true;
            } else {
                returnCb.handleStatus(StatusCode.INTERNAL_SERVER_ERROR.toInt(), "Set operation failed.");
            }
        }
        else {
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Not Available Now");
        }

        Log.e(TAG, "<=== actZoom() : DEBUGSUCCEEDED="+DEBUGSUCCEEDED);
        //////
        } catch(InterruptedException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was interrupted.");
            e.printStackTrace();
            returnCb.handleStatus(StatusCode.ANY.toInt(), "Interrupted Error");
        } catch (TimeoutException e) {
            Log.e(TAG, "Method call of " + apiCallLog.getMethodName() + " was timeout.");
            returnCb.handleStatus(StatusCode.TIMEOUT.toInt(), "Timed out");
        } finally {
            if(null != apiCallLog) {
                apiCallLog.clear();
                apiCallLog = null;
            }
        }
        //////
        return 0;
	}
}
