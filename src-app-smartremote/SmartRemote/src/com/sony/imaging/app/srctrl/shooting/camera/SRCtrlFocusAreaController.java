package com.sony.imaging.app.srctrl.shooting.camera;

import com.sony.imaging.app.base.shooting.camera.FocusAreaController;

public class SRCtrlFocusAreaController extends FocusAreaController {

	@Override
	protected boolean isNeedToCheckAdditionalFactor() {
		// IMDLAPP6-1335
		// SceneMode が Auto の場合でもタッチAFしたいため、排他を行わない
		return false;
	}
}