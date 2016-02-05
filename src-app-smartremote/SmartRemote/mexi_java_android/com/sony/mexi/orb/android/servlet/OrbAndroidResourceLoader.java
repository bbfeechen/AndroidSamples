package com.sony.mexi.orb.android.servlet;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;

import com.sony.mexi.orb.servlet.OrbResourceLoader;

public class OrbAndroidResourceLoader extends OrbResourceLoader {

    private static final long serialVersionUID = 1L;

    private static AssetManager assetManager;

    public static void setAssertManager(AssetManager am) {
        assetManager = am;
    }

    public String getRootPath() {
        return "/doc/";
    }

    @Override
    protected InputStream getContentStream(String uri) {
        try {
            if (assetManager != null && uri.startsWith(getRootPath())) {
                String path = (uri.charAt(0) == '/') ? uri.substring(1) : uri;
                return assetManager.open(path);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

}
