package com.sony.scalar.webapi.service.camera.v1_0.pictureeffect;

import com.sony.mexi.webapi.Callbacks;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.PictureEffectParamCandidate;

public interface GetSupportedPictureEffectCallback extends Callbacks
{
    public void returnCb(PictureEffectParamCandidate[] pictureEffectCandidates);
}
