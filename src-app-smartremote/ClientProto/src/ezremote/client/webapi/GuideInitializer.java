package ezremote.client.webapi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.sony.mexi.orb.android.client.OrbAndroidClient;
import com.sony.mexi.orb.android.client.OrbAndroidLogger;
import com.sony.mexi.orb.android.client.OrbAndroidWebView;
import com.sony.mexi.orb.client.serviceguide.v1_0.ServiceGuideClient;

import ezremote.client.callback.GuideClientListener;
import ezremote.client.data.DataContainer;

public class GuideInitializer {
	private static final String TAG = GuideInitializer.class.getSimpleName();
	private GuideClientListener webListener;

    private OrbAndroidWebView orbWebView;
	private ServiceGuideClient guideClient;
    
    public ServiceGuideClient initialize(final Context context, final GuideClientListener webListener){
    	Log.i(TAG, "Start Initializing guideClient");
    	this.webListener = webListener;
    	WebView webView = createChromeWebView(context);    	
        orbWebView = new OrbAndroidWebView(webView,	DataContainer.getInstance().getBaseUrl()) {
                @Override
                public void onLoadStarted(WebView webView, String url) {
                    guideClient = new ServiceGuideClient(new OrbAndroidClient("guide", orbWebView));
                    Log.i(TAG, "onLoadeStarted");
                }

                @Override
                public void onLoadFinished(WebView webView, String url) {
                	guideClient.open(null);
                    Log.i(TAG, "onLoadeFinished");
        			GuideInitializer.this.webListener.onPageFinishedOfGuide();
                }
        	};
        orbWebView.load();
        return guideClient;
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
