package com.sony.scalar.webapi.service.accesscontrol.v1_0;

import com.sony.mexi.webapi.Service;
import com.sony.scalar.webapi.service.accesscontrol.v1_0.common.struct.DeveloperInfo;

public interface ActEnableMethods extends Service {
	int actEnableMethods(DeveloperInfo developerInfo, ActEnableMethodsCallback returnCb);
}
