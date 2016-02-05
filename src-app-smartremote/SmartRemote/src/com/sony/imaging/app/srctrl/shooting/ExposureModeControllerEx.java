package com.sony.imaging.app.srctrl.shooting;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.ExposureModeController;
import com.sony.imaging.app.base.shooting.trigger.ModeDialDetector;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.base.BaseApp;

public class ExposureModeControllerEx extends ExposureModeController {
	
	@Override
	public boolean isAvailable(String tag) {
		boolean ret = super.isAvailable(tag);
		if(SCENE_SELECTION_MODE.equals(tag)) {
			ret = false;
		}
		return ret;
	}

	@Override
	public List<String> getAvailableValue(String tag) {
		List<String> availables = new ArrayList<String>();
		if (tag.equals(EXPOSURE_MODE)) {
			 availables.add(PROGRAM_AUTO_MODE);
			 availables.add(APERATURE_MODE);
			 availables.add(SHUTTER_MODE);
			 availables.add(MANUAL_MODE);
			 availables.add(INTELLIGENT_AUTO_MODE);
			 if (0 != (BaseApp.REC_MODE_MOVIE & SRCtrl.getRecMode())) {
				 availables.add(MOVIE_MODE);
			 }
		}
		
		 if (0 != (BaseApp.REC_MODE_MOVIE  & SRCtrl.getRecMode())) {
			if (MOVIE_MODE.equals(tag)) {
				 availables.add(MOVIE_PROGRAM_AUTO_MODE);
				 availables.add(MOVIE_APERATURE_MODE);
				 availables.add(MOVIE_SHUTTER_MODE);
				 availables.add(MOVIE_MANUAL_MODE);
			}
		}

		if (availables.isEmpty()) {
			return null;
		 } else {
			 return availables;	
		 }
	}

	@Override
	public boolean isValidDialPosition() {
		boolean isValid = false;
		int code = ModeDialDetector.getModeDialPosition();
		String expMode = null;
		if (ModeDialDetector.INVALID_SCAN_CODE != code) {
			expMode = ExposureModeController.scancode2Value(code);
			if(expMode.equals(SCENE_SELECTION_MODE)) {
				isValid = false;
				return isValid;
			}
		} else {
			isValid = false;
			return isValid;
		}
		
		isValid = isValidValue(expMode);
		return isValid;
	}
	
	@Override
	public List<String> getSupportedValue(String tag) {
		  List<String> supported = new ArrayList<String>();
		  if (!ModeDialDetector.hasModeDial()) {
		   if (tag.equals(EXPOSURE_MODE)) {
		    supported.add(PROGRAM_AUTO_MODE);
		    supported.add(APERATURE_MODE);
		    supported.add(SHUTTER_MODE);
		    supported.add(MANUAL_MODE);
		    supported.add(INTELLIGENT_AUTO_MODE);
		   }
		   if (supported.isEmpty()) {
		    return null;
		   }
		  } else {
		   supported = super.getSupportedValue(tag);
		  }
		  return supported;
	}


}