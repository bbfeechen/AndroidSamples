package ezremote.client.liveview;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.System;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ezremote.client.callback.LoadingListener;
import ezremote.client.data.ImageContainer;
import ezremote.client.R;

public class Liveview extends SurfaceView implements SurfaceHolder.Callback, LoadingListener{
	private static final boolean ENABLE_CLIENT_REPORT = false;
	private static final boolean ENABLE_SKIP_OBSOLETED_PICTURE = true;
	private static final boolean ENABLE_AUTO_RECONNECT = true;
	private static final boolean ENABLE_TRACE_VIEW = false;

	private static Loader loader = null;
	private static Drawer drawer = null;
	private static Reporter reporter = null;
	private static LiveviewListener listener = null;
	private static volatile boolean drawing = false;

	public static final int CAPA = 102400; // 100kB

	private int reconnectCount = 0;
    private static final int MAX_RECONNECT = 3;

	private static final int DRAW_WAIT = 100;
	private static final int DRAW_DELAY = 10;
	protected SurfaceHolder surfaceHolder = null;
	private Paint paint = null;
	private BitmapFactory.Options bmfOptions = null;
	private boolean canReuseBitmap = false;

	private static final int REPORT_WAIT = 10; // sec
	private static final int REPORT_INTERVAL = 5; // sec
	private long startWaitingDataTime = -1;

	private static final String TAG = "Liveview";
	
	private float scale = 0;
	private boolean isInitialized = false;
	//private static int position = 0;
	
	public Liveview(Context context) {
		super(context);
		constructorCommon(context);
	}

    public Liveview(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructorCommon(context);
    }

    public Liveview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        constructorCommon(context);
    }

    private void constructorCommon(Context context){
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();

        canReuseBitmap = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        
        bmfOptions = new BitmapFactory.Options();
        bmfOptions.inSampleSize = 1;
        if (canReuseBitmap == true) {
        	bmfOptions.inBitmap = null; // Android 3.x or later.
        	bmfOptions.inMutable = true; // Android 3.x or later.
        }

        loader = new Loader();
        drawer = new Drawer();
        reporter = new Reporter(context);
    }
    
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = holder.lockCanvas();
		Paint paint = new Paint();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clock64);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		holder.unlockCanvasAndPost(canvas);

		loader.start();
		drawer.start();
		reporter.start();
		
		isInitialized = false;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		pause();
		loader.shutdown();
		drawer.shutdown();
		reporter.shutdown();
	}

	@Override
	public void onLoadFinished(InputStream in) {
	}

	public static void setListener(LiveviewListener liveviewListener) {
		listener = liveviewListener;
	}

	public static void pause() {
		Log.i(TAG, "pause");
		if (!loader.isStopped()) {
			loader.stop();
		}
		if (!drawer.isStopped()) {
			drawer.stop();
		}
		if (!reporter.isStopped()) {
			reporter.stop();
        }
	}
	
	public static void resume() {
		Log.i(TAG, "resume");
		if (loader.isStopped()) {
			loader.start();
		}
		if (drawer.isStopped()) {
			drawer.start();
		}
		if (reporter.isStopped()) {
			reporter.start();
		}
	}

	private class JpegPicture {
		public byte[] data;
		public int size;
	}

	private void notifyNetworkEvent(){
		if (listener != null) {
			Log.i(TAG, "Notify network event.");
			listener.notifyLiveviewNetworkEvent();
		}
	}
	
    private class Loader {
    	private static final boolean USE_RAW_TCP = false;
    	private static final String SERVER_ADDRS = "192.168.122.1";
    	private static final int HTTP_PORT = 8080;
    	private static final int TCP_PORT = 43576;
    	private static final int SOCKET_RECEIVE_BUFFER_SIZE = CAPA * 2;
    	private Socket sock = null;
    	private InputStream is = null;
    	
    	private class PayloadHeader{
    		public int jpegDataSize;
    		public int paddingSize;
    	}
    	private byte[] headerData = null;
    	private static final int COMMON_HEADER_SIZE = 8;
    	private static final int PAYLOAD_HEADER_SIZE = 128;
    	private static final int HEADER_SIZE = COMMON_HEADER_SIZE + PAYLOAD_HEADER_SIZE;
    	private PayloadHeader payloadHeader = null;
    	private JpegPicture jpegPicture = null;
    	
    	private final String TAG = "Liveview.Loader";

    	public Loader(){
    		headerData = new byte[HEADER_SIZE];

    		payloadHeader = new PayloadHeader();
    		payloadHeader.jpegDataSize = -1;
    		payloadHeader.paddingSize = -1;

    		jpegPicture = new JpegPicture();
    		jpegPicture.data = new byte[CAPA];
    		jpegPicture.size = -1;
    	}

    	public void start(){
    		// Connect server.
     		try {
        		InetSocketAddress serverAddress = null;
    			if (USE_RAW_TCP == true) {
    				serverAddress = new InetSocketAddress(InetAddress.getByName(SERVER_ADDRS), TCP_PORT);
    			} else {
    				serverAddress = new InetSocketAddress(InetAddress.getByName(SERVER_ADDRS), HTTP_PORT);
    			}
         		sock = new Socket();
     			sock.setReceiveBufferSize(SOCKET_RECEIVE_BUFFER_SIZE);
				//sock.setKeepAlive(true);
        		sock.connect(serverAddress);
    		} catch (UnknownHostException e1) {
    			e1.printStackTrace();
    			Log.i(TAG, "InetSocketAddress ,UnknownHostException");
    			notifyNetworkEvent();
    			return;
     		} catch (SocketException e2) {
     			e2.printStackTrace();
     			Log.i(TAG, "Socket.setReceiveBuffersize(" 
     					+ String.valueOf(SOCKET_RECEIVE_BUFFER_SIZE) + "), SocketException.");
     			notifyNetworkEvent();
     			return;
     		} catch (IOException e3) {
     			e3.printStackTrace();
     			Log.i(TAG, "Socket.connect(), IOException.");
     			notifyNetworkEvent();
     			return;
     		}

    		if (ENABLE_TRACE_VIEW == true) {
    			Debug.startMethodTracing("patom");
    		}
    		
    		// Get InputStream.
    		if (USE_RAW_TCP == true) {
				try {
					is = new BufferedInputStream(sock.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
					Log.i(TAG, "Socket.getInputStream(), IOException");
					notifyNetworkEvent();
					return;
				}
			} else {
				try {
					PrintStream ps = new PrintStream(sock.getOutputStream());
					//ps.println("GET /liveview/liveview.jpg HTTP/1.1");
					//ps.println("Accept: image/gif, image/jpeg, */*");
					//ps.println("Accept-Encoding: gzip, deflate");
					//ps.println("Host: LiveivewClient"); // TODO
					//ps.println("Host: 192.168.122.1:8080");
					//ps.println("Connection: Keep-Alive");
					//ps.println("");
					ps.printf("%s\r\n", "GET /liveview/liveview.jpg HTTP/1.1");
					ps.printf("%s\r\n", "Accept: image/gif, image/jpeg, */*");
					ps.printf("%s\r\n", "Accept-Encoding: gzip, deflate");
					ps.printf("%s\r\n", "Host: 192.168.122.1:8080");
					ps.printf("%s\r\n", "Connection: Keep-Alive");
					ps.printf("%s\r\n", "");
					ps.flush();
					Log.i(TAG, "Send HTTP GET");
				} catch (IOException e) {
					e.printStackTrace();
					Log.i(TAG, "Send HTTP GET, IOException");
					notifyNetworkEvent();
					return;
				}

				try {
					is = new UnchunkedInputStream(sock.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
					Log.i(TAG, "Socket.getInputStream(), IOException.");
					notifyNetworkEvent();
					return;
				}
			}
    		
    		payloadHeader.jpegDataSize = -1;
			drawing = true;
    	}

    	public void stop(){
    		if (drawing == true) {
    			drawing = false;

        		if (ENABLE_TRACE_VIEW == true) {
        			Debug.stopMethodTracing();
        		}

        		try {
        			Log.i(TAG, "Close InputStream.");
        			is.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		is = null;

        		try {
        			Log.i(TAG, "Close Socket.");
        			sock.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		sock = null;
    		}
    	}

    	public void shutdown(){
    	}

    	public boolean isStopped(){
    		return !drawing;
    	}
    	
    	public boolean isDisconnected() {
    		if (is instanceof UnchunkedInputStream) {
    			UnchunkedInputStream uis = (UnchunkedInputStream)is;
    			return uis.isEof();
    		}
    		return false;
    	}

    	public JpegPicture getJpegPicture(){
    		if (is == null) {
    			return null;
    		}
    		
    		try {
    			if (payloadHeader.jpegDataSize < 0) {
        			int availHeader = is.available();
    				if (availHeader < HEADER_SIZE) {
    					return null;
    				}
    				int readHeaderSize = is.read(headerData, 0, HEADER_SIZE);
    				if (readHeaderSize != HEADER_SIZE) {
        				Log.i(TAG, "Header readSize = " + String.valueOf(readHeaderSize));
        				pause();
        				notifyNetworkEvent();
        				return null;
        			}
    				if (parseHeader(payloadHeader, headerData) == false) {
    					Log.e(TAG, "Failed to parse common/payload header.");
    					
    					for (int i = 0; i < 32; i++) {
    						Log.i(TAG, "headerData[" + String.valueOf(i) + "] = " + String.valueOf(headerData[i]));
    					}

    					pause();
    					notifyNetworkEvent();
    					return null;
    				}
    			}

    			int availData = is.available();
    			int payloadDataSize = payloadHeader.jpegDataSize + payloadHeader.paddingSize;
    			if (availData < payloadDataSize) {
    				return null;
    			}
    			int readPayloadSize = is.read(jpegPicture.data, 0, payloadDataSize);
    			if (readPayloadSize != payloadDataSize) {
    				Log.i(TAG, "payloadDataSize = " + String.valueOf(payloadDataSize)
    							+ " readSize = " + String.valueOf(readPayloadSize));
    				pause();
    				notifyNetworkEvent();
    				return null;
    			}
        		//Log.i(TAG, "Read JPEG data, available = " + String.valueOf(is.available()));
    		} catch (IOException e) {
    			e.printStackTrace();
    			Log.i(TAG, "getJpegPicture() IOException");
    			pause();
    			notifyNetworkEvent();
    			return null;
    		}

    		jpegPicture.size = payloadHeader.jpegDataSize;
    		payloadHeader.jpegDataSize = -1;
    		
    		return jpegPicture;
    	}
    	
    	static final int COMMON_HEADER_START_BYTE = 0xff;
    	static final int COMMON_HEADER_PAYLOAD_TYPE = 0x01;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE1 = 0x24;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE2 = 0x35;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE3 = 0x68;
    	static final int PAYLOAD_HEADER_START_CODE_BYTE4 = 0x79;
    	static final int PAYLOAD_HEADER_FLAG = 0x00;
    	int lastSequenceNumber = -1;
    	private boolean parseHeader(PayloadHeader result, byte[] header){
    		result.jpegDataSize = -1;
    		result.paddingSize = -1;

    		//
    		// Common header
    		//
    		
    		// Start byte
    		if (header[0] != (byte)COMMON_HEADER_START_BYTE) {
    			Log.i(TAG, "Common header, illegal start byte.");
    			return false;
    		}
    		
    		// Payload type
    		if (header[1] != (byte)COMMON_HEADER_PAYLOAD_TYPE) {
    			Log.i(TAG, "Common header, illegal payload type.");
    			return false;
    		}

    		// Sequence number
    		int sequenceNumberH = (header[2] << 8) & 0x0000ff00;
    		int sequenceNumberL = header[3]        & 0x000000ff;
    		int sequenceNumber = sequenceNumberH + sequenceNumberL;
    		if (lastSequenceNumber < 0) {
    			Log.i(TAG, "Sequence number starts from " + String.valueOf(sequenceNumber));
    		} else if (sequenceNumber != lastSequenceNumber + 1) {
    			String msgSeqNum = "Sequence number = " + String.valueOf(sequenceNumber) + 
    								", previous = " + String.valueOf(lastSequenceNumber);
    			Log.i(TAG, msgSeqNum);
    		}
    		lastSequenceNumber = sequenceNumber;

    		/*
    		// Timestamp
    		int timeStamp0 = (header[4] << 24) & 0xff000000;
    		int timeStamp1 = (header[5] << 16) & 0x00ff0000;
    		int timeStamp2 = (header[6] <<  8) & 0x0000ff00;
    		int timeStamp3 = header[7] & 0x000000ff;
    		int timeStamp = timeStamp0 + timeStamp1 + timeStamp2 + timeStamp3;
    		Log.i(TAG, "Common header: Timestamp = " + String.valueOf(timeStamp));
    		*/

    		//
    		// Payload Header
    		//
    		
    		// Start code
    		if (header[8 + 0] != (byte)PAYLOAD_HEADER_START_CODE_BYTE1
    				|| header[8 + 1] != (byte)PAYLOAD_HEADER_START_CODE_BYTE2
    				|| header[8 + 2] != (byte)PAYLOAD_HEADER_START_CODE_BYTE3
    				|| header[8 + 3] != (byte)PAYLOAD_HEADER_START_CODE_BYTE4) {
    			Log.i(TAG, "Payload header, illegal start code.");
    			return false;
    		}
    		
    		// Flag
    		if (header[8 + 12] != (byte)PAYLOAD_HEADER_FLAG) {
    			Log.i(TAG, "Payload header, illegal flag.");
    			return false;
    		}
    		
    		// Jpeg data size
    		int jpegDataSizeH = (header[8 + 4] << 16) & 0x00ff0000;
    		int jpegDataSizeM = (header[8 + 5] << 8)  & 0x0000ff00;
    		int jpegDataSizeL = header[8 + 6]         & 0x000000ff;
    		result.jpegDataSize = jpegDataSizeH + jpegDataSizeM + jpegDataSizeL;

    		// Padding size
    		result.paddingSize = header[8 + 7];

    		/*
    		// Pixel width & height
    		int pixelWidthH = (header[8 + 8] << 8) & 0x0000ff00;
    		int pixelWidthL = header[8 + 9] & 0x000000ff;
    		int pixelHeightH = (header[8 + 10] << 8) & 0x0000ff00;
    		int pixelHeightL = header[8 + 11] & 0x000000ff;
    		int pixelWidth = pixelWidthH + pixelWidthL;
    		int pixelHeight = pixelHeightH + pixelHeightL;
    		Log.i(TAG, "Payload header: pixel width = " + String.valueOf(pixelWidth)
    					+ " height = " + String.valueOf(pixelHeight));

    		// Flag
    		int Flag = header[8 + 12];
    		Log.i(TAG, "Payload header: Flag = " + String.valueOf(Flag));
    		*/

    		return true;
    	}
    }

    private class Drawer {
    	private ScheduledExecutorService scheduler;
    	private Timer timerTask;
    	private ScheduledFuture<?> future;

    	public Drawer(){
    		timerTask = new Timer();
    		scheduler = Executors.newSingleThreadScheduledExecutor();
    	}

    	public void start(){
    		startWaitingDataTime = -1;
    		future = scheduler.scheduleWithFixedDelay(timerTask, DRAW_WAIT, DRAW_DELAY,TimeUnit.MILLISECONDS);
    	}

    	public void stop(){
    		if(future != null){
    			future.cancel(true);
    		}
    	}

    	public void shutdown(){
    		scheduler.shutdown();
    	}

    	public boolean isStopped(){
    		return future.isCancelled();
    	}

    	private class Timer implements Runnable{
    		private Handler handler = new Handler();
    		@Override
    		public void run(){
    			handler.post(new Runnable(){
    				@Override
    				public void run(){
    					if (drawing) {
    						doDraw();
     					}
    				}
    			});
    		}
    	}
    }

	private synchronized void doDraw(){
		long startTime = System.currentTimeMillis();
		if (startWaitingDataTime < 0) {
			startWaitingDataTime = startTime;
		}
		long endWaitingDataTime = startTime;

		if (loader.isDisconnected() == true) {
			if (ENABLE_AUTO_RECONNECT == true) {
				if (reconnectCount < MAX_RECONNECT) {
					Log.i(TAG, "Disconnected by server, try reconnect.");
					reconnect();
					reconnectCount++;
					return;
				}
			}
			Log.i(TAG, "Disconnected by server.");
			pause();
			notifyNetworkEvent();
			return;
		}
		
		// Read JPEG picture data.
		JpegPicture jpegPicture = loader.getJpegPicture();
		if (jpegPicture == null) {
			return;
		}
		//Log.i(TAG, "Get JPEG Picture");

		long waitingDataTime = endWaitingDataTime - startWaitingDataTime;
		if (waitingDataTime > 200) {
			Log.i(TAG, "Receiving JPEG data took " + String.valueOf(waitingDataTime) + "[msec]");
		}
		
		// If next picture data is available, skip current picture.
		if (ENABLE_SKIP_OBSOLETED_PICTURE == true) {
			JpegPicture nextPicture;
			do {
				nextPicture = loader.getJpegPicture();
				if (nextPicture != null) {
					jpegPicture = nextPicture;
					reporter.setSkippedFrameInfo(startTime);
					//Log.i(TAG, "Skip JPEG Picture");
				}
			} while (nextPicture != null);
		}

		// Decode JPEG picture data.
		Bitmap bmp = BitmapFactory.decodeByteArray(jpegPicture.data,  0, jpegPicture.size, bmfOptions);
		if (bmp == null) {
			Log.e(TAG, "Decoding JPEG failed. size = " + String.valueOf(jpegPicture.size));
			return;
		}
		if (canReuseBitmap == true) {
			bmfOptions.inBitmap = bmp; // Android 3.x or later
		}

		reconnectCount = 0;

		// Draw picture.
		Canvas canvas = surfaceHolder.lockCanvas();
		try{
			if(!isInitialized){
				scale = ImageContainer.getInstance().getLiveviewScale(bmp.getWidth(), bmp.getHeight());
				//position = ImageContainer.getInstance().getLiveviewPosition();
				isInitialized = true;
			}
			canvas.scale(scale, scale);
			//canvas.scale(2, 2);//QVGA->VGA
			//canvas.scale(-1, 1, 320, 0); // Reverse Left<=>Right
			canvas.drawBitmap(bmp, 0, 0, paint);
		}catch(NullPointerException e){
			Log.e(TAG, "canvas.drawBitmap NullPointerException");
		}
		surfaceHolder.unlockCanvasAndPost(canvas);

		// Set time info of this picture.
		long endTime = System.currentTimeMillis();
		reporter.setDrawTimeInfo(
				endTime,
				(int)(endTime - startTime),
				(int)(endWaitingDataTime - startWaitingDataTime));
		startWaitingDataTime = -1;
		
		// Intentional sleep for context switching.
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Log.i(TAG, "doDraw() sleep, InterruptedException.");
		}
	}
	
	private void reconnect() {
		loader.stop();
		loader.start();		
	}

    private class Reporter {
    	private ScheduledExecutorService scheduler;
    	private Timer timerTask;
    	private ScheduledFuture<?> future;
    	private int accDrawTime; // msec
    	private int accWaitingDataTime; // msec
    	private int numDrawedFrame;
    	private int numSkippedFrame;
    	private long periodStartTime; // Millis
    	private long periodEndTime; // Millis
    	private static final long INITIAL_DURATION_ADJUST = 125;

    	WifiManager wifiManager = null;
    	
    	public Reporter(Context context){
    		timerTask = new Timer();
    		scheduler = Executors.newSingleThreadScheduledExecutor();
    		accDrawTime = 0;
    		accWaitingDataTime = 0;
    		numDrawedFrame = 0;
    		numSkippedFrame = 0;
    		periodStartTime = -1;
    		periodEndTime = -1;

    		wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    		if(wifiManager == null) {
    			Log.i(TAG, "Can not get WifiManager.");
    		}
    	}

    	public void start(){
    		accDrawTime = 0;
    		accWaitingDataTime = 0;
    		numDrawedFrame = 0;
    		numSkippedFrame = 0;
    		periodStartTime = -1;
    		periodEndTime = -1;
    		future = scheduler.scheduleAtFixedRate(timerTask, REPORT_WAIT, REPORT_INTERVAL,TimeUnit.SECONDS);
    	}

    	public void stop(){
    		if(future != null){
    			future.cancel(true);
    		}
    	}

    	public void shutdown(){
    		scheduler.shutdown();
    	}

    	public boolean isStopped(){
    		return future.isCancelled();
    	}

    	public synchronized void setDrawTimeInfo(long currentMillis, int drawMsec, int waitMsec){
    		accDrawTime = accDrawTime + drawMsec;
    		accWaitingDataTime = accWaitingDataTime + waitMsec;
    		numDrawedFrame++;
    		periodEndTime = currentMillis;
    		if (periodStartTime < 0) {
    			periodStartTime = currentMillis - INITIAL_DURATION_ADJUST;
    		}
    	}

    	public synchronized void setSkippedFrameInfo(long currentMillis){
    		numSkippedFrame++;
    		periodEndTime = currentMillis;
    		if (periodStartTime < 0) {
    			periodStartTime = currentMillis - INITIAL_DURATION_ADJUST;
    		}
    	}
    	
    	private class Timer implements Runnable{
    		private Handler handler = new Handler();
    		@Override
    		public void run(){
    			handler.post(new Runnable(){
    				@Override
    				public void run(){
    					if (drawing) {
    						sendReport();
    					}
    				}
    			});
    		}
    	}

    	private void sendReport(){
        	String report = getReport();
        	if (report == null) {
        		//Log.i(TAG, "No frame is drawed since last info.");
        		return;
        	}

        	if (ENABLE_CLIENT_REPORT == false) {
            	Log.i(TAG, "Info [" + report + "]");
        	} else {
        		try {
        			URL url = new URL("http://192.168.122.1:8080/liveview/liveview.jpg?" + report);
        			url.openStream();
        		} catch (IOException e) {
        			e.printStackTrace();
        			Log.i(TAG, "Reporting failed.");
        		}
        		Log.i(TAG, "Reported [" + report + "]");
        	}

        	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        	Log.i(TAG, "Wi-Fi Signal strength = " + String.valueOf(wifiInfo.getRssi()));
    	}
    	
    	private synchronized String getReport(){
    		if (numDrawedFrame < 1 & numSkippedFrame < 1) {
    			return null;
    		}

    		int period = (int)(periodEndTime - periodStartTime);

    		int frminterval;
    		if (numDrawedFrame > 0) {
    			frminterval = accDrawTime / numDrawedFrame + DRAW_DELAY;
    		} else {
    			frminterval = period / numSkippedFrame;
    		}

    		int idle;
    		if (accWaitingDataTime * 10 >= period) {
    			idle = 1;
    		} else {
    			idle = 0;
    		}

    		String report;
    		if (ENABLE_CLIENT_REPORT) {
    			report = "crinterval=" + String.valueOf(REPORT_INTERVAL) +
    					"&frminterval=" + String.valueOf(frminterval) +
    					"&idle=" + String.valueOf(idle) +
    					"&receivedfrm=" + String.valueOf(numDrawedFrame + numSkippedFrame) +
    					"&skippedfrm=" + String.valueOf(numSkippedFrame) +
    					"&period=" + String.valueOf(period);
    		} else {
    			report = "Receive frame=" + String.valueOf(numDrawedFrame + numSkippedFrame) +
    					" Skipped frame=" + String.valueOf(numSkippedFrame) +
    					" Period=" + String.valueOf(period) + "(ms)";
    		}

    		accDrawTime = 0;
    		accWaitingDataTime = 0;
    		numDrawedFrame = 0;
    		numSkippedFrame = 0;
    		periodStartTime = periodEndTime;

    		return report;
    	}
    }
}
