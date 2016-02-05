package com.sony.imaging.app.srctrl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.BaseBackUpKey;
import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.caution.IkeyDispatchEach;
import com.sony.imaging.app.fw.ContainerState;
import com.sony.imaging.app.fw.IKeyFunction;
import com.sony.imaging.app.fw.StateHandle;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.SafeBroadcastReceiver;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.srctrl.webapi.definition.ServiceType;
import com.sony.imaging.app.srctrl.webapi.util.WsController;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;
import com.sony.imaging.app.util.BackUpUtil;
import com.sony.scalar.lib.ddserver.DdController;
import com.sony.scalar.lib.ddserver.DdController.DdStatusListener;
import com.sony.scalar.lib.ddserver.DdStatus;
import com.sony.scalar.lib.ddserver.ServerConf;
import com.sony.scalar.lib.webapiddservice.IconInfo;
import com.sony.scalar.lib.webapiddservice.WebApiServiceInfo;
import com.sony.scalar.sysnetutil.ScalarWifiInfo;
import com.sony.scalar.sysutil.ScalarProperties;
import com.sony.scalar.sysutil.didep.Caution;
import com.sony.wifi.direct.DirectConfiguration;
import com.sony.wifi.direct.DirectDevice;
import com.sony.wifi.direct.DirectManager;

/**
 * The root state of SmartRemoteControl application.
 * Receives Wi-Fi/Direct broadcast intents.
 * Controls DeviceDiscovery server and WebAPI server.
 * Controls common finish process.
 * @author 0000138134
 *
 */
public class SRCtrlRootState extends ContainerState implements DdStatusListener {
	private final static String TAG = SRCtrlRootState.class.getSimpleName();
	
	public static final String APP_NETWORK = "APP_NETWORK";
	public static final String APP_SHOOTING = "APP_SHOOTING";
	public static final String FATAL_ERROR = "FATAL_ERROR";
	
	private StateHandle stateHandle;
	
	private WifiManager wifiManager;
	
	private boolean isInitialDisabling;	
	private boolean isDisableActionFiltered;
	private boolean isEnableActionFiltered;
	private boolean isGroupCreateActionFiltered;
	private boolean isDisablingForFinish;
	private boolean isRetrying;
		
    private DirectManager directManager;
    private IntentFilter iFilter;
    private SafeBroadcastReceiver bReceiver;

    private Handler handler;
	private Runnable delayedWifiEnabler;
	
	public static WsController wsController;
	private DdController ddController;
	private ServerConf serverConf;
	private final WebApiServiceInfo SERVICE_INFO[] = {
	 		new WebApiServiceInfo( ServiceType.GUIDE, SRCtrlConstants.WEBAPI_BASE_URL, "")
	 		//, new WebApiServiceInfo( ServiceType.SYSTEM, SRCtrlConstants.WEBAPI_BASE_URL, "")
	 		, new WebApiServiceInfo( ServiceType.ACCESS_CONTROL, SRCtrlConstants.WEBAPI_BASE_URL, "")
	 		, new WebApiServiceInfo( ServiceType.CAMERA, SRCtrlConstants.WEBAPI_BASE_URL, "" )
	};
	private final IconInfo ICON_INFO[] = {};
	
	@Override
	public void onResume(){
		super.onResume();
		
		if(Settings.System.getInt(getActivity().getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1){
			Log.v(TAG, "AIRPLANE MODE IS ON. FORCE EXIT APPLICATION WITHOUT CONFIRMATION !!!!");
			((BaseApp)getActivity()).finish(false);
			return;
		}
		
		handler = new Handler();
		wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);        
        directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);

        delayedWifiEnabler = new Runnable(){
			@Override
			public void run() {
    			Log.v(TAG, "Delayed Wi-Fi Enabler works.");
    			wifiManager.setWifiEnabled(true);
			}        	
        };
        
        initFlags();
        StateController.getInstance().init();
        
    	serverConf = new ServerConf();
        serverConf.debugOnPhone = false;
        serverConf.uniqueServiceId = SRCtrlConstants.DEVICE_DISCOVERY_UNIQUE_SERVICE_ID;
        serverConf.serviceInfo = SERVICE_INFO;
        serverConf.iconInfo = ICON_INFO;
        ddController = new DdController(getActivity(), serverConf, this);  
        wsController = new WsController(getActivity());
        
        iFilter = new IntentFilter();
        iFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        iFilter.addAction(DirectManager.DIRECT_STATE_CHANGED_ACTION);
        iFilter.addAction(DirectManager.GROUP_CREATE_SUCCESS_ACTION);
        iFilter.addAction(DirectManager.GROUP_CREATE_FAILURE_ACTION);
        iFilter.addAction(DirectManager.STA_DISCONNECTED_ACTION);
        
        bReceiver = new SafeBroadcastReceiver(){
			@Override
			public void checkedOnReceive(Context context, Intent intent) {
				handleEvent(intent);
			}
        };
        bReceiver.registerThis(getActivity(), iFilter);

        if(wifiManager.getWifiState()!= WifiManager.WIFI_STATE_DISABLED){
        	isDisableActionFiltered = false;
        	isInitialDisabling = true;
        	Log.v(TAG, "Disable Wi-Fi for initialization");
        	wifiManager.setWifiEnabled(false);
        } else {
        	isEnableActionFiltered = false;
        	Log.v(TAG, "Clean start Wi-Fi");
        	wifiManager.setWifiEnabled(true);
        }
        
        StateController.getInstance().setInitialWifiSetup(true);
        StateController.getInstance().setFatalError(false);
        stateHandle = this.addChildState(APP_NETWORK, null);        

		setKeysForLensCaution();
	}
	
	private void initFlags(){
		isInitialDisabling = false;
		isDisableActionFiltered = true;
		isEnableActionFiltered = true;
		isGroupCreateActionFiltered = true;
		isDisablingForFinish = false;
		isRetrying = false;
	}
	
	@Override
	public void onPause(){
		handler.removeCallbacks(delayedWifiEnabler);		
    	isDisablingForFinish = true;
    	
    	bReceiver.unregisterThis(getActivity());
        //String flatten = BackUpUtil.getInstance().getPreferenceString(BaseBackUpKey.ID_PARAMETERS, null);
        //Log.v(TAG, "FLATTEN: " + flatten);
    	
	    invokeFinishProcess();
	    super.onPause();		
	}
	
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
	
    private void handleEvent(Intent intent) {
        String action = intent.getAction();
        Log.v(TAG, "intent: action=" + action);

		if(!StateController.getInstance().isFatalError()){
            Log.v(TAG, "Wi-Fi Action in handleEvent(): " + action);
	        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
	            handleWifiStateChanged(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,  WifiManager.WIFI_STATE_UNKNOWN));
	        } else if (DirectManager.DIRECT_STATE_CHANGED_ACTION.equals(action)) {
	            handleDirectStateChanged(intent.getIntExtra(DirectManager.EXTRA_PREVIOUS_DIRECT_STATE, DirectManager.DIRECT_STATE_UNKNOWN),
	                    intent.getIntExtra(DirectManager.EXTRA_DIRECT_STATE, DirectManager.DIRECT_STATE_UNKNOWN));
	        } else if (DirectManager.GROUP_CREATE_SUCCESS_ACTION.equals(action)) {
	            handleGroupCreateSuccess((DirectConfiguration)intent.getParcelableExtra(DirectManager.EXTRA_DIRECT_CONFIG));
	        } else if (DirectManager.STA_DISCONNECTED_ACTION.equals(action)) {
	            handleStationDisconnected(intent.getStringExtra(DirectManager.EXTRA_STA_ADDR));
	        } else if (DirectManager.GROUP_CREATE_FAILURE_ACTION.equals(action)) {
	            handleGroupCreateFailure(intent.getIntExtra(DirectManager.EXTRA_ERROR_CODE, -1));
	        }
		}
    }
    
    private void handleWifiStateChanged(int state) {
    	Log.v(TAG, "handleWifiStateChanged: " + state);
    	switch(state) {
	        case WifiManager.WIFI_STATE_ENABLED:
	        	if(isEnableActionFiltered){
	        		Log.v(TAG, "Ignore WIFI_STATE_ENABLED");
	        	} else {
	        		isRetrying = false;
	        		directManager.setDirectEnabled(true);
	        	}
	            break;
	        case WifiManager.WIFI_STATE_ENABLING:
	        	isDisableActionFiltered = false;
	        	break;
	        case WifiManager.WIFI_STATE_DISABLED:
	        	if(isDisableActionFiltered){
	        		Log.v(TAG, "Ignore WIFI_STATE_DISABLED");	        		
	        	} else if(isInitialDisabling){
		            isInitialDisabling = false;
		            isEnableActionFiltered = false;
		            Log.v(TAG, "Restarting Wi-Fi");
		            handler.postDelayed(delayedWifiEnabler, SRCtrlConstants.DELAY_WIFI_ENABLE);
	            	//wifiManager.setWifiEnabled(true);
	            } else if(isDisablingForFinish){
		            Log.v(TAG, "Wi-Fi is Disabled for Finish");
	            } else if (isRetrying){
		            Log.e(TAG, "WifiStateChanged: WIFI_STATE_DISABLED");
		            StateController.getInstance().setFatalError(true);
		            replaceChildState(stateHandle, FATAL_ERROR, null);
	            } else {
	            	Log.v(TAG, "Retrying Wi-Fi Enable");
	            	isRetrying = true;
		            handler.postDelayed(delayedWifiEnabler, SRCtrlConstants.DELAY_WIFI_ENABLE);
	            	//wifiManager.setWifiEnabled(true);	            	
	            }
	        	break;
	        case WifiManager.WIFI_STATE_UNKNOWN:
	        	if(isDisableActionFiltered){
	        		Log.v(TAG, "Ignore WIFI_STATE_UNKNOWN");
	        	} else if (isRetrying){
	        		Log.e(TAG, "WifiStateChanged: WIFI_STATE_UNKNOWN");
		            StateController.getInstance().setFatalError(true);
		            replaceChildState(stateHandle, FATAL_ERROR, null);
	        	} else {
	            	Log.v(TAG, "Retrying Wi-Fi Enable");
	            	isRetrying = true;
		            handler.postDelayed(delayedWifiEnabler, SRCtrlConstants.DELAY_WIFI_ENABLE);
	            	//wifiManager.setWifiEnabled(true);	            	
	            }
	        	break;
	        default:
	        	break;
        }
    }
    
    private void handleDirectStateChanged(int previousState, int currentState) {
    	Log.v(TAG, "handleDirectStateChanged: " + currentState);
    	switch(currentState){
    		case DirectManager.DIRECT_STATE_ENABLED:
				isRetrying = false;
    			if(isEnableActionFiltered){
    				Log.v(TAG, "Ignore DIRECT_STATE_ENABLED");
    			} else {
    				startGroupOwner();
    			}
    			break;
    		case DirectManager.DIRECT_STATE_ENABLING:
    			break;
    		case DirectManager.DIRECT_STATE_DISABLED:
    			if(previousState == DirectManager.DIRECT_STATE_ENABLED){
    				break;
    			}
    			if(isDisableActionFiltered){
    				Log.v(TAG, "Ignore DIRECT_STATE_DISABLED");
    			} else if(isDisablingForFinish){
		            Log.v(TAG, "Wi-Fi Direct is Disabled for Finish");	            	
	            } else if(!isInitialDisabling){
	    			Log.e(TAG, "DirectStateChanged: DIRECT_STATE_DISABLED");
		            StateController.getInstance().setFatalError(true);
		            replaceChildState(stateHandle, FATAL_ERROR, null);
    			}
    			break;
			case DirectManager.DIRECT_STATE_UNKNOWN:
				if(isDisableActionFiltered){
	        		Log.v(TAG, "Ignore DIRECT_STATE_UNKNOWN");					
				} else {
		            StateController.getInstance().setFatalError(true);		            
		            replaceChildState(stateHandle, FATAL_ERROR, null);
					Log.e(TAG, "DirectStateChanged: DIRECT_STATE_UNKNOWN");
				}
				break;
			default:
				break;
    	}
    }
    
    private void handleGroupCreateSuccess(DirectConfiguration config) {
    	isRetrying = false;
    	if(isGroupCreateActionFiltered){
    		Log.v(TAG, "Ignore GROUP_CREATE_SUCCESS");
    	} else {
	    	if(!StateController.getInstance().isDdServerReady()){
		        if(!ddController.bindService()){
		        	Log.e(TAG, "Failed Binding DD Server Service");
		            StateController.getInstance().setFatalError(true);
	            	replaceChildState(stateHandle, FATAL_ERROR, null);
		        }
	    	}
	    	if(!wsController.start()){
            	Log.e(TAG, "Failed Binding Web Server Service");
	            StateController.getInstance().setFatalError(true);	
            	replaceChildState(stateHandle, FATAL_ERROR, null);
	        }
    	}
    }

    private void handleGroupCreateFailure(int err) {
    	if(isGroupCreateActionFiltered){
    		Log.v(TAG, "Ignore GROUP_CREATE_FAILURE");    				
    	} else if(isRetrying){
    		replaceChildState(stateHandle, FATAL_ERROR, null);
    	} else {
    		Log.v(TAG, "Retrying group creation");
    		isRetrying = true;
    		startGroupOwner();
    	}
    }
    
    private void handleStationDisconnected(String addr) {
        Log.v(TAG, "SRCtrlRootState#handleStationDisconnected() was called.");
    	wsController.unbind();	
    	StateController stateController = StateController.getInstance();
		if(!wsController.start()){
        	Log.e(TAG, "Failed Binding Web Server Service");
            stateController.setFatalError(true);	
        	replaceChildState(stateHandle, FATAL_ERROR, null);
        	return;
        }
		AppCondition appCondition = stateController.getAppCondition();
        Log.v(TAG, "Current state= "+stateController.getAppCondition().name());
    	if(AppCondition.SHOOTING_INHIBIT.equals(appCondition)
    	        || AppCondition.SHOOTING_LOCAL.equals(appCondition)
    	        || AppCondition.SHOOTING_REMOTE.equals(appCondition)
    	        ){
            Log.v(TAG, "Change the state to network state later.");
    		stateController.setGoBackFlag(true);
    	} else if(!AppCondition.PREPARATION.equals(stateController.getAppCondition())){
            Log.v(TAG, "Change the state to network state now.");
    		stateController.changeToNetworkState();
    	}
    }
    
    /**
     * This method should be called in onPause of Application.
     */
    private void invokeFinishProcess(){
    	StateController stateController = StateController.getInstance();    	
    	stateController.onPauseCalled();

		wsController.unbind();
    	
    	if(stateController.isDdServerReady()){
    		Log.v(TAG, "Unbinding DD Server");
    		try{
    			ddController.unbindService();
    		} catch (IllegalArgumentException e){
    			Log.e(TAG, "IllegalArgumentException at unbinding DDServer");
    		}
    	}
    	
    	directManager.setDirectEnabled(false);
    	wifiManager.setWifiEnabled(false);
    }
	
	@Override
	public void notifyStatus(DdStatus status, int opt) {
		if(status == DdStatus.OK){
  			if(!isDisablingForFinish){
  				Log.v(TAG, "DD Server is ready");
  				StateController.getInstance().setDdServerReady(true);
  			}
  		} else {
  			if(DdStatus.ERROR_SERVICE_START.equals(status)){
  	  			Log.e(TAG, "DD Server Error: ERROR_SERVICE_START");
  	  		} else if(DdStatus.ERROR_FROM_SERVICE.equals(status)){
  	  			Log.e(TAG, "DD Server Error: ERROR_FROM_SERVICE");
  	  		} else if(DdStatus.ERROR_NETWORK.equals(status)){
  	  			Log.e(TAG, "DD Server Error: ERROR_NETWORK");
  	  		} else if(DdStatus.ERROR_SERVICE_NOT_FOUND.equals(status)){
  	  			Log.e(TAG, "DD Server Error: ERROR_SERVICE_NOT_FOUND");
  	  		} else if(DdStatus.ERROR_DEVICE_NAME.equals(status)){
  	  			Log.e(TAG, "DD Server Error: ERROR_DEVICE_NAME");
  	  		}
  			replaceChildState(stateHandle, FATAL_ERROR, null);
  		}
	}
	
	private void startGroupOwner(){
		isGroupCreateActionFiltered = false;
		DirectDevice dDevice = directManager.getMyDevice();
		if(dDevice == null){
			try {
				Log.v(TAG, "Waiting for DirectDevice");
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException");
			}
			dDevice = directManager.getMyDevice();
			/*
			if(dDevice == null){						//  TODO Deferred for next update.
		    	replaceChildState(stateHandle, FATAL_ERROR, null);
		    	return;
			}
			*/
		}
		List<DirectConfiguration> confList = directManager.getConfigurations();
		String deviceName;
    	if(confList.size() > 0){
    		deviceName = dDevice.getName();
			NetworkRootState.setMyDeviceName(deviceName);
	    	directManager.setSsidPostfix(ScalarWifiInfo.getProductCode() + ":" + deviceName);
    		directManager.startGo(confList.get(confList.size()-1).getNetworkId());			// Start GO with latest configuration
    	} else {
    		deviceName = ScalarProperties.getString(ScalarProperties.PROP_MODEL_NAME);
    		NetworkRootState.setMyDeviceName(deviceName);
	    	directManager.setSsidPostfix(ScalarWifiInfo.getProductCode() + ":" + deviceName);
    		directManager.setModelName(deviceName);
    		directManager.setDeviceName(deviceName);
	    	directManager.startGo(DirectManager.PERSISTENT_GO);			// Start GO with new configuration    
    	}
	}
	
	private void setKeysForLensCaution(){
		IkeyDispatchEach changeModeKey = new IkeyDispatchEach(null, null) {
            @Override
            public int onConvertedKeyDown(KeyEvent event, IKeyFunction func)
            {
                CautionUtilityClass.getInstance().executeTerminate();
                return HANDLED;
            }

            @Override
            public int onConvertedKeyUp(KeyEvent event, IKeyFunction func)
            {
                CautionUtilityClass.getInstance().executeTerminate();
                return HANDLED;
            }
		};
		CautionUtilityClass.getInstance().setDispatchKeyEvent(Caution.CAUTION_ID_NO_LENS, changeModeKey);
	}
}
