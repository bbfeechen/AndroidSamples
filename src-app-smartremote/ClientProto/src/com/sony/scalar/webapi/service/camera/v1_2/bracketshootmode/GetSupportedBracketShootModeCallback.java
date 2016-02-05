package com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_2.common.struct.BracketShootModeParamCandidate;

public interface GetSupportedBracketShootModeCallback extends Callbacks
{
    public void returnCb(BracketShootModeParamCandidate[] bracketShootModeCandidates);
}
