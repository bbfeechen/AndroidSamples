package com.sony.imaging.app.srctrl.network.message.wpserror;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwWpsInvalidLayout extends Layout {	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState); // Log
		View view = obtainViewFromPool(R.layout.nw_layout_error);
		return view;
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}

	@Override
	public void onResume(){
		super.onResume();
		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));		
		((TextView)getView().findViewById(R.id.nw_parts_message_1_txt)).setText(R.string.STRID_FUNC_SENDTOSMARTPHONE_MSG_DIRECT_PBC_ERROR_DSLR);
		
		Button button = (Button)getView().findViewById(R.id.nw_parts_cmn_button_0_button);
		if (null != button) {
			button.setFocusable(false);
			button.setText(android.R.string.STRID_AMC_STR_05339);
		}
		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_AMC_STR_06553, 
						android.R.string.STRID_CMN_DUMMY));
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
