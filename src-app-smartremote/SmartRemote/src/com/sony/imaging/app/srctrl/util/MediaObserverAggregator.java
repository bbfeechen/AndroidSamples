package com.sony.imaging.app.srctrl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sony.imaging.app.base.common.MediaNotificationManager;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.shooting.camera.SRCtrlPictureQualityController;
import com.sony.imaging.app.srctrl.util.MediaObserver.MediaObserverListener;
import com.sony.imaging.app.srctrl.util.MediaObserver.MediaType;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.provider.AvindexStore;

public class MediaObserverAggregator implements MediaObserverListener, NotificationListener
{
    private static final String tag = MediaObserverAggregator.class.getName();
    private boolean mRegistered = false;
    
    private Handler mHandler;
    private ContentResolver mContentResolver;
    private List<MediaObserver> mObserverList = new ArrayList<MediaObserver>();
    
    private HashMap<String, Integer> mAccumulatedStoredContentsPerMedia = new HashMap<String, Integer>();
    
    private boolean mIsMounted = false;
    
    @Override
    protected synchronized void finalize() throws Throwable
    {
        super.finalize();
        stop(); // ensure unregistration.
    }
    
    public synchronized boolean start(Context context, Handler handler)
    {
        if (mRegistered)
        {
            Log.v(tag, "Already registered.");
            return false;
        }
        
        Context appContext = context.getApplicationContext();
        mContentResolver = appContext.getContentResolver();
        mHandler = handler;
        
        // Skip registering observers for external media
        // because registering will be done corresponding to mount notification
        // from MediaNotification Manager.
        // String externalMediaIds[] = AvindexStore.getExternalMediaIds();
        // registerObserversToAllMedia(externalMediaIds,
        // MediaObserver.MediaType.EXTERNAL);
        
        String internalMediaIds[] = AvindexStore.getInternalMediaIds();
        registerObserversToAllMedia(internalMediaIds, MediaObserver.MediaType.INTERNAL);
        
        String virtualMediaIds[] = AvindexStore.getVirtualMediaIds();
        registerObserversToAllMedia(virtualMediaIds, MediaObserver.MediaType.VIRTUAL);
        
        MediaNotificationManager.getInstance().setNotificationListener(this);
        
        mRegistered = true;
        return mRegistered;
    }
    
    public synchronized void stop()
    {
        MediaNotificationManager.getInstance().removeNotificationListener(this);
        unregisterObserversFromAllMedia();
        mAccumulatedStoredContentsPerMedia.clear();
        
        mHandler = null;
        mContentResolver = null; // ensure releasing content resolver and
                                 // the context.
        // wake up all waiting thread;
        notifyAll();
        
        mRegistered = false;
    }
    
    private synchronized boolean registerObserversToAllMedia(String mediaIds[], MediaObserver.MediaType mediaType)
    {
        if (null == mediaIds || 0 == mediaIds.length)
        {
            return true; // ignore
        }
        
        for (String mediaId : mediaIds)
        {
            registerObserver(mediaId, mediaType);
        }
        
        return true;
    }
    
    private synchronized MediaObserver registerObserver(String mediaId, MediaObserver.MediaType mediaType)
    {
        MediaObserver observer = new MediaObserver(this, mContentResolver, mHandler, mediaId, mediaType);
        mContentResolver.registerContentObserver(AvindexStore.Images.Media.getContentUri(mediaId), true, observer);
        mObserverList.add(observer);
        
        MediaObserver.MediaInfo media_info = observer.getMediaInfo();
        Log.v(tag, "New observer added: " + "\n" + "  Media ID:             " + media_info.mMediaId + "\n"
                + "  Media Type:           " + media_info.mMediaType.name() + "\n" + "  CurrentContentsCount: "
                + media_info.mCurrentContentsCount + "\n" + "  InitialContentsCount: "
                + media_info.mInitialContentsCount + "\n" + "  InitialRecorderCount: "
                + media_info.mInitialRecorderCount + "\n" + "  Expected Pics Count:  "
                + mAccumulatedStoredContentsPerMedia.get(mediaId));
        
        return observer;
    }
    
    private synchronized void unregisterObserversFromAllMedia()
    {
        // Avoiding concurrent access to mObserverList.
        MediaObserver[] observers = mObserverList.toArray(new MediaObserver[0]);
        for (MediaObserver observer : observers)
        {
            unregisterObserver(observer);
        }
    }
    
    private synchronized void unregisterObserver(MediaObserver observer)
    {
        mObserverList.remove(observer);
        mContentResolver.unregisterContentObserver(observer);
    }
    
    @Override
    public synchronized void notifyMediaContentsInfoChanged()
    {
        notifyAll();
    }
    
    public synchronized int getRecordedContentsCount()
    {
        int total = 0;
        for (MediaObserver o : mObserverList)
        {
            int count = o.getRecordedContentsCount();
            if (MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED == count)
            {
                // error
                return MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED;
            }
            total += count;
        }
        return total;
    }
    
    public synchronized int getRecordedContentsCount(String mediaId)
    {
        MediaObserver observer = getMediaObserver(mediaId);
        if (null == observer)
        {
            // error
            return MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED;
        }
        else
        {
            return observer.getRecordedContentsCount();
        }
    }
    
    public synchronized int getInitialContentsCount(String mediaId)
    {
        MediaObserver observer = getMediaObserver(mediaId);
        if (null == observer)
        {
            // error
            return MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED;
        }
        else
        {
            return observer.getInitialContentsCount();
        }
    }
    
    public synchronized int getCurrentContentsCount(String mediaId)
    {
        MediaObserver observer = getMediaObserver(mediaId);
        if (null == observer)
        {
            // error
            return MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED;
        }
        else
        {
            return observer.getCurrentContentsCount();
        }
    }
    
    public synchronized List<String> getImageFileList(String mediaId, int lastCount)
    {
        MediaObserver observer = getMediaObserver(mediaId);
        if (null != observer)
        {
            return observer.getRecentImageFileList(lastCount);
        }
        return null;
    }
    
    private synchronized MediaObserver getMediaObserver(String mediaId)
    {
        MediaObserver observer = null;
        for (MediaObserver o : mObserverList)
        {
            if (o.isMediaIdEqualsTo(mediaId))
            {
                observer = o;
                break;
            }
        }
        return observer;
    }
    
    public void invokeFlushingMediaDatabase(String mediaId)
    {
        AvindexStore.loadMedia(mediaId, AvindexStore.CONTENT_TYPE_LOAD_STILL);
        AvindexStore.Images.waitAndUpdateDatabase(mContentResolver, mediaId);
        AvindexStore.waitLoadMediaComplete(mediaId);
    }
    
    public void increaseExpectedStoredPictures(String mediaId, int num)
    {
        synchronized (mAccumulatedStoredContentsPerMedia)
        {
            Integer numOfAccumulatedTotalStoredContents = mAccumulatedStoredContentsPerMedia.remove(mediaId);
            if (null == numOfAccumulatedTotalStoredContents)
            {
                numOfAccumulatedTotalStoredContents = num;
            }
            else
            {
                numOfAccumulatedTotalStoredContents = numOfAccumulatedTotalStoredContents.intValue() + num;
            }
            mAccumulatedStoredContentsPerMedia.put(mediaId, numOfAccumulatedTotalStoredContents);
        }
    }
    
    public void decreaseExpectedStoredPictures(String mediaId, int num)
    {
        synchronized (mAccumulatedStoredContentsPerMedia)
        {
            Integer numOfAccumulatedTotalStoredContents = mAccumulatedStoredContentsPerMedia.remove(mediaId);
            if (null == numOfAccumulatedTotalStoredContents)
            {
                numOfAccumulatedTotalStoredContents = 0;
            }
            else
            {
                if (numOfAccumulatedTotalStoredContents.intValue() - num < 0)
                {
                    numOfAccumulatedTotalStoredContents = 0;
                }
                else
                {
                    numOfAccumulatedTotalStoredContents = numOfAccumulatedTotalStoredContents.intValue() - num;
                }
            }
            mAccumulatedStoredContentsPerMedia.put(mediaId, numOfAccumulatedTotalStoredContents);
        }
    }
    
    public synchronized int getExpectedStoredPictures(String mediaId)
    {
        synchronized (mAccumulatedStoredContentsPerMedia)
        {
            Integer numOfAccumulatedTotalStoredContents = mAccumulatedStoredContentsPerMedia.get(mediaId);
            if (null == numOfAccumulatedTotalStoredContents)
            {
                return 0;
            }
            else
            {
                return numOfAccumulatedTotalStoredContents.intValue();
            }
        }
    }
    
    public synchronized MediaObserver.MediaType getMediaType(String mediaId)
    {
        MediaObserver observer = getMediaObserver(mediaId);
        if (null == observer)
        {
            return MediaType.UNKNOWN;
        }
        return observer.getMediaInfo().mMediaType;
    }
    
    public static boolean isExternalMediaMounted()
    {
        String mediaId = SRCtrlExecutorCreator.getRecordingMedia();
        if(null == mediaId)
        {
            return false;
        }
        
        MediaObserverAggregator mediaObservers = ShootingHandler.getInstance().getMediaObserverAggregator();
        if(null != mediaObservers)
        {
            MediaType mediaType = mediaObservers.getMediaType(mediaId);
            if (MediaType.EXTERNAL == mediaType)
            {
                return true;
            }
        }
        return false;
    }
    
    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Following section is related to MediaNotificationManager.
    //
    // BEGINNING of MediaNotificationManager related
    private static final String[] tags =
    { MediaNotificationManager.TAG_MEDIA_STATUS_CHANGE, };
    
    private synchronized void resetMediaObserversForExternalMedia()
    {
        Log.v(tag, "Resetting MediaObservers for external media");
        String externalMediaIds[] = AvindexStore.getExternalMediaIds();
        for (String mediaId : externalMediaIds)
        {
            MediaObserver observer = getMediaObserver(mediaId);
            if (null != observer)
            {
                unregisterObserver(observer);
            }
            mAccumulatedStoredContentsPerMedia.remove(mediaId);
            MediaObserver new_observer = registerObserver(mediaId, MediaObserver.MediaType.EXTERNAL);
        }
    }
    
    private synchronized void removeMediaObserversForExternalMedia() {
        Log.v(tag, "Removing MediaObservers for external media");
        String externalMediaIds[] = AvindexStore.getExternalMediaIds();
        for (String mediaId : externalMediaIds)
        {
            MediaObserver observer = getMediaObserver(mediaId);
            if (null != observer)
            {
                unregisterObserver(observer);
            }
        }
        // wake up all waiting thread;
        notifyAll();
    }
    
    @Override
    public String[] getTags()
    {
        return tags;
    }
    
    @Override
    public void onNotify(String tag)
    {
        int state = MediaNotificationManager.getInstance().getMediaState();
        if (MediaNotificationManager.MEDIA_STATE_MOUNTED == state)
        {
            // External media is mounted newly.
            // Reset the contents information.
            Log.v(tag, "External media was mounted, resetting information of all media");
            resetMediaObserversForExternalMedia();
            ((SRCtrlPictureQualityController)SRCtrlPictureQualityController.getInstance()).setMounted(true);
        }
        else if (MediaNotificationManager.MEDIA_STATE_NOCARD == state)
        {
            Log.v(tag, "External media was not mounted, removing the observer");
            removeMediaObserversForExternalMedia();
            ((SRCtrlPictureQualityController)SRCtrlPictureQualityController.getInstance()).setMounted(false);
        }
        
        boolean changed = ParamsGenerator.updatePostviewImageSize();
        if(changed) {
            ServerEventHandler.getInstance().onServerStatusChanged();
        }
    }
    // END of MediaNotificationManager related
    // /////////////////////////////////////////////////////////////////////////////////////////////////////
}
