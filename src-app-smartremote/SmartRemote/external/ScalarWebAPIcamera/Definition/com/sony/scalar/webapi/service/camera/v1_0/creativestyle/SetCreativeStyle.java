package com.sony.scalar.webapi.service.camera.v1_0.creativestyle;

import com.sony.mexi.webapi.Service;

/**
 * @version 1.0.0
 */
public interface SetCreativeStyle extends Service
{
    public int setCreativeStyle(String creativeStyle, boolean optionEnabled, int creativeStyleContrast,
            int creativeStyleSaturation, int creativeStyleSharpness, SetCreativeStyleCallback returnCb);
}
