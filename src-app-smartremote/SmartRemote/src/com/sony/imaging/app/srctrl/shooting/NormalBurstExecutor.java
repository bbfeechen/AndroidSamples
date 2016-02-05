package com.sony.imaging.app.srctrl.shooting;

import android.hardware.Camera;

import com.sony.imaging.app.base.shooting.camera.executor.IAdapter;
import com.sony.imaging.app.base.shooting.camera.executor.NormalExecutor;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.ShutterListener;

/**
 * ShootingExecutor for Diadem Burst shooting in no-spinal mode.
 *
 */
public class NormalBurstExecutor extends NormalExecutor {
	//private static final String TAG = "NormalBurstExecutor";
	private NormalBurstShutterCb mShutterCb = null;

	@Override
	protected void prepare(Camera camera, CameraEx cameraEx) {
		mShutterCb = new NormalBurstShutterCb(mAdapter);
		super.prepare(camera, cameraEx);
	}

	private static class NormalBurstShutterCb extends MyShutterCb {
		IAdapter mAdapter = null;
		public NormalBurstShutterCb(IAdapter adapter){
			mAdapter = adapter;
		}
		@Override
		public void onShutter(int status, CameraEx cameraEx) {
			super.onShutter(status, cameraEx);
			if (null != mAdapter) {
				mAdapter.onShutter(status, cameraEx);
			}
		}
	}

	@Override
	protected ShutterListener getShutterListener(){
		return mShutterCb;
	}
}
