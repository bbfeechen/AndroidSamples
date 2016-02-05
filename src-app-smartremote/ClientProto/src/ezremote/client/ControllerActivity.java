package ezremote.client;

/*
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
*/

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sony.mexi.json.JsArray;
import com.sony.mexi.json.JsString;
import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.SmartRemoteControlTestClientClient;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.GetEventParams;

import ezremote.client.async.PostviewLoader;
import ezremote.client.callback.ItemListListener;
import ezremote.client.callback.PostviewCallback;
import ezremote.client.callback.WebApiClientListener;
import ezremote.client.data.DataContainer;
import ezremote.client.data.ImageContainer;
import ezremote.client.liveview.Liveview;
import ezremote.client.postview.ExtendedImageView;
import ezremote.client.postview.ImageStatusCallback;
import ezremote.client.util.DevLog;
import ezremote.client.util.DialogManager;
import ezremote.client.util.EvConverter;
import ezremote.client.webapi.GuideCallbacksContainer;
import ezremote.client.webapi.WebApiBackgroundOperator;
import ezremote.client.webapi.WebApiCallbacksContainer;
import ezremote.client.webapi.WebApiForegroundOperator;
import ezremote.client.webapi.WebApiInitializer;
import ezremote.client.webapi.act.liveview.StartLiveviewCallback;
import ezremote.client.webapi.act.liveview.StopLiveviewCallback;
import ezremote.client.webapi.act.recmode.StartRecModeCallback;
import ezremote.client.webapi.act.recmode.StopRecModeCallback;
import ezremote.client.webapi.act.takepicture.ActTakePictureCallback;
import ezremote.client.webapi.act.takepicture.AwaitTakePictureCallback;
import ezremote.client.webapi.common.GetApplicationInfoCallback;
import ezremote.client.webapi.common.MethodTypeHandler;
import ezremote.client.webapi.common.VersionHandler;
import ezremote.client.webapi.get.GetAvailableApiListCallback;
import ezremote.client.webapi.get.exposure.GetAvailableExposureCompensationCallback;
import ezremote.client.webapi.get.exposure.GetExposureCompensationCallback;
import ezremote.client.webapi.get.exposure.GetSupportedExposureCompensationCallback;
import ezremote.client.webapi.get.selftimer.GetAvailableSelfTimerCallback;
import ezremote.client.webapi.get.selftimer.GetSelfTimerCallback;
import ezremote.client.webapi.get.selftimer.GetSupportedSelfTimerCallback;
import ezremote.client.webapi.listener.ActTakePictureListener;
import ezremote.client.webapi.listener.AwaitTakePictureListener;
import ezremote.client.webapi.listener.GetApplicationInfoListener;
import ezremote.client.webapi.listener.GetAvailableApiListListener;
import ezremote.client.webapi.listener.GetAvailableExposureCompensationListener;
import ezremote.client.webapi.listener.GetAvailableSelfTimerListener;
import ezremote.client.webapi.listener.GetEventListener;
import ezremote.client.webapi.listener.GetExposureCompensationListener;
import ezremote.client.webapi.listener.GetSelfTimerListener;
import ezremote.client.webapi.listener.GetSupportedExposureCompensationListener;
import ezremote.client.webapi.listener.GetSupportedSelfTimerListener;
import ezremote.client.webapi.listener.MethodTypeListener;
import ezremote.client.webapi.listener.SetExposureCompensationListener;
import ezremote.client.webapi.listener.SetSelfTimerListener;
import ezremote.client.webapi.listener.StartLiveviewListener;
import ezremote.client.webapi.listener.StartRecModeListener;
import ezremote.client.webapi.listener.StopLiveviewListener;
import ezremote.client.webapi.listener.StopRecModeListener;
import ezremote.client.webapi.listener.VersionHandlerListener;
import ezremote.client.webapi.listener.touchafposition.CancelTouchAFPositionListener;
import ezremote.client.webapi.listener.touchafposition.GetTouchAFPositionListener;
import ezremote.client.webapi.listener.touchafposition.SetTouchAFPositionListener;
import ezremote.client.webapi.set.SetExposureCompensationCallback;
import ezremote.client.webapi.set.SetSelfTimerCallback;
import ezremote.client.webapi.touchafposition.CancelTouchAFPositionCallback;
import ezremote.client.webapi.touchafposition.GetTouchAFPositionCallback;
import ezremote.client.webapi.touchafposition.SetTouchAFPositionCallback;

public class ControllerActivity extends Activity
		implements View.OnClickListener, View.OnTouchListener, PostviewCallback, ImageStatusCallback, WebApiClientListener,
			StartLiveviewListener, StopLiveviewListener, ActTakePictureListener, StopRecModeListener, GetAvailableApiListListener, StartRecModeListener,
			SetExposureCompensationListener, GetExposureCompensationListener, GetSupportedExposureCompensationListener, GetAvailableExposureCompensationListener,
			SetSelfTimerListener, GetSelfTimerListener, GetSupportedSelfTimerListener, GetAvailableSelfTimerListener,
			MethodTypeListener, VersionHandlerListener, GetApplicationInfoListener, AwaitTakePictureListener,
			GetTouchAFPositionListener, SetTouchAFPositionListener, CancelTouchAFPositionListener,
			GetEventListener
			{
	private static final String TAG = ControllerActivity.class.getSimpleName();
	
	private ImageButton iBtnSetting, iBtnSingle;
	private ExtendedImageView postView;
	private ImageView iClose;
	private TextView imageStatusView;
	private Handler handler;
	private Bitmap bitmap;
	private boolean isInitialization;

	private DialogManager dialogManager;
	
	private SmartRemoteControlTestClientClient webapiClient;
	
	private WebApiBackgroundOperator webapiBgOperator;
	private WebApiForegroundOperator webapiFgOperator;
	
    private WifiManager manager;
    
    private Liveview landscapeLiveview;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main_wo_live_p1);

		handler = new Handler();
		
        postView = (ExtendedImageView)this.findViewById(R.id.postView);
        iClose = (ImageView)this.findViewById(R.id.closeImage);

        iBtnSetting = (ImageButton)this.findViewById(R.id.continuousButton);
        iBtnSingle = (ImageButton)this.findViewById(R.id.singleButton);
        imageStatusView = (TextView)this.findViewById(R.id.textImageStatus);
        
        iBtnSetting.setOnClickListener(this);
        iBtnSingle.setOnClickListener(this);
        iClose.setOnClickListener(this);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		ImageContainer.getInstance().setDisplayMetrics(metrics);

		postView.setImageStatusCallback(this);

        DataContainer.getInstance();
        ImageContainer.getInstance().setPostviewOriginal(null);
        postView.setClickable(false);
        postView.setVisibility(View.INVISIBLE);
        iClose.setClickable(false);
        iClose.setVisibility(View.INVISIBLE);
		imageStatusView.setVisibility(View.INVISIBLE);

		dialogManager = new DialogManager(this, handler);
		
		manager = (WifiManager)getSystemService(WIFI_SERVICE);
		
		bitmap = null;
		
		landscapeLiveview = (Liveview)findViewById(R.id.surfaceView1);
		landscapeLiveview.setOnTouchListener(this);
    }

	@Override
	public void onRestart(){
		super.onRestart();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		postView.setSize(findViewById(R.id.postView).getWidth(), findViewById(R.id.postView).getHeight());
        postView.setImage(bitmap);
		ImageContainer.getInstance().setLiveviewAreaSize(landscapeLiveview.getWidth(), landscapeLiveview.getWidth());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
        setContentView(R.layout.main_wo_live_p1);
        iBtnSetting = (ImageButton)this.findViewById(R.id.continuousButton);
        postView = (ExtendedImageView)this.findViewById(R.id.postView);
        iBtnSingle = (ImageButton)this.findViewById(R.id.singleButton);
        imageStatusView = (TextView)this.findViewById(R.id.textImageStatus);

        iBtnSetting.setOnClickListener(this);
        iBtnSingle.setOnClickListener(this);
        iClose.setOnClickListener(this);

		postView.setSize(findViewById(R.id.postView).getWidth(), findViewById(R.id.postView).getHeight());
	}

	@Override
	public void onResume(){
		super.onResume();
		WebApiCallbacksContainer.setInitializedFlag(false);
		GuideCallbacksContainer.setInitializedFlag(false);
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {		// Don't use "guide" service, because we already know the target has "camera" service.
						webapiClient = new WebApiInitializer().initialize(ControllerActivity.this, ControllerActivity.this);
					}
				});
		    }
		}).start();
		isInitialization = true;
		dialogManager.showProgDialog(null, "Loading data...");

		if(null != webapiBgOperator) {
			webapiBgOperator.cancelTouchAFPosition();
		}
	}

	@Override
	public void onPause(){
    	webapiClient = null;
		super.onPause();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Menu.FIRST+2, Menu.NONE, "getAppInfo");
		menu.add(Menu.NONE, Menu.FIRST+3, Menu.NONE, "getAvailableApiList");
		menu.add(Menu.NONE, Menu.FIRST+4, Menu.NONE, "actTakePicture");
		menu.add(Menu.NONE, Menu.FIRST+5, Menu.NONE, "awaitTakePicture");
		menu.add(Menu.NONE, Menu.FIRST+6, Menu.NONE, "startRecMode");
		menu.add(Menu.NONE, Menu.FIRST+7, Menu.NONE, "stopRecMode");
		menu.add(Menu.NONE, Menu.FIRST+8, Menu.NONE, "StartLiveview");
		menu.add(Menu.NONE, Menu.FIRST+9, Menu.NONE, "StopLiveview");
		menu.add(Menu.NONE, Menu.FIRST+10, Menu.NONE, "receiveEvent(short)");
		menu.add(Menu.NONE, Menu.FIRST+11, Menu.NONE, "receiveEvent(long)");
		menu.add(Menu.NONE, Menu.FIRST+12, Menu.NONE, "getSupportedEV");
		menu.add(Menu.NONE, Menu.FIRST+13, Menu.NONE, "getAvailableEV");
		menu.add(Menu.NONE, Menu.FIRST+14, Menu.NONE, "setEV 3 (1.0)");
		menu.add(Menu.NONE, Menu.FIRST+15, Menu.NONE, "getEV");
		menu.add(Menu.NONE, Menu.FIRST+16, Menu.NONE, "getSupportedTimer");
		menu.add(Menu.NONE, Menu.FIRST+17, Menu.NONE, "getAvailableTimer");
		menu.add(Menu.NONE, Menu.FIRST+18, Menu.NONE, "setSelfTimer 2 sec");
		menu.add(Menu.NONE, Menu.FIRST+19, Menu.NONE, "getSelfTimer");		
		return ret;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == Menu.FIRST+2){
			webapiFgOperator.getApplicationInfo();
		} else if(item.getItemId() == Menu.FIRST+3){
			webapiFgOperator.getAvailableApiList();
		} else if(item.getItemId() == Menu.FIRST+4){
			webapiFgOperator.actTakePicture();
		} else if(item.getItemId() == Menu.FIRST+5){
			webapiFgOperator.awaitTakePicture();
		} else if(item.getItemId() == Menu.FIRST+6){
			webapiFgOperator.startRecMode();
		} else if(item.getItemId() == Menu.FIRST+7){
			webapiFgOperator.stopRecMode();
		} else if(item.getItemId() == Menu.FIRST+8){
			webapiFgOperator.startLiveview();
		} else if(item.getItemId() == Menu.FIRST+9){
			webapiFgOperator.stopLiveview();
		} else if(item.getItemId() == Menu.FIRST+10){
			webapiFgOperator.getEvent(false);
		} else if(item.getItemId() == Menu.FIRST+11){
			webapiFgOperator.getEvent(true);
		} else if(item.getItemId() == Menu.FIRST+12){
			webapiFgOperator.getSupportedExposureCompensation();
		} else if(item.getItemId() == Menu.FIRST+13){
			webapiFgOperator.getAvailableExposureCompensation();
		} else if(item.getItemId() == Menu.FIRST+14){
			webapiFgOperator.setExposureCompensation(3);
		} else if(item.getItemId() == Menu.FIRST+15){
			webapiFgOperator.getExposureCompensation();
		} else if(item.getItemId() == Menu.FIRST+16){
			webapiFgOperator.getSupportedSelfTimer();
		} else if(item.getItemId() == Menu.FIRST+17){
			webapiFgOperator.getAvailableSelfTimer();
		} else if(item.getItemId() == Menu.FIRST+18){
			webapiFgOperator.setSelfTimer(2);
		} else if(item.getItemId() == Menu.FIRST+19){
			webapiFgOperator.getSelfTimer();
		}
		return super.onOptionsItemSelected(item);
	}
	
    public void onClick(View view){
    	if(view == iBtnSetting){ 		
    		if(WebApiCallbacksContainer.isInitialized()){
    			webapiBgOperator.getAvailableApiList();
    		}    		
    	}
    	else if(view == iBtnSingle){
    		if(WebApiCallbacksContainer.isInitialized()){
        		dialogManager.showProgDialog(null, "Now Taking Picture");
    			webapiBgOperator.actTakePicture();
    		}
    	}
    	else if(view == iClose){
            postView.setClickable(false);
            iClose.setClickable(false);
            new Thread(new Runnable() {
    			public void run() {
    				handler.post(new Runnable() {
    					public void run() {
    						postView.setVisibility(View.INVISIBLE);
    						iClose.setVisibility(View.INVISIBLE);
    						imageStatusView.setVisibility(View.INVISIBLE);
    					}
    				});
    		    }
    		}).start();
            ImageContainer.getInstance().setPostviewOriginal(null);
            
			webapiFgOperator.startLiveview();
    	}
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e){
    	if(e.getKeyCode() == KeyEvent.KEYCODE_BACK && e.getAction() == KeyEvent.ACTION_UP){
			manager.disableNetwork(manager.getConnectionInfo().getNetworkId());
	    	manager.saveConfiguration();
	    	manager.disconnect();
	    	return super.dispatchKeyEvent(e);
    	}
    	return super.dispatchKeyEvent(e);
    }

	@Override
	public void onStartPostview(InputStream is) {
		postView.setImageStream(is);

		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						postView.setVisibility(View.VISIBLE);
						iClose.setVisibility(View.VISIBLE);
						imageStatusView.setVisibility(View.VISIBLE);
						postView.invalidate();
					}
				});
		    }
		}).start();
		postView.setClickable(true);
		iClose.setClickable(true);
		dialogManager.closeDialogs();
	}

	@Override
	public void onStopPostview() {
		;
	}


	@Override
	public void onReceivingFailed(){
		dialogManager.showTextDialog("Connection Error", "Failed to Receive Postview Image");
	}

	@Override
	public void onStatusUpdate() {
		String status = "Default: "+ ImageContainer.getInstance().getDefaultSize()
				+ "\nScale: " + ImageContainer.getInstance().getScale()
				+ "\nCurrent: "+ ImageContainer.getInstance().getCurrentSize();
		imageStatusView.setText(status);
	}


	@Override
	public void onPageFinishedOfCamera() {
		Log.i(TAG, "onPageFinishedOfCamera");

    	WebApiCallbacksContainer.setMethodTypeHandler(new MethodTypeHandler(this));
    	WebApiCallbacksContainer.setVersionHandler(new VersionHandler(this));
    	WebApiCallbacksContainer.setGetApplicationInfoCallback(new GetApplicationInfoCallback(this));
    	WebApiCallbacksContainer.setGetAvailableApiListCallback(new GetAvailableApiListCallback(this));
    	//WebApiCallbacksContainer.setReceiveEventCallback(new ReceiveEventCallback(this));
    	
    	WebApiCallbacksContainer.setActTakePictureCallback(new ActTakePictureCallback(this));
    	WebApiCallbacksContainer.setAwaitTakePictureCallback(new AwaitTakePictureCallback(this));
    	WebApiCallbacksContainer.setStartLiveviewCallback(new StartLiveviewCallback(this));
    	WebApiCallbacksContainer.setStopLiveviewCallback(new StopLiveviewCallback(this));
    	WebApiCallbacksContainer.setStartRecModeCallback(new StartRecModeCallback(this));
    	WebApiCallbacksContainer.setStopRecModeCallback(new StopRecModeCallback(this));
    	
    	WebApiCallbacksContainer.setSetExposureCompensationCallback(new SetExposureCompensationCallback(this));
    	WebApiCallbacksContainer.setGetExposureCompensationCallback(new GetExposureCompensationCallback(this));
    	WebApiCallbacksContainer.setGetSupportedExposureCompensationCallback(new GetSupportedExposureCompensationCallback(this));
    	WebApiCallbacksContainer.setGetAvailableExposureCompensationCallback(new GetAvailableExposureCompensationCallback(this));
    	
    	WebApiCallbacksContainer.setSetSelfTimerCallback(new SetSelfTimerCallback(this));
    	WebApiCallbacksContainer.setGetSelfTimerCallback(new GetSelfTimerCallback(this));
    	WebApiCallbacksContainer.setGetSupportedSelfTimerCallback(new GetSupportedSelfTimerCallback(this));
    	WebApiCallbacksContainer.setGetAvailableSelfTimerCallback(new GetAvailableSelfTimerCallback(this));    	
    	
    	WebApiCallbacksContainer.setGetTouchAFPositionCallback(new GetTouchAFPositionCallback(this));
    	WebApiCallbacksContainer.setSetTouchAFPositionCallback(new SetTouchAFPositionCallback(this));
    	WebApiCallbacksContainer.setCancelTouchAFPositionCallback(new CancelTouchAFPositionCallback(this));
    	
    	WebApiCallbacksContainer.setInitializedFlag(true);
    	
		webapiBgOperator = new WebApiBackgroundOperator(webapiClient, handler);
		webapiFgOperator = new WebApiForegroundOperator(webapiClient);    	

		dialogManager.closeDialogs();
		
		if(WebApiCallbacksContainer.isInitialized()){
			webapiBgOperator.getMethodTypes("1.0");
			String version = "1.2";
			Toast.makeText(this, "Calling getMethodTypes with a static version: " + version, Toast.LENGTH_SHORT).show();
			webapiBgOperator.getMethodTypes(version);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		boolean ret = false;
		if(view == landscapeLiveview) {
			ret = onLiveviewTouched(event);
		}
		return ret;
	}
	private boolean onLiveviewTouched(MotionEvent event) {
		boolean ret = false;
		Log.e(TAG, "TOUCH: " + event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			double rx=(double)100*event.getX()/landscapeLiveview.getWidth();
			double ry=(double)100*event.getY()/landscapeLiveview.getHeight();
			Toast.makeText(this, "TOUCH: "+rx+"="+event.getX()+"/"+landscapeLiveview.getWidth()+", "+ry+"="+event.getY()+"/"+landscapeLiveview.getHeight(), Toast.LENGTH_SHORT).show();

			webapiBgOperator.setTouchAFPosition(rx, ry);
			ret = true;
			break;
		case MotionEvent.ACTION_DOWN:
			ret = true;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
		return ret;
	}
	@Override
	public void onSuccessActTakePicture(String[] urls) {
		dialogManager.showProgDialog("Success shooting", "Now Downloading Postview Image...");
		DevLog.i("ControlleActivity", "onShootingSucceeded: " + urls[0]);
		new PostviewLoader(this).execute(urls[0]);
	}

	@Override
	public void onFailureActTakePicture(int status) {
		if(status == 40403){
			Log.i(TAG, "received NotFinished. send actTakePicture");
			webapiBgOperator.awaitTakePicture();
		} else if (status == 40402){
			Log.e(TAG, "canceled waiting finish of capturing");
			dialogManager.showTextDialog("Canceled", "canceled waiting finish of capturing");
		} else if(status != 0){
			Log.e(TAG, "Failed actTakePicture: " + status);
			dialogManager.showTextDialog("Error", "Failed ActTakePicture: " + status);
		}
	}

	@Override
	public void onSuccessAwaitTakePicture(String[] urls) {
		dialogManager.showProgDialog("Success await takepicture", "Now Downloading Postview Image...");
		DevLog.i("ControlleActivity", "onShootingSucceeded: " + urls[0]);
		new PostviewLoader(this).execute(urls[0]);
	}

	@Override
	public void onFailureAwaitTakePicture(int status) {
		if(status == 40403){
			Log.i(TAG, "received NotFinished. send awaitTakePicture");
			webapiBgOperator.awaitTakePicture();
		} else if (status == 40402){
			Log.e(TAG, "canceled waiting finish of capturing");
			dialogManager.showTextDialog("Canceled", "canceled waiting finish of capturing");
		} else if(status != 0){
			Log.e(TAG, "Failed actTakePicture: " + status);
			dialogManager.showTextDialog("Error", "Failed AwaitTakePicture: " + status);
		}
	}
	
	@Override
	public void onSuccessStartLiveview(String url) {
		if(isInitialization){
			isInitialization = false;
		}
		//dialogManager.showTextDialog("Success", "Succeeded StartLiveview" + url);
		String msg = "Succeeded StartLiveview: " + url;
		//dialogManager.showTextDialog("Success", msg);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.d("Success", msg);
		
		webapiBgOperator.cancelTouchAFPosition();
		
		Liveview.resume();
	}

	@Override
	public void onFailureStartLiveview(int status) {
		if(isInitialization){
			isInitialization = false;
		}
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed StartLiveview");
		}
	}

	@Override
	public void onSuccessStopLiveview(int ret) {
		dialogManager.showTextDialog("Success", "Succeeded StopLiveview");
	}

	@Override
	public void onFailureStopLiveview(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed StopLiveview");
		}
	}

	@Override
	public void onSuccessStopRecMode(int ret) {
		dialogManager.showTextDialog("Success", "Succeeded StopRecMode");
	}

	@Override
	public void onFailureStopRecMode(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed StopRecMode");
		}
	}

	@Override
	public void onSuccessGetAvailableApiList(final String[] apis) {
		Log.i(TAG, "onSuccessGetAvailableApiListListener");
		List<String> avApiList = Arrays.asList(apis);
		final ArrayList<String> items = new ArrayList<String>();
		if(avApiList.contains("getAvailableExposureCompensation")){
			items.add(getString(R.string.exposure));
		}
		if(avApiList.contains("getAvailableSelfTimer")){
			items.add(getString(R.string.selftimer));
		}
		if(items.size() != 0){
			dialogManager.setItemListListener(new ItemListListener(){
				@Override
				public void onItemSelected(int which) {
					if(getString(R.string.exposure).equals(items.get(which))){
		    			webapiFgOperator.getAvailableExposureCompensation();
					} else if(getString(R.string.selftimer).equals(items.get(which))){
		    			webapiFgOperator.getAvailableSelfTimer();					
					} else {
						dialogManager.showTextDialog("Notification", "Unknown Item is Selected");
					}
				}
			});
			dialogManager.showItemsDialog("Settings", items);
		} else {
			dialogManager.showTextDialog("Notification", "No setting item is available");
		}
	}

	@Override
	public void onFailureGetAvailableApiList(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetAvailableApiList");
		}
	}

	@Override
	public void onSuccessGetAvailableSelfTimer(int current, final int[] available) {
		Log.i(TAG, "onSuccessGetAvailableSelfTimer");
		dialogManager.setItemListListener(new ItemListListener(){
			@Override
			public void onItemSelected(int which) {
				webapiFgOperator.setSelfTimer(available[which]);
			}
		});
		ArrayList<String> strList = new ArrayList<String>();
		for(int i : available){
			if(i == 0){
				strList.add("OFF");
			} else if (i == 1){
				strList.add(i + " second");
			} else {
				strList.add(i + " seconds");
			}
		}
		dialogManager.showItemsDialog("Self Timer Setting", strList);
	}

	@Override
	public void onFailureGetAvailableSelfTimer(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetAvailableSelfTimer");
		}
	}
	
	@Override
	public void onSuccessGetSupportedSelfTimer(int[] supported) {
		JsArray a = new JsArray();
		a.add(new JsArray(supported));
		dialogManager.showTextDialog("Success", a.toString());
	}

	@Override
	public void onFailureGetSupportedSelfTimer(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetSupportedSelfTimer");
		}
	}
	
	@Override
	public void onSuccessGetSelfTimer(int timer) {
		dialogManager.showTextDialog("Success", "getSelfTimer: " + timer);
	}

	@Override
	public void onFailureGetSelfTimer(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetSelfTimer");		
		}
	}

	@Override
	public void onSuccessSetSelfTimer(int ret) {
		dialogManager.showTextDialog("Success", "setSelfTimer");
		
	}

	@Override
	public void onFailureSetSelfTimer(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed SetSelfTimer");
		}
	}


	@Override
	public void onSuccessGetSupportedExposureCompensation(int[] max, int[] min, int[] step) {
		JsArray a = new JsArray();
		a.add(new JsArray(max));
		a.add(new JsArray(min));
		a.add(new JsArray(step));
		dialogManager.showTextDialog("Success", a.toString());
	}

	@Override
	public void onFailureGetSupportedExposureCompensation(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetSupportedExposureCompensation");
		}
	}
	
	@Override
	public void onSuccessGetAvailableExposureCompensation(int current, final int max, final int min, final int step) {
		Log.i(TAG, "onSuccessGetAvailableExposureCompensation");
		float[] available = EvConverter.convertToValueArray(max, min, step);
		if(available != null){
			dialogManager.setItemListListener(new ItemListListener(){
				@Override
				public void onItemSelected(int which) {
					webapiFgOperator.setExposureCompensation(max - which);
				}
			});
			ArrayList<String> strList = new ArrayList<String>();
			for(float f : available){
				strList.add(Float.toString(f));
			}
			dialogManager.showItemsDialog("EV Setting", strList);
		} else {
			dialogManager.showTextDialog("Error", "Unknown response");
		}
	}

	@Override
	public void onFailureGetAvailableExposureCompensation(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetAvailableExposureCompensation");
		}
	}


	@Override
	public void onSuccessGetExposureCompensation(int exposure) {
		dialogManager.showTextDialog("Success", exposure + "");
	}

	@Override
	public void onFailureGetExposureCompensation(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetExposureCompensation");
		}
	}

	@Override
	public void onSuccessSetExposureCompensation(int ret) {
		dialogManager.showTextDialog("Success", ret + "");
	}

	@Override
	public void onFailureSetExposureCompensation(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed SetExposureCompensation");		
		}
	}

	@Override
	public void onSuccessStartRecMode(int ret) {
		dialogManager.showTextDialog("Success", Thread.currentThread().getStackTrace()[2].getMethodName() + ": " + "ret="+ret);
	}

	@Override
	public void onFailureStartRecMode(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed StartRecMode");
		}
	}

	@Override
	public void onSuccessGetApplicationInfo(String name, String version) {
		dialogManager.showTextDialog("Success", "Name: " + name + "\nVersion: " + version);
	}

	@Override
	public void onFailureGetApplicationInfo(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetApplicationInfo");
		}
	}

	@Override
	public void onSuccessVersionHandler(String[] versions) {
        JsArray a = new JsArray(versions);
		dialogManager.showTextDialog("Success", a.toString());
	}

	@Override
	public void onHandledStatusOfVersion(int status) {
		if(status == 0){
			Log.d(TAG, "onHandledStatusOfVersion: OK");
		} else {
			dialogManager.showTextDialog("Error", "getVersions");
		}
	}

	@Override
	public void onSuccessMethodTypeHandler(String methodName, String[] parameterTypes, String[] resultTypes, String version) {
		if(isInitialization){
			// TODO because MethodHandler does not call HandleStatus 20120529
			/*
			if(methodName.equals("getAvailableSelfTimer")){
				webapiBgOperator.startLiveview();
			}
			*/
		} else {
			JsString s = new JsString(methodName);
	        JsArray pt = new JsArray(parameterTypes);
	        JsArray rt = new JsArray(resultTypes);
	        JsString v = new JsString(version);
	        JsArray a = new JsArray();
	        a.add(s);
	        a.add(pt);
	        a.add(rt);
	        a.add(v);
			dialogManager.showTextDialog("Success", a.toString());
		}
	}

	@Override
	public void onHandledStatusOfMethodType(int status) {
		Log.d(TAG, "onHandledStatusOfMethodType: "+ status);
		if(status == 0){
			if(isInitialization){
				webapiBgOperator.startLiveview();
			} else {
				dialogManager.showTextDialog("Finished", "Handled OK Status of MethodTypeHandler");				
			}
		} else {
			dialogManager.showTextDialog("Error", "Failed GetMethodTypes");
		}
	}

	@Override
	public void onSuccessGetEvent(GetEventParams[] params) {
		dialogManager.showTextDialog("Success", params.toString());
	}

	@Override
	public void onFailureGetEvent(int status) {
		if(status != 0){
			Log.e(TAG, "Failed GetEvent:" + status);
			dialogManager.showTextDialog("Error", "Failed GetEvent: " + status);
		}
	}

	@Override
	public void onSuccessCancelTouchAFPosition() {
		//dialogManager.showTextDialog("Success", Thread.currentThread().getStackTrace()[2].getMethodName()+":"+"");
		Toast.makeText(this, "Success: "+Thread.currentThread().getStackTrace()[2].getMethodName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFailureCancelTouchAFPosition(int error) {
		if(error != 0){
			dialogManager.showTextDialog("Error", Thread.currentThread().getStackTrace()[2].getMethodName()+" Failed");
		}
	}

	@Override
	public void onSuccessSetTouchAFPosition(boolean AFResult, String AFType,
			double[] touchCordinate, double[] AFBoxLeftTop,
			double[] AFBoxRightBottom) {
		dialogManager.showTextDialog("Success", Thread.currentThread().getStackTrace()[2].getMethodName()+": "+"AFResult="+AFResult+", AFType="+AFType+", \nx="+touchCordinate[0]+", y="+touchCordinate[1]
				+", \nAFBox_x0="+AFBoxLeftTop[0]+", AFBox_y0="+AFBoxLeftTop[1]
				+", \nAFBox_x1="+AFBoxRightBottom[0]+", AFBox_y1="+AFBoxRightBottom[1]
				);
		Toast.makeText(this, "Issuing cancelTouchAFPosition to clear the setting.", Toast.LENGTH_SHORT).show();
		webapiBgOperator.cancelTouchAFPosition();
	}

    @Override
    public void onFailureSetTouchAFPosition(int error) {
            if(error != 0){
                    dialogManager.showTextDialog("Error", Thread.currentThread().getStackTrace()[2].getMethodName()+" Failed");
            }
    }

    @Override
    public void onSuccessGetlTouchAFPosition(boolean set,
                    double[] touchCordinate) {
            dialogManager.showTextDialog("Success", Thread.currentThread().getStackTrace()[2].getMethodName()+": "+"set="+set+", x="+touchCordinate[0]+", y="+touchCordinate[1]);
    }
    @Override
    public void onFailureGetTouchAFPosition(int error) {
            if(error != 0){
                    dialogManager.showTextDialog("Error", Thread.currentThread().getStackTrace()[2].getMethodName()+" Failed");
            }
    }
}
