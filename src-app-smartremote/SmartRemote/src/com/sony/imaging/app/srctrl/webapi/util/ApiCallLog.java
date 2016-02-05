package com.sony.imaging.app.srctrl.webapi.util;

import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.sony.imaging.app.srctrl.util.SRCtrlConstants;

public class ApiCallLog
{
    private static final String TAG = ApiCallLog.class.getName();

    private static final Object lockObj = new Object();
    private static boolean locked = false;
    
    private String methodName;
    private long callTime = 0L;
    private long startTime = 0L;
    
    public ApiCallLog()
    {
        callTime = System.currentTimeMillis();
        startTime = callTime;
        methodName = MethodName.getMethodName(1);
        Log.v(TAG, "[CALL:   " + methodName + "] Called.");
    }
    
    public void init()  throws InterruptedException, TimeoutException
    {
        init(true);
    }
    public void init(boolean lock)  throws InterruptedException, TimeoutException
    {
        if (lock)
        {
            synchronized (lockObj)
            {
                if(locked) {
                    lockObj.wait(SRCtrlConstants.RECEIVE_EVENT_TIMEOUT);    // throws
                                                                            // InterruptedException
                    if (locked)
                    {
                        throw new TimeoutException("[CREATE: " + methodName + "] Timed out in "
                                + SRCtrlConstants.RECEIVE_EVENT_TIMEOUT);
                    }
                }
                locked = true;
            }
            startTime = System.currentTimeMillis();
        }
        Log.v(TAG, "[START:  " + methodName + "] Started.");
    }
    
    public void clear()
    {
        synchronized (lockObj)
        {
            locked = false;
            lockObj.notify();
        }
        
        long endTime = System.currentTimeMillis();
        Log.v(TAG, "[END:    " + methodName + "] End: Total=" + (endTime - callTime) + ", Process=" + (endTime - startTime));
    }
    public String getMethodName() {
        return methodName;
    }
}
