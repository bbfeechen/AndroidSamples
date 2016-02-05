package com.sony.scalar.webapi.service.camera.v1_0.shootmode;

import com.sony.mexi.webapi.Service;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
* @version 1.0.0
*
*/

public interface ShootMode extends Service {

	/**
	 * Set shoot mode parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="4" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="4"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">setShootMode</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>mode</TD>
	 *   <TD>1</TD>
	 *   <TD>shoot mode parameter</TD>
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
	 *   <TD colspan="4">Set shoot mode parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'setShootMode', params:["sitll"]}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[0],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  mode setShootMode parameter
	 * @param  returnCb  Callback for setShootMode process
	 * @return  error code
	 *
	 */
	int setShootMode(String mode, SetShootModeCallback returnCb);

	/**
	 * Get shoot mode parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getShootMode</TD>
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
	 *   <TD>mode</TD>
	 *   <TD>1</TD>
	 *   <TD>shoot mode parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get shoot mode parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getShootMode'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":['still'],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getShootMode process
	 * @return  error code
	 *
	 */
	int getShootMode(GetShootModeCallback returnCb);

	/**
	 * Get Supported ShootMode parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getSupportedShootMode</TD>
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
	 *   <TD>mode</TD>
	 *   <TD>1</TD>
	 *   <TD>Supported shoot mode parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get Supported shoot mode parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getSupportedShootMode'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":[["still","movie","intervalstill"]],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getSupportedShootMode process
	 * @return  error code
	 *
	 */
	int getSupportedShootMode(GetSupportedShootModeCallback returnCb);

	/**
	 * Get current supported shoot mode parameter.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">getAvailableShootMode</TD>
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
	 *   <TD>current shoot mode parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>mode</TD>
	 *   <TD>2</TD>
	 *   <TD>current supported shoot mode parameter<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Get current supported shoot mode parameter.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'getAvailableShootMode'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":["still",["still","movie","intervalstill"]],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for getAvailableShootMode process
	 * @return  error code
	 *
	 */
	int getAvailableShootMode(GetAvailableShootModeCallback returnCb);

}
