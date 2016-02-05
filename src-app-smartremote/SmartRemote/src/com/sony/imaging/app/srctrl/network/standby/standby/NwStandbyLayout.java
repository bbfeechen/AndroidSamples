package com.sony.imaging.app.srctrl.network.standby.standby;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;
import com.sony.imaging.app.util.PTag;

public class NwStandbyLayout extends Layout {
	private static AnimationDrawable aDrawable;
	public static final String PTAG_SMART_REMOTE="Smart Remote Control";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState); // Log
		View view = obtainViewFromPool(R.layout.nw_layout_standby);
		return view;
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}

	@Override
	public void onResume(){
		PTag.end(PTAG_SMART_REMOTE);
		super.onResume();
		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));		
		RelativeLayout r1 = (RelativeLayout)getView().findViewById(R.id.nw_parts_standby_1);
		((TextView)r1.findViewById(R.id.nw_parts_standby_special_2_txt)).setText(getString(R.string.STRID_FUNC_NETWORK_SETTINGS_MSG_WIFI_STANDBY));

		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_FOOTERGUIDE_EXITAPP_BY_MENU, 
						android.R.string.STRID_FOOTERGUIDE_EXITAPP_BY_SK1));
		}
		
		ImageView progressDot =  ((ImageView)r1.findViewById(R.id.nw_parts_standby_special_1_img));		
		
		aDrawable = (AnimationDrawable)getActivity().getResources().getDrawable(R.drawable.progress_dot_animation);
		progressDot.setImageDrawable(aDrawable);
		
		setKeyBeepPattern(getResumeKeyBeepPattern());		
		
		ViewTreeObserver vtObserver = getView().getViewTreeObserver();
		vtObserver.addOnGlobalLayoutListener(listener);
	}
	
	ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout(){
			aDrawable.start();
		}
	};
	
	@Override
	public void onPause(){
		getView().getViewTreeObserver().removeGlobalOnLayoutListener(listener);
		aDrawable.stop();
		aDrawable = null;
		super.onPause();
	}
	
	protected String getLogTag() {
		return this.getClass().getSimpleName();
	}
	
	protected NetworkRootState getRootContainer() {
		return (NetworkRootState) getData("NetworkRoot");
	}
}
