package com.sony.imaging.app.srctrl.network;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.srctrl.SRCtrlRootState;
import com.sony.imaging.app.srctrl.network.base.NwStateBase;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.AppInfo;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;
import com.sony.scalar.sysutil.didep.Caution;
import com.sony.wifi.direct.DirectConfiguration;
import com.sony.wifi.direct.DirectManager;

/**
 * 
 * This class is root state of Connection screens.
 * @author 0000138134
 *
 */

public class NetworkRootState extends NwStateBase{
	public static final String ID_STANDBY = "ID_STANDBY";
	public static final String ID_REGISTERING = "ID_REGISTERING";
	public static final String ID_CONNECTED = "ID_CONNECTED";
	public static final String ID_WPS_PBC = "ID_WPS_PBC";
	public static final String ID_WPS_PIN_INPUT = "ID_WPS_PIN_INPUT";
	public static final String ID_WPS_PIN_PRINT = "ID_WPS_PIN_PRINT";
	public static final String ID_WPS_ERROR = "ID_WPS_ERROR";
	public static final String ID_WAITING = "ID_WAITING";
	public static final String ID_CONFIRM = "ID_CONFIRM";
	public static final String ID_RESTARTING = "ID_RESTARTING";
	
	private static String mySsid;
	private static String myPsk;
	private static String myDeviceName;
	private static String myWpsPin;	
	private static String directTargetDeviceName;
	private static String directTargetDeviceAddress;	
	
	public static final String PROP_ID_APP_ROOT = "NetworkRoot";
	
	@Override
	public Object getData(final String name) {
		return super.getData(name);
	}

	@Override
	public boolean setData(final String name, final Object o) {
		return super.setData(name, o);
	}
	
	
	@Override
	protected NetworkRootState getContainer() {
		Log.v(getLogTag(), "getContainer should not be called for NetworkRootContainer");
		return null;
	}

	// Fragment
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        StateController.getInstance().setState(this);
        
		Log.v(getLogTag(), "setRoot " + setData(PROP_ID_APP_ROOT, this));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		AppInfo.notifyAppInfo(getActivity().getApplicationContext(), getActivity().getPackageName(), getActivity().getClass().getName(),
				AppInfo.LARGE_CATEGORY_REC, AppInfo.SMALL_CATEGORY_STILL,
				BaseApp.PULLING_BACK_KEYS_FOR_PLAYBACK, BaseApp.RESUME_KEYS_FOR_SHOOTING);
		
		StateController stateController = StateController.getInstance();
		stateController.setAppCondition(AppCondition.PREPARATION);
		
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);        
        DirectManager directManager = (DirectManager) getActivity().getSystemService(DirectManager.WIFI_DIRECT_SERVICE);
        
        if(stateController.isInitialWifiSetup()){
			this.addChildState(ID_STANDBY, null);
        } else if (stateController.isFatalError()){
	    	setNextState(SRCtrlRootState.FATAL_ERROR, null);
        } else if (wifiManager.isWifiEnabled()){
        	if(directManager.isDirectEnabled()){
        		if (directManager.getMyDevice().getOperatingMode() == DirectConfiguration.OperatingMode.GO){
                	if(directManager.getStationList().size() != 0){
            			this.addChildState(ID_CONNECTED, null);
                	} else {
            			this.addChildState(ID_WAITING, null);
                	}
                } else {
        	    	setNextState(SRCtrlRootState.FATAL_ERROR, null);
                }
        	} else {
    	    	setNextState(SRCtrlRootState.FATAL_ERROR, null);       		
        	}        	
        } else {
	    	setNextState(SRCtrlRootState.FATAL_ERROR, null);
        }
        
        stateController.setIsClosingShootingState(false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		removeData(PROP_ID_APP_ROOT);
		super.onDestroy();
	}
	
	
	@Override
    public APO_TYPE getApoType(){
    	return APO_TYPE.NONE;
    }
	
	
	@Override
	public Integer getCautionMode(){
		return Integer.valueOf( Caution.MODE_ID_PB );
	}
	
	
	
	protected Bundle getBundleToLayout(String layoutTag){
		return null;
	}
	
	protected void setDirectTargetDeviceName(String name){
		directTargetDeviceName = name;
	}
	public static String getDirectTargetDeviceName(){
		return directTargetDeviceName;
	}
	
	protected void setDirectTargetDeviceAddress(String addr){
		directTargetDeviceAddress = addr;
	}
	public static String getDirectTargetDeviceAddress(){
		return directTargetDeviceAddress;
	}
	
	protected void setWpsPin(String pin){
		myWpsPin = pin;
	}
	public static String getWpsPin(){
		return myWpsPin;
	}
	
	protected void setMySsid(String ssid){
		mySsid = ssid;
	}
	public static String getMySsid(){
		return mySsid;
	}
	
	protected void setMyPsk(String psk){
		myPsk = psk;
	}
	public static String getMyPsk(){
		return myPsk;
	}
	
	public static void setMyDeviceName(String name){
		myDeviceName = name;
	}
	public static String getMyDeviceName(){
		return myDeviceName;
	}
}
