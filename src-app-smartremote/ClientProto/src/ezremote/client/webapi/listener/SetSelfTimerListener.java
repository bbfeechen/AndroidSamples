package ezremote.client.webapi.listener;

public interface SetSelfTimerListener {
	public void onSuccessSetSelfTimer(int ret);
	public void onFailureSetSelfTimer(int error);
}
