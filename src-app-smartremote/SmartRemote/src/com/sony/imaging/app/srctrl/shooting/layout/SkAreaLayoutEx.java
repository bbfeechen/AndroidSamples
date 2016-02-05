package com.sony.imaging.app.srctrl.shooting.layout;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sony.imaging.app.fw.Layout;
import com.sony.imaging.app.srctrl.R;

public class SkAreaLayoutEx extends Layout {
	protected ViewGroup mCurrentLayout = null;
	protected View mMainView = null;

	protected class DelayAttachView implements IdleHandler {
		@Override
		public boolean queueIdle() {
			if (null == mMainView) {
				mMainView = obtainViewFromPool(R.layout.shooting_main_skarea_ex);
			}
			mCurrentLayout.addView(mMainView);
			return false;
		}
	}
	protected DelayAttachView idleHandler = new DelayAttachView();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (null == mCurrentLayout) {
			mCurrentLayout = new RelativeLayout(getActivity());
		}

		Looper.myQueue().addIdleHandler(idleHandler);

		return mCurrentLayout;
	}

	private void detachView() {
		Looper.myQueue().removeIdleHandler(idleHandler);
		if (null != mCurrentLayout) {
			mCurrentLayout.removeView(mMainView);
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		detachView();
	}
}
