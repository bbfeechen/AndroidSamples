package com.sony.scalar.webapi.service.camera.v1_0.common;

import com.sony.mexi.webapi.Callbacks;



public interface ReceiveEventCallback extends Callbacks {

	void returnCb(String status, boolean liveviewstatus, int zoomposission, int zoom_numbox, int zoom_idxcurtbox, int zoom_poscurtbox, String[] name, String[] type, boolean[] range, String[] current, String[] list);

}
