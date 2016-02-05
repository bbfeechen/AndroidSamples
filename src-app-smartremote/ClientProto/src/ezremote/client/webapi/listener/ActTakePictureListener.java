package ezremote.client.webapi.listener;

public interface ActTakePictureListener {
	public void onSuccessActTakePicture(String[] urls);
	public void onFailureActTakePicture(int error);
}
