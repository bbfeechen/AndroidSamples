package ezremote.client.webapi.listener.touchafposition;

public interface GetTouchAFPositionListener {
       public void onSuccessGetlTouchAFPosition(boolean set, double[] touchCordinate);
       public void onFailureGetTouchAFPosition(int error);
}