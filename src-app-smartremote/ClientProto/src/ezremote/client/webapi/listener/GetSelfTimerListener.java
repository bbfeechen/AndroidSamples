package ezremote.client.webapi.listener;

public interface GetSelfTimerListener {
	public void onSuccessGetSelfTimer(int timer);
	public void onFailureGetSelfTimer(int error);
}
