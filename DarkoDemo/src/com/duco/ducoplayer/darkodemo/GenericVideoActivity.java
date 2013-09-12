package com.duco.ducoplayer.darkodemo;


import com.duco.ducoplayer.utils.FileUtil;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.widget.VideoView;

public class GenericVideoActivity extends Activity {
	private VideoView mVideo;
	private String videoPath;
	private String homePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_video);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generic_video, menu);
		return true;
	}
		
	@Override
	public void onResume()  {
		super.onResume();
		initVideo();
	}

	private void initVideo()  {
		mVideo = (VideoView)findViewById(R.id.genericVideoView);
		
		Bundle recdData = getIntent().getExtras();
		this.homePath = 
		   	 (String)recdData.get("com.duco.ducoplayer.darkodemo.homepath");
		
		this.videoPath = 
				   	 (String)recdData.get("com.duco.ducoplayer.darkodemo.videopath");
				
		if(FileUtil.exists(videoPath))  {
			mVideo.setVideoPath(videoPath);
			//mVideo.setMediaController(new MediaController(this));
			mVideo.setMediaController(null);
			mVideo.requestFocus();
			mVideo.start();
			//mVideo.seekTo(500);
			
			
			
			
		}
		else  {
			showError("Can not find assets in SDCARD!!!");
		}
	}
	
	
	
    @Override 
    public boolean onTouchEvent(android.view.MotionEvent event) {
    	if(mVideo != null)  {
    		if(!mVideo.isPlaying())  {
    			mVideo.start();
    			
    		}
    	} 	
    	return super.onTouchEvent(event);
    }
	
	private void showError(String error)  {
		final AlertDialog alertDialog = new AlertDialog.Builder(GenericVideoActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setIcon(R.drawable.exclamationpoint);
        
        alertDialog.setMessage(error);
        	
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              return;
          } });
        alertDialog.show();
	}

}
