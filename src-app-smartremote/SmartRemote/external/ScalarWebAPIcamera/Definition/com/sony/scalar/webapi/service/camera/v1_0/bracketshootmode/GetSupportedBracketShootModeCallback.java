package com.sony.scalar.webapi.service.camera.v1_0.bracketshootmode;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.BracketShootModeParamCandidate;

public interface GetSupportedBracketShootModeCallback extends Callbacks
{
    public void returnCb(BracketShootModeParamCandidate[] bracketShootModeCandidates);
}
