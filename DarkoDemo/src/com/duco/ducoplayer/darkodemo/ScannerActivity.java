package com.duco.ducoplayer.darkodemo;
/*
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.duco.ducoplayer.utils.FileUtil;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.VideoView;
/*
public class ScannerActivity extends Activity {

	private final String SDCARD = "/mnt/sdcard/";
	//private final String SDCARD = "/mnt/extsd/";
	private String videoPath;
	private VideoView mVideo;
	private ImageView imageHand;
	private static Bitmap handImage;
	private int status = 0;
	private Timestamp ttmst = null;
	private Timer myTimer;
	private ScheduledExecutorService scheduleTaskExecutor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_video2);
		
	
		this.ttmst = new Timestamp(new Date().getTime());
		
		Display display = getWindowManager().getDefaultDisplay();
		AbsoluteLayout layout = (AbsoluteLayout) findViewById(R.id.genericVideoId2);
		imageHand = new ImageView(this);
		//String imagePath = Environment.getExternalStorageDirectory() + "/darko_demo_assets/items/shelftalker/misc/hand.png";
		String imagePath = SDCARD + "/darko_demo_assets/items/scanner/misc/hand.png";
		
		if(handImage == null)
			handImage = BitmapFactory.decodeFile(imagePath);
			imageHand.setImageBitmap(handImage);
			imageHand.setId(123);
		

			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,0,0);
			imageHand.setLayoutParams(params);
			   //add button to the layout
			   layout.addView(imageHand);   
			
			   imageHand.setOnTouchListener(
			   
			   	 new OnTouchListener() {
			
			   	 @Override
			   	 public boolean onTouch(View v, MotionEvent event) {
				   	 status=1;
			   		 return false;
			   	 }
			   	 });

			   layout.setOnTouchListener(new OnTouchListener() {
			
			   	 @Override
			   	 public boolean onTouch(View v, MotionEvent event) {
			   	 // TODO Auto-generated method stub
			
			
			   	 if(status==1) // any event from down and move
			   	 {
			
				   	 android.widget.AbsoluteLayout.LayoutParams lp 
				   	 = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,(int)event.getX()-imageHand.getWidth()/2,(int)event.getY()-imageHand.getHeight()/2);
				   	 imageHand.setLayoutParams(lp);
				   	 
				   	 int coordinate[] = {0,0};
				   	 imageHand.getLocationOnScreen(coordinate);
		

				   	 float x = coordinate[0];
				   	 float y = coordinate[1];

				   	Display display = getWindowManager().getDefaultDisplay();
			   	 }
			   	 if(event.getAction()==MotionEvent.ACTION_UP){
				   	 status=0;
				   	 imageHand.setBackgroundColor(Color.TRANSPARENT);
			
			   	 }
			   	 return true;
			   	 }
			   	 }
			   
			   );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scanner, menu);
		return true;
	}
		
	@Override
	public void onResume()  {
		super.onResume();
		initVideo();
	    this.scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

	 // This schedule a runnable task every 2 minutes
	 scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
	   public void run() {
		   screensaver();
	   }
	 }, 0, 1, TimeUnit.MINUTES);
	    
	}
	    
	private void initVideo()  {
		mVideo = (VideoView)findViewById(R.id.genericVideoView);
		
		Bundle recdData = getIntent().getExtras();
	
		//this.videoPath = Environment.getExternalStorageDirectory() + "/darko_demo_assets/items/shelftalker/videos/openingvideo.mp4";
		this.videoPath = SDCARD + "/darko_demo_assets/items/scanner/videos/openingvideo.mp4";
		
		if(FileUtil.exists(videoPath))  {
			mVideo.setVideoPath(videoPath);
			//mVideo.setMediaController(new MediaController(this));
			mVideo.setMediaController(null);
			mVideo.requestFocus();
			mVideo.start();
			mVideo.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.setLooping(true);
					mp.start();
				}
			});
		}
		else  {
			showError("Can not find assets in SDCARD!!!");
		}
	}
	
	
	
    @Override 
    public boolean onTouchEvent(android.view.MotionEvent event) {
    	this.ttmst = new Timestamp(new Date().getTime());
    	if(mVideo != null)  {
    		if(!mVideo.isPlaying())  {
    			//mVideo.start();
    			
    			
    		}
    	} 	
    	return super.onTouchEvent(event);
    }
	
	private void showError(String error)  {
		final AlertDialog alertDialog = new AlertDialog.Builder(ScannerActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setIcon(R.drawable.exclamationpoint);
        
        alertDialog.setMessage(error);
        	
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              return;
          } });
        alertDialog.show();
	}
	
	
	
	private void screensaver()  {
		Timestamp tmst = new Timestamp(new Date().getTime());
		long diff = tmst.getTime() - this.ttmst.getTime();
		if((diff / (60*1000)) >= 1 )  {
			this.scheduleTaskExecutor.shutdown();
			if(mVideo != null && mVideo.isPlaying())  {
				mVideo.stopPlayback();
			}
			
			Intent anotherActivityIntent = new Intent(ScannerActivity.this, ScreenSaverActivity.class);
			//anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", Environment.getExternalStorageDirectory() + "/darko_demo_assets/screensaver/screensaver.mp4");
			anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", SDCARD + "/darko_demo_assets/screensaver/screensaver.mp4");
			
			startActivity(anotherActivityIntent);
		}
		
	}
}
*/


import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

import com.duco.ducoplayer.utils.GlobalParameters;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;

//import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;


public class ScannerActivity extends Activity {
	
	private static Drawable background;
	private static Bitmap handImage;
	private ImageView imageHand;
	private int status = 0;
	private int hits = 0;
	private String homePath;
	private boolean busy = false;
	
	private native void sendData(String usbPath, int[] buffer,int length); 
	private native void setLed(String usbPath, char num,int color); 
	
	private Timestamp ttmst = null;
	private ScheduledExecutorService scheduleTaskExecutor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_scanner);
	Display display = getWindowManager().getDefaultDisplay();
	AbsoluteLayout layout = (AbsoluteLayout) findViewById(R.id.scannerLayout);
	Bundle recdData = getIntent().getExtras();
	   this.homePath = 
	   	 (String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	   if(background == null)
	   	background = BitmapDrawable.createFromPath(homePath + "/pages/" + "page01.jpg");
	   
	   layout.setBackgroundDrawable(background);
	   
	   //set the properties for button
	imageHand = new ImageView(this);
	if(handImage == null)
	handImage = BitmapFactory.decodeFile(homePath + "/misc/" + "hand.png");
	imageHand.setImageBitmap(handImage);
	imageHand.setId(123);
	
	LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	imageHand.setLayoutParams(params);
	   //add button to the layout
	   layout.addView(imageHand);   
	
	   imageHand.setOnTouchListener(
	   
	   	 new OnTouchListener() {
	
	   	 @Override
	   	 public boolean onTouch(View v, MotionEvent event) {
	
	   	 status=1;
	
	
	   	 return false;
	   	 }
	   	 });
	   	 layout.setOnTouchListener(new OnTouchListener() {
	
	   	 @Override
	   	 public boolean onTouch(View v, MotionEvent event) {
	   	 // TODO Auto-generated method stub
	
	
	   	 if(status==1) // any event from down and move
	   	 {
	
		   	 android.widget.AbsoluteLayout.LayoutParams lp 
		   	 = new android.widget.AbsoluteLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,(int)event.getX()-imageHand.getWidth()/2,(int)event.getY()-imageHand.getHeight()/2);
		   	 imageHand.setLayoutParams(lp);
		   	 
		   	 int coordinate[] = {0,0};
		   	 imageHand.getLocationOnScreen(coordinate);
/*
		   	 float x = imageHand.getX();
		   	 float y = imageHand.getY();
		   	 */

		   	 float x = coordinate[0];
		   	 float y = coordinate[1];

		   	Display display = getWindowManager().getDefaultDisplay();
		   	
		   	 if(((y / display.getHeight()) > 0.45f) && ((y / display.getHeight()) < 0.60f))  {
		   		 
		   		 if(((x / display.getWidth()) > 0.60f) && ((x / display.getWidth()) < 0.70f))  {
		   			 if(!busy)  {
		   				 BeepTask task = new BeepTask();
					   	 task.execute();
				   	 }
		   		 }
		   		 /*
		   		 if(ScannerActivity.this.hits == 10)  {
		   			Intent anotherActivityIntent = new Intent(ScannerActivity.this, GenericMultiplePageActivity.class);
	            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", ScannerActivity.this.homePath + "/pages/");
	            	startActivity(anotherActivityIntent);
		   		 }
		   		 */
		   	 }
	
	   	 }
	   	 if(event.getAction()==MotionEvent.ACTION_UP){
	   	 status=0;
	
	   	 imageHand.setBackgroundColor(Color.TRANSPARENT);
	
	   	 }
	   	 return true;
	   	 }
	   	 }
	   
	   );
	   
	   
	}
	
	
	private class BeepTask extends AsyncTask<Void, Void, Void> {
		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		
		@Override
		protected Void doInBackground(Void... params) {
		    ScannerActivity.this.busy = true;
			tg.startTone(ToneGenerator.TONE_PROP_BEEP);
			return null;
		} 
		
		@Override
	    protected void onPostExecute(Void result) {
		    tg.stopTone();
		    tg.release();
		    ScannerActivity.this.hits++;
		    if(ScannerActivity.this.hits == 10)  {
	   			Intent anotherActivityIntent = new Intent(ScannerActivity.this, GenericMultiplePageActivity.class);
            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", ScannerActivity.this.homePath + "/pages/");
            	startActivity(anotherActivityIntent);
	   		 }
		    ScannerActivity.this.busy = false;
		}
		
	}
	
	@Override
	public void onResume()  {
		super.onResume();
		this.hits = 0;
		this.busy = false;
		
		this.scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		 // This schedule a runnable task every 2 minutes
		 scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		   public void run() {
			   screensaver();
		   }
		 }, 0, 1, TimeUnit.MINUTES);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.scanner, menu);
	return true;
	}
	

	private void screensaver()  {
		Timestamp tmst = new Timestamp(new Date().getTime());
		long diff = tmst.getTime() - this.ttmst.getTime();
		if((diff / (60*1000)) >= 1 )  {
			this.scheduleTaskExecutor.shutdown();

			
			Intent anotherActivityIntent = new Intent(ScannerActivity.this, ScreenSaverActivity.class);
			//anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", Environment.getExternalStorageDirectory() + "/darko_demo_assets/screensaver/screensaver.mp4");
			anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", GlobalParameters.SDCARD + "/darko_demo_assets/screensaver/screensaver.mp4");
			
			startActivity(anotherActivityIntent);
		}
		
	}

	
}
