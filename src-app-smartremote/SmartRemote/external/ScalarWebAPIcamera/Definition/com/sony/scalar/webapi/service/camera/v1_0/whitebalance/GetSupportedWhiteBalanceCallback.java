package com.sony.scalar.webapi.service.camera.v1_0.whitebalance;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParamCandidate;

public interface GetSupportedWhiteBalanceCallback extends Callbacks
{

    public void returnCb(WhiteBalanceParamCandidate[] whiteBalanceCandidates);

}
