package com.sony.imaging.app.srctrl.webapi.availability;

import java.util.ArrayList;

/**
 * 
 * Available parameters for receiveEvent callback.
 * @author 0000138134
 *
 */
public class AvailableParams {
	ArrayList<String> names;
	ArrayList<String> types;
	ArrayList<Boolean> rangeFlags;
	ArrayList<String> currents;
	ArrayList<String> availables;
	
	public AvailableParams(){
		names = new ArrayList<String>();
		types = new ArrayList<String>();
		rangeFlags = new ArrayList<Boolean>();
		currents = new ArrayList<String>();
		availables = new ArrayList<String>();
	}
	
	public void addData(String name, String type, boolean flag, String current, ArrayList<String> available){
		names.add(name);
		types.add(type);
		rangeFlags.add(flag);
		currents.add(current);
		availables.addAll(available);
	}
	
	public ArrayList<String> getNames(){
		return names;
	}
	
	public String[] getNamesArray(){
		return (String[])names.toArray(new String[0]);
	}
	
	public ArrayList<String> getTypes(){
		return types;
	}
	
	public String[] getTypesArray(){
		return (String[])types.toArray(new String[0]);
	}
	
	public ArrayList<Boolean> getRangeFlags(){
		return rangeFlags;
	}

	public boolean[] getRangeFlagsArray(){
		boolean[] flagsArray = new boolean[rangeFlags.size()];
		for(int i = 0; i<rangeFlags.size(); i++){
			flagsArray[i] = rangeFlags.get(i);
		}
		return flagsArray;
	}
	
	public ArrayList<String> getCurrents(){
		return currents;
	}

	public String[] getCurrentsArray(){
		return (String[])currents.toArray(new String[0]);
	}
	
	public ArrayList<String> getAvailables(){
		return availables;
	}
	
	public String[] getAvailablesArray(){
		return (String[])availables.toArray(new String[0]);
	}
}
