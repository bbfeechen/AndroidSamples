package com.sony.mexi.orb.android.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.content.res.AssetManager;
import android.util.Log;

import com.sony.mexi.orb.servlet.OrbChunkTransfer;

public class OrbAndroidChunkTransfer extends OrbChunkTransfer {

    private static final long serialVersionUID = 1L;

    private static AssetManager assetManager;

    private static int count = 0;

    private static Map<Long, Object> threadIdToObj = new HashMap<Long, Object>();

    public static void setAssertManager(AssetManager am) {
        assetManager = am;
    }

    public String getRootPath() {
        return "/chunk/";
    }

    private String getCancelString() {
        return "cancel";
    }

    private String getExtraHeaderFieldForCancel() {
        return "X-Scalar-CancelId";
    }

    protected InputStream getContentStream(String uri) {
        try {
            return assetManager.open(uri);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Long requestedId = (long) -1;
        Thread thread = Thread.currentThread();
        Long threadId = thread.getId();

        String uri = req.getRequestURI();
        String pathAfterRootPath = uri.substring(getRootPath().length());
        if (pathAfterRootPath.startsWith(getCancelString())) {
            String requestedThreadId = pathAfterRootPath
                    .substring(getCancelString().length() + 1);
            requestedId = Long.valueOf(requestedThreadId);
            OrbChunkTransfer chunkTransferInstance = (OrbChunkTransfer) threadIdToObj
                    .get(requestedId);

            Log.d("OrbChunkTransfer cancel() ThreadId ->", requestedThreadId);

            chunkTransferInstance.cancel();
            threadIdToObj.remove(requestedId);

            // decrement log
            int threadIdToObjNum = threadIdToObj.size();
            String msgStr = Integer.toString(threadIdToObjNum);
            Log.d("ThreadIdToObj deleted threadId num->", msgStr);

            return;
        } else {
            if (this != threadIdToObj.get(threadId)) {
                threadIdToObj.put(threadId, this);

                // increment log
                int threadIdToObjNum = threadIdToObj.size();
                String msgStr = Integer.toString(threadIdToObjNum);
                Log.d("Added New ThreadId threadIdToObj num ->", msgStr);
            }
        }

        res.setHeader(getExtraHeaderFieldForCancel(), threadId.toString());

        super.doGet(req, res);
    }

    @Override
    protected boolean doChunkTransfer(HttpServletRequest req,
            HttpServletResponse res) {
        byte[] data = new byte[4096];
        int size = -1;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        String uri = req.getRequestURI();
        if (count % 2 == 0) {
            uri = "text1.txt";
        } else {
            uri = "text2.txt";
        }
        InputStream is = getContentStream(uri);
        try {
            while ((size = is.read(data)) != -1) {
                os.write(data, 0, size);
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        try {
            res.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (count < 10) {
            count++;
            return true;
        } else {
            count = 0;
            return false;
        }
    }

}
