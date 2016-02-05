package ezremote.client.scanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ezremote.client.R;
import ezremote.client.data.ap.APInfo;

public abstract class APAdapter extends ArrayAdapter<APInfo>{
	protected LayoutInflater inflater;
	protected int position;
	
	public APAdapter(Context context, int resourceId){
		super(context, resourceId);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;
		if(v == null){
			v = inflater.inflate(R.layout.row_capa, null);
		}
		this.position = position;
		final APInfo finfo = getThisAPInfo();
		TextView apName = (TextView)v.findViewById(R.id.textView2_capa);
		TextView apMac = (TextView)v.findViewById(R.id.textView5_capa);
		apName.setText(finfo.getName());
		apMac.setText(finfo.getMac());
		
		TextView apCapa = (TextView)v.findViewById(R.id.textCapabilities_capa);
		apCapa.setText(finfo.getStatus());
		return v;
	}
	
	protected abstract APInfo getThisAPInfo();
}
