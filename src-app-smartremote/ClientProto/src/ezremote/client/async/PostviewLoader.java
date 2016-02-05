package ezremote.client.async;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ezremote.client.callback.PostviewCallback;
import ezremote.client.data.ImageContainer;
import ezremote.client.util.DevLog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class PostviewLoader extends AsyncTask<String, String, Void> implements OnCancelListener {
	private String url;
	private PostviewCallback pvCb;
	private InputStream in;
	
	public PostviewLoader(PostviewCallback pvCb){
		this.pvCb = pvCb;
	}
	
	@Override
	protected Void doInBackground(String... params) {
		url = params[0];
		DevLog.i("PostviewLoader", url);

		try {
			in = getImageStream(url);			
		} catch (MalformedURLException e) {
			DevLog.e("ImageLoader", "getImage malformedURLException");
			pvCb.onReceivingFailed();
		} catch (IOException e) {
			DevLog.e("ImageLoader", "getImage IOException");
			pvCb.onReceivingFailed();
		}
		DevLog.i("PostviewLoder", "Finished Downloading");
    	pvCb.onStartPostview(in);
		return null;
	}
    
    
	@Override
	protected void onCancelled(){
		System.out.println("-------------- onCancelled ------------------");
	}
    
	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
	}

	
    private InputStream getImageStream(String url_str) throws MalformedURLException, IOException{
    	URL url = new URL(url_str);
    	try{
    		return url.openStream();
    	} catch (IOException e){
    		DevLog.e("PostviewLoader", "IOException");
    	}    	
    	return null;
    }
}
