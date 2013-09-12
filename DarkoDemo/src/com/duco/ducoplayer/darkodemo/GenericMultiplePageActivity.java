package com.duco.ducoplayer.darkodemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import com.duco.ducoplayer.utils.FileUtil;
import com.duco.ducoplayer.darkodemo.gestures.OnSwipeTouchListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
//import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GenericMultiplePageActivity extends Activity {

	
	private int index = 0;
	private String homePath;
	private ArrayList<String> pages;
	private ImageView imageView;
	private float height;
	private float width;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_multiple_page);
		
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	 	
	    init();
		
	}

	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generic_multiple_page, menu);
		return true;
	}
	*/
	
	private void init()  {
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		if(FileUtil.exists(this.homePath))  {
			Vector<String> tmp = FileUtil.dirList(this.homePath + "/pages/", FileUtil.FILES_ONLY);
			this.pages = new ArrayList<String>(tmp);
			Collections.sort(this.pages);
		}
		if(this.pages.size() > 0)  {
			if(index < this.pages.size() && index == 0)  {
				initialisePicture(this.homePath + "/pages/" + this.pages.get(0));
				//index++;
			}
		}
	}

	private void initialisePicture(String path)  {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.genericMultipleLayout);
		Drawable drawable;
				
		//check if file really exists
		if(!FileUtil.exists(path))
			return;
				
		drawable = BitmapDrawable.createFromPath(path);
		//check if visible
		if(!drawable.isVisible())
			return;

		//set the properties for button
	 	imageView = new ImageView(this);
	 	imageView.setBackgroundDrawable(drawable);
	 	imageView.setId(123);
	 	LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	 	
	 	imageView.setLayoutParams(params);

	    layout.addView(imageView);  
	    
	    initialiseGestures();
	}
	
	private void changeImage(String path)  {

		Drawable drawable;
				
		//check if file really exists
		if(!FileUtil.exists(path))
			return;
				
		drawable = BitmapDrawable.createFromPath(path);
		//check if visible
		if(!drawable.isVisible())
			return;

	 	imageView.setBackgroundDrawable(drawable);
  
	}
	

	private void initialiseGestures()  {
		 //mDetector = new GestureDetectorCompat(this, new MyGestureListener());
		imageView.setOnTouchListener(new OnSwipeTouchListener(){
	        public boolean onSwipeTop() {
	            return true;
	        }
	        public boolean onSwipeRight() {
	        	if(index < (GenericMultiplePageActivity.this.pages.size() - 1))  {
	        		index++;
	        	}
	        	String file = GenericMultiplePageActivity.this.pages.get(index);
	        	changeImage(GenericMultiplePageActivity.this.homePath + "/pages/" + file);
	            return true;
	        }
	        public boolean onSwipeLeft() {
	        	if(index > 0)  {
	        		index--;
	        	}
	        	String file = GenericMultiplePageActivity.this.pages.get(index);
	        	changeImage(GenericMultiplePageActivity.this.homePath + "/pages/" + file);
	            return true;
	        }
	        public boolean onSwipeBottom() {
	            return true;
	        }
	        
	        @Override
            public boolean onTouch(View v, MotionEvent event) {
	        	switch (event.getAction()) {
	        		case MotionEvent.ACTION_DOWN:       
	        		{       
	        	    	
	        	    	float x = event.getX();
	        	    	float y = event.getY();
	        	    	
	        	    	if(GenericMultiplePageActivity.this.homePath.contains("scanner") && GenericMultiplePageActivity.this.pages.get(index).contains("page01"))  {
	        	    		if((y / height > 0.55f) && (x / width < 0.25f))  {
	        	    			onSwipeRight();
	        	    		}
	        	    	}
	        	    	
	            		break;
	        		}
	        		default:  {
	        			break;
	        		}
	        	} 
	        	return super.onTouch(v, event);
	        }
	    }
				);
		
	    imageView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View arg0) {
	            
	        }
	    });
		
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
}
