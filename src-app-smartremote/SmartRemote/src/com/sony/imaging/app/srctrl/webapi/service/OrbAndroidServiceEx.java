package com.sony.imaging.app.srctrl.webapi.service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServlet;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.mexi.orb.server.OrbServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * Extended OrbAndroidService to notify the onBind event to StateController.
 * @author 0000138134
 * 
 */

public class OrbAndroidServiceEx extends Service {

	private static final String TAG = OrbAndroidServiceEx.class.getSimpleName();
    protected OrbServer server = null;
    protected ExecutorService pool = null;
    
    public static final String AUTO_SERVICE_GUIDE = "autoServiceGuide";
    
    @Override
    @SuppressWarnings("unchecked")
    public IBinder onBind(Intent intent) {
		Log.v(TAG, "onBind");
        pool = Executors.newFixedThreadPool(intent.getIntExtra(SRCtrlConstants.NUM_OF_THREADS, SRCtrlConstants.NUM_OF_SERVER_THREADS));
        server = new OrbServer(
                    (Map<String, HttpServlet>)intent.getSerializableExtra(SRCtrlConstants.SERVLETS),
                    intent.getIntExtra(SRCtrlConstants.PORT, SRCtrlConstants.HTTP_PORT_INT),
                    pool,
                    intent.getIntExtra(SRCtrlConstants.NUM_OF_BACKLOGS, SRCtrlConstants.NUM_OF_SERVER_BACKLOGS),
                    intent.getBooleanExtra(AUTO_SERVICE_GUIDE, true));
        server.start();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        server.finish();
        pool.shutdown();
        server.shutdown();
        pool.shutdownNow();
        return super.onUnbind(intent);
    }
    
	@Override
	public void onDestroy() {
		Log.v(TAG, "onDestroy");
		super.onDestroy();
	}

}
