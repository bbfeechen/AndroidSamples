package ezremote.client.scanner;

import ezremote.client.callback.ConnectionStateCallback;
import ezremote.client.data.ap.Capability;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ConnectionManager {
    private WifiManager manager;
    private WifiConfiguration config;
    private int[] capa;		// = {keyMgmt, protocol, group cipher, pairwise cipher}
    private int target;
    private ConnectionStateCallback csCb;
    
    public ConnectionManager(WifiManager manager, ConnectionStateCallback csCb){
    	config = new WifiConfiguration();
    	
		this.manager = manager;
		this.csCb = csCb;
    }
    
    public void setTargetCapabilities(String capa){
    	this.capa = new Capability().capa_parser(capa);
    }
    
    public boolean connect(String ssid, String key){
    	if(capa == null){
    		return false;
    	}
		config.SSID = "\"" + ssid + "\"";
		switch(capa[0]) {
			case KeyMgmt.WPA_PSK:
				config.preSharedKey = "\"" + key + "\"";
	    		config.allowedKeyManagement.set(capa[0]);
	    		config.allowedProtocols.set(capa[1]);
	    		config.allowedGroupCiphers.set(capa[2]);
	    		config.allowedPairwiseCiphers.set(capa[3]);
	    		break;
			default:
				return false;
		}
		tempDisconnection();
    	target = manager.addNetwork(config);
		manager.updateNetwork(config);
		manager.enableNetwork(target, true);
		return true;
    }
    
    public boolean connect_remembered(int netId){
		target = netId;
		tempDisconnection();
		manager.enableNetwork(target, true);
    	return true;
    }
    
    public boolean disconnect(){
    	Log.v("APScanner", "NetID: " + manager.getConnectionInfo().getNetworkId());
    	manager.disableNetwork(manager.getConnectionInfo().getNetworkId());
    	manager.saveConfiguration();
    	manager.disconnect();
    	csCb.onDisconnected();
    	return true;
    }
    
    public void tempDisconnection(){
    	if(manager.getConnectionInfo().getNetworkId() != -1)
			manager.disconnect();
    }
    
    public void onConnected(){
    }
}
