package com.sony.imaging.app.srctrl.shooting.layout;

import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;

import com.sony.imaging.app.base.shooting.layout.StableLayout;
import com.sony.imaging.app.srctrl.util.StateController;

public class SRCtrlStableLayout extends StableLayout
{
    private static final String tag = SRCtrlStableLayout.class.getName();
    
    public SRCtrlStableLayout()
    {
        super();
        this.idleHandler = new _SRCtrlDelayAttachView();
    }
    
    protected void detachView() {
        super.detachView();
        Looper.myQueue().removeIdleHandler(mNotifyCameraSetupFinishedIdleHandler);
    }
    
    protected class _SRCtrlDelayAttachView extends StableLayout.DelayAttachView
    {
        public boolean queueIdle()
        {
            StateController sc = StateController.getInstance();
            sc.setDuringCameraSetupRoutine(true);
            Looper.myQueue().removeIdleHandler(mNotifyCameraSetupFinishedIdleHandler);
            Looper.myQueue().addIdleHandler(mNotifyCameraSetupFinishedIdleHandler);
            
            return super.queueIdle(); // Invoking
                                      // StableLayout$DelayAttachView#queIdle()
        }
    }
    
    private class _SRCtrlNotifyCameraSetupFinishedIdleHandler implements IdleHandler {

        @Override
        public boolean queueIdle()
        {
            StateController sc = StateController.getInstance();
            sc.setDuringCameraSetupRoutine(false);
            return false;
        }
        
    }
    
    private _SRCtrlNotifyCameraSetupFinishedIdleHandler mNotifyCameraSetupFinishedIdleHandler= new _SRCtrlNotifyCameraSetupFinishedIdleHandler();
}
