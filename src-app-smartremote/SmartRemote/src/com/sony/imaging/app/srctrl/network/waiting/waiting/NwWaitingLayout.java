package com.sony.imaging.app.srctrl.network.waiting.waiting;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwWaitingLayout extends Layout {	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState); // Log
		View view = obtainViewFromPool(R.layout.nw_layout_waiting);
		return view;
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}

	@Override
	public void onResume(){
		super.onResume();
		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));
		((TextView)getView().findViewById(R.id.nw_parts_wait_0_txt)).setText(R.string.STRID_FUNC_SENDTOSMARTPHONE_MSG_WAITING2);		
		RelativeLayout r2 = (RelativeLayout)getView().findViewById(R.id.nw_parts_wait_2);
		((TextView)r2.findViewById(R.id.nw_parts_wait_special_1_txt)).setText(getString(R.string.STRID_CMN_SSID));
		((TextView)r2.findViewById(R.id.nw_parts_wait_special_1_txt)).setTypeface(Typeface.UNIVERS);
		((TextView)r2.findViewById(R.id.nw_parts_wait_special_2_txt)).setText(NetworkRootState.getMySsid());
		RelativeLayout r3 = (RelativeLayout)getView().findViewById(R.id.nw_parts_wait_3);
		((TextView)r2.findViewById(R.id.nw_parts_wait_special_1_txt)).setTypeface(Typeface.DEFAULT);
		((TextView)r3.findViewById(R.id.nw_parts_wait_special_1_txt)).setText(getString(R.string.STRID_CMN_PASSWORD));
		((TextView)r3.findViewById(R.id.nw_parts_wait_special_2_txt)).setText(NetworkRootState.getMyPsk());
		RelativeLayout r4 = (RelativeLayout)getView().findViewById(R.id.nw_parts_wait_4);
		((TextView)r2.findViewById(R.id.nw_parts_wait_special_1_txt)).setTypeface(Typeface.DEFAULT);
		((TextView)r4.findViewById(R.id.nw_parts_wait_special_1_txt)).setText(getString(R.string.STRID_CMN_WIFI_DEVICENAME_DSLR));
		((TextView)r4.findViewById(R.id.nw_parts_wait_special_2_txt)).setText(NetworkRootState.getMyDeviceName());
		
		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_FOOTERGUIDE_EXITAPP_BY_MENU, 
						android.R.string.STRID_FOOTERGUIDE_EXITAPP_BY_SK1));
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
