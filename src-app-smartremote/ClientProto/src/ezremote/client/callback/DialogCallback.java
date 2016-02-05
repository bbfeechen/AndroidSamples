package ezremote.client.callback;

import ezremote.client.data.ap.APInfo;

public interface DialogCallback {
	public abstract void onConnectionSelected(APInfo info, String password);
	public abstract void onRememberedSelected(APInfo info, int netId);
	public abstract void onDisconnectionSelected();
	public abstract void onDeviceDiscoverSelected();
}
