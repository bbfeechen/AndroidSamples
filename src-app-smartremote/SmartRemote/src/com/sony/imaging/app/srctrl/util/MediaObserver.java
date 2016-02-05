package com.sony.imaging.app.srctrl.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;

import com.sony.scalar.media.AvindexContentInfo;
import com.sony.scalar.provider.AvindexStore;

public class MediaObserver extends ContentObserver
{
    private static final String tag = MediaObserver.class.getName();
    
    public enum MediaType
    {
        UNKNOWN, EXTERNAL, INTERNAL, VIRTUAL
    }
    
    public interface MediaObserverListener
    {
        public void notifyMediaContentsInfoChanged();
    }
    
    public static class MediaInfo
    {
        public String mMediaId;
        public MediaType mMediaType = MediaType.UNKNOWN;
        
        public static final int COUNT_NOT_INITIALIZED = -1;
        public int mInitialRecorderCount = COUNT_NOT_INITIALIZED;
        public int mInitialContentsCount;
        public int mCurrentContentsCount;
        
        public MediaInfo clone()
        {
            MediaInfo info = new MediaInfo();
            synchronized (this)
            {
                info.mMediaId = mMediaId;
                info.mMediaType = mMediaType;
                info.mInitialContentsCount = mInitialContentsCount;
                info.mCurrentContentsCount = mCurrentContentsCount;
                info.mInitialRecorderCount = mInitialRecorderCount;
            }
            return info;
        }
    }
    
    private MediaInfo mMediaInfo = new MediaInfo();
    
    private WeakReference<MediaObserverListener> mAggregatorRef;
    private ContentResolver mContentResolver;
    
    private SparseArray<String> mFolderNameCache = new SparseArray<String>();
    
    public MediaObserver(MediaObserverListener aggregator, ContentResolver contentResolver, Handler handler,
            String mediaId, MediaType mediaType)
    {
        super(handler);
        mAggregatorRef = new WeakReference<MediaObserverListener>(aggregator);
        mContentResolver = contentResolver;
        synchronized (mMediaInfo)
        {
            
            mMediaInfo.mMediaId = mediaId;
            mMediaInfo.mMediaType = mediaType;
            
            int count = getContentsCount();
            mMediaInfo.mInitialContentsCount = count;
            mMediaInfo.mCurrentContentsCount = count;
        }
    }
    
    public boolean isMediaIdEqualsTo(String mediaId)
    {
        synchronized (mMediaInfo)
        {
            return mMediaInfo.mMediaId.equals(mediaId);
        }
    }
    
    public int getRecordedContentsCount()
    {
        synchronized (mMediaInfo)
        {
            return mMediaInfo.mCurrentContentsCount - mMediaInfo.mInitialContentsCount;
        }
    }
    
    public int getCurrentContentsCount()
    {
        synchronized (mMediaInfo)
        {
            return mMediaInfo.mCurrentContentsCount;
        }
    }
    
    public int getInitialContentsCount()
    {
        synchronized (mMediaInfo)
        {
            return mMediaInfo.mInitialContentsCount;
        }
    }
    
    private int getContentsCount()
    {
        int ret = 0;
        synchronized (mMediaInfo)
        {
            Uri uri = AvindexStore.Images.Media.getContentUri(mMediaInfo.mMediaId);
            String[] projection = new String[]
            { AvindexStore.Images.ImageColumns._ID, AvindexStore.Images.ImageColumns.REC_ORDER };
            String selection = AvindexStore.Images.ImageColumns.IMAGE_CONTENT_TYPE + "=?";
            String[] selectionArgs =
            { Integer.toString(AvindexStore.Images.ImageColumns.CONT_TYPE_DCF) };
            String sortOrder = null;
            if (MediaInfo.COUNT_NOT_INITIALIZED == mMediaInfo.mInitialRecorderCount)
            {
                sortOrder = AvindexStore.Images.ImageColumns.REC_ORDER + " DESC";
            }
            Cursor cursor = mContentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
            if (null != cursor)
            {
                ret = cursor.getCount();
                
                if (cursor.moveToFirst())
                {
                    final int COLUMN_INDEX_REC_ORDER = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.REC_ORDER);
                    if (MediaInfo.COUNT_NOT_INITIALIZED == mMediaInfo.mInitialRecorderCount)
                    {
                        if (!cursor.isAfterLast())
                        {
                            mMediaInfo.mInitialRecorderCount = cursor.getInt(COLUMN_INDEX_REC_ORDER);
                            Log.v(tag, "  InitialRecOrderCount: " + mMediaInfo.mInitialRecorderCount);
                        }
                    }
                }
                cursor.close();
            }
        }
        return ret;
    }
    
    private static String getMountPoint(String mediaId, MediaType mediaType)
    {
        if (MediaType.EXTERNAL != mediaType)
        {
            Log.v(tag, "Mount point for Media type(" + mediaType.name() + ") is not defined yet.  Using the "
                    + MediaType.EXTERNAL.name() + " one.");
        }
        return "/mnt/sdcard";
    }
    
    private static String getFolderName(int folderNumber, String mediaId, MediaType mediaType)
    {
        String mountPoint = getMountPoint(mediaId, mediaType);
        final File mountPointDir = new File(mountPoint + "/DCIM");
        
        final String folderNamePrefix = String.format("%03d", folderNumber);
        FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                if (dir.equals(mountPointDir) && name.startsWith(folderNamePrefix))
                {
                    return true;
                }
                return false;
            }
        };
        String[] targetDirs = mountPointDir.list(filter);
        if (0 == targetDirs.length)
        {
            Log.e(tag, "Unexpected error for the system.  Returnning null...");
            return null;
        }
        else
        {
            return targetDirs[0]; // use the first one because only one write
                                  // target can be used by the system.
        }
    }
    
    private static final String sFilePathFormat = "/%s/DCIM/%s/%s%04d%s";
    private static final String sExtJPG = ".JPG";
    private static final String sExtRAW = ".ARW";
    
    public List<String> getRecentImageFileList(int lastCount)
    {
        List<String> result = new ArrayList<String>();
        
        synchronized (mMediaInfo)
        {
            Uri uri = AvindexStore.Images.Media.getContentUri(mMediaInfo.mMediaId);
            String[] projection = new String[]
            { AvindexStore.Images.ImageColumns._ID, AvindexStore.Images.ImageColumns.DATA, AvindexStore.Images.ImageColumns.REC_ORDER,
                    AvindexStore.Images.ImageColumns.DCF_FOLDER_NUMBER,
                    AvindexStore.Images.ImageColumns.DCF_FILE_NUMBER, AvindexStore.Images.ImageColumns.EXIST_JPEG,
                    AvindexStore.Images.ImageColumns.EXIST_RAW };
            String selection = AvindexStore.Images.ImageColumns.IMAGE_CONTENT_TYPE + "=?";
            if (MediaInfo.COUNT_NOT_INITIALIZED != mMediaInfo.mInitialRecorderCount)
            {
                selection += " AND " + AvindexStore.Images.ImageColumns.REC_ORDER + " >?";
            }
            String[] selectionArgs =
            { Integer.toString(AvindexStore.Images.ImageColumns.CONT_TYPE_DCF) };
            if (MediaInfo.COUNT_NOT_INITIALIZED != mMediaInfo.mInitialRecorderCount)
            {
                selectionArgs = new String[]
                { Integer.toString(AvindexStore.Images.ImageColumns.CONT_TYPE_DCF),
                        Integer.toString(mMediaInfo.mInitialRecorderCount) };
            }
            String sortOrder = AvindexStore.Images.ImageColumns.REC_ORDER + " DESC";
            Cursor cursor = mContentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
            if (null != cursor)
            {
                // Log.e(tag, "requested=" + lastCount + ", total=" +
                // cursor.getCount());
                if (cursor.moveToFirst())
                {
                    final int COLUMN_INDEX_DATA = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.DATA);
                    final int COLUMN_INDEX_FOLDER_NUMBER = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.DCF_FOLDER_NUMBER);
                    final int COLUMN_INDEX_FILE_NUMBER = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.DCF_FILE_NUMBER);
                    final int COLUMN_INDEX_EXIST_JPG = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.EXIST_JPEG);
                    final int COLUMN_INDEX_EXIST_RAW = cursor
                            .getColumnIndex(AvindexStore.Images.ImageColumns.EXIST_RAW);
                    int index = 0;
                    while (!cursor.isAfterLast() && index < lastCount)
                    {
                        // for (int i = 0; i < cursor.getColumnCount(); i++)
                        // {
                        // Log.e(tag, "COLUMN(" + i + "):" +
                        // cursor.getColumnName(i)
                        // +
                        // " -> " + cursor.getInt(i));
                        // }
                        String data = cursor.getString(COLUMN_INDEX_DATA);
                        int folderNumber = cursor.getInt(COLUMN_INDEX_FOLDER_NUMBER);
                        int fileNumber = cursor.getInt(COLUMN_INDEX_FILE_NUMBER);
                        short existJPG = cursor.getShort(COLUMN_INDEX_EXIST_JPG);
                        short existRAW = cursor.getShort(COLUMN_INDEX_EXIST_RAW);
                        
                        String folderName = mFolderNameCache.get(folderNumber);
                        if (null == folderName)
                        {
                            folderName = MediaObserver.getFolderName(folderNumber, mMediaInfo.mMediaId,
                                    mMediaInfo.mMediaType);
                            if (null != folderName)
                            {
                                mFolderNameCache.put(folderNumber, folderName);
                            }
                        }
                        
                        if (null == folderName)
                        {
                            Log.e(tag, "Skipping unexpected folder name: " + folderName);
                        }
                        else
                        {
                            AvindexContentInfo avindexInfo = AvindexStore.Images.Media.getImageInfo(data);
                            int COLOR_TYPE = avindexInfo.getAttributeInt(AvindexContentInfo.TAG_COLOR_TYPE, Integer.MIN_VALUE);
                            String filePrefix = "DSC0";
                            if(Integer.MIN_VALUE != COLOR_TYPE && AvindexContentInfo.COLOR_TYPE_ADOBERGB == COLOR_TYPE) {
                                filePrefix = "_DSC";
                                //Log.e("COLOR_SPACE", "ADOBE");
                            }
                            String ext = "";
                            if (0 != existJPG)
                            {
                                ext = sExtJPG;
                            }
                            else if (0 != existRAW)
                            {
                                ext = sExtRAW;
                            }
                            String filePath = String.format(sFilePathFormat, mMediaInfo.mMediaId, folderName,
                                    filePrefix, fileNumber, ext);
                            result.add(filePath);
                        }
                        
                        index++;
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
            else
            {
                // error
                return null;
            }
        }
        return result;
    }
    
    public MediaInfo getMediaInfo()
    {
        synchronized (mMediaInfo)
        {
            return mMediaInfo.clone();
        }
    }
    
    @Override
    public boolean deliverSelfNotifications()
    {
        // super.deliverSelfNotifications();
        return true;
    }
    
    @Override
    public void onChange(boolean selfChange)
    {
        super.onChange(selfChange);
        Log.v(tag, "MediaObserver#onChange(" + selfChange + ")");
        
        synchronized (mMediaInfo)
        {
            mMediaInfo.mCurrentContentsCount = getContentsCount();
        }
        MediaObserverListener listener = mAggregatorRef.get();
        if (null != listener)
        {
            listener.notifyMediaContentsInfoChanged();
        }
    }
}
