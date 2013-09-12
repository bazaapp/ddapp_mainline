package com.duco.ducoplayer.darkodemo.ps;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.duco.ducoplayer.darkodemo.R;
import com.duco.ducoplayer.darkodemo.ScannerActivity;
import com.duco.ducoplayer.darkodemo.ScreenSaverActivity;
import com.duco.ducoplayer.darkodemo.gestures.OnSwipeTouchListener;
import com.duco.ducoplayer.utils.FileUtil;
import com.duco.ducoplayer.utils.GlobalParameters;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PSLandingActivity extends Activity {

	private String homePath;
	private ImageView imageView;
	
	private Timestamp ttmst = null;
	private ScheduledExecutorService scheduleTaskExecutor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pslanding);
		
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	 	
	    
	    init();
	    initialiseGestures();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pslanding, menu);
		return true;
	}
	
	private void init()  {
		String path = this.homePath + "/pages";
		Vector<String> files = FileUtil.dirList(path, FileUtil.FILES_ONLY);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.psLandingLayout);
		
		if(files.size() > 0)  {
			for(String file : files)  {
				Drawable drawable;
				
				//check if file really exists
				if(!FileUtil.exists(path + "/" + file))
					continue;
				
				
				//hidden file
				if(file.startsWith("."))
					continue;
				
				drawable = BitmapDrawable.createFromPath(path + "/" + file);
				//check if visible
				if(!drawable.isVisible())
					continue;

				
				//set the properties for button
			 	imageView = new ImageView(this);
			 	//Drawable drawable = BitmapDrawable.createFromPath(path + "/page00.jpg");
			 	//imageView.setBackground(drawable);
			 	imageView.setBackgroundDrawable(drawable);
			 	imageView.setId(123);
			 	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			 	
			 	imageView.setLayoutParams(params);
	
			    layout.addView(imageView);   
				
				break;
			}
		}
		

	    

	}
	
	private void initialiseGestures()  {
		 //mDetector = new GestureDetectorCompat(this, new MyGestureListener());
		imageView.setOnTouchListener(new OnSwipeTouchListener(){
	        public boolean onSwipeTop() {
	            return true;
	        }
	        public boolean onSwipeRight() {
	        	PSLandingActivity.this.onSwipeRight();
	            return true;
	        }
	        public boolean onSwipeLeft() {
	        	PSLandingActivity.this.onSwipeLeft();
	            return true;
	        }
	        public boolean onSwipeBottom() {
	            return true;
	        }
	    }
				);
		
	    imageView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            
	        }
	    });
		
	}
	
	
    private void onSwipeLeft() {
    	Intent anotherActivityIntent = new Intent(PSLandingActivity.this, PSPage01Activity.class);
    	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", homePath);
    	startActivity(anotherActivityIntent);
    }
    
    private void onSwipeRight()  {
    	
    }
    
	private void screensaver()  {
		Timestamp tmst = new Timestamp(new Date().getTime());
		long diff = tmst.getTime() - this.ttmst.getTime();
		if((diff / (60*1000)) >= 1 )  {
			this.scheduleTaskExecutor.shutdown();

			
			Intent anotherActivityIntent = new Intent(PSLandingActivity.this, ScreenSaverActivity.class);
			//anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", Environment.getExternalStorageDirectory() + "/darko_demo_assets/screensaver/screensaver.mp4");
			anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", GlobalParameters.SDCARD + "/darko_demo_assets/screensaver/screensaver.mp4");
			
			startActivity(anotherActivityIntent);
		}
		
	}
	
	public void onResume()  {
		super.onResume();
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	    
		this.scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		 // This schedule a runnable task every 2 minutes
		 scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		   public void run() {
			   screensaver();
		   }
		 }, 0, 1, TimeUnit.MINUTES);
	    
	}
	
}
