package ezremote.client.data;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class ImageContainer {
	private static ImageContainer sContainer;
    //private Bitmap liveview = null;
    private Bitmap postview = null;
    //private Bitmap postviewOriginal = null;
    private InputStream postviewOriginal = null;
    private DisplayMetrics metrics;
    private String defaultSize = "";
    private String scale = "";
    private String currentSize = "";
    
    public static ImageContainer getInstance() {
        if (null == sContainer) {
            sContainer = new ImageContainer();
        }
        return sContainer;
    }
    
    public ImageContainer(){
    	//liveview = null;
    	postview = null;
    }
    
    public void setPostview(final Bitmap image) {
        this.postview = image;
    }
    
    /*
    public void setPostviewOriginal(final Bitmap image){
    	this.postviewOriginal = image;
    }
    */
    public void setPostviewOriginal(final InputStream is){
    	this.postviewOriginal = is;
    }
    
    /*
    public void setRecyclePostviewOriginal(){
    	postviewOriginal.recycle();
    }
    */
    public void setRecyclePostview(){
    	postview.recycle();
    }
    
    public Bitmap getPostview() {
        return postview;
    }    
    /*
    public Bitmap getPostviewOriginal(){
    	return postviewOriginal;
    }
    */
    public InputStream getPostvviewOriginal(){
    	return postviewOriginal;
    }
    
    public void setDisplayMetrics(DisplayMetrics metrics){
    	this.metrics = metrics;
    }
    
    public DisplayMetrics getDisplayMetrics(){
    	return metrics;
    }
    
    public void setScale(String scale){
    	this.scale = scale;
    }
    
    public void setDefaultSize(String size){
    	this.defaultSize = size;
    }
    
    public void setCurrentSize(String size){
    	this.currentSize = size;
    }
    
    public String getScale(){
    	return scale;
    }
    
    public String getDefaultSize(){
    	return this.defaultSize;
    }
    
    public String getCurrentSize(){
    	return this.currentSize;
    }
    
    
    int liveviewWidth = 0;
    int liveviewHeight = 0;
    
    public void setLiveviewAreaSize(int width, int height){
    	this.liveviewWidth = width;
    	this.liveviewHeight = height;
    }
    
    public float getLiveviewScale(int width, int height){
    	float areaRatio = (float)liveviewWidth/(float)liveviewHeight;
    	float imageRatio = (float)width/(float)height;
    	if(areaRatio > imageRatio){
    		return (float)liveviewHeight/(float)height;
    		//return 480.0f/(float)height;
    	} else {
    		return (float)liveviewWidth/(float)width;
    		//return 640.0f/(float)width;
    	}
    }
}