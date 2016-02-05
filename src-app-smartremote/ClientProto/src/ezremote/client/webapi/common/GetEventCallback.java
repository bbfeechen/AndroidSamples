package ezremote.client.webapi.common;

import com.sony.mexi.webapi.DefaultCallbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.GetEventParams;

import ezremote.client.webapi.listener.GetEventListener;

public class GetEventCallback
extends DefaultCallbacks
implements com.sony.scalar.webapi.service.camera.v1_2.getevent.GetEventCallback
{
    GetEventListener listener;

    public GetEventCallback(GetEventListener listener){
            this.listener = listener;
    }

    @Override
    public void handleStatus(int error, String message){
    listener.onFailureGetEvent(error);
    }


    @Override
    public void returnCb(GetEventParams[] params) {
            listener.onSuccessGetEvent(params);
    }
}
