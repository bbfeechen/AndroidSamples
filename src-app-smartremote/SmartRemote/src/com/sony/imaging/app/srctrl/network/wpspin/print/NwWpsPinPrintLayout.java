package com.sony.imaging.app.srctrl.network.wpspin.print;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwWpsPinPrintLayout extends Layout {
	private static final String TAG = NwWpsPinPrintLayout.class.getSimpleName();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState); // Log
		View view = obtainViewFromPool(R.layout.nw_layout_pin);
		return view;
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}

	@Override
	public void onResume(){
		super.onResume();
		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));
		String pin = NetworkRootState.getWpsPin();
		Log.v(TAG, pin);

//		ImageView bgImage = ((ImageView)getView().findViewById(R.id.nw_parts_pin_2_img));
//		bgImage.setBackgroundResource(android.R.drawable.s_16_dd_parts_pincodebackground_divi9_c2);
//		bgImage.setVisibility(View.VISIBLE);
		((ImageView)getView().findViewById(R.id.nw_parts_pin_2_img)).setVisibility(View.GONE);

		TextView pinText = ((TextView)getView().findViewById(R.id.nw_parts_pin_1_txt_l));
		pinText.setText(pin);
		pinText.setVisibility(View.VISIBLE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_1_txt_m)).setVisibility(View.GONE);
		((EditText)getView().findViewById(R.id.nw_parts_pin_2_edittxt)).setVisibility(View.GONE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_3_txt)).setText(R.string.STRID_INFO_PIN_CODE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_4_txt)).setText(NetworkRootState.getDirectTargetDeviceName());
		((TextView)getView().findViewById(R.id.nw_parts_pin_5_txt)).setText(R.string.STRID_FUNC_SENDTOSMARTPHONE_MSG_DIRECT_PIN_OUTPUT_DSLR);
		
		Button button = (Button)getView().findViewById(R.id.nw_parts_cmn_button_0_button);
		if (null != button) {
			button.setVisibility(View.GONE);
		}
		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_AMC_STR_06562, 
						android.R.string.STRID_FOOTERGUIDE_RETURN_FOR_SK));
		}

		setKeyBeepPattern(getResumeKeyBeepPattern());
	}
	
	protected String getLogTag() {
		return this.getClass().getSimpleName();
	}
	
	protected NetworkRootState getRootContainer() {
		return (NetworkRootState) getData("NetworkRoot");
	}
}
