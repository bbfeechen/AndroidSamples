package ezremote.client.webapi.listener;

public interface StartLiveviewListener {
	public void onSuccessStartLiveview(String url);
	public void onFailureStartLiveview(int error);
}
