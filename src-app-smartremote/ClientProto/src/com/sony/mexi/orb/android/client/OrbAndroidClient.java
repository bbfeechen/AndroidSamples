package com.sony.mexi.orb.android.client;

import com.sony.mexi.json.JsArray;
import com.sony.mexi.json.JsFunction;
import com.sony.mexi.json.JsNull;
import com.sony.mexi.json.JsValue;
import com.sony.mexi.json.Json;
import com.sony.mexi.orb.client.OrbAbstractClient;
import com.sony.mexi.webapi.CloseHandler;
import com.sony.mexi.webapi.Status;
import com.sony.mexi.webapi.OpenHandler;

public class OrbAndroidClient extends OrbAbstractClient {

    private String invokerName = null;
    private OrbAndroidWebView webView = null;

    private class Invoker {
        // used by JS
        @SuppressWarnings("unused")
        public String invoke(String name, String params) {
            JsValue val = Json.parse(params);
            if (val == null || val.type() != JsValue.Type.ARRAY) {
                return null;
            }
            JsArray ja = (JsArray) val;
            JsFunction func = registry.get(name);
            JsValue res = func.invoke(ja);
            if (res == null) {
                res = JsNull.getInstance();
            }
            return res.toString();
        }
    }

    public OrbAndroidClient(String name, OrbAndroidWebView webView) {
        setName(name);
        setWebView(webView);

        Invoker invoker = new Invoker();
        webView.addJavascriptInterface(invoker, invokerName);
    }

    protected void setName(String name) {
        super.setName(name);
        this.invokerName = name + "$callbacks";
    }

    private void setWebView(OrbAndroidWebView webView) {
        if (webView == null) {
            throw new IllegalArgumentException();
        }
        this.webView = webView;
        assert (webView.isLoaded());
    }

    public Status call(
            String method,
            JsArray params,
            int timeout,
            String onReturn,
            String onStatus) {
        if (method != null
            && method.length() != 0
            && params != null
            && onReturn != null
            && onReturn.length() != 0
            && onStatus != null
            && onStatus.length() != 0) {
            if (timeout <= 0) {
                timeout = 0;
            } else if (timeout < 1000) {
                timeout = 1000;
            }
            return webView.runJs(clientName
                    + ".call({method:'" + method
                    + "',params:" + params.toString()
                    + ",timeout:" + Integer.toString(timeout)
                    + ",callbacks:{onResult:'" + onReturn
                    + "',onStatus:'" + onStatus
                    + "'}})");
        } else {
            return Status.ILLEGAL_ARGUMENT;
        }
    }

    private String toClosure(String cbName) {
        return "function(){var ps=[];for(var i=0;i<arguments.length;i++)ps.push(arguments[i]);"
                + invokerName + ".invoke('" + cbName + "',JSON.stringify(ps));}";
    }

    public Status register(String cbName, JsFunction func) {
        Status status = super.register(cbName, func);
        if (status == Status.OK) {
            return webView.runJs(clientName
                    + ".register({callback:'" + cbName
                    + "',functor:" + toClosure(cbName)
                    + "})");
        } else {
            return status;
        }
    }

    public Status unregister(String cbName) {
        Status status = super.unregister(cbName);
        if (status == Status.OK) {
            return webView.runJs(clientName
                    + ".unregister({name:'" + cbName
                    + "'})");
        } else {
            return status;
        }
    }

    @Override
    public int open(OpenHandler handler) {
        if (webView.isLoaded() && super.open(handler) == Status.OK.toInt()) {
            Status status;
            if (handler == null) {
                status = webView.runJs(clientName + ".open()");
            } else {
                status = webView.runJs(clientName
                            + ".open({onOpen:" + toClosure(handler.getName() + "#R")
                            + ",onError:" + toClosure(handler.getName() + "#S")
                            + "})");
            }
            return status.toInt();
        } else {
            return Status.ILLEGAL_STATE.toInt();
        }
    }

    @Override
    public int close(CloseHandler handler) {
        if (isOpen) {
            webView.runJs(clientName + ".close()");
        }
        return super.close(handler);
    }

}
