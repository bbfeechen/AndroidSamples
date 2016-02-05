package ezremote.client;

import ezremote.client.callback.ConnectionStateCallback;
import ezremote.client.callback.AuthDialogCallback;
import ezremote.client.callback.GuideClientListener;
import ezremote.client.callback.WebApiClientListener;
import ezremote.client.data.ApContainer;
import ezremote.client.data.DataContainer;
import ezremote.client.data.ap.APInfo;
import ezremote.client.data.ap.Capability;
import ezremote.client.scanner.APAdapter;
import ezremote.client.scanner.ConnectionManager;
import ezremote.client.scanner.CurrentAPAdapter;
import ezremote.client.scanner.ScannedAPAdapter;
import ezremote.client.util.AuthDialog;
import ezremote.client.util.DevLog;
import ezremote.client.util.DialogManager;
import ezremote.client.webapi.GuideBackgroundOperator;
import ezremote.client.webapi.GuideCallbacksContainer;
import ezremote.client.webapi.GuideForegroundOperator;
import ezremote.client.webapi.GuideInitializer;
import ezremote.client.webapi.WebApiCallbacksContainer;
import ezremote.client.webapi.WebApiInitializer;
import ezremote.client.webapi.WebApiBackgroundOperator;
import ezremote.client.webapi.act.recmode.StartRecModeCallback;
import ezremote.client.webapi.common.GetApplicationInfoCallback;
import ezremote.client.webapi.common.MethodTypeHandler;
import ezremote.client.webapi.common.VersionHandler;
import ezremote.client.webapi.get.GetAvailableApiListCallback;
import ezremote.client.webapi.guide.GuideMethodTypeHandler;
import ezremote.client.webapi.guide.GuideProtocolHandler;
import ezremote.client.webapi.guide.GuideVersionHandler;
import ezremote.client.webapi.listener.GetApplicationInfoListener;
import ezremote.client.webapi.listener.GetAvailableApiListListener;
import ezremote.client.webapi.listener.MethodTypeListener;
import ezremote.client.webapi.listener.StartRecModeListener;
import ezremote.client.webapi.listener.VersionHandlerListener;
import ezremote.client.webapi.listener.guide.GuideProtocolHandlerListener;

import java.util.List;

import com.sony.mexi.orb.client.serviceguide.v1_0.ServiceGuideClient;
import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.SmartRemoteControlTestClientClient;
import com.sony.scalar.lib.ddclient.DeviceInfo;
import com.sony.scalar.lib.ddclient.WebApiDdClient;
import com.sony.scalar.lib.ddclient.WebApiDdClient.SearchNotify;
import com.sony.scalar.lib.ddclient.WebApiDdClient.SearchStatus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ScannerActivity extends Activity
		implements View.OnClickListener, AuthDialogCallback, SearchNotify, ConnectionStateCallback, WebApiClientListener
			, StartRecModeListener, MethodTypeListener, VersionHandlerListener, GetApplicationInfoListener, GetAvailableApiListListener
			, GuideClientListener, GuideProtocolHandlerListener {
	private ListView sListview, cListview;
    private APAdapter sAdapter, cAdapter;
    private CheckBox wifiOnOff;
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;
    private BroadcastReceiver bReceiver;
	private APInfo apinfo;
	private TextView connectionText, authText;

	private DialogManager dialogManager;

	private boolean isApFilterEnabled;
	
	//private ScalaraInvoker scalaraInvoker;
	private SmartRemoteControlTestClientClient webapiClient;
	private ServiceGuideClient guideClient;
	
	
	private IntentFilter filter;
	private Handler handler;
	private WebApiBackgroundOperator webapiBgOperator;
	private GuideBackgroundOperator guideBgOperator;

	private WebApiDdClient ddClient;

	private ConnectionManager connectionManager;
	private boolean isTryingConnection;
	private boolean isTryingDd;
	
	static final String SCALARWEBAPI_SERVICE_TYPE[] = { "camera" };
	static final String TAG = ScannerActivity.class.getSimpleName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        setContentView(R.layout.connection);        
        
		handler = new Handler();
		
        sAdapter = new ScannedAPAdapter(this, R.layout.row_capa);
        sListview = (ListView)findViewById(R.id.listView1);
        sListview.setAdapter(sAdapter);
        cAdapter = new CurrentAPAdapter(this, R.layout.row_capa);
        cListview = (ListView)findViewById(R.id.listView2);
        cListview.setAdapter(cAdapter);
        connectionText = (TextView)findViewById(R.id.textConnectionState);
        authText = (TextView)findViewById(R.id.textAuthState);
com.sony.scalar.lib.ddclient.util.DevLog.enable( true );
        wifiOnOff = (CheckBox)findViewById(R.id.checkBox1);
        wifiOnOff.setOnClickListener(this);

		wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		
		/*
		if(wifiManager.isWifiEnabled()){
			ddClient = new WebApiDdClient(this);
		}
		*/
		
		dialogManager = new DialogManager(this, handler);
		
		connectionManager = new ConnectionManager(wifiManager, this);
		isTryingConnection = false;
		isTryingDd = false;
		isApFilterEnabled = true;
		
        filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        bReceiver = new BroadcastReceiver(){
    		@Override
    		public void onReceive(Context context, Intent intent) {
    			String action = intent.getAction();
    	        if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
    	        	handleWifiStateChangedAction(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
    	    	} else if(action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
    	    		handleSupplicantConnectionChangeAction(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false));
    	    	} else if(action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)){
    	    		handleSupplicantStateChangedAction();
    	    	} else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
    	    		handleNetworkStateChangedAction((NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO));
    	    	} else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
    	    		handleConnectivityAction(intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO), intent.getStringExtra(ConnectivityManager.EXTRA_REASON));
    	    	}
    		}
    	};
        
		sListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				ListView sListview = (ListView) parent;
				final APInfo item = (APInfo) sListview.getItemAtPosition(position);
				boolean bool = true;
				DevLog.i("ConfiguredNetworks", wifiManager.getConfiguredNetworks().size()+"");
				for(WifiConfiguration config : wifiManager.getConfiguredNetworks()){
					Log.v("APScanner", config.SSID + " - " + item.getName());
    				if(config.SSID.replace("\"", "").equals(item.getName())){
    					Log.v("APScanner", "Remembered Access Point");
    					if(config.preSharedKey != null){
    						new AuthDialog(ScannerActivity.this, ScannerActivity.this).preAuthDialog(item, config.networkId).show();
    						bool = false;
        					break;
    					}
    				}
    			}
				if(bool){
					if(item.getStatus().contains("WPA-PSK") || item.getStatus().contains("WPA2-PSK")){
						new AuthDialog(ScannerActivity.this, ScannerActivity.this).authDialog(item).show();
					}
					else{
						dialogManager.showTextDialog("Error", "Only WPA/WPA2-PSK is supported");
					}
				}
			}
		});

		cListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				new AuthDialog(ScannerActivity.this, ScannerActivity.this).preConnectedDialog().show();
			}
		});
    	
		DevLog.v("APScanner", "Finished OnCreate");
    }

    @Override
    public void onResume(){
    	super.onResume();
    	
		WebApiCallbacksContainer.setInitializedFlag(false);
		
		if(wifiManager.isWifiEnabled()){
			wifiOnOff.setChecked(true);
			if(connectivityManager.getActiveNetworkInfo()!=null){
				if(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()){
					showCurrent(true);
				}
			}
			showScanResults();
			connectionText.setText("WIFI_STATE_ENABLED");
			authText.setText("");
		} else {
			wifiOnOff.setChecked(false);
		}
        registerReceiver(bReceiver, filter);
    }

    @Override
    public void onPause(){
        dialogManager.closeDialogs();
    	Log.v("APScanner", "onPause");
    	webapiClient = null;
    	guideClient = null;
    	unregisterReceiver(bReceiver);
        sAdapter.clear();
        cAdapter.clear();
    	super.onPause();
    }

    @Override
    public void onStop(){
    	Log.v("APScanner", "onStop");
    	super.onStop();
    }


	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Update AP Info");				// Update Access Point Scan result
		menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, "Switch AP Filter");			// Enable/Disable AP Name Filter
		return ret;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == Menu.FIRST){
			if(wifiManager.isWifiEnabled()){
				if(connectivityManager.getActiveNetworkInfo() != null){
					if(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()){
						showCurrent(true);
					}
				}
				showScanResults();
			} else {
				if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING)
					dialogManager.showTextDialog("Wait a second", "Now Enabling Wi-Fi");
				else
					dialogManager.showTextDialog("Wi-Fi is Disabled", "Turn Wi-Fi on to search devices");
			}
		} else if(item.getItemId() == Menu.FIRST+1){
			isApFilterEnabled = !isApFilterEnabled;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		if(v == wifiOnOff){
			if(wifiManager.isWifiEnabled()){
				wifiManager.setWifiEnabled(false);
				wifiOnOff.setChecked(false);
				cAdapter.clear();
				sAdapter.clear();
				Log.v("APScanner", "Disabled WiFi");
			} else {
				wifiManager.setWifiEnabled(true);
				wifiOnOff.setChecked(true);
				Log.v("APScanner", "Enabled WiFi");
			}
		}
	}

	private void handleWifiStateChangedAction(int wifiState){
		connectionText.setText(Integer.toString(wifiManager.getWifiState()));
        if(wifiState == WifiManager.WIFI_STATE_ENABLING){
    		wifiOnOff.setChecked(true);
    		wifiOnOff.setClickable(false);
    		connectionText.setText("WIFI_STATE_ENABLING");
			authText.setText("");
    	}else if(wifiState == WifiManager.WIFI_STATE_ENABLED){
    		wifiOnOff.setChecked(true);
    		wifiOnOff.setClickable(true);
    		connectionText.setText("WIFI_STATE_ENABLED");
    		//ddClient = new WebApiDdClient(this);
			authText.setText("");
    	}else if(wifiState == WifiManager.WIFI_STATE_DISABLING){
    		wifiOnOff.setChecked(false);
    		wifiOnOff.setClickable(false);
    		connectionText.setText("WIFI_STATE_DISABLING");
			authText.setText("");
    	}else if(wifiState == WifiManager.WIFI_STATE_DISABLED){
    		wifiOnOff.setChecked(false);
    		wifiOnOff.setClickable(true);
    		connectionText.setText("WIFI_STATE_DISABLED");
			authText.setText("");
    	}else if(wifiState == WifiManager.WIFI_STATE_UNKNOWN){
    		wifiOnOff.setChecked(false);
    		wifiOnOff.setClickable(false);
    		wifiOnOff.setEnabled(false);
    		connectionText.setText("WIFI_STATE_UNKNOWN");
			authText.setText("");
    	}
	}
	
	private void handleSupplicantConnectionChangeAction(Boolean isSupplicantConnected){
		if(isSupplicantConnected){
    		showToast(this, "EXTRA_SUPPLICANT_CONNECTED TRUE");
		}else{
    		showToast(this, "EXTRA_SUPPLICANT_CONNECTED FALSE");
    		onConnectionFailed();
		}
	}
	
	private void handleNetworkStateChangedAction(NetworkInfo nInfo){
		if(nInfo != null){
			showCurrent(true);
			//DevLog.e("*** skipped startScan" );
			showToast(this, "NETWORK_STATE_CHANGED");
		}
	}
	
	private void handleSupplicantStateChangedAction(){
		if(wifiManager.isWifiEnabled()){
			authText.setText(wifiManager.getConnectionInfo().getSupplicantState().name());
		}
	}
	
	private void handleConnectivityAction(String extraInfo, String extraReason){
		NetworkInfo nInfo = connectivityManager.getActiveNetworkInfo();
		Log.i(TAG, "handleConnectivityAction: info: " + extraInfo);
		Log.i(TAG, "handleConnectivityAction: reason: " + extraReason);
		
		if(nInfo != null){
			if(nInfo.isConnectedOrConnecting()){
				if(nInfo.isConnected()){
					if(isTryingConnection && !isTryingDd){
						this.onConnected();
					}
				}
			} else {
				dialogManager.closeDialogs();
				//this.onConnectionFailed();
			}
		}
	}
	
	
	public void showScanResults(){
        sAdapter.clear();
        ApContainer.getInstance().renewAPList();
        if(!isTryingConnection){
        	wifiManager.startScan();
        }
		List<ScanResult> results = wifiManager.getScanResults();
        WifiInfo current = wifiManager.getConnectionInfo();
        if(results != null){
			for(int i=0; i<results.size(); i++){
				apinfo = new APInfo(results.get(i).SSID, results.get(i).capabilities, results.get(i).level, results.get(i).BSSID);
				if(isApFilterEnabled){
			        if (apinfo.getName().startsWith("DIRECT") && !apinfo.getMac().equals(current.getBSSID())){
		        		sAdapter.add(apinfo);
			        	ApContainer.getInstance().addAP(apinfo);
		        	}	
				} else {
					sAdapter.add(apinfo);
		        	ApContainer.getInstance().addAP(apinfo);
				}
	        }
        }
	}

	private void showCurrent(boolean connected){
		cAdapter.clear();
		WifiInfo current = wifiManager.getConnectionInfo();
        if(current.getBSSID() != null){
        	WifiConfiguration aConfig = null;
        	for(WifiConfiguration tConfig : wifiManager.getConfiguredNetworks()){
        		if(tConfig.status == WifiConfiguration.Status.CURRENT){
        			aConfig = tConfig;
        			break;
        		}
        	}
			String capabilities = "";
    		if(aConfig != null)
    			capabilities = new Capability().CapabilityAnalyzer(aConfig);
			apinfo = new APInfo(current.getSSID(), capabilities, current.getRssi(), current.getBSSID());
			cAdapter.add(apinfo);
			if(connected){
				ApContainer.getInstance().setCurrent(apinfo);
			}
        }
	}

	@Override
	public void onConnectionSelected(APInfo info, String password) {
		DevLog.v(TAG, "onConnectionSelected");
		dialogManager.showProgDialog(null, "Connecting...");
		isTryingConnection = true;
		connectionManager.setTargetCapabilities(info.getStatus());
		connectionManager.connect(info.getName(), password);
	}

	@Override
	public void onDeviceDiscoverSelected() {
		DevLog.v(TAG, "onDeviceDiscoverSelected");
		isTryingConnection = true;
		this.onConnected();
	}


	@Override
	public void onRememberedSelected(APInfo info, int netId) {
		DevLog.v(TAG, "onRememberedSelected");
		dialogManager.showProgDialog(null, "Connecting...");
		isTryingConnection = true;
		connectionManager.setTargetCapabilities(info.getStatus());
		connectionManager.connect_remembered(netId);
	}

	@Override
	public void onDisconnectionSelected() {
		DevLog.v(TAG, "onDisconnectionSelected");
		connectionManager.disconnect();
		showCurrent(false);
		showScanResults();
	}


	@Override
	public void onConnected() {
		showCurrent(true);
		showScanResults();
		
		if(isTryingConnection && !isTryingDd){
			ddClient = new WebApiDdClient(this);
			dialogManager.showProgDialog(null, "Searching WebAPI Devices...");
			ddClient.searchDevices(SCALARWEBAPI_SERVICE_TYPE, this, 10000);
			isTryingDd = true;
		}
	}

	@Override
	public void onDisconnected() {
		showCurrent(false);
		showScanResults();
	}

	@Override
	public void onConnectionFailed() {
		DevLog.v(TAG, "onDeviceNotFound");
		connectionManager.disconnect();
		isTryingConnection = false;
		showCurrent(false);
		showScanResults();
	}

	private static void showToast(Context context, String text){
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void onNotifySearchStatus(SearchStatus status) {
		DevLog.i("Scanner DD", "onNotifySearchStatus");
		
		if(status == SearchStatus.SearchError){
			isTryingDd = false;
			DevLog.i("Scanner DD", "DD Search Error");
		}
		else if(status == SearchStatus.SearchCanceled){
			isTryingDd = false;
			DevLog.i("Scanner DD", "DD Search Canceled");
		} else if (status == SearchStatus.SearchStart){
			isTryingDd = true;
			DevLog.i("Scanner DD", "DD Serach Start");
		} else if (status == SearchStatus.SearchFinished){
			isTryingDd = false;
			DevLog.i("Scanner DD", "DD Serach Finished");
			dialogManager.closeDialogs();
		}
	}

	@Override
	public void onFindDevice(DeviceInfo info) {
		DevLog.v(TAG, "onDeviceFound");
		ddClient.cancelSearchDevices();
		DataContainer.getInstance().setBaseUrl(info.getWebApiListURL(SCALARWEBAPI_SERVICE_TYPE[0]));
		
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						guideClient = new GuideInitializer().initialize(ScannerActivity.this, ScannerActivity.this); 
					}
				});
		    }
		}).start();
	}

	@Override
	public void onPageFinishedOfCamera() {
		dialogManager.showProgDialog(null, "Obtaining capabilities...");

    	WebApiCallbacksContainer.setMethodTypeHandler(new MethodTypeHandler(this));
    	WebApiCallbacksContainer.setVersionHandler(new VersionHandler(this));
    	WebApiCallbacksContainer.setGetApplicationInfoCallback(new GetApplicationInfoCallback(this));
    	WebApiCallbacksContainer.setGetAvailableApiListCallback(new GetAvailableApiListCallback(this));
    	WebApiCallbacksContainer.setStartRecModeCallback(new StartRecModeCallback(this));
		webapiBgOperator = new WebApiBackgroundOperator(webapiClient, handler);
		
		webapiBgOperator.getVersions();
		// TODO Temporary
        //dialogManager.closeDialogs();
	}

	@Override
	public void onSuccessStartRecMode(int ret) {
		dialogManager.closeDialogs();
    	startActivity(new Intent(this, ControllerActivity.class));
	}

	@Override
	public void onFailureStartRecMode(int status) {
		if(status != 0){
			dialogManager.showTextDialog("WebAPI Error", "Failed StartRecMode");
		}
	}

	@Override
	public void onSuccessGetAvailableApiList(String[] apis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailureGetAvailableApiList(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetAvailableApiList");
		}
	}

	@Override
	public void onSuccessGetApplicationInfo(String name, String version) {
		// TODO Temporary
		Log.i(TAG, "onSuccessGetApplicationInfo");
		/*
		if(name.equals("AppBase") && version.equals("0.1")){
			webapiBgOperator.startRecMode();
		}
		*/
	}

	@Override
	public void onFailureGetApplicationInfo(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetApplicationInfo");	
		}
	}

	@Override
	public void onSuccessVersionHandler(String[] versions) {
		String lastVersion = null;
		for(String version : versions){
			if(version.equals("1.0")){
				//webapiBgOperator.getMethodTypes(version);
				//webapiBgOperator.getMethodTypes("2.0");
			}
			if( null != version ) {
				Log.e(TAG, "Version: " + version);
				lastVersion = version;
			}
		}
		if(null != lastVersion) {
			Toast.makeText(this, "Calling getMethodTypes with a version: " + lastVersion, Toast.LENGTH_SHORT).show();
			webapiBgOperator.getMethodTypes(lastVersion);
		}
	}

	@Override
	public void onHandledStatusOfVersion(int status) {
		if(status != 0){
			dialogManager.showTextDialog("Error", "Failed GetVersions");
		}
	}

	@Override
	public void onSuccessMethodTypeHandler(String methodName, String[] parameterTypes, String[] resultTypes, String version) {
		Log.d(TAG, "onSuccessMethodTypeHandler: " + methodName);
	}

	@Override
	public void onHandledStatusOfMethodType(int status) {
		Log.d(TAG, "onHandledStatusOfMethodType: "+ status);
		if(status == 0){
			webapiBgOperator.startRecMode();			
		} else {
			dialogManager.showTextDialog("Error", "Failed GetMethodTypes");	
		}
	}

	@Override
	public void onPageFinishedOfGuide() {
		Log.i(TAG, "onPageFinishedOfGuide");
    	GuideCallbacksContainer.setProtocolHandler(new GuideProtocolHandler(this));    	
    	GuideCallbacksContainer.setInitializedFlag(true);
		guideBgOperator = new GuideBackgroundOperator(guideClient, handler);
		guideBgOperator.getServiceProtocols();
	}

	@Override
	public void onSuccessGuideProtocolHandler(String service, String[] protocols) {
		Log.d(TAG, "onSuccessGuideProtocolHandler: " + service);
		if(service.equals("camera")){
			new Thread(new Runnable() {
				public void run() {
					handler.post(new Runnable() {
						public void run() {
							webapiClient = new WebApiInitializer().initialize(ScannerActivity.this, ScannerActivity.this);
						}
					});
			    }
			}).start();
		}
	}

	@Override
	public void onHandledGuideProtocol(int status) {
		Log.d(TAG, "onHandledGuideProtocol: "+ status);
		if(status == 0){
			Log.d(TAG, "onHandledGuideProtocol: OK");
		} else {
			dialogManager.showTextDialog("Error", "Failed GetServiceProtocols");	
		}
	}
}