package ezremote.client.webapi.listener;

public interface MethodTypeListener {
	public void onSuccessMethodTypeHandler(String methodName, String[] parameterTypes, String[] resultTypes, String version);
	public void onHandledStatusOfMethodType(int status);
}
