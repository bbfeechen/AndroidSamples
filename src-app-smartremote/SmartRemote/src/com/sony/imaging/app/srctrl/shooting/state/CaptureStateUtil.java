package com.sony.imaging.app.srctrl.shooting.state;

import android.util.Log;

import com.sony.imaging.app.base.caution.CautionProcessingFunction;
import com.sony.imaging.app.base.caution.IkeyDispatchEach;
import com.sony.imaging.app.base.common.MediaNotificationManager;
import com.sony.imaging.app.base.menu.IController.NotSupportedException;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DROAutoHDRController;
import com.sony.imaging.app.base.shooting.camera.PictureQualityController;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.fw.ICustomKey;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.shooting.camera.SRCtrlPictureQualityController;
import com.sony.imaging.app.srctrl.util.MediaObserver;
import com.sony.imaging.app.srctrl.util.MediaObserverAggregator;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.servlet.PostviewResourceLoader;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler;
import com.sony.imaging.app.srctrl.webapi.specific.ShootingHandler.ShootingStatus;
import com.sony.imaging.app.srctrl.webapi.specific.ShutterListenerNotifier;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraEx.JpegListener;
import com.sony.scalar.sysutil.PlainCalendar;
import com.sony.scalar.sysutil.TimeUtil;

/**
 * The Base class for capture child state.
 * 
 * @author 0000114112
 *
 */
public class CaptureStateUtil {
    private static final String TAG = CaptureStateUtil.class.getName();
    
    public static final String KEY_NUMOFBURSTPICTURES    = CaptureStateUtil.class.getName() + "_NUMOFBURSTPICTURES";
    public static final String KEY_NUMOFFINISHEDPICTURES = CaptureStateUtil.class.getName() + "_FINISHEDPICTURES";
    public static final String KEY_USEFINALPICTURE       = CaptureStateUtil.class.getName() + "_USEFINALPICTURE";
    static int numOfBurstPictures;
    static int numOfFinishedPictures;
    static boolean useFinalPicture;
    static boolean jpeg_generation_finished;
    public static boolean remote_shooting_mode;
    
    protected ShutterListenerNotifier notifier;
    
    /**
     * 
     * <pre>
     * Extended JpegListener to store Jpeg Images to PostviewResourceLoader.
     * When the planned numbers of pictures are stored,
     * inform Ready message.
     * </pre>
     * 
     */
    private final class JpegListenerEx implements JpegListener{
        private String mediaId;
        public JpegListenerEx(String id) {
            super();
            mediaId = id;
        }
        @Override
        public void onPictureTaken(byte[] jpeg, CameraEx cam) {
            Log.v(TAG, "onPictureTaken in JpegListenerEx");
            jpeg_generation_finished = true;
            
            if(useFinalPicture){
                if(numOfFinishedPictures + 1 == numOfBurstPictures){
                    addToServer(jpeg);
                }
            } else {
                addToServer(jpeg);
            }
            
            numOfFinishedPictures++;
            MediaObserverAggregator mediaObservers = ShootingHandler.getInstance().getMediaObserverAggregator();
            if(null != mediaObservers) {
                mediaObservers.increaseExpectedStoredPictures(mediaId, 1);
            }
            
            Log.v(TAG, "Finished: " + numOfFinishedPictures + ", Full: " + numOfBurstPictures);
            
            if(numOfFinishedPictures == numOfBurstPictures){
                ShootingHandler.getInstance().setShootingStatus(ShootingStatus.DEVELOPING);
            }
        }

        private void addToServer(byte[] jpeg){
            PlainCalendar cal = TimeUtil.getCurrentCalendar();
            String filename_base = SRCtrlConstants.POSTVIEW_FILENAME_PREFIX
                    + String.format("%04d" ,cal.year) + String.format("%02d", cal.month) + String.format("%02d", cal.day)
                    + "_"
                    + String.format("%02d", cal.hour) + String.format("%02d", cal.minute) + String.format("%02d", cal.second)
                    + "_";
            String url = filename_base + numOfFinishedPictures + SRCtrlConstants.POSTVIEW_FILENAME_EXTENTION;
            
            ShootingHandler.getInstance().addToUrlList(SRCtrlConstants.POSTVIEW_DIRECTORY + url);
            
            PostviewResourceLoader.addPicture(jpeg, url);
            Log.v(TAG, url);
        }
    }

    
    public void init(){
        PostviewResourceLoader.initData();

        numOfFinishedPictures = 0;
        numOfBurstPictures = 1;
        useFinalPicture = false;
        jpeg_generation_finished = false;
        ShootingHandler shootingHandler = ShootingHandler.getInstance();
        shootingHandler.clearUrlList();
        shootingHandler.setShootingStatus(ShootingStatus.PROCESSING);

        String mediaId = SRCtrlExecutorCreator.getRecordingMedia();
        ShootingExecutor.setJpegListener(new JpegListenerEx(mediaId));
        
        if(DROAutoHDRController.MENU_ITEM_ID_HDR.equals(
                DROAutoHDRController.getInstance().getValue(DROAutoHDRController.MENU_ITEM_ID_DROHDR))){
            numOfBurstPictures++;
            useFinalPicture = true;
        }
        if(MediaObserverAggregator.isExternalMediaMounted()){
            int remaining = MediaNotificationManager.getInstance().getRemaining();
            if(remaining < numOfBurstPictures)
            {
                numOfBurstPictures = remaining;
            }
        }
        
        MediaObserverAggregator mediaObservers = ShootingHandler.getInstance().getMediaObserverAggregator();
        int initialRecordingCount = 0;
        if(null != mediaObservers) {
            initialRecordingCount = mediaObservers.getInitialContentsCount(mediaId);
        }
        if(MediaObserver.MediaInfo.COUNT_NOT_INITIALIZED == initialRecordingCount) {
            Log.e(TAG, "MediaObserver may not be initialized or target media(ID="+mediaId+") may not be mounted so that initial contents count cannot be obtained.");
            ShootingHandler.getInstance().setShootingStatus(ShootingStatus.FAILED);
        }
    }

    public void setNotifier(ShutterListenerNotifier notifier){
        this.notifier = notifier;
        if(null != notifier) {
            remote_shooting_mode = true;
        } else {
            remote_shooting_mode = false;
        }
    }
    public ShutterListenerNotifier getNotifier()
    {
        return notifier;
    }
    
    public boolean isReadyToTakePicture()
    {
        // Check IMDLAPP5-509 on EYS/EYC/RV
        if(CameraSetting.getPfApiVersion() < 2)
        {
            try
            {
                String quality = SRCtrlPictureQualityController.getInstance().getValue(null);
                if(!MediaNotificationManager.getInstance().isMounted())
                {
                    if(PictureQualityController.PICTURE_QUALITY_RAWJPEG.equals(quality))
                    {
                        Log.v(TAG, "Force canceling taking a RAW-JPEG picture because media isn't mounted");
                        return false;
                    }
                }
            }
            catch (NotSupportedException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    /*
     * Copied from CautionProcessingFunction [IMDLAPP5-509]
     */
    public static class SRCtrlKeyDispatchForDLT06 extends IkeyDispatchEach {
        public SRCtrlKeyDispatchForDLT06(CautionProcessingFunction p) {
            super(p, null);
        }
        
        @Override
        protected String getKeyConvCategory() {
            return ICustomKey.CATEGORY_MENU;
        }

        @Override
        public int pushedCenterKey() {
            CautionProcessingFunction.executeTerminate();
            return HANDLED;
        }

        @Override
        public int pushedS1Key() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int pushedS2Key() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int pushedPlayBackKey() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int pushedMovieRecKey() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int turnedModeDial() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int attachedLens() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }

        @Override
        public int detachedLens() {
            CautionProcessingFunction.executeTerminate();
            return THROUGH;
        }
        
        @Override
        public int releasedS1Key() {
            return THROUGH; // [IMDLAPP5-509]
        }
    }
}
