package com.sony.imaging.app.srctrl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.widget.AppIconView;
import com.sony.imaging.app.base.shooting.widget.AppNameView;
import com.sony.imaging.app.fw.AppRoot;
import com.sony.imaging.app.fw.BootFactor;
import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.shooting.camera.EachDriveModeController;
import com.sony.imaging.app.srctrl.shooting.camera.SRCtrlFocusAreaController;
import com.sony.imaging.app.srctrl.shooting.camera.SRCtrlPictureQualityController;
import com.sony.imaging.app.srctrl.util.SRCtrlKikiLogUtil;
import com.sony.imaging.app.util.AppInfo;
import com.sony.scalar.sysutil.KeyStatus;
import com.sony.scalar.sysutil.ScalarInput;

/**
 * 
 * Base class of Smart Remote Control.
 * @author 0000138134
 *
 */
public class SRCtrl extends BaseApp {
    private static final String tag = SRCtrl.class.getSimpleName();
	public static final String SRCTRL_ROOT = "APP_SRCTRL";
	private static String bootApp = SRCTRL_ROOT;
	
	public SRCtrl() {
		super();
		new Factory();
		new SRCtrlExecutorCreator();
		CameraSetting.registController(ExposureModeControllerEx.getName(), ExposureModeControllerEx.class);
		CameraSetting.registController(EachDriveModeController.getName(), EachDriveModeController.class);
		CameraSetting.registController(SRCtrlPictureQualityController.getName(), SRCtrlPictureQualityController.class);
		CameraSetting.registController(SRCtrlFocusAreaController.getName(), SRCtrlFocusAreaController.class);	// IMDLAPP6-1335
	}
	
	@Override
	public String getStartApp() {
		return bootApp;
	}
	
	@Override
	public int getSupportingRecMode(){
		return getRecMode();
	}

	public static int getRecMode() {
		return REC_MODE_STILL;
	}
	
	@Override
	protected void onBoot(BootFactor factor) {
		super.onBoot(factor);
		
		boolean isHousingAttached = false;
		if( !isHousingSupported() ){
		    KeyStatus status = ScalarInput.getKeyStatus(USER_KEYCODE.WATER_HOUSING);
		    if(null != status && KeyStatus.VALID == status.valid && KeyStatus.STATUS_ON == status.status ){
		        isHousingAttached = true;
		    }
		}
		
		if(isHousingAttached){
		    return; // do nothing
		}
		
		SRCtrlKikiLogUtil.logAppLaunch();
		
		AppIconView.setIcon(R.drawable.p_16_dd_parts_sr_appicon, R.drawable.p_16_aa_parts_sr_appicon);
		AppNameView.setText(getResources().getString(SRCtrl.getAppStringId(this)));
		AppNameView.show(true);

		bootApp = SRCTRL_ROOT;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		doAbortInCaseUncautghtException = true; // abort when Uncaught Exception occurs.
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		notifyAppInfo();
	}
	private void notifyAppInfo() {
		AppInfo.notifyAppInfo(this, this.getPackageName(), this.getClass().getName(),
				AppInfo.LARGE_CATEGORY_REC, AppInfo.SMALL_CATEGORY_STILL,
				PULLING_BACK_KEYS_FOR_PLAYBACK, RESUME_KEYS_FOR_SHOOTING);
	}
	
	//*
    @Override
    public void onSystemReady()
    {
        if(isAirPlaneModeOn()) {
            notifyAppInfo();
            startAirPlaneModeActivity();
        } else {
            setBootLogo(R.drawable.p_16_dd_parts_sr_launchericon, SRCtrl.getAppStringId(this)); // moved from the constructor
            super.onSystemReady();
        }
    }
    //*/
	
    private static final String ACTION_AIRPLANEMODE_ACTIVITY = "com.sony.scalar.ScalarAAirplaneModeActivity.AIRPLANEMODE_ACTIVITY";
    private void startAirPlaneModeActivity() {
        Intent i = new Intent(ACTION_AIRPLANEMODE_ACTIVITY);
        startActivity(i);
        finish(AppRoot.FINISH_TYPE.ONLY_ACTIVITY);
    }
    private boolean isAirPlaneModeOn() {
        return Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }
    
    private static Integer CACHED_APP_STRING_ID = null;
    /** get application string id.
     * 
     * @param context initialized context.  If the context was not initialized, it never returns the correct id and causes NULL pointer exception.
     * @return application string id.
     */
    public static int getAppStringId(Context context) {
        if(null!=CACHED_APP_STRING_ID) {
            return CACHED_APP_STRING_ID;
        }
        
        if(null == context) {
            Log.e(tag, "ERROR: context is null.");
            return R.string.STRID_FUNC_EZREMOTE;
        }
        
        if(isSystemApp(context)) {
            CACHED_APP_STRING_ID = new Integer(R.string.STRID_FUNC_EZREMOTE_EMBEDDED);
        } else {
            CACHED_APP_STRING_ID = new Integer(R.string.STRID_FUNC_EZREMOTE);
        }
        return CACHED_APP_STRING_ID;
    }
    
    private static Boolean IS_SYSTEM_APP = null;
    public static String VERSION_NAME_STR = null;
    public static boolean isSystemApp(Context context) {
        if(null != IS_SYSTEM_APP) {
            return IS_SYSTEM_APP;
        }
        
        if(null == VERSION_NAME_STR) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(SRCtrl.class.getPackage().getName(), PackageManager.GET_META_DATA);
                VERSION_NAME_STR = packageInfo.versionName;
                Log.v(tag, "APP-Version: " + VERSION_NAME_STR);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        ApplicationInfo appInfo = context.getApplicationInfo();
        if( (1 == (ApplicationInfo.FLAG_SYSTEM & appInfo.flags)) 
                && (0x80 != (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP & appInfo.flags)) )
        {
            // a preinstall app, not updated yet.
            IS_SYSTEM_APP = Boolean.TRUE;
            Log.v(tag, "APP-MODE: PreInstlled App");
        } else {
            // a preinstall already-updated app, or just a download app. 
            IS_SYSTEM_APP = Boolean.FALSE;
            Log.v(tag, "APP-MODE: Downloaded App");
        }
        return IS_SYSTEM_APP;
    }

    @Override
    protected int getDefaultBackupResName(){
        return R.xml.default_value;
    }
}
