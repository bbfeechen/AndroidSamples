package ezremote.client.webapi;

import com.sony.mexi.orb.android.client.OrbAndroidClient;
import com.sony.mexi.orb.android.client.OrbAndroidLogger;
import com.sony.mexi.orb.android.client.OrbAndroidWebView;
import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.SmartRemoteControlTestClientClient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import ezremote.client.callback.WebApiClientListener;
import ezremote.client.data.DataContainer;

public class WebApiInitializer {
	private static final String TAG = WebApiInitializer.class.getSimpleName();
	private WebApiClientListener webListener;

    private OrbAndroidWebView orbWebView;
	private SmartRemoteControlTestClientClient cameraClient;
    
    public SmartRemoteControlTestClientClient initialize(final Context context, final WebApiClientListener webListener){
    	Log.i(TAG, "Start Initializing webapiClient");
    	this.webListener = webListener;
    	WebView webView = createChromeWebView(context);  
        orbWebView = new OrbAndroidWebView(webView,	DataContainer.getInstance().getBaseUrl()) {
            @Override
            public void onLoadStarted(WebView webView, String url) {
                cameraClient = new SmartRemoteControlTestClientClient(new OrbAndroidClient("camera", orbWebView));
                Log.i(TAG, "onLoadStarted");
            }

            @Override
            public void onLoadFinished(WebView webView, String url) {
            	cameraClient.open(null);
                Log.i(TAG, "onLoadFinished");
    			WebApiInitializer.this.webListener.onPageFinishedOfCamera();
            }
    	};
        orbWebView.load();
        return cameraClient;
    }

    private WebView createChromeWebView(final Context context){
    	WebView webView = new WebView(context);
    	webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                    final android.webkit.JsResult result) {
                OrbAndroidLogger.log("orb client", url);
                new AlertDialog.Builder(context)
                        .setTitle("JS Alert")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            };
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                OrbAndroidLogger.log("orb client", message);
            }
        });
    	return webView;
    }
}
