package ezremote.client.webapi.listener;

public interface GetAvailableExposureCompensationListener {
	public void onSuccessGetAvailableExposureCompensation(int current, int max, int min, int step);
	public void onFailureGetAvailableExposureCompensation(int error);
}
