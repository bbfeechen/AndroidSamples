package com.sony.imaging.app.srctrl.shooting;

import android.hardware.Camera.Parameters;
import android.util.Log;
import android.util.Pair;

import com.sony.imaging.app.base.shooting.ForceSettingState;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DriveModeController;
import com.sony.imaging.app.base.shooting.camera.PictureEffectController;
import com.sony.imaging.app.base.shooting.camera.PictureQualityController;
import com.sony.scalar.hardware.CameraEx.ParametersModifier;

public class SRCtrlForceSettingState extends ForceSettingState {
    private static final String tag = SRCtrlForceSettingState.class.getName();
    
	@Override
	protected boolean isForceSetting() {
		return true;
	}

	@Override
	protected Pair<Parameters, ParametersModifier> onSetForceSetting(Pair<Parameters, ParametersModifier> params) {
		
		DriveModeController dController = DriveModeController.getInstance();
		String dMode = dController.getValue(DriveModeController.DRIVEMODE);
		if(dMode.equals(DriveModeController.SINGLE) || dMode.equals(DriveModeController.SELF_TIMER)){
		    // OK
		} else if (dController.isAvailable(DriveModeController.BURST)){
            dController.setValue(DriveModeController.DRIVEMODE, DriveModeController.SINGLE);        // set to Single
		}
		
		Pair<Parameters, ParametersModifier> curParams = CameraSetting.getInstance().getParameters();

		if(curParams.second.getPictureStorageFormat().equals(ParametersModifier.PICTURE_STORAGE_FMT_RAW)) {
            params.first.setJpegQuality(PictureQualityController.QUALITY_FINE);
            params.second.setPictureStorageFormat(ParametersModifier.PICTURE_STORAGE_FMT_RAWJPEG);
		}
		
		
		// [SR2.0に対する特別暫定処理]
		// BaseApp側でオプション値の排他ができていないため項目削除。
		// SR1.2->SR2.0アップデート時に排他すべきMiniature-Autoが設定されていたらH-Centerへ変更する
		final String picEffectInhParam = PictureEffectController.MINIATURE_AUTO;
        final String resetPicEffectParam = PictureEffectController.MINIATURE_HCENTER;
        String curPicEffectParam = PictureEffectController.MINIATURE_AUTO;
        
        PictureEffectController pec = PictureEffectController.getInstance();
        String picEffect = pec.getValue(PictureEffectController.PICTUREEFFECT);
        if (picEffect != null && PictureEffectController.MODE_MINIATURE.equals(picEffect)) {
        	curPicEffectParam = pec.getValue(PictureEffectController.MODE_MINIATURE);
        }
        if (picEffectInhParam.equals(curPicEffectParam)) {
        	pec.setValue(PictureEffectController.MODE_MINIATURE, PictureEffectController.MINIATURE_HCENTER);
        	Log.e( tag, "PictureEffect changed from AUTO to HCENTER");
        }
		
		return params;
	}
}
