package ezremote.client.webapi.listener;

import com.sony.scalar.webapi.service.camera.v1_2.common.struct.GetEventParams;

public interface GetEventListener {
	public void onSuccessGetEvent(GetEventParams[] params);
	public void onFailureGetEvent(int error);
}
