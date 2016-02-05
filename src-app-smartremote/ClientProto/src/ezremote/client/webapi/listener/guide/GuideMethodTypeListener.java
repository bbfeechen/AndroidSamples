package ezremote.client.webapi.listener.guide;

public interface GuideMethodTypeListener {
	public void onSuccessGuideMethodTypeHandler(String methodName, String[] parameterTypes, String[] resultTypes, String version);
	public void onFailureGuideMethodTypeHandler(int error);
}
