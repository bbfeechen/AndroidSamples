package ezremote.client.scanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import ezremote.client.R;
import ezremote.client.data.ApContainer;
import ezremote.client.data.ap.APInfo;

public class CurrentAPAdapter extends APAdapter{

	private Context context;
	private int res_id;
	
	public CurrentAPAdapter(Context context, int resourceId) {
		super(context, resourceId);
		this.context = context;
		res_id = android.R.drawable.presence_online;
	}
	
	protected APInfo getThisAPInfo(){
		return ApContainer.getInstance().getCurrentAPInfo();
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = convertView;

		if(v == null){
			v = inflater.inflate(R.layout.row_capa, null);
		}
		this.position = position;		
		final APInfo finfo = getThisAPInfo();
		
		if(finfo != null){
			TextView apName = (TextView)v.findViewById(R.id.textView2_capa);
			TextView apMac = (TextView)v.findViewById(R.id.textView5_capa);
			apName.setText(finfo.getName());
			apMac.setText(finfo.getMac());
			
			TextView apCapa = (TextView)v.findViewById(R.id.textCapabilities_capa);
			apCapa.setText(finfo.getStatus());
	
			ImageView iV = (ImageView)v.findViewById(ezremote.client.R.id.imageInvisible);
			
			iV.setImageResource(res_id);
		}
		return v;
	}
}
