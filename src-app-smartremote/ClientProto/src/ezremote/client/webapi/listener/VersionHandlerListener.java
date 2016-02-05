package ezremote.client.webapi.listener;

public interface VersionHandlerListener {
	public void onSuccessVersionHandler(String[] versions);
	public void onHandledStatusOfVersion(int status);	
}
