package com.sony.imaging.app.srctrl.util.cameraoperation;

import java.lang.ref.WeakReference;
import android.util.Log;
import com.sony.imaging.app.base.shooting.camera.CameraNotificationManager;
import com.sony.imaging.app.base.shooting.camera.CameraSetting;
import com.sony.imaging.app.base.shooting.camera.DigitalZoomController;
import com.sony.imaging.app.base.shooting.camera.executor.ExecutorCreator;
import com.sony.imaging.app.srctrl.webapi.availability.ParamsGenerator;
import com.sony.imaging.app.srctrl.webapi.specific.ServerEventHandler;
import com.sony.imaging.app.util.NotificationListener;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.sysutil.ScalarProperties;
import com.sony.scalar.webapi.service.camera.v1_0.common.struct.TouchAFCurrentPositionParams;

public class CameraOperationZoom
{
    private static final String TAG = CameraOperationZoom.class.getSimpleName();
    
    private static final String ZOOM_DIRECTION_IN = "in";
    private static final String ZOOM_DIRECTION_OUT = "out";
    private static final String ZOOM_MOVEMENT_STRAT = "start";
    private static final String ZOOM_MOVEMENT_STOP = "stop";
    private static final String ZOOM_MOVEMENT_SHORT_PUSH = "1shot";
        
    private static final int ZOOM_NUMBER_BOX_OPT = 1;
    private static final int ZOOM_NUMBER_BOX_DIGIT = 1;
    private static final int ZOOM_NUMBER_BOX_OPT_DIGIT = 2;
    
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
                      { CameraNotificationManager.ZOOM_INFO_CHANGED,
                		CameraNotificationManager.POWER_ZOOM_CHANGED,
        				CameraNotificationManager.DEVICE_LENS_CHANGED,
                		CameraNotificationManager.PICTURE_QUALITY,
                		CameraNotificationManager.PICTURE_SIZE};
            }
            
        	/**
        	 * Display pattern which shows optical+digital zoom bar
        	 * */
        	public static final int DISPLAY_PATTERN_OPT_DIGIT = 0;
        	
        	//public static final int DISPLAY_PATTERN_MULT = 1;
        		
        	/**
        	 * Display pattern which shows digital zoom bar
        	 * */
        	public static final int DISPLAY_PATTERN_DIGIT = 2;

        	/**
        	 * Display pattern which shows optical zoom bar
        	 * */
        	public static final int DISPLAY_PATTERN_OPT = 3;

        	/**
        	 * Display pattern which is not displayed 
        	 * */
        	public static final int DISPLAY_PATTERN_NONE = 5;

        	/**
        	 * Display pattern which is undefined
        	 * */
        	public static final int DISPLAY_PATTERN_UNDEFINED = 10;
            
        	private static final int INIT_ZOOM_MAG = 100;

            private int getDispPattern()
            {
        		int dispPattern = DISPLAY_PATTERN_UNDEFINED;

    			if(ScalarProperties.INTVAL_CATEGORY_ILDC_E == ScalarProperties.getInt(ScalarProperties.PROP_MODEL_CATEGORY))
    			{ //E-mnt対応
    				if(DigitalZoomController.getInstance().isZoomAvailable())
    				{
    					if(ExecutorCreator.getInstance().isSpinalZoom())
    					{
    						if(CameraEx.PowerZoomListener.STATUS_AVAILABLE == CameraSetting.getInstance().getPowerZoomStatus()) 
    						{
    							dispPattern = DISPLAY_PATTERN_OPT_DIGIT;
    						} 
    						else 
    						{
    							dispPattern = DISPLAY_PATTERN_DIGIT;
    						} 
    					} 
    					else 
    					{
    						dispPattern = DISPLAY_PATTERN_OPT;
    					}
    				} 
    				else 
    				{
						if(CameraEx.PowerZoomListener.STATUS_AVAILABLE == CameraSetting.getInstance().getPowerZoomStatus()) 
						{
							dispPattern = DISPLAY_PATTERN_OPT;
						}
						else
						{
							dispPattern = DISPLAY_PATTERN_NONE;
						}
    				}
    			} 
    			else 
    			{ //TODO A, DSC対応
					if(ScalarProperties.INTVAL_SUPPORTED == ScalarProperties.getInt(ScalarProperties.PROP_DEVICE_ZOOM_LEVER)) 
					{
						if(INIT_ZOOM_MAG == DigitalZoomController.getInstance().getMaxDigitalZoomMagnification(DigitalZoomController.DIGITAL_ZOOM_TYPE_PRECISION))
						{
							dispPattern = DISPLAY_PATTERN_OPT;
						}
						else
						{
							dispPattern = DISPLAY_PATTERN_OPT_DIGIT;
						}
					}
					else 
					{
						dispPattern = DISPLAY_PATTERN_DIGIT;
					}
    			}
                Log.v(TAG, "getDispPattern: dispPattern=" + dispPattern);
    			return dispPattern;
    		} 
            
            @Override
            public void onNotify(String tag)
            {
            	if (CameraNotificationManager.ZOOM_INFO_CHANGED.equals(tag) 
           			|| CameraNotificationManager.POWER_ZOOM_CHANGED.equals(tag)
           			|| CameraNotificationManager.DEVICE_LENS_CHANGED.equals(tag)
           			|| CameraNotificationManager.PICTURE_QUALITY.equals(tag)
           			|| CameraNotificationManager.PICTURE_SIZE.equals(tag))
            	{
	        		int position = -1;
	        		int numberBox = -1;
	        		int indexCurrentBox = -1;
	        		int positionCurrentBox = -1;
	
            		switch(getDispPattern()) {
        			default:
            		case DISPLAY_PATTERN_UNDEFINED:
            		case DISPLAY_PATTERN_NONE:
            			break;

            		case DISPLAY_PATTERN_OPT_DIGIT:
            			numberBox = ZOOM_NUMBER_BOX_OPT_DIGIT;
                		if (DigitalZoomController.getInstance().isDigitalZoomStatus())
                    	{
                    		// DigitalZoom
                    		indexCurrentBox = 1;
                    		positionCurrentBox = DigitalZoomController.getInstance().getDigitalZoomPosition();
                    		position = (positionCurrentBox + 100) / 2;
                    	}
                    	else
                    	{
                    		indexCurrentBox = 0;
                    		positionCurrentBox = DigitalZoomController.getInstance().getOpticalZoomPosition();
                    		position = positionCurrentBox / 2;
                    	}
            			break;
            			
            		case DISPLAY_PATTERN_DIGIT:
            			numberBox = ZOOM_NUMBER_BOX_DIGIT;
                		indexCurrentBox = 0;
                		position = DigitalZoomController.getInstance().getDigitalZoomPosition();
                		positionCurrentBox = position;
            			break;
            			
            		case DISPLAY_PATTERN_OPT:
            			numberBox = ZOOM_NUMBER_BOX_OPT;
                		indexCurrentBox = 0;
                		position = DigitalZoomController.getInstance().getOpticalZoomPosition();
                		positionCurrentBox = position;
            			break;
            		}
            		
	                Log.v(TAG, "onNotify: position=" + position + "  numberBox:" + numberBox + "  indexCurrentBox:" + indexCurrentBox + "  positionCurrentBox:" + positionCurrentBox);
            		boolean toBeNotified = ParamsGenerator.updateZoomInformationParams(position, numberBox, indexCurrentBox, positionCurrentBox);
                    if (toBeNotified)
                    {
                    	TouchAFCurrentPositionParams param = CameraOperationTouchAFPosition.get();
                    	if (param.set)
                    	{
                    		CameraOperationTouchAFPosition.leaveTouchAFMode(true);
                    	}
                        ServerEventHandler.getInstance().onServerStatusChanged();
                    }
            	}
            }
        };
        s_NotificationListenerRef = new WeakReference<NotificationListener>(notificationListener);
        return notificationListener;
    }
    
    private static int getBaseIdStr(String param)
    {
        int baseId = -1;
        if (ZOOM_DIRECTION_IN.equals(param))
        {
        	baseId = DigitalZoomController.DIRECTION_TELE;
        }
        else if (ZOOM_DIRECTION_OUT.equals(param))
        {
        	baseId = DigitalZoomController.DIRECTION_WIDE;
        } 
        else 
        {
            Log.e(TAG, "invalid direction. param=" + param);
        }
        return baseId;
    }
 
    private static int getZoomSpeed() {
    	// TODO 要チューニング（外部ファイル等から読み込めるように？）
    	int speed = DigitalZoomController.getInstance().getMaxZoomSpeed();
    	speed = (speed / 8);
    	
    	return speed;
    }

    private static int getOneShotZoomSpeed() {
    	// TODO 要チューニング（外部ファイル等から読み込めるように？）
    	int speed = DigitalZoomController.getInstance().getMaxZoomSpeed();
    	speed = (speed / 4);
    	
    	return speed;
    }

    private static int getOneShotWaitingTime() {
    	// TODO 要チューニング（外部ファイル等から読み込めるように）
    	return 100;
    }
    
    public static boolean set(String direction, String movement)
    {
    	if (ZOOM_MOVEMENT_STRAT.equals(movement)) 
    	{
    		int baseId = getBaseIdStr(direction);
            if (-1 == baseId)
            {
                return false;
            }
        	// TODO ここに脊髄ZOOMオフ指定処理入れる？
        	DigitalZoomController.getInstance().startZoom(baseId, getZoomSpeed());
    	}
    	else if (ZOOM_MOVEMENT_STOP.equals(movement))
    	{
        	DigitalZoomController.getInstance().stopZoom();
        	// TODO ここに脊髄ZOOMオン指定処理入れる？
    	}
    	else if (ZOOM_MOVEMENT_SHORT_PUSH.equals(movement))
    	{
    		int baseId = getBaseIdStr(direction);
            if (-1 == baseId)
            {
                return false;
            }
        	// TODO ここに脊髄ZOOMオフ指定処理入れる？
        	DigitalZoomController.getInstance().startZoom(baseId, getOneShotZoomSpeed());
        	
        	try {
				Thread.sleep(getOneShotWaitingTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
        	finally
        	{
            	DigitalZoomController.getInstance().stopZoom();
            	// TODO ここに脊髄ZOOMオン指定処理入れる？
        	}
    	}
        return true;
    }
}
