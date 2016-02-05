package com.sony.imaging.app.srctrl.webapi.specific;

import com.sony.scalar.hardware.CameraEx;

public interface ShutterListenerNotifier {
	public void onShutterNotify(int status, CameraEx cam);
}
