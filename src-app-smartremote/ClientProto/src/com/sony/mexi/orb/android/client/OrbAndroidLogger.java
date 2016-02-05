package com.sony.mexi.orb.android.client;

import android.util.Log;

public final class OrbAndroidLogger {

    private OrbAndroidLogger() {
    }

    public static void log(String tag, String msg) {
        Log.d(tag, msg);
    }

}
