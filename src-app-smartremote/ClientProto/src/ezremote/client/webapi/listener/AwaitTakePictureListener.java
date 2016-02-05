package ezremote.client.webapi.listener;

public interface AwaitTakePictureListener {
	public void onSuccessAwaitTakePicture(String[] urls);
	public void onFailureAwaitTakePicture(int error);
}
