package com.sony.imaging.app.srctrl.shooting.keyhandler;

import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.fw.BaseKeyHandler;
import com.sony.imaging.app.fw.ICustomKey;

public class NormalCaptureStateKeyHandlerEx extends BaseKeyHandler
{
    // copied from ShootingKeyHandlerBase
    @Override
    protected String getKeyConvCategory() {
        ExposureModeController cntl = ExposureModeController.getInstance();
        String expMode = cntl.getValue(null);
        String mode;
        if(ExposureModeController.PROGRAM_AUTO_MODE.equals(expMode)){
            mode = ICustomKey.CATEGORY_SHOOTING_P;
        }
        else if(ExposureModeController.APERATURE_MODE.equals(expMode)){
            mode = ICustomKey.CATEGORY_SHOOTING_A;
        }
        else if(ExposureModeController.SHUTTER_MODE.equals(expMode)){
            mode = ICustomKey.CATEGORY_SHOOTING_S;
        }
        else if(ExposureModeController.MANUAL_MODE.equals(expMode)){
            mode = ICustomKey.CATEGORY_SHOOTING_M;
        }
        else{
            mode = ICustomKey.CATEGORY_SHOOTING_OTHER;
        }
        return mode;
    }
    
    @Override
    public int pushedS2Key() {
        return INVALID;
    }
    @Override
    public int pushedUmRemoteS2Key()
    {
        return INVALID;
    }
}
