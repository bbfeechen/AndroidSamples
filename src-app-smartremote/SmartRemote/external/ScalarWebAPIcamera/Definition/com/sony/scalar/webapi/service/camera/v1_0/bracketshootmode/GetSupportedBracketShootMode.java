package com.sony.scalar.webapi.service.camera.v1_0.bracketshootmode;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface GetSupportedBracketShootMode extends Service
{
    public int getSupportedBracketShootMode(GetSupportedBracketShootModeCallback returnCb);
}
