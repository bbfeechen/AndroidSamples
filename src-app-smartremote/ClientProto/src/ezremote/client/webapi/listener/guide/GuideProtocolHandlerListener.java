package ezremote.client.webapi.listener.guide;

public interface GuideProtocolHandlerListener {
	public void onSuccessGuideProtocolHandler(String service, String[] protocols);
	public void onHandledGuideProtocol(int status);
}
