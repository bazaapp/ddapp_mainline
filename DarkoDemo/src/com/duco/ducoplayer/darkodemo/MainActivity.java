package com.duco.ducoplayer.darkodemo;


import com.duco.ducoplayer.darkodemo.ps.PSLandingActivity;
import com.duco.ducoplayer.darkodemo.st.ShelfTalkerActivity;
import com.duco.ducoplayer.utils.FileUtil;
import com.duco.ducoplayer.utils.GlobalParameters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView sounds;
	private ImageView productselector;
	private ImageView scanner;
	private ImageView shelftalker;
	
	private ImageView ball01;
	private ImageView ball02;
	
	private boolean ball01Flag = true;
	private boolean ball02Flag = true;
			
	private int defaultX;
	private Handler _handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        _handler = new Handler();
		
		// initialise the two ball
		ball01 = (ImageView)findViewById(R.id.ball01);
		ball02 = (ImageView)findViewById(R.id.ball02);
	
		ball01.setOnTouchListener(new MyTouchListener());
		ball02.setOnTouchListener(new MyTouchListener());
		
		View container = findViewById(R.id.container);
		container.setOnDragListener(new MyDragListener());
		
		sounds = (ImageView)findViewById(R.id.button01);
		productselector = (ImageView)findViewById(R.id.button02);
		scanner = (ImageView)findViewById(R.id.button03);
		shelftalker = (ImageView)findViewById(R.id.button04);
		
		defaultX = (int)sounds.getX();
		
		//hideFragment();
		showFragment();
		
		sounds.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(FileUtil.dirList(GlobalParameters.SOUNDS_DIR + "/pages", FileUtil.FILES_ONLY).size() > 1)  {
					Intent anotherActivityIntent = new Intent(MainActivity.this, GenericMultiplePageActivity.class);
	            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", GlobalParameters.SOUNDS_DIR);
	            	startActivity(anotherActivityIntent);
				}
				else if(FileUtil.dirList(GlobalParameters.SOUNDS_DIR + "/pages", FileUtil.FILES_ONLY).size() == 1)  {
					Intent anotherActivityIntent = new Intent(MainActivity.this, GenericPageActivity.class);
	            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", GlobalParameters.SOUNDS_DIR);
	            	startActivity(anotherActivityIntent);
				}
			}
		});
		
		shelftalker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent anotherActivityIntent = new Intent(MainActivity.this, ShelfTalkerActivity.class);
            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath",  GlobalParameters.SHELFTALKER_DIR);
            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.videopath", GlobalParameters.SHELFTALKER_DIR + "/misc/video01.mp4");
            	startActivity(anotherActivityIntent);
			}
		});

		
		productselector.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent anotherActivityIntent = new Intent(MainActivity.this, PSLandingActivity.class);
            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", GlobalParameters.PRODUCT_DIR);
            	startActivity(anotherActivityIntent);
			}
		});
		
		scanner.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
            	Intent anotherActivityIntent = new Intent(MainActivity.this, ScannerActivity.class);
            	anotherActivityIntent.putExtra("com.duco.ducoplayer.darkodemo.homepath", GlobalParameters.SCANNER_DIR);
            	startActivity(anotherActivityIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private final class MyTouchListener implements OnTouchListener {
		 public boolean onTouch(View view, MotionEvent motionEvent) {
			 if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				 ClipData data = ClipData.newPlainText("", "");
				 DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				 view.startDrag(data, shadowBuilder, view, 0);
				 return true;
		     } 
			 else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
				 view.setVisibility(View.INVISIBLE);
				 return false;
			 }
			 else {
				 return false;
		     }
		 }
		  
	 }

	
	 class MyDragListener implements OnDragListener {
		 
		    @Override
		    public boolean onDrag(View v, DragEvent event) {
		      int action = event.getAction();
		      switch (event.getAction()) {
		      case DragEvent.ACTION_DRAG_STARTED:
		        // Do nothing
		        break;
		      case DragEvent.ACTION_DRAG_ENTERED:  {
		        //v.setBackgroundDrawable(enterShape);
		    	  View view = (View) event.getLocalState();
		    	  if(view.getId() == R.id.ball01)  {
			    	  if(ball01Flag)  {
				        	sounds.setVisibility(View.VISIBLE);
				        	productselector.setVisibility(View.VISIBLE);
				        	scanner.setVisibility(View.VISIBLE);
				        	shelftalker.setVisibility(View.VISIBLE);
				        	new Thread(new AnimateButton()).start();
				        	
				        	ball01Flag = false;
				      }
		    	  }
		    	  else if(view.getId() == R.id.ball02)  {
		    		  if(ball02Flag)  {
		    			  
		    			  ball02Flag = false;
		    		  }
		    		  sounds.setVisibility(View.INVISIBLE);
		    		  productselector.setVisibility(View.INVISIBLE);
		    		  scanner.setVisibility(View.INVISIBLE);
		    		  shelftalker.setVisibility(View.INVISIBLE);
		    	  }
		    	  //hideFragment();
		    	  break;
		      }
		      case DragEvent.ACTION_DRAG_EXITED:  {
		        //v.setBackgroundDrawable(normalShape);
		    	  
		        break;
		      }
		      case DragEvent.ACTION_DROP:  {
		        // Dropped, reassign View to ViewGroup
		        View view = (View) event.getLocalState();
		        ViewGroup owner = (ViewGroup) view.getParent();
		        owner.removeView(view);
		        
		        AbsoluteLayout container = (AbsoluteLayout) v;
		        container.addView(view);
		        
		        //view.setX(event.getX() - view.getWidth() / 2);
		        //view.setY(event.getY() - view.getHeight() /2);
		        
		        view.setVisibility(View.VISIBLE);
		        
		        if(view.getId() == R.id.ball01)  {
			        if(!ball01Flag)  {
			        	  //button01.setX(400);
			        	  //button02.setX(400);
			        	  //button03.setX(400);
			        	  //button04.setX(400);
			        	  //button01.setVisibility(View.INVISIBLE);
			    		  //button02.setVisibility(View.INVISIBLE);
			    		  //button03.setVisibility(View.INVISIBLE);
			    		  //button04.setVisibility(View.INVISIBLE);
			    		  ball01Flag = true;
			    	}
		        }
		        else if(view.getId() == R.id.ball02)  {
			        if(!ball02Flag)  {
			    		  ball02Flag = true;
			    	}
		        }
		       
		        //showFragment();
		        break;
		      }
		      case DragEvent.ACTION_DRAG_ENDED:
		        //v.setBackgroundDrawable(normalShape);
		        break;
		      default:
		        break;
		      }
		      return true;
		    }		   
	 }
	 
	 
	 
	 
	 private void animateButtons(ImageView button)  {
		 
		 //Display display = getWindowManager().getDefaultDisplay();

		 /*
		 PropertyValuesHolder pvhX = 
				 PropertyValuesHolder.ofFloat("x", button.getX(), 
						 button.getWidth());
		 
		 PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", button.getY());
	   
	   
		 ObjectAnimator handAnimator = ObjectAnimator.ofPropertyValuesHolder(button, pvhX, pvhY);
	   
		 handAnimator.setDuration(500);
		 handAnimator.setRepeatCount(0);
		 handAnimator.start();
		 */
		 Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	     button.startAnimation(shake);

	 }
	 
	 
	 private class AnimateButton implements Runnable  {

		@Override
		public void run() {
			_handler.post(new Runnable() {	
				@Override
				public void run() {
					new AnimateAsyncTask().doInBackground(sounds);
					new AnimateAsyncTask().doInBackground(productselector);
					new AnimateAsyncTask().doInBackground(scanner);
					new AnimateAsyncTask().doInBackground(shelftalker);
				}
			});			
		}
		 
	 }
	 
	 
	 private class AnimateAsyncTask extends AsyncTask<ImageView, Void, Void> {
	     
		 protected Void doInBackground(ImageView... button) {
			animateButtons(button[0]);
			return null;
			 
	     }

	     protected void onProgressUpdate() {
	     }

	     protected void onPostExecute() {
	     }
	 }
	 

		public void showFragment() {
			
			Fragment newContentFragment = (Fragment) getFragmentManager()
					.findFragmentById(R.id.contentfragment);

			FragmentTransaction fragmentTransaction = fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.show(newContentFragment);
			fragmentTransaction.commit();
			

		}

		public void hideFragment() {
			
			Fragment newContentFragment = (Fragment) getFragmentManager()
					.findFragmentById(R.id.contentfragment);

			FragmentTransaction fragmentTransaction = fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.hide(newContentFragment);
			fragmentTransaction.commit();
			

		}


}
