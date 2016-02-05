package ezremote.client.webapi.listener;

public interface GetSupportedExposureCompensationListener {
	public void onSuccessGetSupportedExposureCompensation(int[] max, int[] min, int[] step);
	public void onFailureGetSupportedExposureCompensation(int error);
}
