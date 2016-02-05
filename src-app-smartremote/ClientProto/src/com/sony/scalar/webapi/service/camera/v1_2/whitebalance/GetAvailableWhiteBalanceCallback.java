package com.sony.scalar.webapi.service.camera.v1_2.whitebalance;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.WhiteBalanceParamCandidate;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.WhiteBalanceParams;

public interface GetAvailableWhiteBalanceCallback extends Callbacks
{
    public void returnCb(WhiteBalanceParams currentWhiteBalance,
            WhiteBalanceParamCandidate[] whiteBalanceCandidates);
}
