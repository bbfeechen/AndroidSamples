package com.sony.imaging.app.srctrl.shooting.keyhandler;

import com.sony.imaging.app.base.shooting.trigger.DevelopmentStateKeyHandler;

public class DevelopmentStateKeyHandlerEx extends DevelopmentStateKeyHandler
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
