package com.sony.scalar.webapi.service.camera.v1_2.focusmode;

import com.sony.mexi.webapi.Callbacks;

public interface GetAvailableFocusModeCallback extends Callbacks
{

    public void returnCb(String currentFocusMode, String[] focusModeCandidates);

}