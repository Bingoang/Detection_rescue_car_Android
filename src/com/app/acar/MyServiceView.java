package com.app.acar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class MyServiceView extends SurfaceView implements Callback, Runnable {
	//声明
	private SurfaceHolder sfh;
	private Canvas canvas;
	private String imageURL = "http://192.168.8.1:8083/?action=snapshot";		//视频地址
	URL videoURL;
	HttpURLConnection conn;
	private Bitmap mBitmap;
	Bitmap bitmap;
	private Paint paint;
	InputStream inputStream;
	
	private static int mScreenWidth; 
    private static int mScreenHeight;
    float myEyesDistence;
    int numberOfFaceDetected;
    Paint myPaint = new Paint();	

	public MyServiceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ScreenValue();
		paint = new Paint();
		paint.setAntiAlias(true);
		myPaint.setColor(Color.GREEN);
		myPaint.setStyle(Paint.Style.STROKE);
		myPaint.setStrokeWidth(3);
		
		sfh = this.getHolder();
  	  	sfh.addCallback(this);
  	  	this.setKeepScreenOn(true);
  	  	setFocusable(true);
  	  	this.getWidth();
  	  	this.getHeight();
	}
	
	/**
	 * 初始化——获得屏幕像素值
	 */
	private void ScreenValue() {
		DisplayMetrics dm = new  DisplayMetrics();     
		dm = getResources().getDisplayMetrics();
	    mScreenWidth = dm.widthPixels; 
	    mScreenHeight = dm.heightPixels;		
	}

	class DrawVideo extends Thread{
		public DrawVideo(){
			
		}
		
		public void run() {
			while(true){
				try {
					videoURL = new URL(imageURL);
					conn = (HttpURLConnection) videoURL.openConnection();
					conn.connect();
					inputStream = conn.getInputStream();		//	获取流
					BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();// 配置原图缩放值
					BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
					bitmap = BitmapFactory.decodeStream(inputStream,null,BitmapFactoryOptionsbfo);//从获取的流中构建出BMP图像
					mBitmap = Bitmap.createScaledBitmap(bitmap, mScreenWidth, mScreenHeight, true);
					
					canvas = sfh.lockCanvas();   //锁定画布
					canvas.drawColor(Color.WHITE);
					canvas.drawBitmap(mBitmap, 0,0, null);//把BMP图像画在画布上
//					Thread.sleep(30);
					sfh.unlockCanvasAndPost(canvas);//画完一副图像，解锁画布
				} catch (Exception ex) {
					ex.printStackTrace();
				}finally{
					conn.disconnect();
				}
			}
		}
	}	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new DrawVideo().start();
	}	


	@Override
	public void run() {
		while(true){
			try {
				videoURL = new URL(imageURL);
				conn = (HttpURLConnection) videoURL.openConnection();
				conn.connect();
				
	         	inputStream = conn.getInputStream(); //获取流
	         	
	         	bitmap = BitmapFactory.decodeStream(inputStream);//从获取的流中构建出BMP图像
	         	mBitmap = Bitmap.createScaledBitmap(bitmap, mScreenWidth, mScreenHeight, true);
	            canvas = sfh.lockCanvas();
	            canvas.drawColor(Color.WHITE);
	            canvas.drawBitmap(mBitmap, 0,0, null);//把BMP图像画在画布上
	            sfh.unlockCanvasAndPost(canvas);//画完一副图像，解锁画布 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				conn.disconnect();
			}
		}			
	}
	
    public void drowblack(){
    	
    }

}
