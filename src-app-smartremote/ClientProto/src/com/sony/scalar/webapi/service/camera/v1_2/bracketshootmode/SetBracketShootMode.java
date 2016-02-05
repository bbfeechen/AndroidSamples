package com.sony.scalar.webapi.service.camera.v1_2.bracketshootmode;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.2.0
 */
public interface SetBracketShootMode extends Service
{
    public int setBracketShootMode(String bracketShootMode, String bracketShootModeOption, SetBracketShootModeCallback returnCb);
}
