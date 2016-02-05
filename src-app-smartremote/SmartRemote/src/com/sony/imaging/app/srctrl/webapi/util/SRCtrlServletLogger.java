package com.sony.imaging.app.srctrl.webapi.util;

import android.util.Log;

import com.sony.mexi.orb.servlet.OrbServletLogger;

public class SRCtrlServletLogger extends OrbServletLogger
{
    private static final String tag = SRCtrlServletLogger.class.getName();
    
    @Override
    public void log(String msg)
    {
        Log.v(tag, msg);
    }

    @Override
    public void debug(String msg)
    {
        log(msg);
    }
}
