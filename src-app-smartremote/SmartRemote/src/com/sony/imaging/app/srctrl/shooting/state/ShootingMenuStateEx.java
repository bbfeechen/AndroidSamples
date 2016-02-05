package com.sony.imaging.app.srctrl.shooting.state;

import android.app.Activity;
import android.os.Bundle;

import com.sony.imaging.app.base.BaseApp;
import com.sony.imaging.app.base.caution.Info;
import com.sony.imaging.app.base.common.EventParcel;
import com.sony.imaging.app.base.shooting.S1OffEEState;
import com.sony.imaging.app.base.shooting.ShootingMenuState;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.fw.AppRoot;
import com.sony.imaging.app.srctrl.shooting.ExposureModeControllerEx;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.util.StateController.AppCondition;
import com.sony.imaging.app.util.PTag;
import com.sony.imaging.app.util.ApoWrapper.APO_TYPE;

/**
 * 
 * Added state reporting function to StateController.
 * @author 0000138134
 *
 */
public class ShootingMenuStateEx extends ShootingMenuState {
	StateController stateController;
	private final String ID_LASTBASTIONLAYOUT = "LastBastionLayout";

	@Override
	public void onResume(){
		super.onResume();
		stateController = StateController.getInstance();
		stateController.setState(this);
		
		stateController.setAppCondition(AppCondition.SHOOTING_MENU);
	}
	
	@Override
	public void onPause(){
		if(!AppCondition.DIAL_INHIBIT.equals(StateController.getInstance().getAppCondition())){
			StateController.getInstance().setAppCondition(AppCondition.SHOOTING_INHIBIT);
		}
		super.onPause();
	}
	
	@Override
	public APO_TYPE getApoType() {
		return APO_TYPE.NONE;
	}
	
	@Override
	protected boolean canRemoveState() {
		boolean ret = true;
		if (ModeDialDetector.hasModeDial()) {
			if(ExposureModeControllerEx.getInstance().getCautionId() != Info.INVALID_CAUTION_ID){
				ret = ExposureModeControllerEx.getInstance().isValidDialPosition();
			}
		}
		return ret;
	}
	
	@Override
	protected String getLastBastionLayoutName() {
		return ID_LASTBASTIONLAYOUT;
	}
	
	@Override
	public int pushedPlayBackKey() {
		return INVALID;
	}
	
}
