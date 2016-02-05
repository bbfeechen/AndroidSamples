package com.sony.imaging.app.srctrl.util;

import com.sony.scalar.sysutil.didep.Kikilog;
import com.sony.scalar.sysutil.didep.Kikilog.Options;

/**
 * 
 * Add log to KikiLog when remote/local shooting is invoked.
 * @author 0000138134
 *
 */
public class SRCtrlKikiLogUtil {
    /**
     * Count app launching.
     */
	public static void logAppLaunch(){
		// LogID: A-I5
        Kikilog.setUserLog(SRCtrlConstants.KIKILOG_ID_APP_LAUNCH, getAccumulateOptions());		
	}
	
	/**
	 * Count remote shooting.
	 */
    public static void logRemoteShooting(){
    	// LogID: A-I61
        Kikilog.setUserLog(SRCtrlConstants.KIKILOG_ID_REMOTE_SHOOTING, getAccumulateOptions());
    }
    
    /**
     * Count local shooting.
     */
    public static void logLocalShooting(){
    	// LogID: A-I62
        Kikilog.setUserLog(SRCtrlConstants.KIKILOG_ID_LOCAL_SHOOTING, getAccumulateOptions());
    }
    
    private static Options getAccumulateOptions(){
        Options options = new Kikilog.Options();
        options.recType = options.RECTYPE_ACCUMULATE;
    	return options;
    }
}
