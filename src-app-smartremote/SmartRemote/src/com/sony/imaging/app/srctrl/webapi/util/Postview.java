package com.sony.imaging.app.srctrl.webapi.util;

/**
 * Postview structure.
 * @author 0000138134
 *
 */
public class Postview {
	private byte[] picture;
	private String url;
	
	public Postview(byte[] picture, String url){
		this.picture = picture;
		this.url = url;
	}
	
	public void initData(){
		picture = null;
		url = null;
	}

	public void setPostview(byte[] picture, String url){
		this.picture = picture;
		this.url = url;
	}
	public byte[] getPicture(){
		return picture;
	}
	public String getUrl(){
		return url;
	}
}
