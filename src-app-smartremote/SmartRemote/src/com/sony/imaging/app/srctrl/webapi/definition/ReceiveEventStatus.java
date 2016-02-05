package com.sony.imaging.app.srctrl.webapi.definition;

/**
 * 
 * ReceiveEvent server status definition.
 * @author 0000138134
 *
 */
public class ReceiveEventStatus {

    public static final String ERROR = "Error";
    public static final String NOT_READY = "NotReady";
    public static final String IDLE = "IDLE";
    public static final String STILL_CAPTURING = "StillCapturing";
    public static final String STILL_SAVING = "StillSaving";
    public static final String MOVIE_STARTING_REC = "MovieWaitRecStart";
    public static final String MOVIE_RECORDING = "MovieRecording";
    public static final String MOVIE_SAVING = "MovieSaving";
    public static final String INTERVAL_STARTING_REC = "IntervalWaitRecStart";
    public static final String INTERVAL_RECORDING = "IntervalRecording";
    public static final String INTERVAL_STOPPING_REC = "IntervalWaitRecStop";
    public static final String DUMMY = "dummy";
    
}
