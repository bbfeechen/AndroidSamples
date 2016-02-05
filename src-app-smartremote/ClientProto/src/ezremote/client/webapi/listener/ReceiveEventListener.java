package ezremote.client.webapi.listener;

public interface ReceiveEventListener {
	public void onSuccessReceiveEvent(String arg0, boolean arg1, int arg2, int arg3,
			int arg4, int arg5, String[] arg6, String[] arg7, boolean[] arg8, String[] arg9, String[] arg10);
	public void onFailureReceiveEvent(int error);
}
