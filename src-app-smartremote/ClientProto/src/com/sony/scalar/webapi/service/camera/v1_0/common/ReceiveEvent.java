package com.sony.scalar.webapi.service.camera.v1_0.common;

import com.sony.mexi.webapi.Service;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
* @version 1.0.0
*
*/

public interface ReceiveEvent extends Service {


	/**
	 * Receive event Info.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="4"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">receiveEvent</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>bool</TD>
	 *   <TD>polling</TD>
	 *   <TD>1</TD>
	 *   <TD>long polling type parameter</TD>
	 *   </TR>
 	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="12"><B>result</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>type</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>name</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>arg</B></TD>
	 *   <TD BGCOLOR="#CCFFCC"><B>description</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>status</TD>
	 *   <TD>1</TD>
	 *   <TD>server status<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>bool</TD>
	 *   <TD>liveviewstatus</TD>
	 *   <TD>2</TD>
	 *   <TD>liveview status<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>zoomposission</TD>
	 *   <TD>3</TD>
	 *   <TD>zoomposission<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>zoom_numbox</TD>
	 *   <TD>4</TD>
	 *   <TD>zoom_numbox<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>zoom_idxcurtbox</TD>
	 *   <TD>5</TD>
	 *   <TD>zoom_idxcurtbox<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>int</TD>
	 *   <TD>zoom_poscurtbox</TD>
	 *   <TD>6</TD>
	 *   <TD>zoom_poscurtbox<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>name</TD>
	 *   <TD>7</TD>
	 *   <TD>api name list<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>type</TD>
	 *   <TD>8</TD>
	 *   <TD>paraeter data type list<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>boolean*</TD>
	 *   <TD>range</TD>
	 *   <TD>9</TD>
	 *   <TD>range flag<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string</TD>
	 *   <TD>current</TD>
	 *   <TD>10</TD>
	 *   <TD>current parameter list<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD>string*</TD>
	 *   <TD>list</TD>
	 *   <TD>11</TD>
	 *   <TD>available parameter list<br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">receive event Info.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'receiveEvent', params:[true]}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":['IDLE','still',7,3,0,20,["setSelfTimer"],["int"],[false],["2"],["3","0","2","10"] ],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for receiveEvent process
	 * @return  error code
	 *
	 */
	int receiveEvent(boolean polling, ReceiveEventCallback returnCb);


}
