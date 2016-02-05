package ezremote.client.data;

import java.util.List;

public class DataContainer {
    private static DataContainer sContainer;

    private String address;
    
	private String baseUrl;
	
    public static DataContainer getInstance() {
        if (null == sContainer) {
            sContainer = new DataContainer();
        }
        return sContainer;
    }
    
    public void setHostAddress(String address){
    	this.address = address;
    }
    
    public String getHostAddress(){
    	return address;
    }
    
    public void setBaseUrl(String baseUrl){
    	this.baseUrl = baseUrl + "/index.html";
    }
    
    public String getBaseUrl(){
    	return baseUrl;
    }
    
   
    
    private String[] listToArray(List<String> list){
    	String[] strArray = new String[list.size()];
    	for(int i=0; i<list.size(); i++){
    		strArray[i] = list.get(i);
    	}
    	return strArray;
    }
}
