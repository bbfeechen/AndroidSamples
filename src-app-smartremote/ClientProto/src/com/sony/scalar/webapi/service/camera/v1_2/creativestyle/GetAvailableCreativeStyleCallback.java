package com.sony.scalar.webapi.service.camera.v1_2.creativestyle;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.CreativeStyleParamCandidate;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.CreativeStyleParams;

public interface GetAvailableCreativeStyleCallback extends Callbacks
{

    public void returnCb(CreativeStyleParams currentCreativeStyle,
            CreativeStyleParamCandidate[] creativeStyleCandidates);

}
