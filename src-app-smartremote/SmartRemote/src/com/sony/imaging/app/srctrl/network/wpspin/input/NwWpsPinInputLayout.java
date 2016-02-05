package com.sony.imaging.app.srctrl.network.wpspin.input;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.base.beep.BeepUtilityIdTable;
import com.sony.imaging.app.base.common.widget.FooterGuide;
import com.sony.imaging.app.base.common.widget.FooterGuideDataResId;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.network.NetworkRootState;

public class NwWpsPinInputLayout extends Layout {
	private final static String TAG = NwWpsPinInputLayout.class.getSimpleName();
	
	private static InputMethodManager imManager;
	
	//private static TextView pinTextView;
	//private ImageView pinImageView;
	
	private static EditText pinEdit;
	private static Button okButton;
	
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
		imManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

		((TextView)getView().findViewById(R.id.nw_parts_cmn_header_0_txt)).setText(SRCtrl.getAppStringId(getActivity()));		
		//pinImageView = ((ImageView)getView().findViewById(R.id.nw_parts_pin_2_img));
		//pinTextView = ((TextView)getView().findViewById(R.id.nw_parts_pin_1_txt_m));
		((ImageView)getView().findViewById(R.id.nw_parts_pin_2_img)).setVisibility(View.GONE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_1_txt_m)).setVisibility(View.GONE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_1_txt_l)).setVisibility(View.GONE);
		pinEdit = ((EditText)getView().findViewById(R.id.nw_parts_pin_2_edittxt));
		pinEdit.setVisibility(View.VISIBLE);
		pinEdit.setEnabled(true);
		pinEdit.setFocusable(true);
		pinEdit.requestFocus();
		((TextView)getView().findViewById(R.id.nw_parts_pin_3_txt)).setText(R.string.STRID_INFO_PIN_CODE);
		((TextView)getView().findViewById(R.id.nw_parts_pin_4_txt)).setText(NetworkRootState.getDirectTargetDeviceName());
		((TextView)getView().findViewById(R.id.nw_parts_pin_5_txt)).setText(R.string.STRID_FUNC_SENDTOSMARTPHONE_MSG_DIRECT_INPUT_DSLR);		
			
		okButton = (Button)getView().findViewById(R.id.nw_parts_cmn_button_0_button);
		okButton.setText(android.R.string.STRID_AMC_STR_05339);
		okButton.setVisibility(View.VISIBLE);
		okButton.setEnabled(false);
		okButton.setFocusable(false);
		FooterGuide guide = (FooterGuide)getView().findViewById(R.id.nw_parts_cmn_footer_0_footer_guide);
		if (null != guide) {
			guide.setData(
				new FooterGuideDataResId( 
						getActivity(), 
						android.R.string.STRID_AMC_STR_06548, 
						android.R.string.STRID_FOOTERGUIDE_RETURN_FOR_SK));
		}

		/*
		pinTextView.setFocusable(true);
		pinTextView.setFocusableInTouchMode(true);
		pinTextView.requestFocus();
		pinTextView.setLongClickable(false);
		
		pinImageView.setBackgroundResource(R.drawable.edittext_background_selector);
		pinImageView.setLongClickable(false);
		*/
		
		//pinEdit.setBackgroundResource(R.drawable.edittext_background_selector);
		pinEdit.setLongClickable(false);
		
		/*
		OnTouchListener onTouchListener = new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getActionMasked()){
					case MotionEvent.ACTION_DOWN:
						pinImageView.setPressed(true);
						return false;
					case MotionEvent.ACTION_UP:
						pinImageView.setPressed(false);
						return false;
					case MotionEvent.ACTION_CANCEL:
						return true;
				}
				return false;
			}
		};
		*/
		/*
		OnClickListener onClickListener = new OnClickListener(){
			@Override
			public void onClick(View view) {				
				BeepUtility.getInstance().playBeep(BeepUtilityRsrcTable.BEEP_ID_SELECT);
				openKeypad();
			}			
		};
		*/
		OnEditorActionListener onEditorActionListener = new OnEditorActionListener(){
			@Override
			public boolean onEditorAction(TextView v, int actionId,	KeyEvent event) {
				if(EditorInfo.IME_ACTION_DONE == actionId){
					hideKeypad(v);
				}
				return false;
			}			
		};
		
		pinEdit.setOnEditorActionListener(onEditorActionListener);
		
		/*
		pinTextView.setOnTouchListener(onTouchListener);
		pinTextView.setOnEditorActionListener(onEditorActionListener);
		pinTextView.setClickable(false);
		
		pinImageView.setOnClickListener(onClickListener);
		*/
		setKeyBeepPattern(getResumeKeyBeepPattern());
		//pinTextView.requestFocus();
		pinEdit.requestFocus();
	}
	
	@Override
	public void onPause(){
		//pinTextView.setText("");
		//hideKeypad(pinTextView);
		//pinTextView = null;
		//pinImageView = null;
		pinEdit.setText("");
		//hideKeypad(pinEdit);
		hideKeypad(getView());
		pinEdit = null;
		okButton.setEnabled(true);
		okButton = null;
		imManager = null;
		super.onPause();
	}
	
	protected String getLogTag() {
		return this.getClass().getSimpleName();
	}
	
	protected NetworkRootState getRootContainer() {
		return (NetworkRootState) getData("NetworkRoot");
	}
	
	public static String getPin() {
		//return pinTextView.getText().toString();
		return pinEdit.getText().toString();
	}
	
	public static void openKeypad(){
		/*
		if(!pinTextView.requestFocus()){
			Log.e(TAG, "Failure: requestFocus");
		}
		if(!imManager.showSoftInput(pinTextView, InputMethodManager.SHOW_FORCED)){
			Log.e(TAG, "Failure: showSoftInput");
		}
		*/
		if(!pinEdit.requestFocus()){
			Log.e(TAG, "Failure: requestFocus");
		}
		if(!imManager.showSoftInput(pinEdit, InputMethodManager.SHOW_FORCED)){
			Log.e(TAG, "Failure: showSoftInput");
		}
	}
	
	public static void hideKeypad(View v){
		if(!imManager.hideSoftInputFromWindow(v.getWindowToken(), 0)){
			Log.e(TAG, "Failure: hideSoftInput");
		}
	}
	
	public static void clearPin(){
		//pinTextView.setText("");
		pinEdit.setText("");
	}

	@Override
	public int pushedUpKey() {
		if(null != pinEdit){
			pinEdit.setEnabled(true);
			pinEdit.setFocusable(true);
			pinEdit.requestFocus();
		}
		if(null != okButton){
			okButton.setEnabled(false);
		}
		return HANDLED;
	}
	
	@Override
	public int pushedDownKey() {
		if(null != pinEdit){
			pinEdit.clearFocus();
			pinEdit.setFocusable(false);
			pinEdit.setEnabled(false);
		}
		if(null != okButton){
			okButton.setEnabled(true);
		}
		return HANDLED;
	}
	
	public static boolean isEditTextFocused(){
		boolean ret;
		ret = false;
		if(null != pinEdit && pinEdit.isEnabled()){
			ret = true;
		}
		return ret;
	}
}
