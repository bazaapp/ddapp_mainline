package com.duco.ducoplayer.darkodemo.ps;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.duco.ducoplayer.darkodemo.R;
import com.duco.ducoplayer.darkodemo.ScreenSaverActivity;
import com.duco.ducoplayer.utils.FileUtil;
import com.duco.ducoplayer.utils.GlobalParameters;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PSPage01Activity extends Activity {

	private String homePath;
	private ImageView imageView;
	
	private ImageView startBtn;

	private Timestamp ttmst = null;
	private ScheduledExecutorService scheduleTaskExecutor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_pspage01);
		
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	 	
	    init();
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pspage01, menu);
		return true;
	}
	
	private void init()  {
		
		/*
		String path = this.homePath + "/pages";
		String file = path + "/page01.jpg";
		RelativeLayout layout = (RelativeLayout) findViewById(R.layout.activity_pspage01);
		
		Drawable drawable;
		
		//check if file really exists
		if(!FileUtil.exists(file))
			return;
		
		drawable = BitmapDrawable.createFromPath(file);
		//check if visible
		if(!drawable.isVisible())
			return;
				
		imageView = new ImageView(this);
		Bitmap handImage = BitmapFactory.decodeFile(path);
		imageView.setImageBitmap(handImage);
		imageView.setId(123);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	 	imageView.setLayoutParams(params);
	    //add button to the layout
	    layout.addView(imageView); 
	    */
		
		String path = this.homePath + "/pages";
		String file = path + "/page01.jpg";
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.psPage01);
		Drawable drawable;
				
		//check if file really exists
		if(!FileUtil.exists(file))
			return;
				

		drawable = BitmapDrawable.createFromPath(file);
		//check if visible
		if(!drawable.isVisible())
			return;

		//layout.setBackground(drawable);
		layout.setBackgroundDrawable(drawable);
		
		startBtn = (ImageView)findViewById(R.id.startButton);
	    startBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent anotherActivityIntent = new Intent(PSPage01Activity.this, PSGenericPage.class);
            	anotherActivityIntent
            	.putExtra("com.duco.ducoplayer.darkodemo.homepath", 
            			PSPage01Activity.this.homePath + "/pages");
            	anotherActivityIntent
            	.putExtra("com.duco.ducoplayer.darkodemo.next", 
            			new Integer(2));
            	anotherActivityIntent
            	.putExtra("com.duco.ducoplayer.darkodemo.previous", 
            			new Integer(1));
            	startActivity(anotherActivityIntent);
			}
		});
	}

	
	private void screensaver()  {
		Timestamp tmst = new Timestamp(new Date().getTime());
		long diff = tmst.getTime() - this.ttmst.getTime();
		if((diff / (60*1000)) >= 1 )  {
			this.scheduleTaskExecutor.shutdown();

			
			Intent anotherActivityIntent = new Intent(PSPage01Activity.this, ScreenSaverActivity.class);
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
