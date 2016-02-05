package com.sony.imaging.app.srctrl.webapi.availability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.fw.State;
import com.sony.imaging.app.srctrl.SRCtrl;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.util.StateController;
import com.sony.imaging.app.srctrl.webapi.definition.Name;

public class AvailableApiFilterFactory
{
    private static final String tag = AvailableApiFilterFactory.class.getName();
    
    public interface IAvailableApiFilter
    {
        public String[] squeezeApiList(String[] apiList);
    }
    
    private static class DefaultAvailableApiFilter implements IAvailableApiFilter
    {
        public String[] getAllApiList()
        {
            return Name.s_DEFAULT_API_LIST;
        }
        
        @Override
        public String[] squeezeApiList(String[] apiList)
        {
            if (null == apiList || 0 == apiList.length)
            {
                return SRCtrlConstants.s_EMPTY_STRING_ARRAY;
            }
            
            final String[] allApiList = getAllApiList();
            List<String> result = new ArrayList<String>();
            for (String api : apiList)
            {
                int offset = Arrays.binarySearch(allApiList, api, Name.s_COMP);
                if (0 <= offset)
                {
                    result.add(api);
                }
            }
            
            return result.toArray(SRCtrlConstants.s_EMPTY_STRING_ARRAY);
        }
    }
    
    private static class PreinstallAppAvailableApiFilter extends DefaultAvailableApiFilter
    {
        @Override
        public String[] getAllApiList()
        {
            return Name.s_PREINSTALL_API_LIST;
        }
    }
    
    private static IAvailableApiFilter s_ApiFilter = null;
    
    public static IAvailableApiFilter createApiFilter()
    {
        if (null != s_ApiFilter)
        {
            return s_ApiFilter;
        }
        
        State state = StateController.getInstance().getState();
        if (null == state)
        {
            Log.e(tag, "Api Filter is not ready...");
            return s_ApiFilter;
        }
        
        boolean isSystemApp = SRCtrl.isSystemApp(state.getActivity());
        
        if (isSystemApp)
        {
            Log.v(tag, "This is a default system app using limited APIs.");
            s_ApiFilter = new PreinstallAppAvailableApiFilter();
        }
        else
        {
            Log.v(tag, "This is a downloaded app with full APIs.");
            s_ApiFilter = new DefaultAvailableApiFilter();
        }
        return s_ApiFilter;
    }
}
