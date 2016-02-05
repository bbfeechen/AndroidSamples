package com.sony.scalar.webapi.service.camera.v1_0.intervalstillformat;

import com.sony.mexi.webapi.Service;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
* @version 1.0.0
*
*/

public interface IntervalStillFormat extends Service {

	/**
	 * Set Interval Still Format parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="4" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="4"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">setIntervalStillFormat</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>format</TD>
	 *   <TD>1</TD>
	 *   <TD>Interval Still Format parameter</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>ret</TD>
	 *   <TD>1</TD>
	 *   <TD>API result<br>0:Success<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Set interval still format parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'setIntervalStillFormat', params:["xxx"]}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[0],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  format setIntervalStillFormat parameter
	 * @param  returnCb  Callback for setIntervalStillFormat process
	 * @return  error code
	 *
	 */
	int setIntervalStillFormat(String format, SetIntervalStillFormatCallback returnCb);

	/**
	 * Get interval still format parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getIntervalStillFormat</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>format</TD>
	 *   <TD>1</TD>
	 *   <TD>Interval still format parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get interval still format parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getIntervalStillFormat'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":['xxx'],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getIntervalStillFormat process
	 * @return  error code
	 *
	 */
	int getIntervalStillFormat(GetIntervalStillFormatCallback returnCb);

	/**
	 * Get Supported Interval Still Format parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getSupportedIntervalStillFormat</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>format</TD>
	 *   <TD>1</TD>
	 *   <TD>Supported interval still format parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get Supported interval still format parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getSupportedIntervalStillFormat'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[["xxx","yyy","zzz"]],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getSupportedIntervalStillFormat process
	 * @return  error code
	 *
	 */
	int getSupportedIntervalStillFormat(GetSupportedIntervalStillFormatCallback returnCb);

	/**
	 * Get current supported interval still format parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getAvailableIntervalStillFormat</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="3"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>current</TD>
	 *   <TD>1</TD>
	 *   <TD>current interval still format parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>format</TD>
	 *   <TD>2</TD>
	 *   <TD>current supported interval still format parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get current supported still format parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getAvailableIntervalStillFormat'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":["xxx",["xxx","yyy","zzz"]],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getAvailableIntervalStillFormat process
	 * @return  error code
	 *
	 */
	int getAvailableIntervalStillFormat(GetAvailableIntervalStillFormatCallback returnCb);

}
