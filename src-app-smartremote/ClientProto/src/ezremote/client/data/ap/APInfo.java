package ezremote.client.data.ap;

public class APInfo {
	private String name;
	private String status;
	private int strength;
	private String macAddress;
	
	public APInfo(String name, String status, int strength, String macAddress){
		this.setName(name);
		this.setStatus(status);
		this.setStrength(strength);
		this.setMac(macAddress);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public void setStrength(int strength){
		this.strength = strength;
	}
	
	public void setMac(String macAddress){
		this.macAddress = macAddress;
	}
	
	public String getName(){
		return name;
	}
	
	public String getStatus(){
		return status;
	}
	
	public int getStrength(){
		return strength;
	}
	
	public String getMac(){
		return macAddress;
	}
}
