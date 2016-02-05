package com.sony.imaging.app.srctrl.webapi.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.util.Postview;
import com.sony.mexi.orb.servlet.OrbDivisionTransfer;

/**
 * 
 * This class provide function to load postview images.
 * 
 * @author 0000138134
 * 
 */
public class PostviewResourceLoader extends OrbDivisionTransfer
{
    private static final String TAG = PostviewResourceLoader.class.getSimpleName();
    
    private static final long serialVersionUID = 1L;
    
    private static ArrayList<Postview> postviews = new ArrayList<Postview>();
    
    private static ThreadLocal<File> mThreadLocalPostview = new ThreadLocal<File>() {
        protected synchronized File initialValue() {
            return null;
        }
    };

    public synchronized static void addPicture(byte[] picture, String url)
    {
        synchronized(postviews) {
            postviews.add(new Postview(picture, url));
        }
    }
    
    public synchronized static void initData()
    {
        synchronized(postviews) {
            postviews.clear();
        }
    }
    
    public String getRootPath()
    {
        return SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW;
    }
    
    protected synchronized InputStream getContentStream(String url)
    {
        Log.v(TAG, "getContentStream: " + url);

        File postviewFile = mThreadLocalPostview.get();
        if (null != postviewFile)
        {
            return getContentStreamOnMemoryCard(postviewFile);
        }

        String renamedUrl = url.replaceFirst(SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW, "");
        ByteArrayInputStream input = null;
        synchronized(postviews) {
            for (Postview postview : postviews)
            {
                if (postview.getUrl().equals(renamedUrl))
                {
                    input = new ByteArrayInputStream(postview.getPicture());
                    break;
                }
            }
        }
        
        return input;
    }
    
    private InputStream getContentStreamOnMemoryCard(File actualFile)
    {
        InputStream in = null;
        try {
            in = new FileInputStream(actualFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return in;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
    {
        try
        {
            String tmpUrl = req.getRequestURI();
            File tmpUrlFile = new File(tmpUrl);
            String url = null;
            try
            {
                url = tmpUrlFile.getCanonicalPath();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                send403(res);
                return;
            }
            if (url.startsWith(SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW_ON_MEMORY))
            {
                // read from a file
                String renamedUrl = url.replaceFirst(SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW_ON_MEMORY, "");
            
                boolean exist = false;
                String[] pictureUrlList = ShootingHandler.getInstance().getUrlArrayOnMemoryResult();
                for(String pictureUrl : pictureUrlList) {
                    pictureUrl = pictureUrl.replaceFirst(SRCtrlConstants.POSTVIEW_DIRECTORY_ON_MEMORY, "");
                    if(renamedUrl.equals(pictureUrl)) {
                        exist = true;
                        break;
                    }
                }
                if(!exist) {
                    // access is forbidden?!
                    Log.e(TAG, "This picture was not taken by this app so that the access is not allowed: " + url);
                    send403(res);
                    return;
                }
            
                File renamedUrlFile = new File(renamedUrl);
                try
                {
                    String renamedUrlPath = renamedUrlFile.getCanonicalPath();
                    String mediaId = null;
                    if (3 < renamedUrlPath.length())
                    { // '/./.'
                        int pos = renamedUrlPath.indexOf(File.separatorChar, 1);
                        if (-1 != pos)
                        {
                            mediaId = renamedUrlPath.substring(1, pos);
                            renamedUrlPath = renamedUrlPath.substring(pos);
                        }
                    }
                
                    if(null != mediaId) {
                        String actualFileName = "/mnt/sdcard" + renamedUrlPath;
                        Log.v(TAG, "File name: " + actualFileName);
                        File actualFile = new File(actualFileName);
                        Log.v(TAG, "File size from a memory card: " + actualFile.length());
                        setTotal((int)actualFile.length());
                        mThreadLocalPostview.set(actualFile); // Set the file name to a thread-local variable
                    }
                    else
                    {
                        Log.e(TAG, "Media Id was null.");
                        send403(res);
                        return;
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    send403(res);
                    return;
                }
            }
            else
            {
                String renamedUrl = url.replaceFirst(SRCtrlConstants.SERVLET_ROOT_PATH_POSTVIEW, "");
                synchronized(postviews) {
                    boolean processed = false;
                    for (Postview postview : postviews)
                    {
                        if (postview.getUrl().equals(renamedUrl))
                        {
                            int len = postview.getPicture().length;
                            setTotal(len);
                            processed = true;
                            Log.v(TAG, "File size on memory: " + len);
                            break;
                        }
                    }
                    if(!processed) {
                        Log.e(TAG, "No postview data...");
                        send403(res);
                        return;
                    }
                }
            }
            
            super.doGet(req, res); // This calls the following doDivisionTransfer()
        }
        finally
        {
            Log.v(TAG, "Forget a thread-local variable.");
            mThreadLocalPostview.remove();
        }
    }
    
    private String getCanonicalUrl(String tmpUrl)
    {
        File tmpUrlFile = new File(tmpUrl);
        String url = null;
        try
        {
            url = tmpUrlFile.getCanonicalPath();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    @Override
    protected boolean doDivisionTransfer(HttpServletRequest req, HttpServletResponse res)
    {
        OutputStream os = null;
        try {
            os = res.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
            cancel();
            return false;
        } catch (IllegalStateException e2) {
            Log.v(TAG, "HttpServletResponse.getOutputStream() IllegalStateException.");
            cancel();
            return false;
        }
        
        String url = getCanonicalUrl(req.getRequestURI());
        if(null == url) {
            cancel();
            Log.v(TAG, "HttpServletRequest has a malformed URI.");
            return false;
        }
        InputStream in = getContentStream(url);
        if(null == in) {
            Log.v(TAG, "Inputstream for \"" + url + "\" is null.");
            cancel();
            return false;
        }

        boolean result = false;
        byte[] buff = new byte[100*1024];
        int count = 0;
        long totalCount = 0;
        long startTime = System.currentTimeMillis(); 
        try
        {
            while(-1 != (count = in.read(buff))) {
                os.write(buff, 0, count);
                os.flush();
                res.flushBuffer();
                totalCount += count;
            }
            Log.v(TAG, "Wrote " + count + "/" + totalCount);
            result = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            cancel();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime-startTime;
        if(0 != elapsedTime) {
            Log.v(TAG, "TRANSFER SPEED: " + totalCount*8*1000/1024/elapsedTime + "[Kibps]");
        }

        try
        {
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
