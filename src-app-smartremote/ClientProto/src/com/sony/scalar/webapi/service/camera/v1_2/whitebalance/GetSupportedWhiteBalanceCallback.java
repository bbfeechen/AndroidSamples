package com.sony.scalar.webapi.service.camera.v1_2.whitebalance;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.WhiteBalanceParamCandidate;

public interface GetSupportedWhiteBalanceCallback extends Callbacks
{

    public void returnCb(WhiteBalanceParamCandidate[] whiteBalanceCandidates);

}
