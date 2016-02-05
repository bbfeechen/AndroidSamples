package com.sony.scalar.webapi.service.system.v1_0;

import com.sony.mexi.webapi.Service;
import com.sony.scalar.webapi.service.system.v1_0.common.struct.CurrentTime;

public interface SetCurrentTime extends Service {
	int setCurrentTime(CurrentTime  currentTime, SetCurrentTimeCallback returnCb);

}
