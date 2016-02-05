package com.sony.scalar.webapi.service.camera.v1_0.common;

import com.sony.mexi.webapi.Service;

/**
*
* @author Kouji.Tsukaya@jp.sony.com
* @version 1.0.0
*
*/

public interface TakePicture extends Service {

	/**
	 * Take picture and get picture url.
	 * <TABLE BORDER="1" WIDTH="80%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC" rowspan="2"><B>request</B></TD>
	 *   <TD BGCOLOR="#CCFFCC" colspan="4"><B>method</B></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD colspan="4">actTakePicture</TD>
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
	 *   <TD>url</TD>
	 *   <TD>1</TD>
	 *   <TD>URLs of the picture files<br><br></TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>comment</B></TD>
	 *   <TD colspan="4">Take picture and get picture url.</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. request(TBD)</B></TD>
	 *   <TD colspan="4">http://host:port/sony/camera<BR>{method:'actTakePicture'}</TD>
	 *   </TR>
	 *   <TR>
	 *   <TD BGCOLOR="#CCFFCC"><B>e.g. result(TBD)</B></TD>
	 *   <TD colspan="4">{"result":["http://192.168.20.1:8080/DCIM/file001.jpg", "http://192.168.20.1:8080/DCIM/file002.jpg", "http://192.168.20.1:8080/DCIM/file003.jpg"],"error":null}</TD>
	 *   </TR>
	 * </TABLE>
	 *
	 *
	 * @param  returnCb  Callback for actTakePicture process
	 * @return  error code
	 *
	 */
	int actTakePicture(ActTakePictureCallback returnCb);

}
