package ezremote.client.webapi.listener.touchafposition;

public interface SetTouchAFPositionListener {
	public void onSuccessSetTouchAFPosition(boolean AFResult, String AFType, double[] touchCordinate, double[] AFBoxLeftTop, double[] AFBoxRightBottom);
	public void onFailureSetTouchAFPosition(int error);
}
