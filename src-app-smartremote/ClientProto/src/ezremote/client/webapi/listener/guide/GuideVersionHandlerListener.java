package ezremote.client.webapi.listener.guide;

public interface GuideVersionHandlerListener {
	public void onSuccessGuideVersionHandler(String[] versions);
	public void onFailureGuideVersionHandler(int error);	
}
