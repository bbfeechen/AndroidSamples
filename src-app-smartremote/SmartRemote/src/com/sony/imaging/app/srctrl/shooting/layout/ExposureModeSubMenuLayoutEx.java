package com.sony.imaging.app.srctrl.shooting.layout;

import com.sony.imaging.app.base.menu.layout.ExposureModeMenuLayout;
import com.sony.imaging.app.srctrl.shooting.ModeDialCautionCaller;
import com.sony.imaging.app.srctrl.shooting.ModeDialCautionCaller.CautionType;
import com.sony.imaging.app.srctrl.util.StateController;

/**
 * 
 * Displays mode dial caution when closing layout, if it's in unsupported position.
 * @author 0000138134
 *
 */
public class ExposureModeSubMenuLayoutEx extends ExposureModeMenuLayout {
/*	@Override
	protected String getMenuLayoutID() {
        return "ID_EXPOSUREMODEMENULAYOUT";
    }
*/	
	@Override
	public int pushedPlayBackKey(){
		return INVALID;
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
    @Override
    public void closeLayout() {

/*    	if(!StateController.getInstance().isClosingShootingState()){
	    	ModeDialCautionCaller cautionCaller = new ModeDialCautionCaller();
	    	cautionCaller.checkModeDialCautionRequirement();
			if(cautionCaller.requireModeDialCaution()){
				if(StateController.getInstance().hasModeDial()){
					cautionCaller.showChangeModeDialCaution(getActivity(), CautionType.ON_EE);
				} else {
					cautionCaller.showModeChangedCaution();
				}
			}
    	}
*/		super.closeLayout();
    }
}
