package com.sony.imaging.app.srctrl.liveview;

import java.nio.ByteBuffer;
import java.util.Arrays;

import android.graphics.ImageFormat;
import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.executor.BaseShootingExecutor;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.base.shooting.camera.executor.ShootingExecutor;
import com.sony.imaging.app.fw.RunStatus;
import com.sony.imaging.app.srctrl.shooting.SRCtrlExecutorCreator;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.servlet.LiveviewChunkTransfer;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.CameraSequence;
import com.sony.scalar.hardware.DeviceBuffer;
import com.sony.scalar.hardware.DeviceMemory;

/**
 * 
 * This class repeatedly get preview frame from scalar infra.
 * Also generate chunked stream packet.
 * @author 0000138134
 *
 */
public class LiveviewLoader {
	//public static final int CAPA = 102400; //100KkB

	private static LiveviewLoader sInstance;
	private static JpegLoader loader = null;
	private static volatile boolean isGenerating = false;
	private static volatile boolean isLoading = false;
	
	//private static final int FRAME_INTERVAL = 70; // msec
    public static final int LIVEVIEW_OBTAINING_INTERVAL = 30;
    private static final int FRAME_INTERVAL = LIVEVIEW_OBTAINING_INTERVAL; // msec
	private static LiveviewChunkTransfer chunkTransfer = null;

	private static final String TAG = LiveviewLoader.class.getSimpleName();
	private static CameraSequence camera_seq;
	
	private static final int[] mQParamList =
        {//サンプル
                0x80010101, // en_iqscaleInit=0x01, en_iqscaleDiff=0x01, en_targetSizeThres=0x01, en_iqscaleLmt=0x80
                0x00020075, // iqscaleMin=0x02, iqscaleMax=0x75
                0x006C5926, // targetSizeMin0Nume=0x6C(85%),  targetSizeMin1Nume=0x59(70%),  targetSizeMin2Nume=0x26(30%)
                0x00798089, // targetSizeMax0Nume=0x79(95%),  targetSizeMin1Nume=0x80(100%), targetSizeMin2Nume=0x89(107%),
                0x00010204, // MinDiff0-2: +1, +2, +4
                0x00FFFEF0, // MaxDiff0-2: -1, -2, -16
                0x00000201, // Retry=2
                0x9C9C9C9C  //
        };

	public class LiveviewData{
		public byte[] headerData;
		public int headerDataSize;
		public byte [] jpegData;
		public int jpegDataAndPaddingSize;
	}
	
    public LiveviewLoader() {
        isGenerating = false;
        isLoading = false;
        loader = new JpegLoader();
    }
    
    private boolean startGeneratingPreview(){
        if(isGeneratingPreview()) {
            Log.v(TAG, "Liveview has been already being generated...");
            return true;
        }
        
        if(RunStatus.RUNNING != RunStatus.getStatus()) {
            Log.e(TAG, "CAMERA STATUS ERROR: Camera is not running (Status="+RunStatus.getStatus()+") at " + Thread.currentThread().getStackTrace()[2].toString());
            return false;
        }
        
        CameraSequence tmp_camera_seq = null;
        for(int i = 0; i < 20; i++) { // retry in at most 2000 msec
            ExecutorCreator creator = SRCtrlExecutorCreator.getInstance();
            if(null == creator) {
                Log.v(TAG, "ExecutorCreator is null.");
            } else {
                BaseShootingExecutor executor = creator.getSequence();
                if(null == executor) {
                    Log.v(TAG, "ShootingExecutor is null.");
                } else {
                    CameraEx cameraEx = executor.getCameraEx();
                    if(null == cameraEx) {
                        Log.v(TAG, "CameraSequence is null.");
                    } else {
                        tmp_camera_seq = CameraSequence.open(cameraEx);
                    }
                }
            }
            
            if(null == tmp_camera_seq) {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // OK
                break;
            }
        }
        
        if(null == tmp_camera_seq) {
            Log.e(TAG, "CameraSequence has not been changed.");
            return false;
        } else {
            camera_seq = tmp_camera_seq;
        }

        if (!setEESize()) {
        	return false;
        }
        
        LiveviewContainer liveview = LiveviewContainer.getInstance();
   		CameraSequence.Options opts = new CameraSequence.Options();
   		opts.setOption(CameraSequence.Options.PREVIEW_FRAME_RATE,  liveview.getFrameRate());
   		opts.setOption(CameraSequence.Options.PREVIEW_FRAME_WIDTH, liveview.getWidth());
   		opts.setOption(CameraSequence.Options.PREVIEW_FRAME_HEIGHT, 0);
   		opts.setOption(CameraSequence.Options.PREVIEW_FRAME_FORMAT, ImageFormat.JPEG);
   		opts.setOption(CameraSequence.Options.PREVIEW_FRAME_MAX_NUM, 1);
   		opts.setOption(CameraSequence.Options.JPEG_COMPRESS_RATE_DENOM, liveview.getCompressRate());
   		opts.setOption(CameraSequence.Options.JPEG_COMPRESS_MAX_SIZE, liveview.getMaxFileSize());
   		if(liveview.getLiveviewSize().equals(LiveviewContainer.s_LARGE_LIVEVIEW_SIZE))
   		{
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_1, mQParamList[0]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_2, mQParamList[1]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_3, mQParamList[2]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_4, mQParamList[3]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_5, mQParamList[4]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_6, mQParamList[5]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_7, mQParamList[6]);
            opts.setOption(CameraSequence.Options.JPEG_COMPRESS_QUALITY_PARAM_8, mQParamList[7]);
   		}
   		
   		try{
   			camera_seq.startPreviewSequence(opts);
   		} catch (RuntimeException e){
   			Log.e(TAG, "Failed to Start Preview Sequence");
   			return false;
   		}

   		setGeneratingPreview(true);
   		return true;
    }
    
    private boolean stopGeneratingPreview(){
    	if(isGenerating){
	    	setGeneratingPreview(false);
	    	try{
	    		camera_seq.stopPreviewSequence();
	            camera_seq.release();
	    	} catch (RuntimeException e){
	    		Log.e(TAG, "Failed to stop Preview Sequence");
	    		return false;
	    	}
		}
    	return true;
    }
    
	private static void setGeneratingPreview(boolean lo) {
		isGenerating = lo;
    	Log.v(TAG, "generating = " + String.valueOf(isGenerating));
    }
	
	public static boolean isGeneratingPreview(){
		return isGenerating;
	}

	public static void setLoadingPreview(boolean lo) {
		if(isLoading != lo){
		    isLoading = lo;
		    ParamsGenerator.updateLiveviewStatus();
			ServerEventHandler.getInstance().onServerStatusChanged();
			Log.v(TAG, "loading = " + String.valueOf(isLoading));
		}
    }
	
	public static boolean isLoadingPreview(){
		return isLoading;
	}
	
	public static void setLiveviewChunkTransferInstance(LiveviewChunkTransfer transfer){
		chunkTransfer = transfer;
	}
	
	public static LiveviewData getLiveviewData() throws IllegalStateException {
		if (isGeneratingPreview() != true) {
			Log.e(TAG, "LiveviewLoader is not generating liveview data.");
			throw new IllegalStateException();
		}
		return loader.getLiveviewData();
	}
    
    public static synchronized boolean startObtainingImages(){
        Log.v(TAG, "Make liveview ready!");
        
        boolean ret = false;
        if(null == sInstance) {
            sInstance = new LiveviewLoader();
        }
        if(null != sInstance) {
            ret = sInstance.startGeneratingPreview();
        }
    	return ret;
    }
    
    public static synchronized boolean stopObtainingImages(){
    	if(isLoading){
	    	setLoadingPreview(false);
		}
        if(null != sInstance) {
            Log.v(TAG, "Make liveview down!");
            sInstance.stopGeneratingPreview();
        }
    	return true;
    }
    
    public static synchronized void clean() {
        LiveviewLoader.stopObtainingImages();
        if(null != sInstance) {
            sInstance.kill();
            sInstance = null;
        }
    }
    
    public void kill(){
    	if (chunkTransfer != null) {
			chunkTransfer.notifyGetScalarInfraIsKilled();
		}
    	loader = null;
    }
    
    private class JpegLoader {
    	private long lastGetJpegTime = 0;
    	
    	private DeviceMemory[] memList;
    	private DeviceBuffer dbuf;
    	private ByteBuffer bbuf;
    	private byte[] jpegData;
    	private int jpegDataSize;
    	private byte[] headerData;
    	private static final int COMMON_HEADER_SIZE = 8;
    	private static final int PAYLOAD_HEADER_SIZE = 128;
    	private static final int HEADER_DATA_SIZE_MAX = COMMON_HEADER_SIZE + PAYLOAD_HEADER_SIZE;
    	private LiveviewData liveviewData;

    	private int getCount = 0;
    	private long getCountStartTime = -1;
    	private long totalSentDataSize = 0;
    	
    	private final String TAG = JpegLoader.class.getName();
    	
    	private int mCAPA;

    	public JpegLoader(){
    	    mCAPA = getCAPA();
    		bbuf = ByteBuffer.allocateDirect(mCAPA);
    		jpegData = new byte[mCAPA];
    		headerData = new byte[HEADER_DATA_SIZE_MAX];

    		liveviewData = new LiveviewData();
    		liveviewData.headerData = headerData;
    		liveviewData.headerDataSize = 8 + 128;
    		liveviewData.jpegData = jpegData;
    		
    		commonHeaderTimeStampBase = System.currentTimeMillis();

    		isLoading = false;
    	}

    	public LiveviewData getLiveviewData() throws IllegalStateException {
    		if(isLoading){
	    		long currentTime = getCurrentTimeAndCheckInterval();
	    		if (currentTime < 0) {
	    			return null;
	    		}
	    		lastGetJpegTime = currentTime;
	
	    		int dataSize = getJpegData(jpegData);
	    		if (dataSize > 0) {
	    			jpegDataSize = dataSize;
	    		} else {
	    			if (jpegDataSize <= 0) {
	    				Log.e(TAG, "Failed to get picture.");
	    				return null;
	    			}
	    		}
	    		
	    		liveviewData.jpegDataAndPaddingSize = makeHeaderAndPadding(currentTime);
	    		totalSentDataSize += jpegDataSize; 
                printInfo(currentTime);
	    		return liveviewData;
    		} else {
    			Log.v(TAG, "JpegLoader is not loading liveview data.");
    			throw new IllegalStateException();
    		}
    	}
    	
    	private long getCurrentTimeAndCheckInterval(){
    		long currentTime = System.currentTimeMillis();
    		long interval = currentTime - lastGetJpegTime + 5;
    		if (interval < FRAME_INTERVAL) {
    			return -1;
    		}
    		return currentTime;
    	}

    	private void printInfo(long currentTime) {
    		if (getCountStartTime < 0) {
    			getCountStartTime = currentTime;
    			getCount = 0;
    		}
            getCount++;
    		long period = currentTime - getCountStartTime;
    		if (period > 5*1000) {
    			long fps = getCount * 1000 / period;
    			long avarageDataSize = totalSentDataSize / 1024 / getCount;
    			Log.v(TAG, "Output is " + String.valueOf(fps) + " [fps] / " + String.valueOf(avarageDataSize) + "[KiB/f]");
    			getCount = 0;
    			totalSentDataSize = 0;
    			getCountStartTime = currentTime;
    		}
    	}
    	
    	private int getJpegData(byte[] dataBuf){
    		try {
    			memList = camera_seq.getPreviewSequenceFrames(1);
    		} catch (RuntimeException e){
    			e.printStackTrace();
    			return -1;
    		}
			if (memList == null) {
				return -1;
			}
			
			dbuf = (DeviceBuffer)memList[0];
			int dataSize = Math.min(dbuf.getSize(), bbuf.capacity());
			if (dataSize > mCAPA) {
				String msgDataSizeOver = "JPEG data size (="+String.valueOf(dataSize)+") is over! (CAP=" + String.valueOf(mCAPA) + " bytes)";
				Log.v(TAG, msgDataSizeOver);
                releaseDeviceMemories(memList);
				return -1;
			}
			bbuf.rewind();
			dbuf.read(bbuf, Math.min(dataSize, bbuf.capacity()), 0);
            releaseDeviceMemories(memList);
			bbuf.get(dataBuf);
			
			return dataSize;
    	}
    	
    	static final int COMMON_HEADER_START_BYTE = 0xff;
    	static final int COMMON_HEADER_PAYLOAD_TYPE = 0x01;
    	int commonHeaderSequenceNumber = 0;
    	long commonHeaderTimeStampBase = 0;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE1 = 0x24;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE2 = 0x35;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE3 = 0x68;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE4 = 0x79;
    	static final int PAYLOAD_HEADER_FLAG = 0x00;
    	
    	private int makeHeaderAndPadding(long currentTime){
    		Arrays.fill(headerData, (byte)0x00);
    		
    		//
    		// Common header
    		//
    		
    		// Start byte
    		headerData[0] = (byte)COMMON_HEADER_START_BYTE;

    		// Payload type
    		headerData[1] = (byte)COMMON_HEADER_PAYLOAD_TYPE;
    		
    		// Sequnece number
    		commonHeaderSequenceNumber++;
    		setNetworkByte(headerData, 2, commonHeaderSequenceNumber, 2);
    		
    		// Timestamp
    		int timeStamp = (int)(System.currentTimeMillis() - commonHeaderTimeStampBase);
    		setNetworkByte(headerData, 4, timeStamp, 4);
    		
    		//
    		// Payload header
    		//
    		
    		// Start code
    		headerData[8 + 0] = (byte)PAYLOAD_HEADER_START_CODE_BYTE1;
    		headerData[8 + 1] = (byte)PAYLOAD_HEADER_START_CODE_BYTE2;
    		headerData[8 + 2] = (byte)PAYLOAD_HEADER_START_CODE_BYTE3;
    		headerData[8 + 3] = (byte)PAYLOAD_HEADER_START_CODE_BYTE4;
    		
    		// Jpeg data size
    		setNetworkByte(headerData, (8 + 4), jpegDataSize, 3);
    		
    		// Padding size
    		int paddingSize = 4 - jpegDataSize % 4;
    		if (paddingSize == 4) {
    			paddingSize = 0;
    		}
    		setNetworkByte(headerData, (8 + 7), paddingSize, 1);
    		
    		// Pixel width & height
            //int pixelWidth = liveviewContainer.getWidth();
            int pixelWidth = 0;
    		setNetworkByte(headerData, (8 + 8), pixelWidth, 2);
    		//int pixelHeight = liveviewContainer.getHeight();
    		int pixelHeight = 0;
    		setNetworkByte(headerData, (8 + 10), pixelHeight, 2);
    		
    		// Flag
    		headerData[8 + 12] = (byte)PAYLOAD_HEADER_FLAG;
    		
    		return jpegDataSize + paddingSize;
    	}
    	
    	private void setNetworkByte(byte[] dst, int dstIndex, int src, int srcSize){
    		int di = dstIndex + srcSize;
    		int s = src;
    		for (int i = 0; i < srcSize; i++) {
    			di--;
    			dst[di] = (byte)(s & 0xff);
    			s = s >>> 8;
    		}
    	}
    	
    	private int getCAPA() {
            LiveviewContainer liveview = LiveviewContainer.getInstance();
            int CAPA = liveview.getMaxFileSize() * 1024;
            return CAPA;
    	}
    }
    
    private static void releaseDeviceMemories(DeviceMemory[] memories) {
        for(DeviceMemory dbuf : memories) {
            dbuf.release();
        }
    }
    
    private boolean setEESize() {
    	if (LiveviewContainer.getInstance().getEESize() != LiveviewContainer.PANEL_EE_SIZE_INVALID) {
    		return true;
    	}
    	
    	if (camera_seq == null) {
   			Log.e(TAG, "CameraSequence is null");
    		return false;
    	}

		CameraSequence.PreviewSequenceFrameInfo finfo = camera_seq.getPreviewSequenceFrameInfo();
		if (finfo != null) {
//			int i = 0;
//			for (byte b : finfo.detailData) {
//	   			Log.v(TAG, String.format("finfo[%d] = 0x%02X",i, b));
//				i++;
//			}
			int x = ((int)(finfo.detailData[161]<<8) & 0xFF00) + (finfo.detailData[160] & 0xFF);
			int y = ((int)(finfo.detailData[163]<<8) & 0xFF00) + (finfo.detailData[162] & 0xFF);
   			Log.v(TAG, "EESize validSize = ("+ x +", " + y + ")");
   			
   			LiveviewContainer.getInstance().setEEWidthSize(x);
   			return true;
		} else {
   			Log.e(TAG, "PreviewSequenceFrameInfo is null");
			return false;
		}
    }
}
