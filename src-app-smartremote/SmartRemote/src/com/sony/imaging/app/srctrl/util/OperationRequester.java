package com.sony.imaging.app.srctrl.util;

import java.util.List;

import android.os.Message;

public class OperationRequester<T>
{
    protected Object mSync = new Object();
    protected Object mResult;
    
    public OperationRequester()
    {
    }
    
    /**
     * This class is supposed to be called by requestXxx according to the return
     * value.
     * 
     * @param requestID
     * @param params
     * @return Original result.
     */
    @SuppressWarnings("unchecked")
    public T request(int requestID, Object... params)
    {
        OperationReceiver rcv = StateController.getInstance().getReceiver();
        if (null == rcv || false == rcv.isAlive())
        {
            throw new RuntimeException("Receiver is already terminated");
        }
        Object[] data = new Object[2];
        data[0] = this;
        data[1] = params;
        Message msg = rcv.mHandler.obtainMessage(requestID, data);
        synchronized (mSync)
        {
            rcv.mHandler.sendMessage(msg);
            try
            {
                mSync.wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        try
        {
            return (T) mResult; // TODO to avoid this type-cast,
                                // OperationReceiver should be expressed by
                                // using generics, too.
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean[] copy(List<Boolean> src)
    {
        if (null == src)
        {
            return null;
        }
        
        int size = src.size();
        boolean[] ret = new boolean[size];
        for(int i = 0; i < size; i++)
        {
            ret[i] = src.get(i);
        }
        return ret;
    }
    
    public static boolean[] copy(Boolean[] src)
    {
        if (null == src)
        {
            return null;
        }
        
        boolean[] ret = new boolean[src.length];
        for(int i = 0; i < src.length; i++)
        {
            ret[i] = src[i];
        }
        return ret;
    }
}
