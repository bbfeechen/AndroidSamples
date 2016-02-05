package ezremote.client.callback;

import java.io.InputStream;

public interface PostviewCallback {
	public abstract void onStartPostview(InputStream in);
	public abstract void onStopPostview();
	public abstract void onReceivingFailed();
}
