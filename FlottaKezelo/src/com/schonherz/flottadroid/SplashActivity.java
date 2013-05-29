package com.schonherz.flottadroid;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.schonherz.classes.SessionManager;


public class SplashActivity extends Activity {

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 5; // Sleep for some time
	private SessionManager sessionManager;
	ImageView iv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sessionManager=new SessionManager(getApplicationContext());

		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // Removes
																// notification
																// bar

		setContentView(R.layout.activity_splash);
	
		
		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();

	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		
		ImageView img = (ImageView) findViewById(R.id.imageView3);
	    img.setBackgroundResource(R.anim.load_sequence);
	    AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
	    frameAnimation.start();
		
		super.onWindowFocusChanged(hasFocus);
	}
	private class IntentLauncher extends Thread {
		@Override
		/**
		 * Sleep for some time and than start new activity.
		 */
		public void run() {
			try {
				// Sleeping
				Thread.sleep(SLEEP_TIME * 500);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
			if(!sessionManager.isLoggedIn()) {
				// Start login activity
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				SplashActivity.this.startActivity(intent);			
				SplashActivity.this.finish();
				
			}
			else {
				// Start main activity
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				SplashActivity.this.startActivity(intent);			
				SplashActivity.this.finish();
			}
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_splash, menu);
		return true;
	}
} 