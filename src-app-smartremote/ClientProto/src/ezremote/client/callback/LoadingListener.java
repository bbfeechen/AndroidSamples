package ezremote.client.callback;

import java.io.InputStream;

public interface LoadingListener {
	public abstract void onLoadFinished(InputStream in);
}
