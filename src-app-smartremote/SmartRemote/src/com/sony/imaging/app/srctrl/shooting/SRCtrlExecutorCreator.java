package com.sony.imaging.app.srctrl.shooting;

import android.hardware.Camera;
import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.executor.BaseShootingExecutor;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.camera.executor.IProcess;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.scalar.hardware.CameraEx;

public class SRCtrlExecutorCreator extends ExecutorCreator {
    private static final String TAG = SRCtrlExecutorCreator.class.getName();
	private NormalBurstExecutor mNormalBurstExecutor = new NormalBurstExecutor();

	private SingleProcess singleProcess = new SingleProcess();	
	
	@Override
	protected IProcess getProcess(Camera camera, CameraEx cameraEx) {
		return singleProcess;
	}
	
	@Override
	protected boolean isSpinal() {
		return false;
	}

	@Override
	protected boolean isSpecial() {
		return false;
	}

	@Override
	protected boolean isImmediatelyEEStart() {
		return true;
	}

	@Override
	protected boolean isInheritSetting() {
		return false;
	}
	
	@Override
	protected BaseShootingExecutor getExecutor(Class<?> clazz){
		BaseShootingExecutor executor = null;
		if (NormalBurstExecutor.class.equals(clazz)){
			executor = mNormalBurstExecutor;
		}
		else {
			executor = super.getExecutor(clazz);
		}
		return executor;
	}

	public static String getRecordingMedia()
	{
	    String ids[] = getMediaId();
	    if(null == ids || 0 == ids.length)
	    {
	        return null;
	    }
	    // ShootingState#MyNotificationListener#onNotify()
	    return ids[0];
	}

    @Override
    public synchronized void init()
    {
        StackTraceElement[] stackTraceArray = Thread.currentThread().getStackTrace();
        Log.v(TAG, "SRCtrlExecutorCreator#init() was called by...");
        for(StackTraceElement s : stackTraceArray) {
            Log.v(TAG, "  + " + s.toString());
        }
        super.init();
    }

	@Override
	protected boolean isSpinalZoomSetting() {
		return true;
	}

	@Override
	protected boolean isInheritDigitalZoomSetting() {
		return true;
	}
}
