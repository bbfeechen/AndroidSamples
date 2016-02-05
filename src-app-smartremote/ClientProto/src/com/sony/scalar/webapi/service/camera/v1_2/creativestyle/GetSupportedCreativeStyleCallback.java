package com.sony.scalar.webapi.service.camera.v1_2.creativestyle;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.CreativeStyleParamCandidate;

public interface GetSupportedCreativeStyleCallback extends Callbacks
{
    public void returnCb(CreativeStyleParamCandidate[] creativeStyleCandidates);
}
