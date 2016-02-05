package com.sony.imaging.app.srctrl.shooting.keyhandler;

import android.view.KeyEvent;

import com.sony.imaging.app.base.shooting.trigger.MfAssistKeyHandler;
import com.sony.imaging.app.fw.AppRoot.USER_KEYCODE;
import com.sony.imaging.app.fw.IKeyFunction;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;

public class S1OnEEStateKeyHandlerForTouchAFAssist extends MfAssistKeyHandler
{
    private static final String tag = S1OnEEStateKeyHandlerForTouchAFAssist.class.getName();
    
    private boolean mS1KeyDown = false;

    @Override
    public int onKeyDown(int keyCode, KeyEvent event)
    {
        int code = event.getScanCode();
        switch (code)
        {
        case USER_KEYCODE.S1_ON:
            mS1KeyDown = true;
            return HANDLED;
        case USER_KEYCODE.S2_ON:
            if(mS1KeyDown)
            {
                mS1KeyDown = false;
                StateController stateController = StateController.getInstance();
                stateController.setLastAppConditionBeforeCapturing(AppCondition.SHOOTING_REMOTE_TOUCHAF);
                return super.onKeyDown(keyCode, event);
            }
            return INVALID;
        case USER_KEYCODE.S1_OFF:
        case USER_KEYCODE.S2_OFF:
            return INVALID;
        default:
            break;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public int onKeyUp(int keyCode, KeyEvent event)
    {
        int code = event.getScanCode();
        switch (code)
        {
        case USER_KEYCODE.S1_ON:
            if(mS1KeyDown) {
                mS1KeyDown = false;
                returnToNormalEE();
                return HANDLED;
            }
            return INVALID;
        case USER_KEYCODE.S1_OFF:
        case USER_KEYCODE.S2_ON:
        case USER_KEYCODE.S2_OFF:
            return INVALID;
        case USER_KEYCODE.LENS_ATTACH:
        default:
            break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public int onConvertedKeyDown(KeyEvent event, IKeyFunction func)
    {
        int result = INVALID;
        int scanCode = event.getScanCode();
        switch (scanCode) {
        case USER_KEYCODE.S1_ON:
        case USER_KEYCODE.S1_OFF:
        case USER_KEYCODE.S2_ON:
        case USER_KEYCODE.S2_OFF:
        case USER_KEYCODE.UP:
        case USER_KEYCODE.DOWN:
        case USER_KEYCODE.RIGHT:
        case USER_KEYCODE.LEFT:
        case USER_KEYCODE.CENTER:
        case USER_KEYCODE.DIAL1_LEFT:
        case USER_KEYCODE.DIAL1_RIGHT:
        case USER_KEYCODE.DIAL2_LEFT:
        case USER_KEYCODE.DIAL2_RIGHT:
        case USER_KEYCODE.DIAL3_LEFT:
        case USER_KEYCODE.DIAL3_RIGHT:
        case USER_KEYCODE.SHUTTLE_LEFT:
        case USER_KEYCODE.SHUTTLE_RIGHT:
        case USER_KEYCODE.MENU:
        case USER_KEYCODE.DELETE:
        case USER_KEYCODE.EV_DIAL_CHANGED:
        case USER_KEYCODE.EXPAND_FOCUS:
            result = super.onConvertedKeyDown(event, func);
            break;
        default:
            break;
        }
        return result; 
    }

    @Override
    public int onConvertedKeyUp(KeyEvent event, IKeyFunction func)
    {
        int result = INVALID;
        int scanCode = event.getScanCode();
        switch (scanCode) {
        case USER_KEYCODE.UP:
        case USER_KEYCODE.DOWN:
        case USER_KEYCODE.RIGHT:
        case USER_KEYCODE.LEFT:
        case USER_KEYCODE.CENTER:
        case USER_KEYCODE.DIAL1_LEFT:
        case USER_KEYCODE.DIAL1_RIGHT:
        case USER_KEYCODE.DIAL2_LEFT:
        case USER_KEYCODE.DIAL2_RIGHT:
        case USER_KEYCODE.DIAL3_LEFT:
        case USER_KEYCODE.DIAL3_RIGHT:
        case USER_KEYCODE.SHUTTLE_LEFT:
        case USER_KEYCODE.SHUTTLE_RIGHT:
        case USER_KEYCODE.MENU:
        case USER_KEYCODE.DELETE:
        case USER_KEYCODE.EV_DIAL_CHANGED:
        case USER_KEYCODE.EXPAND_FOCUS:
        case USER_KEYCODE.LENS_ATTACH:
            result = super.onConvertedKeyUp(event, func);
            break;
        default:
            break;
        }
        return result;
    }

    @Override
    public int detachedLens()
    {
        StateController stateController = StateController.getInstance();
        stateController.setLastAppConditionBeforeCapturing(AppCondition.SHOOTING_EE);
        returnToNormalEE();
        return HANDLED;
    }
}
