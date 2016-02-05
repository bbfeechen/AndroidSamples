package ezremote.client.webapi.listener;

public interface GetApplicationInfoListener {
	public void onSuccessGetApplicationInfo(String name, String version);
	public void onFailureGetApplicationInfo(int error);
}
