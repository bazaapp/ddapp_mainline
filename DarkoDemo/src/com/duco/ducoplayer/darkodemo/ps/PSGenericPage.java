package com.duco.ducoplayer.darkodemo.ps;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.Vector;

import com.duco.ducoplayer.darkodemo.MainActivity;
import com.duco.ducoplayer.darkodemo.R;
import com.duco.ducoplayer.darkodemo.ScannerActivity;
import com.duco.ducoplayer.darkodemo.ScreenSaverActivity;
import com.duco.ducoplayer.utils.FileUtil;
import com.duco.ducoplayer.utils.GlobalParameters;


import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class PSGenericPage extends Activity {

	private Timestamp ttmst = null;
	private ScheduledExecutorService scheduleTaskExecutor;
	
	private String homePath;
	private int previous;
	private int current;
	private int numberOfPages;
	private String page;
	
	private float height;
	private float width;
	
	private float btn01_x;
	private float btn01_y;

	
	private float btn02_x;
	private float btn02_y;
	
	private float btn03_x;
	private float btn03_y;
	
	private float btn04_x;
	private float btn04_y;
	
	private boolean _lock = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_psgeneric_page);
		
		init();
	}

	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.psgeneric_page, menu);
		return true;
	}
	 */

	@Override
	public void onResume()  {
		super.onResume();
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	    
	    this.current = (Integer)recdData.get("com.duco.ducoplayer.darkodemo.next");
	    this.previous = (Integer)recdData.get("com.duco.ducoplayer.darkodemo.previous");
	    
	    numberOfPages = FileUtil.dirList(this.homePath, FileUtil.FILES_ONLY).size();
	    
		this.scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

		 // This schedule a runnable task every 2 minutes
		 scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
		   public void run() {
			   screensaver();
		   }
		 }, 0, 1, TimeUnit.MINUTES);
	    
	}
	
	
	private void init()  {
		// get the screen size
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		//set the button size of the first button
		btn01_x = width / 3.0f;
		btn01_y = height / 8.0f;
		
		
		//set the button size of the second button
		btn02_x = width / 3.0f;
		btn02_y = height * (3.0f / 8.0f);
		
		// set the button size of the third button
		btn03_x = width * (2.0f / 3.0f);
		btn03_y = height / 8.0f;
		
		// set the button size of the fourth button
		btn04_x = width * (2.0f / 3.0f);
		btn04_y = height * (3.0f / 8.0f);
		
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	    
	    this.current = (Integer)recdData.get("com.duco.ducoplayer.darkodemo.next");
	    this.previous = (Integer)recdData.get("com.duco.ducoplayer.darkodemo.previous");
	    
	    
	    if(this.current < 10)
	    	this.page = this.homePath + "/page0" + current + ".jpg";
	    else
	    	this.page = this.homePath + "/page" + current + ".jpg";
	    
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.psGenericLayout);
		Drawable drawable;
				
		//check if file really exists
		if(!FileUtil.exists(this.page))
			return;
				

		drawable = BitmapDrawable.createFromPath(this.page);
		//check if visible
		if(!drawable.isVisible())
			return;
		
		layout.setBackgroundDrawable(drawable);	
		
		numberOfPages = FileUtil.dirList(this.homePath, FileUtil.FILES_ONLY).size();
		
	}
	
	
	@Override
	public void onBackPressed()  {
		this.current = this.previous;
		this.previous = this.previous - 1;
		Intent anotherActivityIntent = new Intent(PSGenericPage.this, PSGenericPage.class);
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.homepath", 
    			PSGenericPage.this.homePath + "/pages");
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.next", 
    			this.current);
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.previous", 
    			this.previous);	
    		
		super.onBackPressed();
	}
	
	
    @Override 
    public boolean onTouchEvent(android.view.MotionEvent event) {
    	
    	int x = (int)event.getX();
    	int y = (int)event.getY();
    	
    	switch (event.getAction()) {
    		case MotionEvent.ACTION_DOWN:       
    		{       
    			
    			//check if first button was pressed
    			if(current < numberOfPages)  {
	        		if((x <= btn01_x) && (Math.abs(y-btn01_y) <= (height * 0.15)))  {
	        			nextPage();
	        			//Toast.makeText(this, "button 1", Toast.LENGTH_SHORT).show();
	        		}
	        		//check if second button was pressed
	        		else if((x <= btn02_x) && (Math.abs(y-btn02_y) <= (height * 0.15)) && ((Math.abs(y-btn01_y) > (height * 0.22))))  {
	        			nextPage();
	        			//Toast.makeText(this, "button 2", Toast.LENGTH_SHORT).show();
	        		}    		
	        		//check if third button was pressed
	        		else if((x >= btn03_x) && (Math.abs(y-btn03_y) <= (height * 0.15)))  {
	        			nextPage();
	        			//Toast.makeText(this, "button 3", Toast.LENGTH_SHORT).show();
	        		}	
	        		//check if foourth button was pressed
	        		else if((x >= btn04_x) && (Math.abs(y-btn04_y) <= (height * 0.15)) && (Math.abs(y-btn03_y) > (height * 0.22)))  {
	        			nextPage();
	        			//Toast.makeText(this, "button 4", Toast.LENGTH_SHORT).show();
	        		}
	        		else if(current == (numberOfPages -2))  {
	        			nextPage();
	        		}
	        		else if(current == (numberOfPages - 1))  {
	        			if((x >= (width * 0.25) && x <= (width * 0.75)) && (y >= (height * 0.80)))  {
	        				String couponPath = this.homePath.replace("pages", "coupons");
	        				Vector <String>coupons = FileUtil.dirList(couponPath, FileUtil.FILES_ONLY);
	        				
	        				if(coupons != null && coupons.size() > 0)  {
	        					String coupon = couponPath + "/" + coupons.get(0);
	        				}
	        			}
	        			
	        		}
    			}
        		else{}
        		break;
    		}
    		default:  {
    			break;
    		}
    	}    	
    	return super.onTouchEvent(event);
    }
	
    private void nextPage()  {
    	int previous = this.current;
    	int next = ++this.current;
    	
		Intent anotherActivityIntent = new Intent(PSGenericPage.this, PSGenericPage.class);
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.homepath", 
    			PSGenericPage.this.homePath);
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.next", 
    			next);
    	anotherActivityIntent
    	.putExtra("com.duco.ducoplayer.darkodemo.previous", 
    			previous);	
    	startActivity(anotherActivityIntent);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
    	super.onCreateOptionsMenu(menu);
    	createMenu(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
    	return menuChoice(item);
    }
    
    private void createMenu(Menu menu)  {
    	
    	MenuItem homeMenu = menu.add(0, 0, 0, "Home");
    	{
    		homeMenu.setAlphabeticShortcut('h');
    		homeMenu.setIcon(R.drawable.homeshortcut);
    	}
    }
    
    private boolean menuChoice(MenuItem item)  {
    	switch(item.getItemId())  {
    		case 0:  {
    			Intent anotherActivityIntent = new Intent(this, MainActivity.class);
		    	startActivity(anotherActivityIntent);
    			return true;
    		}
    	}
		return false;
    }
    
	private void screensaver()  {
		Timestamp tmst = new Timestamp(new Date().getTime());
		long diff = tmst.getTime() - this.ttmst.getTime();
		if((diff / (60*1000)) >= 1 )  {
			this.scheduleTaskExecutor.shutdown();

			
			Intent anotherActivityIntent = new Intent(PSGenericPage.this, ScreenSaverActivity.class);
			//anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", Environment.getExternalStorageDirectory() + "/darko_demo_assets/screensaver/screensaver.mp4");
			anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", GlobalParameters.SDCARD + "/darko_demo_assets/screensaver/screensaver.mp4");
			
			startActivity(anotherActivityIntent);
		}
		
	}
    
}
