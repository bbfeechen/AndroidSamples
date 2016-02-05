package ezremote.client.webapi.listener;

public interface GetAvailableSelfTimerListener {
	public void onSuccessGetAvailableSelfTimer(int current, int[] available);
	public void onFailureGetAvailableSelfTimer(int error);
}
