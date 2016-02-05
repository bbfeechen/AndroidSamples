package com.sony.imaging.app.srctrl.shooting.keyhandler;

import android.util.Log;
import android.view.KeyEvent;

import com.sony.imaging.app.base.caution.CautionUtilityClass;
import com.sony.imaging.app.base.shooting.trigger.S1OnEEStateKeyHandler;
import com.sony.imaging.app.fw.AppRoot.USER_KEYCODE;
import com.sony.imaging.app.srctrl.caution.InfoEx;
import com.sony.imaging.app.srctrl.util.cameraoperation.CameraOperationTouchAFPosition;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;

public class S1OnEEStateKeyHandlerForTouchAF extends S1OnEEStateKeyHandler
{
    
    @Override
    public int onKeyUp(int keyCode, KeyEvent event)
    {
        int ret = INVALID;
        int code = event.getScanCode();
    	
        switch (code)
        {
        case USER_KEYCODE.S1_ON:
        case USER_KEYCODE.LENS_ATTACH:
            CameraOperationTouchAFPosition.set(null, null, null);
            ret = HANDLED;
            break;
        default:
            break;
        }
        return ret;
    }
    @Override
    public int onKeyDown(int keyCode, KeyEvent event)
    {
        int ret = INVALID;
        int code = event.getScanCode();
    	
        switch (code)
        {
        case USER_KEYCODE.S1_ON:
        case USER_KEYCODE.LENS_ATTACH:
        	// Do nothing here. Handle in onKeyUp().
        	ret = INVALID;
        	break;
        case 0x205: // S1_2 : this event comes after S1_ON(0x204)
        case USER_KEYCODE.S2_ON:
        case USER_KEYCODE.S2_OFF:
        	ret = INVALID;
        	break;
        case USER_KEYCODE.RING_CLOCKWISE:	// IMDLAPP6-1375
        case USER_KEYCODE.RING_COUNTERCW:	// IMDLAPP6-1375
        	// Lens Ring
        	ret = super.onKeyDown(keyCode, event);
        	break;
        case USER_KEYCODE.FOCUS_MODE_DIAL_CHANGED:	// IMDLAPP6-1340
        	// AF/DMF/MF Switch : Touch AF Mode is canceled. 
        	TouchAFCurrentPositionParams param = CameraOperationTouchAFPosition.get();
        	if (param.set)
        	{
        		CameraOperationTouchAFPosition.leaveTouchAFMode(true);
        	}
        	ret = super.onKeyDown(keyCode, event);
        	break;
        default:
        	CautionUtilityClass.getInstance().requestTrigger(InfoEx.CAUTION_ID_SMART_REMOTE_RSRC_ID_STRID_FUNC_SRPLUS_IN_TOUCH_AF_MODE);    	
        	break;
        }
        return ret;        	
    }
}
