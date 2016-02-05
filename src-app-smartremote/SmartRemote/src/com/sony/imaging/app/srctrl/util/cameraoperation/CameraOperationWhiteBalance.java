package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.WhiteBalanceController;
import com.sony.imaging.app.base.shooting.camera.WhiteBalanceController.WhiteBalanceParam;
import com.sony.imaging.app.srctrl.util.SRCtrlConstants;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParamCandidate;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.WhiteBalanceParams;

public class CameraOperationWhiteBalance
{
    private static final String TAG = CameraOperationWhiteBalance.class.getSimpleName();
    
    private static final String WHITEBALANCE_AUTO = "Auto WB";
    private static final String WHITEBALANCE_DAYLIGHT = "Daylight";
    private static final String WHITEBALANCE_SHADE = "Shade";
    private static final String WHITEBALANCE_CLOUDY = "Cloudy";
    private static final String WHITEBALANCE_INCANDESCENT = "Incandescent";
    private static final String WHITEBALANCE_FLUORESCENT_WARMWHITE = "Fluorescent: Warm White (-1)";
    private static final String WHITEBALANCE_FLUORESCENT_COOLWHITE = "Fluorescent: Cool White (0)";
    private static final String WHITEBALANCE_FLUORESCENT_DAY = "Fluorescent: Day White (+1)";
    private static final String WHITEBALANCE_FLUORESCENT_DAYLIGHT = "Fluorescent: Daylight (+2)";
    private static final String WHITEBALANCE_FLASH = "Flash";
    private static final String WHITEBALANCE_COLOR_TEMPERATURE = "Color Temperature";
    private static final String WHITEBALANCE_CUSTOM = "Custom";
    private static final String WHITEBALANCE_CUSTOM1 = "Custom 1";
    private static final String WHITEBALANCE_CUSTOM2 = "Custom 2";
    private static final String WHITEBALANCE_CUSTOM3 = "Custom 3";
    
    // Basically, the menu UI order should be taken care of by the platform.
    // Now the order is unique over several models so this app rearranges
    // items following the unique order.
    // However, the order varies over the models, this ordering should be
    // handled by PF connected to a model tightly.
    private static final String[] WHITEBALANCE_MENU_ORDER =
        { WHITEBALANCE_AUTO, WHITEBALANCE_DAYLIGHT
        , WHITEBALANCE_SHADE
        , WHITEBALANCE_CLOUDY
        , WHITEBALANCE_INCANDESCENT
        , WHITEBALANCE_FLUORESCENT_WARMWHITE
        , WHITEBALANCE_FLUORESCENT_COOLWHITE
        , WHITEBALANCE_FLUORESCENT_DAY
        , WHITEBALANCE_FLUORESCENT_DAYLIGHT
        , WHITEBALANCE_FLASH
        , WHITEBALANCE_COLOR_TEMPERATURE
        , WHITEBALANCE_CUSTOM
        , WHITEBALANCE_CUSTOM1
        , WHITEBALANCE_CUSTOM2
        , WHITEBALANCE_CUSTOM3
        };
        
    private static WeakReference<NotificationListener> s_NotificationListenerRef = new WeakReference<NotificationListener>(
            null);
    
    public static NotificationListener getNotificationListener()
    {
        NotificationListener notificationListener = s_NotificationListenerRef.get();
        if (null != notificationListener)
        {
            return notificationListener;
        }
        notificationListener = new NotificationListener()
        {
            @Override
            public String[] getTags()
            {
                return new String[]
                { CameraNotificationManager.WB_MODE_CHANGE, CameraNotificationManager.WB_DETAIL_CHANGE };
            }
            
            @Override
            public void onNotify(String tag)
            {
                WhiteBalanceController wbc = WhiteBalanceController.getInstance();
                String modeInBase = wbc.getValue();
                
                int temperature = -1;
                WhiteBalanceParam param = (WhiteBalanceParam) wbc.getDetailValue();
                if (modeInBase.equals(WhiteBalanceController.COLOR_TEMP))
                {
                    temperature = param.getColorTemp();
                }
                String mode = getIdFromBase(modeInBase);
                WhiteBalanceParamCandidate[] available = getAvailable();
                if (null != mode)
                {
                    boolean toBeNotified = ParamsGenerator.updateWhiteBalanceParams(mode, temperature, available);
                    if (toBeNotified)
                    {
                        ServerEventHandler.getInstance().onServerStatusChanged();
                    }
                }
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    private static String getBaseIdStr(String param)
    {
        String baseId = null;
        if (WHITEBALANCE_AUTO.equals(param))
        {
            baseId = WhiteBalanceController.AUTO;
        }
        else if (WHITEBALANCE_DAYLIGHT.equals(param))
        {
            baseId = WhiteBalanceController.DAYLIGHT;
        }
        else if (WHITEBALANCE_SHADE.equals(param))
        {
            baseId = WhiteBalanceController.SHADE;
        }
        else if (WHITEBALANCE_CLOUDY.equals(param))
        {
            baseId = WhiteBalanceController.CLOUDY;
        }
        else if (WHITEBALANCE_INCANDESCENT.equals(param))
        {
            baseId = WhiteBalanceController.INCANDESCENT;
        }
        else if (WHITEBALANCE_FLUORESCENT_WARMWHITE.equals(param))
        {
            baseId = WhiteBalanceController.WARM_FLUORESCENT;
        }
        else if (WHITEBALANCE_FLUORESCENT_COOLWHITE.equals(param))
        {
            baseId = WhiteBalanceController.FLUORESCENT_COOLWHITE;
        }
        else if (WHITEBALANCE_FLUORESCENT_DAY.equals(param))
        {
            baseId = WhiteBalanceController.FLUORESCENT_DAYWHITE;
        }
        else if (WHITEBALANCE_FLUORESCENT_DAYLIGHT.equals(param))
        {
            baseId = WhiteBalanceController.FLUORESCENT_DAYLIGHT;
        }
        else if (WHITEBALANCE_FLASH.equals(param))
        {
            baseId = WhiteBalanceController.FLASH;
        }
        else if (WHITEBALANCE_COLOR_TEMPERATURE.equals(param))
        {
            baseId = WhiteBalanceController.COLOR_TEMP;
        }
        else if (WHITEBALANCE_CUSTOM.equals(param))
        {
            baseId = WhiteBalanceController.CUSTOM;
        }
        else if (WHITEBALANCE_CUSTOM1.equals(param))
        {
            baseId = WhiteBalanceController.CUSTOM1;
        }
        else if (WHITEBALANCE_CUSTOM2.equals(param))
        {
            baseId = WhiteBalanceController.CUSTOM2;
        }
        else if (WHITEBALANCE_CUSTOM3.equals(param))
        {
            baseId = WhiteBalanceController.CUSTOM3;
        }
        else
        {
            Log.e(TAG, "Unknown whitebalance parameter name: " + param);
        }
        return baseId;
    }
    
    private static String getIdFromBase(String baseId)
    {
        String id = null;
        if (WhiteBalanceController.AUTO.equals(baseId))
        {
            id = WHITEBALANCE_AUTO;
        }
        else if (WhiteBalanceController.DAYLIGHT.equals(baseId))
        {
            id = WHITEBALANCE_DAYLIGHT;
        }
        else if (WhiteBalanceController.SHADE.equals(baseId))
        {
            id = WHITEBALANCE_SHADE;
        }
        else if (WhiteBalanceController.CLOUDY.equals(baseId))
        {
            id = WHITEBALANCE_CLOUDY;
        }
        else if (WhiteBalanceController.INCANDESCENT.equals(baseId))
        {
            id = WHITEBALANCE_INCANDESCENT;
        }
        else if (WhiteBalanceController.WARM_FLUORESCENT.equals(baseId))
        {
            id = WHITEBALANCE_FLUORESCENT_WARMWHITE;
        }
        else if (WhiteBalanceController.FLUORESCENT_COOLWHITE.equals(baseId))
        {
            id = WHITEBALANCE_FLUORESCENT_COOLWHITE;
        }
        else if (WhiteBalanceController.FLUORESCENT_DAYWHITE.equals(baseId))
        {
            id = WHITEBALANCE_FLUORESCENT_DAY;
        }
        else if (WhiteBalanceController.FLUORESCENT_DAYLIGHT.equals(baseId))
        {
            id = WHITEBALANCE_FLUORESCENT_DAYLIGHT;
        }
        else if (WhiteBalanceController.FLASH.equals(baseId))
        {
            id = WHITEBALANCE_FLASH;
        }
        else if (WhiteBalanceController.COLOR_TEMP.equals(baseId))
        {
            id = WHITEBALANCE_COLOR_TEMPERATURE;
        }
        else if (WhiteBalanceController.CUSTOM.equals(baseId))
        {
            id = WHITEBALANCE_CUSTOM;
        }
        else if (WhiteBalanceController.CUSTOM1.equals(baseId))
        {
            id = WHITEBALANCE_CUSTOM1;
        }
        else if (WhiteBalanceController.CUSTOM2.equals(baseId))
        {
            id = WHITEBALANCE_CUSTOM2;
        }
        else if (WhiteBalanceController.CUSTOM3.equals(baseId))
        {
            id = WHITEBALANCE_CUSTOM3;
        }
        else
        {
            Log.e(TAG, "Unknown BaseApp whitebalance parameter name: " + baseId);
        }
        return id;
    }
    
    private static int[] getMaxAndMinRange(WhiteBalanceController controller, boolean available)
    {
        List<String> optionRange;
        if (available)
        {
            optionRange = controller.getAvailableValue(WhiteBalanceController.SETTING_TEMPERATURE);
        }
        else
        {
            optionRange = controller.getSupportedValue(WhiteBalanceController.SETTING_TEMPERATURE);
        }
        
        if (null == optionRange)
        {
            return SRCtrlConstants.s_EMPTY_INT_ARRAY;
        }
        
        int min = Integer.MIN_VALUE;
        int max = Integer.MIN_VALUE;
        for (String option : optionRange)
        {
            int tmp = Integer.parseInt(option);
            if (Integer.MIN_VALUE == min)
            {
                min = tmp;
            }
            if (Integer.MIN_VALUE == max)
            {
                max = tmp;
            }
            if (max < tmp)
            {
                max = tmp;
            }
            else if (tmp < min)
            {
                min = tmp;
            }
            else
            {
            }
        }
        return new int[]
        { max, min, 100 }; // step(=100) is a private variable in
                           // WhiteBalanceController.
    }
    
    public static boolean set(WhiteBalanceParams param, Boolean optionEnabled)
    {
        
        String baseId = getBaseIdStr(param.whiteBalanceMode);
        if (null == baseId)
        {
            return false;
        }
        
        boolean check = true;
        WhiteBalanceController controller = WhiteBalanceController.getInstance();
        controller.setValue(WhiteBalanceController.WHITEBALANCE, baseId);
        String result = controller.getValue(WhiteBalanceController.WHITEBALANCE);
        if (!result.equals(baseId))
        {
            check = false;
            Log.e(TAG, "Couldn't set white balance mode properly:" + baseId + "<-" + result);
        } else {
            if (WHITEBALANCE_COLOR_TEMPERATURE.equals(param.whiteBalanceMode) && optionEnabled)
            {
                WhiteBalanceController.WhiteBalanceParam detailedParam = (WhiteBalanceController.WhiteBalanceParam)controller.getDetailValue();
                detailedParam.setColorTemp(param.colorTemperature);
                controller.setDetailValue(detailedParam);
                WhiteBalanceController.WhiteBalanceParam resultParam = (WhiteBalanceController.WhiteBalanceParam)controller.getDetailValue();
                if (resultParam.getColorTemp() != param.colorTemperature)
                {
                    check = false;
                    Log.e(TAG, "Couldn't set color temperature properly:" + param.colorTemperature + "<-" + resultParam.getColorTemp());
                }
            }
        }
        return check;
    }
    
    public static WhiteBalanceParams get()
    {
        WhiteBalanceController controller = WhiteBalanceController.getInstance();
        String baseId = controller.getValue(WhiteBalanceController.WHITEBALANCE);
        String id = getIdFromBase(baseId);
        if (null == id)
        {
            return null;
        }
        
        WhiteBalanceParams ret = new WhiteBalanceParams();
        ret.whiteBalanceMode = id;
        if (WhiteBalanceController.COLOR_TEMP.equals(baseId))
        {
            String temperature = controller.getValue(WhiteBalanceController.COLOR_TEMP);
            ret.colorTemperature = Integer.parseInt(temperature);
        }
        
        return ret;
    }
    
    public static WhiteBalanceParamCandidate[] getAvailable()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if (null == exposureMode ||CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode))
        {
            Log.v(TAG, "Set empty array to the available value due to the exposure mode " + exposureMode);
            return s_EMPTY_CANDIDATE;
        }
        
        return getAvailableOrSupportd(true);
    }
    
    public static WhiteBalanceParamCandidate[] getSupportd()
    {
        String exposureMode = CameraOperationExposureMode.get();
        if (null == exposureMode || CameraOperationExposureMode.EXPOSURE_MODE_INTELLIGENT_AUTO.equals(exposureMode))
        {
            Log.v(TAG, "Set empty array to the supported value due to the exposure mode " + exposureMode);
            return s_EMPTY_CANDIDATE;
        }
        
        return getAvailableOrSupportd(false);
    }
    
    public static final WhiteBalanceParamCandidate[] s_EMPTY_CANDIDATE = new WhiteBalanceParamCandidate[0];
    
    private static WhiteBalanceParamCandidate[] getAvailableOrSupportd(boolean bAvailable)
    {
        List<WhiteBalanceParamCandidate> ret = new ArrayList<WhiteBalanceParamCandidate>();
        WhiteBalanceController controller = WhiteBalanceController.getInstance();
        
        List<String> baseCandidate = null;
        if (bAvailable)
        {
            baseCandidate = controller.getAvailableValue(WhiteBalanceController.WHITEBALANCE);
        }
        else
        {
            baseCandidate = controller.getSupportedValue(WhiteBalanceController.WHITEBALANCE);
        }
        if (null != baseCandidate)
        {
            for (String baseId : baseCandidate)
            {
                String id = getIdFromBase(baseId);
                if (null != id)
                {
                    WhiteBalanceParamCandidate candidate = new WhiteBalanceParamCandidate();
                    candidate.whiteBalanceMode = id;
                    if (WhiteBalanceController.COLOR_TEMP.equals(baseId))
                    {
                        candidate.colorTemperatureRange = getMaxAndMinRange(controller, false);
                    }
                    else
                    {
                        candidate.colorTemperatureRange = SRCtrlConstants.s_EMPTY_INT_ARRAY;
                    }
                    ret.add(candidate);
                }
            }
        }
        
        
        int index = 0;
        final int size = ret.size();
        WhiteBalanceParamCandidate[] tmp = new WhiteBalanceParamCandidate[size];
        for(final String aMode : WHITEBALANCE_MENU_ORDER)
        {
            for(int i = 0; i < size && index < size; i++)
            {
                WhiteBalanceParamCandidate param = ret.get(i);
                String thisMode = param .whiteBalanceMode;
                if(aMode.equals(thisMode))
                {
                    tmp[index] = param;
                    index++;
                }
            }
            
            if(index == size)
            {
                // no more match needed
                break;
            }
        }
        return tmp;
    }
}
