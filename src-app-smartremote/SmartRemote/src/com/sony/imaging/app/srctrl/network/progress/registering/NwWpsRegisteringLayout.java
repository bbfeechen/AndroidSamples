package com.sony.imaging.app.srctrl.network.progress.registering;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwWpsRegisteringLayout extends Layout {
	private static AnimationDrawable aDrawable;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState); // Log
		View view = obtainViewFromPool(R.layout.nw_layout_progress);
		return view;
	}
	
	protected int getResumeKeyBeepPattern(){
		return BeepUtilityIdTable.KEY_BEEP_PATTERN_INVALID;
	}

	@Override
	public void onResume(){
		super.onResume();
		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));		
		((TextView)getView().findViewById(R.id.nw_parts_prog_1_txt)).setVisibility(View.GONE);
		ImageView progressDot = ((ImageView)getView().findViewById(R.id.nw_parts_prog_2_img));
		((TextView)getView().findViewById(R.id.nw_parts_prog_3_txt)).setVisibility(View.GONE);
		if(NetworkRootState.getDirectTargetDeviceName() == null){
			((TextView)getView().findViewById(R.id.nw_parts_prog_4_txt)).setVisibility(View.GONE);
		} else {
			((TextView)getView().findViewById(R.id.nw_parts_prog_4_txt)).setVisibility(View.VISIBLE);
			((TextView)getView().findViewById(R.id.nw_parts_prog_4_txt)).setText(NetworkRootState.getDirectTargetDeviceName());			
		}
		((TextView)getView().findViewById(R.id.nw_parts_prog_5_txt)).setText(R.string.STRID_AMC_STR_06084);

		Button button = (Button)getView().findViewById(R.id.nw_parts_cmn_button_0_button);
		if (null != button) {
			button.setFocusable(false);
			button.setText(android.R.string.STRID_AMC_STR_00926);
			button.setVisibility(View.VISIBLE);
		}
		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_AMC_STR_06553, 
						android.R.string.STRID_CMN_DUMMY));
		}

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
