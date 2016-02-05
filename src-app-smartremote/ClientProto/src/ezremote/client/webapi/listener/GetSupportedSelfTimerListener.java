package ezremote.client.webapi.listener;

public interface GetSupportedSelfTimerListener {
	public void onSuccessGetSupportedSelfTimer(int[] supported);
	public void onFailureGetSupportedSelfTimer(int error);
}
