package ezremote.client.webapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sony.mexi.orb.client.smartremotecontroltestclient.v1_2.SmartRemoteControlTestClientClient;
import com.sony.scalar.webapi.interfaces.client.srctestclient.v1_2.SmartRemoteControlTestClient;

import android.os.Handler;
import android.util.Log;


public class WebApiBackgroundOperator {
	private static final String TAG = WebApiBackgroundOperator.class.getSimpleName();
	private SmartRemoteControlTestClientClient webApiClient;
	private Handler handler;
	
	public WebApiBackgroundOperator(SmartRemoteControlTestClientClient webApiClient, Handler handler){
		this.webApiClient = webApiClient;
		this.handler = handler;
	}

	public void getVersions(){		
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "getVersions");
						webApiClient.getVersions(WebApiCallbacksContainer.getVersionHandler());
					}
				});
		    }
		}).start();
	}
	
	public void getMethodTypes(final String version){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "getMethodTypes");
						webApiClient.getMethodTypes(version, WebApiCallbacksContainer.getMethodTypeHandler());
					}
				});
		    }
		}).start();
	}
	
	public void getApplicationInfo(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "getApplicationInfo");
						webApiClient.getApplicationInfo(WebApiCallbacksContainer.getGetApplicationInfoCallback());
					}
				});
		    }
		}).start();
	}
	
	public void startRecMode(){		
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "startRecMode");
						webApiClient.startRecMode(WebApiCallbacksContainer.getStartRecModeCallback());
					}
				});
		    }
		}).start();
		//invokeNoArgumentMethod(WebApiCallbacksContainer.getStartRecModeCallback(), "startRecMode");
	}
	
	public void stopRecMode(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "stopRecMode");
						webApiClient.stopRecMode(WebApiCallbacksContainer.getStopRecModeCallback());
					}
				});
		    }
		}).start();
		//invokeNoArgumentMethod(WebApiCallbacksContainer.getStopRecModeCallback(), "stopRecMode");
	}
	
	public void actTakePicture(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "actTakePicture");
						webApiClient.actTakePicture(WebApiCallbacksContainer.getActTakePictureCallback());
					}
				});
		    }
		}).start();
		//invokeNoArgumentMethod(WebApiCallbacksContainer.getActTakePictureCallback(), "actTakePicture");
	}
	
	public void awaitTakePicture(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "awaitTakePicture");
						webApiClient.awaitTakePicture(WebApiCallbacksContainer.getAwaitTakePictureCallback());
					}
				});
		    }
		}).start();
	}
	
	public void startLiveview(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "startLiveview");
						webApiClient.startLiveview(WebApiCallbacksContainer.getStartLiveviewCallback());
					}
				});
		    }
		}).start();
		//invokeNoArgumentMethod(WebApiCallbacksContainer.getStartLiveviewCallback(), "startLiveview");
	}
	
	public void stopLiveview(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						Log.i(TAG, "stopLiveview");
						webApiClient.stopLiveview(WebApiCallbacksContainer.getStopLiveviewCallback());
					}
				});
		    }
		}).start();
		//invokeNoArgumentMethod(WebApiCallbacksContainer.getStopLiveviewCallback(), "stopLiveview");
	}
	
	public void getAvailableApiList(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getAvailableApiList(WebApiCallbacksContainer.getGetAvailableApiListCallback());
					}
				});
		    }
		}).start();
	}
	
	public void getAvailableExposureCompensation(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getAvailableExposureCompensation(WebApiCallbacksContainer.getGetAvailableExposureCompensationCallback());
					}
				});
		    }
		}).start();
	}
	
	public void getAvailableSelfTimer(){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.getAvailableSelfTimer(WebApiCallbacksContainer.getGetAvailableSelfTimerCallback());
					}
				});
		    }
		}).start();
	}

	public void setSelfTimer(final int timer){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.setSelfTimer(timer, WebApiCallbacksContainer.getSetSelfTimerCallback());
					}
				});
		    }
		}).start();
	}
	
	public void setExposureCompensation(final int exposure){
		new Thread(new Runnable() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						webApiClient.setExposureCompensation(exposure, WebApiCallbacksContainer.getSetExposureCompensationCallback());
					}
				});
		    }
		}).start();
	}
	
	public void setTouchAFPosition(final double rx, final double ry) {
		handler.post(new Runnable() {
			public void run() {
				webApiClient.setTouchAFPosition(rx, ry, WebApiCallbacksContainer.getSetTouchAFPositionCallback());
			}
		});
	}
	public void cancelTouchAFPosition(){
		handler.post(new Runnable() {
			public void run() {
				webApiClient.cancelTouchAFPosition(WebApiCallbacksContainer.getCancelTouchAFPositionCallback());
			}
		});
	}

	private void invokeNoArgumentMethod(final Object callbacks, String methodName){
		Log.e(TAG, callbacks.getClass().getSimpleName());
		Log.e(TAG, callbacks.getClass().getSuperclass().getSimpleName());
		Log.e(TAG, callbacks.getClass().getSuperclass().getSuperclass().getSimpleName());
		try {
			final Method method = SmartRemoteControlTestClientClient.class.getMethod(methodName, new Class[]{callbacks.getClass().getSuperclass()});
			new Thread(new Runnable() {
				public void run() {
					handler.post(new Runnable() {
						public void run() {
							try {
								method.invoke(webApiClient, callbacks);
							} catch (IllegalArgumentException e) {
								Log.e(TAG, "Illegal Argument Exception");
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								Log.e(TAG, "Illegal Access Exception");
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								Log.e(TAG, "Illegal Target Exception");
								e.printStackTrace();
							}
						}
					});
			    }
			}).start();
		} catch (SecurityException e1) {
			Log.e(TAG, "Security Exception");
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			Log.e(TAG, "No Such Method Exception");
			e1.printStackTrace();
		}
	}
}
