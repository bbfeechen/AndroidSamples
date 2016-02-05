package com.sony.mexi.orb.android.client;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sony.mexi.webapi.Status;

public class OrbAndroidWebView {

    private String url = null;
    private WebView webView = null;
    private boolean isLoaded = false;

    public OrbAndroidWebView(WebView wv, String url) {
        setWebView(wv);
        setUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView(WebView wv) {
        if (wv == null) {
            throw new IllegalArgumentException();
        }
        webView = wv;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setUrl(String url) {
        if (url == null || url.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.url = url;
    }

    public void load() {
        if (!isLoaded) {
            onLoadStarted(webView, url);
            webView.loadUrl(url);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // sometimes not completed before onLoadFinished
                    // onLoadStarted(view, url);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    isLoaded = true;
                    onLoadFinished(view, url);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void onLoadStarted(WebView view, String url) {
    }

    public void onLoadFinished(WebView view, String url) {
    }

    public Status runJs(String code) {
        if (isLoaded) {
            OrbAndroidLogger.log("orb client", code);
            webView.loadUrl("javascript:" + code);
            return Status.OK;
        } else {
            return Status.ILLEGAL_STATE;
        }
    }

    public void addJavascriptInterface(Object obj, String interfaceName) {
        webView.addJavascriptInterface(obj, interfaceName);
    }

}
