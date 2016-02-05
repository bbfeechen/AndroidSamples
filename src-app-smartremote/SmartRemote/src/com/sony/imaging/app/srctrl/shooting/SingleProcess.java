/**
 * 
 */
package com.sony.imaging.app.srctrl.shooting;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.executor.IAdapter;
import com.sony.imaging.app.base.shooting.camera.executor.ICaptureProcess;
import com.sony.scalar.hardware.CameraEx;

public class SingleProcess implements ICaptureProcess {
	private static final String TAG = SingleProcess.class.getSimpleName();
	private CameraEx mCameraEx;
	private IAdapter mAdapter;

	@Override
	public void prepare(CameraEx cameraEx, IAdapter adapter) {
		mCameraEx = cameraEx;
		mAdapter = adapter;
	}

	@Override
	public void terminate(){
		mCameraEx = null;
		mAdapter = null;
	}
	
	@Override
	public void preTakePicture() {
	}

	@Override
	public void takePicture() {
		Log.v(TAG, "takePicture without SelfTimer");
		mCameraEx.burstableTakePicture();
	}

	@Override
	public void startSelfTimerShutter() {
		Log.v(TAG, "takePicture with SelfTimer");
		mCameraEx.startSelfTimerShutter();
	}

	@Override
	public void onShutter(int status, CameraEx cameraEx) {
		mAdapter.enableNextCapture(status);
	}

}
