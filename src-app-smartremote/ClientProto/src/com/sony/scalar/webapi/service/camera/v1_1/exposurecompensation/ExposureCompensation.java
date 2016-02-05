package com.sony.scalar.webapi.service.camera.v1_1.exposurecompensation;

import com.sony.mexi.webapi.Service;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
* @version 1.1.0
*
*/

public interface ExposureCompensation extends Service {

	/**
	 * Set exposure compensation index parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="4" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="4"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">setExposureCompensation</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>exposure</TD>
	 *   <TD>1</TD>
	 *   <TD>exposure compensation index parameter</TD>
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
	 *   <TD colspan="4">Set the exposure compensation index parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'setExposureCompensation', params:[5]}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[0],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  exposure set exposure compensation parameter
	 * @param  returnCb  Callback for setExposureCompensation process
	 * @return  error code
	 *
	 */
	int setExposureCompensation(int exposure, SetExposureCompensationCallback returnCb);


	/**
	 * Get exposure compensation index parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getExposureCompensation</TD>
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
	 *   <TD>exposure</TD>
	 *   <TD>1</TD>
	 *   <TD>exposure compensation index parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get the exposure compensation index parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getExposureCompensation'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[5],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getExposureCompensation process
	 * @return  error code
	 *
	 */
	int getExposureCompensation(GetExposureCompensationCallback returnCb);


	/**
	 * Get supported exposure compensation parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getSupportedExposureCompensation</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="4"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int*</TD>
	 *   <TD>max</TD>
	 *   <TD>1</TD>
	 *   <TD>maximum exposure compensation index<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int*</TD>
	 *   <TD>min</TD>
	 *   <TD>2</TD>
	 *   <TD>minimum exposure compensation index<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int*</TD>
	 *   <TD>step</TD>
	 *   <TD>3</TD>
	 *   <TD>exposure compensation parameter<br>
	 *   0: Unknown<br>
	 *   1: 1/3EV Step<br>
	 *   2: 1/2EV Step<br>
	 *   </TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get the exposure compensation parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getSupportedExposureCompensation'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[[6,6],[-6,-6],[1,2]],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getSupportedExposureCompensation process
	 * @return  error code
	 *
	 */
	int getSupportedExposureCompensation(GetSupportedExposureCompensationCallback returnCb);


	/**
	 * Get current supported exposure compensation parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getAvailableExposureCompensation</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="5"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>current</TD>
	 *   <TD>1</TD>
	 *   <TD>current exposure compensation index parameter<br><br>
	 *   </TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>max</TD>
	 *   <TD>2</TD>
	 *   <TD>maximum exposure compensation index<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>min</TD>
	 *   <TD>3</TD>
	 *   <TD>minimum exposure compensation index<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>step</TD>
	 *   <TD>4</TD>
	 *   <TD>exposure compensation parameter<br>
	 *   0: Unknown<br>
	 *   1: 1/3EV Step<br>
	 *   2: 1/2EV Step<br>
	 *   </TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get the exposure compensation parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getAvailableExposureCompensation'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[0,6,-6,1],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getAvailableExposureCompensation process
	 * @return  error code
	 *
	 */
	int getAvailableExposureCompensation(GetAvailableExposureCompensationCallback returnCb);

}
