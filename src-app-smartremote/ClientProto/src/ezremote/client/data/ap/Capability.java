package ezremote.client.data.ap;

import java.util.BitSet;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.util.Log;

public class Capability {

	public Capability(){
	}
	
	public String CapabilityAnalyzer(WifiConfiguration config){
		String capabilities = "";
		BitSet keyMgmt = config.allowedKeyManagement;
		if(keyMgmt.get(KeyMgmt.WPA_PSK)){
			BitSet pCiphers = config.allowedPairwiseCiphers;
			if(config.allowedProtocols.get(Protocol.RSN)){
				capabilities += "[WPA2-PSK-";
				if(pCiphers.get(PairwiseCipher.CCMP))
					capabilities += "CCMP]";
				else if(pCiphers.get(PairwiseCipher.TKIP))
					capabilities += "TKIP]";
				else
					capabilities += "NONE]";
			}
			if(config.allowedProtocols.get(Protocol.WPA)){
				capabilities += "[WPA-PSK-";
				if(pCiphers.get(PairwiseCipher.CCMP))
					capabilities += "CCMP]";
				else if(pCiphers.get(PairwiseCipher.TKIP))
					capabilities += "TKIP]";
				else
					capabilities += "NONE]";
			}
		}
		if(keyMgmt.get(KeyMgmt.WPA_EAP)){
			capabilities += "[WPA-EAP]";
		}
		if(keyMgmt.get(KeyMgmt.IEEE8021X)){
			capabilities += "[IEEE8021X]";				        				
		}
		if(keyMgmt.get(KeyMgmt.NONE)){
			capabilities += "[WEP]";
		}
		return capabilities;			
	}

	
	public int[] capa_parser(String capabilities){
		int[] capa = new int[4];
    	if(capabilities.contains("WPA2-PSK")){
    		Log.v("APScanner", "capability parser: WPA2-PSK");
    		capa[0] = KeyMgmt.WPA_PSK;
    		capa[1] = Protocol.RSN;
    	} else if(capabilities.contains("WPA-PSK")){
    		Log.v("APScanner", "capability parser: WPA-PSK");
    		capa[0] = KeyMgmt.WPA_PSK;
    		capa[1] = Protocol.WPA;
    	} else {
    		Log.v("APScanner", "capability parser: Not WPA-PSK");
    		capa[0] = KeyMgmt.NONE;
    		capa[1] = -1;
    	}
    	
    	if(capabilities.contains("CCMP")){
    		Log.v("APScanner", "capability parser: CCMP");
    		capa[2] = GroupCipher.CCMP;
    		capa[3] = PairwiseCipher.CCMP;
    	} else if(capabilities.contains("TKIP")){
    		Log.v("APScanner", "capability parser: TKIP");
    		capa[2] = GroupCipher.TKIP;
    		capa[3] = PairwiseCipher.TKIP;
    	} else {
    		capa[2] = -1;
    		capa[3] = -1;
    	}
    	return capa;
    }
}
