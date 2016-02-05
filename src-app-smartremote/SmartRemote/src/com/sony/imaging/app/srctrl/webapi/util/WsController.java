package com.sony.imaging.app.srctrl.webapi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.SRCtrlServlet;
import com.sony.imaging.app.srctrl.webapi.availability.AvailabilityDetector;
import com.sony.imaging.app.srctrl.webapi.service.OrbAndroidServiceEx;
import com.sony.imaging.app.srctrl.webapi.servlet.LiveviewChunkTransfer;
import com.sony.imaging.app.srctrl.webapi.servlet.OrbAndroidResourceLoader;
import com.sony.imaging.app.srctrl.webapi.servlet.PostviewResourceLoader;
import com.sony.mexi.orb.servlet.accesscontrolinterface.AccessControlInterfaceServlet;
import com.sony.mexi.orb.servlet.serviceguide.OrbServiceGuideServlet;
import com.sony.scalar.webapi.lib.authlib.AuthLibManager;
import com.sony.scalar.webapi.lib.authlib.EnableMethodsState;
import com.sony.scalar.webapi.lib.authlib.EnableMethodsStateListener;

/**
 * 
 * This class provide functions to launch Web servers.
 * @author 0000138134
 *
 */
public class WsController {
	private static final String TAG = WsController.class.getSimpleName();
    private Context context;
    private Intent intent;
    
    private boolean isReady;
    
    private ServiceConnection emptyConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public WsController(Context context) {
    	this.context = context;
    	isReady = false;
    }
    
    public synchronized boolean start(){
    	if(!isReady){
            Log.v(TAG, "Starting AuthLibManager...");
            AuthLibManager authLibManager = AuthLibManager.getInstance();
            authLibManager.clearEnableMethods();
            authLibManager.clearEnableMethodsHookHandler();
            authLibManager.clearPrivateMethods();
            authLibManager.initialize();
            ArrayList<String> privateApiList = AvailabilityDetector.getPrivateApiList();
            Log.v(TAG, "  PRIVATE API LIST: " + Arrays.toString(privateApiList.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY)));
            authLibManager.setPrivateMethods(privateApiList);
            authLibManager.addEnableMethodsStateListener(new EnableMethodsStateListener() {
                @Override
                public void onStateChange(EnableMethodsState state, String devName, String devId,
                    String methods, String sg) {
                    Log.v(TAG, "AuthLib: state=" + state.name() + ", devName=" + devName + ", devId="+devId+", methods="+methods+", sg="+sg);
                }

                @Override
                public void onError(int responseCode, String devName, String devId, String methods,
                        String sg)
                {
                    Log.v(TAG, "AuthLib: responseCode=" + responseCode + ", devName=" + devName + ", devId="+devId+", methods="+methods+", sg="+sg);
                }
            });

	        Map<String, HttpServlet> servlets = new HashMap<String, HttpServlet>();
	        SRCtrlServlet sRCtrlServlet = new SRCtrlServlet();
	        //SRCtrlServletLogger logger = new SRCtrlServletLogger();
	        //sRCtrlServlet.setLogger(logger);
	        
	        OrbAndroidResourceLoader.setAssertManager(context.getAssets());
	        OrbAndroidResourceLoader resourceLoader = new OrbAndroidResourceLoader();
	        PostviewResourceLoader postviewLoader = new PostviewResourceLoader();
	        LiveviewChunkTransfer liveviewChunkTransfer = new LiveviewChunkTransfer();
	        AccessControlInterfaceServlet accessControlInterfaceServlet = new AccessControlInterfaceServlet();
	        //accessControlInterfaceServlet.setLogger(logger);
	        
	        servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI_CAMERA, sRCtrlServlet);
            servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI + "index.html", resourceLoader);
            servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI + "orb-client.min.js", resourceLoader);
			servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW + "*", postviewLoader);
			servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_LIVEVIEW + "*", liveviewChunkTransfer);
			servlets.put(SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI_ACCESS_CONTROL, accessControlInterfaceServlet);
			
			authLibManager.setServlets(servlets);
			
	        intent = new Intent(context, OrbAndroidServiceEx.class);
	        intent.putExtra(SRCtrlConstants.SERVLETS, (Serializable)servlets);
	        intent.putExtra(SRCtrlConstants.PORT, SRCtrlConstants.HTTP_PORT_INT);
	        intent.putExtra(SRCtrlConstants.NUM_OF_THREADS, SRCtrlConstants.NUM_OF_SERVER_THREADS);
	        intent.putExtra(SRCtrlConstants.NUM_OF_BACKLOGS, SRCtrlConstants.NUM_OF_SERVER_BACKLOGS);
	        //intent.putExtra(OrbAndroidServiceEx.AUTO_SERVICE_GUIDE, true); // The default is true in OrbAndroidServiceEx.java
	        if(bind()){
	        	isReady = true;
	        } else {
	        	Log.e(TAG, "Failed binding Web Server");
	        	isReady = false;
	        	
	        	stopAuthManager();
	        }
    	} else {
    		Log.v(TAG, "Web Server has already been bound.");    		
    	}
    	return isReady;
    }

    private boolean bind(){
    	try{
    		if(context.bindService(intent, emptyConn, Context.BIND_AUTO_CREATE)){
    			Log.v(TAG, "bound");
            	return true;
        	} else {
        		return false;
        	}
    	} catch (SecurityException e){
    		Log.e(TAG, "SecurityException in bind");
    		return false;
    	}
    }

    public synchronized boolean unbind(){
    	if(isReady){
        	PostviewResourceLoader.initData();		// Clear postview images from PostviewResourceLoader
    		try{
    			context.unbindService(emptyConn);
    			Log.v(TAG, "unbound");
    		} catch (IllegalStateException e){
    			e.printStackTrace();
    		}
			isReady = false;
			
			stopAuthManager();
			
	    	return true;
    	} else {
    		Log.v(TAG, "Web Server has already been unbound.");
    		return true;
    	}
    }
    
    private void stopAuthManager()
    {
        Log.v(TAG, "Stopping AuthLibManager...");
        AuthLibManager authLibManager = AuthLibManager.getInstance();
        authLibManager.uninitialize();
    }
}
