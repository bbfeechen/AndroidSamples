package ezremote.client.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ezremote.client.R;
import ezremote.client.callback.AuthDialogCallback;
import ezremote.client.data.ApContainer;
import ezremote.client.data.ap.APInfo;

public class AuthDialog {
    private Context context;
	private AuthDialogCallback dCb;
	
	public AuthDialog(Context context, AuthDialogCallback dCb){
		this.context = context;
		this.dCb = dCb;
	}
	
	public AlertDialog authDialog(final APInfo info){				// Authentication Dialog for unknown Access Point
		LayoutInflater inflator = LayoutInflater.from(context);
		final View authView = inflator.inflate(R.layout.dialog_textpw, null);
		TextView message = (TextView)authView.findViewById(R.id.dialog_message2);
		message.setText("Input Password to connect to & control \"" + info.getName() + "\"");
		return new AlertDialog.Builder(context)
			.setTitle("Unknown Device")
			.setView(authView)
			.setPositiveButton("Go!", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	    			EditText edit = (EditText) authView.findViewById(R.id.username_edit);
	                String password = edit.getText().toString().trim();
	                dCb.onConnectionSelected(info, password);
	    		}
	    	})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	    		}
	    	})
			.create();	
	}
	
	public AlertDialog preAuthDialog(final APInfo info, final int netId){				// Authentication Dialog for remembered Access POint
		LayoutInflater inflator = LayoutInflater.from(context);
		final View authView = inflator.inflate(R.layout.dialog_text, null);
		TextView message = (TextView)authView.findViewById(R.id.dialog_message1);
		message.setText("Connect to & control \"" + info.getName() + "\" ?");
		return new AlertDialog.Builder(context)
			.setTitle("Remembered Device")
			.setView(authView)
			.setPositiveButton("Go!", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	                dCb.onRememberedSelected(info, netId);
	    		}
	    	})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	    		}
	    	})
			.create();	
	}
	
	public AlertDialog preConnectedDialog(){							// Authentication Dialog for connecting Access Point
		LayoutInflater inflator = LayoutInflater.from(context);
		final View disconView = inflator.inflate(R.layout.dialog_text, null);
		TextView message = (TextView)disconView.findViewById(R.id.dialog_message1);
		message.setText("Control \"" + ApContainer.getInstance().getCurrentAPInfo().getName() + "\" ?");
		return new AlertDialog.Builder(context)
			.setTitle("Pre-connected Device")
			.setView(disconView)
	    	.setPositiveButton("Go!", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dCb.onDeviceDiscoverSelected();
				}
			})
			.setNeutralButton("Disconnect", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	    			dCb.onDisconnectionSelected();
	    		}
	    	})
			.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
	    		public void onClick(DialogInterface dialog, int whichButton){
	    		}
	    	})
			.create();	
	}
}
