package com.sony.scalar.webapi.service.camera.v1_0.bracketshootmode;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface SetBracketShootMode extends Service
{
    public int setBracketShootMode(String bracketShootMode, String bracketShootModeOption, SetBracketShootModeCallback returnCb);
}
