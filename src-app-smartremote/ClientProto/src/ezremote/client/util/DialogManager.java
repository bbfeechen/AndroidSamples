package ezremote.client.util;

import java.util.ArrayList;

import ezremote.client.callback.ItemListListener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;

public class DialogManager {
	private ProgressDialog progDialog;
	private AlertDialog aDialog;
	private Handler handler;
	private Context context;
	private ItemListListener itemListListener;
	
	
	public DialogManager(Context context, Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	public void setItemListListener(ItemListListener listener){
		this.itemListListener = listener;
	}
	
	public void initItemListListener(){
		this.itemListListener = null;
	}
	
	public void showProgDialog(final String title, final String message){
		closeDialogs();

		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						createProgDialog(title, message);
						progDialog.show();
					}
				});
		    }
		}).start();
	}

	public void showTextDialog(final String title, final String message){
		closeDialogs();

		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						aDialog = createTextDialog(title, message);
						aDialog.show();
					}
				});
		    }
		}).start();
	}
	
	public void showItemsDialog(final String title, final ArrayList<String> itemsList){
		closeDialogs();
		final String[] itemsArray=(String[])itemsList.toArray(new String[0]);
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						aDialog = createItemsDialog(title, itemsArray);
						aDialog.show();
					}
				});
		    }
		}).start();
	}
	
	public void closeDialogs(){
		closeProgDialog();
		closeAlertDialog();
	}
	
	private void createProgDialog(String title, String message){
		progDialog = new ProgressDialog(context);
    	progDialog.setTitle(title);
    	progDialog.setMessage(message);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	private void closeProgDialog(){
		if(progDialog != null){
			if(progDialog.isShowing()){
				progDialog.dismiss();
			}
			progDialog = null;
		}
	}

	private AlertDialog createTextDialog(String title, String message){

		AlertDialog alertDialog = new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int whichButton){
    		}
    	})
		.create();

		return alertDialog;
	}
	
	private AlertDialog createItemsDialog(String title, String[] items){

		AlertDialog alertDialog = new AlertDialog.Builder(context)
		.setTitle(title)
		.setItems(items, new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				itemListListener.onItemSelected(which);
			}			
		})
		.create();

		return alertDialog;
	}

	private void closeAlertDialog(){
		if(aDialog != null){
			if(aDialog.isShowing()){
				aDialog.dismiss();
			}
			aDialog = null;
		}
	}
}
