package com.sony.scalar.webapi.service.accesscontrol.v1_0;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.accesscontrol.v1_0.common.struct.DeveloperKey;

public interface ActEnableMethodsCallback extends Callbacks {
	void returnCb( DeveloperKey developerKey );
}
