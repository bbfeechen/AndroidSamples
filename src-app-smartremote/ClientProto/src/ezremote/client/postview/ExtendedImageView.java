package ezremote.client.postview;

import java.io.InputStream;

import ezremote.client.data.ImageContainer;
import ezremote.client.util.DevLog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ExtendedImageView extends ImageView{
	protected Bitmap image = null;
	
	private int viewWidth, viewHeight;
	
	private float pinchScale = 1.0f;
	private PointF pinchStartPointF = new PointF();
	//private PointF pinchPrePointF = new PointF();
	//private float pinchPreDistance = 0.0f;
	private float pinchStartDistance = 0.0f;
	private float moveX = 0.0f;
	private float moveY = 0.0f;
	private PointF dragStartPointF = new PointF(), pF = new PointF();
	private PointFId[] pointFIds;
	
	private static final int TOUCH_OFF = 0;
	private static final int TOUCH_DRAG = 1;
	private static final int TOUCH_ZOOM = 2;
	private static final int TOUCH_ZOOM_TO_DRAG = 3;
	private int touchMode = TOUCH_OFF;
	
	private float s_x, s_y, defaultScale;
	private float baseScale, tempScale;
	private float baseMoveX, baseMoveY, tempMoveX, tempMoveY;
	
	private ImageStatusCallback isCb;
	
	public ExtendedImageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public ExtendedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ExtendedImageView(Context context) {
		super(context);
	}
		
	public void setSize(int width, int height){
		this.viewWidth = width;
		this.viewHeight = height;
		//DevLog.i("ViewSize", width + "x" + height);
	}
	
	private void initScale(){
		if(image != null){
			s_x = (float)viewWidth / (float)image.getWidth();
			s_y = (float)viewHeight / (float)image.getHeight();
			defaultScale = Math.min(s_x, s_y);
			updateBaseScale(defaultScale);
		}
	}
	
	private void initSettings(){
		initScale();
		tempScale = baseScale;
		updateBaseMove(0, 0);
		tempMoveX = baseMoveX;
		tempMoveY = baseMoveY;
		//invalidate();
	}
	
	public void setImage(Bitmap image){
		if(image != null){
			this.image = image;
			initSettings();
			ImageContainer.getInstance().setDefaultSize(image.getWidth()+"x"+image.getHeight());
		}
	}
	
	public void setImageStream(InputStream is){
		if(is != null){
			this.image = BitmapFactory.decodeStream(is);
			initSettings();
			ImageContainer.getInstance().setDefaultSize(image.getWidth()+"x"+image.getHeight());
		}
	}
	
	private void updateBaseScale(float scale){
		baseScale = scale;
	}
	
	private void updateBaseMove(float x, float y){
		baseMoveX = x;
		baseMoveY = y;
	}

	
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(image == null){
			return;
		}
		tempScale = baseScale * pinchScale;
		if(tempScale > 1.0f){
			tempScale = 1.0f;
		}
		Matrix matrix = new Matrix();
		if(touchMode == TOUCH_ZOOM){
			tempMoveX = moveX + baseMoveX - (tempScale / baseScale - 1) * pinchStartPointF.x;
			tempMoveY = moveY + baseMoveY - (tempScale / baseScale - 1) * pinchStartPointF.y;
		} else if (touchMode == TOUCH_DRAG){
			tempMoveX = moveX + baseMoveX;
			tempMoveY = moveY + baseMoveY;
		}
		
		if((float)image.getWidth()*tempScale < (float)viewWidth){
			tempMoveX = ((float)viewWidth - (float)image.getWidth()*tempScale) / 2.0f;
		} else {
			if(tempMoveX > 0){
				tempMoveX = 0.0f;
				baseMoveX = 0.0f;
				moveX = 0.0f;
				dragStartPointF.x = pF.x;
			}
			if((float)image.getWidth()*tempScale + tempMoveX < (float)viewWidth){
				tempMoveX = (float)viewWidth - (float)image.getWidth()*tempScale;
				dragStartPointF.x = pF.x;
				baseMoveX= tempMoveX;
				moveX = 0.0f;
			}
		}

		if((float)image.getHeight()*tempScale < (float)viewHeight){
			tempMoveY = ((float)viewHeight - (float)image.getHeight()*tempScale) / 2.0f;
		} else {
			if(tempMoveY > 0){
				tempMoveY = 0;
				baseMoveY = 0.0f;
				moveY = 0;
				dragStartPointF.y = pF.y;			
			}
			if((float)image.getHeight()*tempScale + tempMoveY < (float)viewHeight){
				tempMoveY = (float)viewHeight - (float)image.getHeight()*tempScale;
				dragStartPointF.y = pF.y;
				baseMoveY = tempMoveY;
				moveY = 0.0f;
			}
		}

		if((float)image.getWidth()*tempScale < (float)viewWidth && (float)image.getHeight()*tempScale < (float)viewHeight){
			tempScale = defaultScale;
			tempMoveY = ((float)viewHeight - (float)image.getHeight()*tempScale) / 2.0f;
			tempMoveX = ((float)viewWidth - (float)image.getWidth()*tempScale) / 2.0f;
		}
		

		matrix.postScale(tempScale, tempScale);
		matrix.postTranslate(tempMoveX, tempMoveY);
		canvas.drawBitmap(image, matrix, null);
		if(touchMode == TOUCH_ZOOM){
			//updateBaseScale(tempScale);
			//updateBaseMove(tempMoveX, tempMoveY);
		}

		ImageContainer.getInstance().setScale(Float.toString(tempScale));
		ImageContainer.getInstance().setCurrentSize((int)((float)image.getWidth()*tempScale) + "x" + (int)((float)image.getHeight()*tempScale));
		isCb.onStatusUpdate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction() & MotionEvent.ACTION_MASK){
			case MotionEvent.ACTION_DOWN:
				if(event.getPointerCount() == 1){
					//DevLog.i("TouchMode: DRAG");
					dragStartPointF = getHere(event);
					touchMode = TOUCH_DRAG;
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				//DevLog.i("onTouchEvent: ACTION_POINTER_DOWN");
				if(event.getPointerCount() >= 2){
					pinchStartDistance = getDistance(event);
					if(pinchStartDistance > 50f){
						pinchStartPointF = getCenter(event);
						updateBaseMove(tempMoveX, tempMoveY);
						moveX = 0.0f;
						moveY = 0.0f;
						//pinchPrePointF = getCenter(event);
						//pinchPreDistance = getDistance(event);
						touchMode = TOUCH_ZOOM;
						//DevLog.i("TouchMode: ZOOM");						
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(touchMode == TOUCH_ZOOM && pinchStartDistance > 0){
					//DevLog.i("TouchAction: Scaling");
					//pinchScale = getDistance(event) / pinchPreDistance;
					pinchScale = getDistance(event) / pinchStartDistance;
					pF = getCenter(event);
					//moveX = pF.x - pinchPrePointF.x;
					//moveY = pF.y - pinchPrePointF.y;
					moveX = pF.x - pinchStartPointF.x;
					moveY = pF.y - pinchStartPointF.y;	
					//pinchPrePointF = pF;
					//pinchPreDistance = getDistance(event);
				} else if(touchMode == TOUCH_DRAG){
					pF = getHere(event);
					moveX = pF.x - dragStartPointF.x;
					moveY = pF.y - dragStartPointF.y;
					//DevLog.i("TouchAction: Drag");
				} else if(touchMode == TOUCH_ZOOM_TO_DRAG){
					dragStartPointF = getRemainingPointF(event);
					pF = getHere(event);
					moveX = pF.x - dragStartPointF.x;
					moveY = pF.y - dragStartPointF.y;
					touchMode = TOUCH_DRAG;
					//DevLog.i("TouchAction: First Drag");
					//DevLog.i("TouchMode: DRAG");
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				updateBaseScale(tempScale);
				updateBaseMove(tempMoveX, tempMoveY);
				moveX = 0.0f;
				moveY = 0.0f;
				touchMode = TOUCH_OFF;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				if(touchMode == TOUCH_ZOOM && event.getPointerCount()==2){
					//DevLog.i("TouchMode: ZOOM_TO_DRAG");
					updateBaseScale(tempScale);
					updateBaseMove(tempMoveX, tempMoveY);
					pinchScale = 1.0f;
					pointFIds = getHereDouble(event);
					moveX = 0.0f;
					moveY = 0.0f;
					touchMode = TOUCH_ZOOM_TO_DRAG;
				}
				break;
		}
		return true;
	}
	
	private float getDistance(MotionEvent event){
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return android.util.FloatMath.sqrt(x*x + y*y);
	}
	
	private PointF getCenter(MotionEvent event){
		PointF pointF = new PointF();
		pointF.x = (event.getX(0) + event.getX(1)) * 0.5f;
		pointF.y = (event.getY(0) + event.getY(1)) * 0.5f;
		return pointF;
	}
	
	private PointF getHere(MotionEvent event){
		PointF pointF = new PointF();
		if(event.getPointerCount() == 1){
			pointF.x = event.getX(0);
			pointF.y = event.getY(0);
		}
		return pointF;
	}
	
	private PointFId[] getHereDouble(MotionEvent event){
		if(event.getPointerCount()==2){
			PointFId[] pointFIds = new PointFId[event.getPointerCount()];
			for(int i=0; i<pointFIds.length; i++){
				int id = event.getPointerId(i);
				try{
					pointFIds[id] = new PointFId(new PointF(event.getX(id), event.getY(id)), id);
				} catch (IllegalArgumentException e){
					DevLog.e("IllegalArgumentException");
				}
			}
			return pointFIds;
		} else {
			return null;
		}
	}
	
	private PointF getRemainingPointF(MotionEvent event){
		PointF pF = new PointF();
		int currentId = event.getPointerId(0);
		for(int i=0; i<pointFIds.length; i++){
			try{
				if(pointFIds[i].getPointerId() == currentId){
					pF = pointFIds[i].getPointF();
				} 
			}catch (NullPointerException e){
				DevLog.e("NullPointerException");
			}
		}
		return pF;
	}
	
	private class PointFId{
		PointF pointF;
		int id;
		public PointFId(PointF pointF, int id){
			setPointF(pointF);
			setId(id);
		}
		
		private void setPointF(PointF pointF){
			this.pointF = pointF;
		}
		private void setId(int id){
			this.id = id;
		}
		public PointF getPointF(){
			return pointF;
		}
		public int getPointerId(){
			return id;
		}
	}

	public void setImageStatusCallback(ImageStatusCallback isCb){
		this.isCb = isCb;
	}
	
}
