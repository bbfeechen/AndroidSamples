if(0 == WScript.Arguments.length)
{
    WScript.Echo("Usage: sp_plog.js <LOG_FILE>")
    WScript.Quit(1)
}

var fs = new ActiveXObject( "Scripting.FileSystemObject" )
var objShell = WScript.CreateObject("WScript.Shell")

// =============================================================
var TimeData = function(type)
{
    this.type = type
    this.setStartTime = function(time)
    {
        this.start = time
        if(type == KEY_WIFI)
        {
            TimeData.prototype.WIFI_START = time
        }
    }
    this.setEndTime = function(time)
    {
        this.end = time
        if(type == KEY_WIFI)
        {
            TimeData.prototype.WIFI_CONNECTED = time
            this.elapsed = TimeData.prototype.WIFI_START - TimeData.prototype.WIFI_CONNECTED
        }
        else
        {
            this.elapsed = time - TimeData.prototype.WIFI_CONNECTED
	}
    }
}

var KEY_WIFI = "Wi-Fi Connected"
var KEY_DEVICEDISCOVERY = "DeviceDiscovery Completed"
var KEY_GETVERSIONS = "API: getVersions"
var KEY_GETAPPLICATIONINFO = "API: getApplicationInfo"
var KEY_GETEVENT_FALSE = "API: getEvent(false)"
var KEY_STARTRECMODE = "API: startRecMode"
var KEY_ACTENABLEMETHODS = "API: actEnableMethods"
var KEY_STARTLIVEVIEW = "API: startLiveview"
var KEY_DRAWLIVEVIEW = "Draw Liveview"

var TD_WIFI                   = null
var TD_DeviceDiscovery        = null
var TD_API_GetVersions        = null
var TD_API_GetApplicationInfo = null
var TD_API_GetEvent           = null
var TD_API_StartRecMode       = null
var TD_API_ActEnableMethods   = null
var TD_API_StartLiveview      = null
var TD_DrawLiveview          = null

var LOG_WIFI_START = "Started to establish connection with shooting device."
var LOG_WIFI_END = "Connected with shooting device. / Started device discovoring."
var LOG_DEVICEDISCOVERY_END = "Parsed device description. / Start remote control feature."
var LOG_GETVERSIONS_START = "WebAPI getVersions() called."
var LOG_GETVERSIONS_END = "getVersions returned."
var LOG_GETAPPLICATIONINFO_START = "WebAPI getApplicationInfo() called."
var LOG_GETAPPLICATIONINFO_END = "getApplicationInfo returned"
var LOG_GETEVENT_START = "WebAPI getEvent(false) called."
var LOG_GETEVENT_END = "getEvent(false) returned."
var LOG_STARTRECMODE_START = "WebAPI startRecMode() called."
var LOG_STARTRECMODE_END = "StartRecMode returned."
var LOG_ACTENABLEMETHODS_START = "WebAPI actEnableMethods called."
var LOG_ACTENABLEMETHODS_END = "actEnableMethods returned."
var LOG_STARTLIVEVIEW_START = "WebAPI startLiveview() called."
var LOG_STARTLIVEVIEW_END = "startLiveview returned."
var LOG_DRAW_LIVEVIEW_END = "Draw first Liveview."

function setTime(msg, time)
{
    if(0 == msg.indexOf(LOG_WIFI_START))
    {
      TD_WIFI.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_WIFI_END))
    {
      TD_WIFI.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_DEVICEDISCOVERY_END))
    {
        TD_DeviceDiscovery.setStartTime(TD_WIFI.end)
        TD_DeviceDiscovery.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETVERSIONS_START))
    {
        TD_API_GetVersions.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETVERSIONS_END))
    {
        TD_API_GetVersions.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETAPPLICATIONINFO_START))
    {
        TD_API_GetApplicationInfo.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETAPPLICATIONINFO_END))
    {
	TD_API_GetApplicationInfo.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETEVENT_START))
    {
        TD_API_GetEvent.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_GETEVENT_END))
    {
	TD_API_GetEvent.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_STARTRECMODE_START))
    {
	TD_API_StartRecMode.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_STARTRECMODE_END))
    {
	TD_API_StartRecMode.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_ACTENABLEMETHODS_START))
    {
	TD_API_ActEnableMethods.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_ACTENABLEMETHODS_END))
    {
	TD_API_ActEnableMethods.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_STARTLIVEVIEW_START))
    {
	TD_API_StartLiveview.setStartTime(time)
    }
    else if(0 == msg.indexOf(LOG_STARTLIVEVIEW_END))
    {
	TD_API_StartLiveview.setEndTime(time)
    }
    else if(0 == msg.indexOf(LOG_DRAW_LIVEVIEW_END))
    {
	TD_DrawLiveview.setStartTime(TD_API_StartLiveview.end)
	TD_DrawLiveview.setEndTime(time)
    }
    else
    {
	// Unknown message... ignored
    }
}

// =============================================================

for(var arglen = 0; arglen < WScript.Arguments.length; arglen++) {
    TimeData.prototype.WIFI_START = 0
    TimeData.prototype.WIFI_CONNECTED = 0

    TD_WIFI                   = new TimeData(KEY_WIFI)
    TD_DeviceDiscovery        = new TimeData(KEY_DEVICEDISCOVERY)
    TD_API_GetVersions        = new TimeData(KEY_GETVERSIONS)
    TD_API_GetApplicationInfo = new TimeData(KEY_GETAPPLICATIONINFO)
    TD_API_GetEvent           = new TimeData(KEY_GETEVENT_FALSE)
    TD_API_StartRecMode       = new TimeData(KEY_STARTRECMODE)
    TD_API_ActEnableMethods   = new TimeData(KEY_ACTENABLEMETHODS)
    TD_API_StartLiveview      = new TimeData(KEY_STARTLIVEVIEW)
    TD_DrawLiveview           = new TimeData(KEY_DRAWLIVEVIEW)
    var TD_ALL = [
        TD_WIFI
        , TD_DeviceDiscovery
        , TD_API_GetVersions
        , TD_API_GetApplicationInfo
        , TD_API_GetEvent
        , TD_API_StartRecMode
        , TD_API_ActEnableMethods 
        , TD_API_StartLiveview
        , TD_DrawLiveview ]


    var logFile = WScript.Arguments(arglen)
    var input_file = fs.OpenTextFile(logFile, 1, false)
    var tmp = input_file.ReadAll()
    input_file.close()
    tmp = tmp.replace(/\r\n/g,"\n")
    tmp = tmp.replace(/\r/g,"\n")
    tmp = tmp.replace(/\n/g,"\r\n")
    input_file = fs.OpenTextFile(logFile, 2, false)
    input_file.Write(tmp)
    input_file.close()
    input_file = fs.OpenTextFile(logFile, 1, false)

    while(!input_file.AtEndOfStream)
    {
        var line = input_file.ReadLine()
        var values0 = line.split(" >>>>>> ")
        if(2 != values0.length)
        {
            continue
        }

        var values1 = values0[1].split(" ... ")
        if(2 != values1.length)
        {
            continue
        }

        var values2 = values1[1].split(" ")

        setTime(values1[0], values2[0])
    }
    input_file.close()
    input_file = null

    var logFileAbsolutePath = fs.GetAbsolutePathName(logFile)
    var outputFileName = logFileAbsolutePath + ".csv"
    var output_file = fs.OpenTextFile(outputFileName, 2, true)

    output_file.WriteLine( "EVENT,START,STOP,DUMMY,DIFF,ELAPSED" )
    for( var i in TD_ALL )
    {
        var td = TD_ALL[i]
        output_file.WriteLine( td.type +","+ (td.start-TD_WIFI.start) + "," + (td.end-TD_WIFI.start) + "," + (td.end-TD_WIFI.start) + "," + (td.end-td.start) +"," + td.elapsed)
    }

    output_file.close()
    output_file = null

    objShell.Run("rundll32.exe url.dll"
            + ",FileProtocolHandler " + outputFileName
            , 1, false)
    WScript.Sleep(100)
} // for(;;)

objShell = null
fs = null

WScript.Quit(0)
