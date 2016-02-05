package com.sony.imaging.app.srctrl.shooting.layout;

import android.util.Log;
import android.view.View;

import com.sony.imaging.app.base.R;
import com.sony.imaging.app.base.shooting.layout.AutoReviewLayout;
import com.sony.imaging.app.srctrl.shooting.state.DevelopmentStateEx;

/**
 * 
 * Be careful, this class is almost copy of AutReviewLayout.
 * Just want to removed SKArea icons and touchable areas.
 * @author 0000138134
 *
 */
public class AutoReviewLayoutEx extends AutoReviewLayout {
    private static final String tag = AutoReviewLayoutEx.class.getName();

    @Override
    public void onResume()
    {
        super.onResume();  // onResume in super will call setReviewInfoListener() and it will intercepted by the overriding method of this class below
        
        View footerGuide = (View)this.getActivity().findViewById(R.id.footer_guide);
        if(null != footerGuide)
        {
            footerGuide.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void setReviewInfoListener()
    {
        if(null != DevelopmentStateEx.s_ReviewInfo)
        {
            // review info has been changed so the display can be updated right now.
            displayInfo(DevelopmentStateEx.s_ReviewInfo);
        }
        else
        {
            super.setReviewInfoListener();
        }
    }
    
    @Override
    protected void unsetReviewInfoListener()
    {
        if(null != DevelopmentStateEx.s_ReviewInfo)
        {
            // listener was not set.
        }
        else
        {
            super.unsetReviewInfoListener();
        }
    }
}
