package ezremote.client.callback;

public interface ConnectionStateCallback {
	public abstract void onConnected();
	public abstract void onDisconnected();
	public abstract void onConnectionFailed();
}
