package com.duco.ducoplayer.darkodemo;

import java.util.Vector;

import com.duco.ducoplayer.utils.FileUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GenericPageActivity extends Activity {
	private String homePath;
	private ImageView homeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_page);
		
		Bundle recdData = getIntent().getExtras();
	    this.homePath = 
	    		(String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
	 	
	    
	    init();
	}

	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generic_page, menu);
		return true;
	}
	*/
	

	  
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

	
	private void back()  {
		super.onBackPressed();
	}
	
	private void init()  {
		String path = this.homePath + "/pages";
		Vector<String> files = FileUtil.dirList(path, FileUtil.FILES_ONLY);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.genericLayout);
		
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
				
				//layout.setBackground(drawable);
				layout.setBackgroundDrawable(drawable);
				break;
			}
		}
		
		homeButton = (ImageView)findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				back();
			}
		});
		
	}

}
