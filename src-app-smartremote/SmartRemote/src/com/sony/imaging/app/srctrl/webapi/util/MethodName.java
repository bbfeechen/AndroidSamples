package com.sony.imaging.app.srctrl.webapi.util;

/**
 * 
 * This class is supposed to be used to detect WebAPI method name.
 * @author 0000138134
 *
 */
public class MethodName {
	public static String getMethodName(int offset){		
		return Thread.currentThread().getStackTrace()[3+offset].getMethodName();
	}
    public static String getMethodName(){       
        return getMethodName(1); 
    }
}
