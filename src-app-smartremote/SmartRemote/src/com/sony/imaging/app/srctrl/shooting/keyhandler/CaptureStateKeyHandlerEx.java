package com.sony.imaging.app.srctrl.shooting.keyhandler;

import com.sony.imaging.app.base.shooting.trigger.CaptureStateKeyHandler;

public class CaptureStateKeyHandlerEx extends CaptureStateKeyHandler
{
    @Override
    public int pushedS2Key() {
        return INVALID;
    }
    @Override
    public int pushedUmRemoteS2Key()
    {
        return INVALID;
    }
}
