package com.sony.imaging.app.srctrl.webapi.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.content.res.AssetManager;
import android.util.Log;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.mexi.orb.servlet.MimeType;
import com.sony.mexi.orb.servlet.OrbResourceLoader;

/**
 * 
 * This class provide function as a standard HTTP (WebAPI) Server
 * @author 0000138134
 *
 */
public class OrbAndroidResourceLoader extends OrbResourceLoader {

    private static final long serialVersionUID = 1L;

    private static AssetManager assetManager;

    public static void setAssertManager(AssetManager am) {
        assetManager = am;
    }

    public String getRootPath() {
        return SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding(SRCtrlConstants.TEXT_ENCODING_UTF8);
        if (req.getQueryString() != null) {
            send404(res);
            return;
        }
        String uri = req.getRequestURI();
        if (uri == null) {
            send404(res);
            return;
        }
        
        String type;
        if(uri.equals(SRCtrlConstants.SERVLET_ROOT_PATH_WEBAPI)){
    		type = MimeType.getType(SRCtrlConstants.FILE_EXTENTION_HTML);
    	} else {
    		type = getContentType(uri);
            if (type == null) {
                send404(res);
                return;
            }
    	}
        
        InputStream is = getContentStream(uri);
        if (is == null) {
            send404(res);
            return;
        }

        res.setContentType(type);

        byte[] data = new byte[4096];
        int total = 0;
        int size = -1;
        OutputStream os = res.getOutputStream();
        while ((size = is.read(data)) != -1) {
            os.write(data, 0, size);
            total += size;
        }
        res.setContentLength(total);
        log(uri + " " + type + " " + total);
    }
    
    @Override
    protected InputStream getContentStream(String uri) {
        try {
        	Log.v("OrbAndroidResourceLoader", "Requested URL: " + uri);
        	String rootPath = getRootPath();
        	Log.v("OrbAndroidResourceLoader", "Root  Path:    " + rootPath);
            if (assetManager != null && uri.startsWith(rootPath))
            	if(uri.equals(rootPath)){
            		Log.v("OrbAndroidResourceLoader", "Try to load " + SRCtrlConstants.WEBAPI_ROOT_FILENAME);
            		return assetManager.open(SRCtrlConstants.WEBAPI_ROOT_FILENAME);
            	} else {
            		Log.v("OrbAndroidResourceLoader", "Try to load " + uri.substring(rootPath.length()));
            		return assetManager.open(uri.substring(rootPath.length()));
            	}
            else
                return null;
        } catch (IOException e) {
            return null;
        }
    }
}
