package ezremote.client.webapi.listener;

public interface GetExposureCompensationListener {
	public void onSuccessGetExposureCompensation(int exposure);
	public void onFailureGetExposureCompensation(int error);
}
