package com.schonherz.flottadroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button jobsButton;
	Button adminButton;
	Button mapButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Log.i("proba","pusholashoz");
        
        jobsButton=(Button) findViewById(R.id.buttonJobs);
        jobsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, JobsActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        adminButton = (Button)findViewById(R.id.buttonAdmin);
        adminButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,AdminActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        mapButton = (Button)findViewById(R.id.buttonMap);
        mapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, MapActivity.class);
				MainActivity.this.startActivity(intent); 
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
        
        
        
        
        
        
        
    }
}
