package ezremote.client.webapi.listener;

public interface GetAvailableApiListListener {
	public void onSuccessGetAvailableApiList(String[] apis);
	public void onFailureGetAvailableApiList(int error);
}
