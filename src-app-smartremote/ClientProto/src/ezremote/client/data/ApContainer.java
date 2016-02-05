package ezremote.client.data;

import java.util.ArrayList;

import ezremote.client.data.ap.APInfo;


public class ApContainer {
	private static ApContainer sContainer;
	private ArrayList<APInfo> apList;
	private APInfo current;
	
    public static ApContainer getInstance() {
        if (null == sContainer) {
            sContainer = new ApContainer();
        }
        return sContainer;
    }
	
	public ApContainer(){
		apList = new ArrayList<APInfo>();
	}
	
	public void renewAPList(){
		apList.clear();
	}
	
	public void setCurrent(APInfo info){
		current = info;
	}
	
	public void addAP(APInfo api){
		apList.add(api);
	}
	
	public int sizeOfAPList(){
		return apList.size();
	}
	
	public APInfo getAPInfo(int index){
		return apList.get(index);
	}
	
	public APInfo getCurrentAPInfo(){
		return current;
	}
}
