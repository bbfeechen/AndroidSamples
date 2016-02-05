package com.sony.imaging.app.srctrl.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 
 * Extended BroadcastReceiver to restrict action after unregisterReceiver.
 * Use {@link SafeBroadcastReceiver#registerThis(Activity, IntentFilter)} and {@link SafeBroadcastReceiver#unregisterThis(Activity)}
 * instead of {@link Activity#registerReceiver(BroadcastReceiver, IntentFilter)} and {@link Activity#unregisterReceiver(BroadcastReceiver)}.
 * @author 0000138134
 *
 */
public abstract class SafeBroadcastReceiver extends BroadcastReceiver {
	private boolean isReceiverAvailable;
	private static final String TAG = SafeBroadcastReceiver.class.getSimpleName();
	
	/**
	 * Force close application with exception before ANR dialog is displayed.
	 * @author 0000138134
	 *
	 */
	private class BlockingDetector extends Thread {
		@Override
    	public void run(){
			try {
				Thread.sleep(8000);
				Log.e(TAG, "onReceive hasn't finished for 8 sec. Now throw exception.");
				//throw new IllegalStateException();
			} catch (InterruptedException e) {
				// No problem
			}
    	}
	}
	
	public SafeBroadcastReceiver(){
		isReceiverAvailable = false;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(isReceiverAvailable){
			BlockingDetector detector = new BlockingDetector();
			detector.start();
			checkedOnReceive(context, intent);
			detector.interrupt();
		} else {
			Log.e(TAG, "Receiver is not available. onReceive is ignored.");
		}
	}
	
	/**
	 * Register SafeBroadCastReceiver to Activity and set available flag. 
	 * @param activity
	 * @param iFilter
	 */
	public void registerThis(Activity activity, IntentFilter iFilter) {
		activity.registerReceiver(this, iFilter);
		isReceiverAvailable = true;
	}
	
	/**
	 * Set unavailable flag and unregister SafeBroadcastReceiver from Activity.
	 * @param activity
	 */
	public void unregisterThis(Activity activity){
		isReceiverAvailable = false;
		activity.unregisterReceiver(this);
	}
	
	/**
	 * Will be called if SafeBroadcastReciever is registered to Activity and available flag is set.
	 * @param context
	 * @param intent
	 */
	public abstract void checkedOnReceive(Context context, Intent intent);
}
