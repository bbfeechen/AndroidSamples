package com.sony.imaging.app.srctrl.shooting.layout;

import com.sony.imaging.app.base.common.DisplayModeObserver;
import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.R;
import com.sony.imaging.app.util.NotificationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * This class adds Wi-Fi icon to the Shooting Layout.
 * @author 0000138134
 *
 */
public class WifiIconLayout extends Layout {
	private DisplayModeObserver mDisplayObserver;
    private NotificationListener mListener;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		
		int device = DisplayModeObserver.getInstance().getActiveDevice();
		if(device == DisplayModeObserver.DEVICE_ID_FINDER) {
        	return obtainViewFromPool(R.layout.srctrl_parts_cmn_wifi_img_evf);
        } else {
        	return obtainViewFromPool(R.layout.srctrl_parts_cmn_wifi_img_panel);
        }
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(mListener != null) {
			mDisplayObserver.setNotificationListener(mListener);
		}
	}
	
	@Override
	public void onPause() {
		if(mListener != null) {
			mDisplayObserver.removeNotificationListener(mListener);
		}	
		super.onPause();
	}

	public WifiIconLayout(){
		super();
		mDisplayObserver = DisplayModeObserver.getInstance();
		
		mListener = new NotificationListener() {
			private String[] TAGS = new String[] {
					DisplayModeObserver.TAG_DEVICE_CHANGE,
					DisplayModeObserver.TAG_OLEADSCREEN_CHANGE
			};
			@Override
			public void onNotify(final String tag) {
				//refresh();
				updateView();
			}

			@Override
			public String[] getTags() {
				return TAGS;
			}
		};	
	}
	
	/*
	private void refresh() {
		int device = DisplayModeObserver.getInstance().getActiveDevice();
		View view;
		
        if(device == DisplayModeObserver.DEVICE_ID_PANEL) {
        	view = obtainViewFromPool(R.layout.srctrl_parts_cmn_wifi_img_panel);
        } else if(device == DisplayModeObserver.DEVICE_ID_FINDER) {
        	view = obtainViewFromPool(R.layout.srctrl_parts_cmn_wifi_img_evf);
        } else if(device == DisplayModeObserver.DEVICE_ID_HDMI) {
        	view = obtainViewFromPool(R.layout.srctrl_parts_cmn_wifi_img_panel);
        }
	}
	*/
}
