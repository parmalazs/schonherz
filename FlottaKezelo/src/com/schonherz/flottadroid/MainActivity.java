package com.schonherz.flottadroid;

import com.google.android.gms.internal.ca;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button jobsButton;
	Button adminButton;
	Button mapButton;
	Button refreshButton;
	Button carButton;
	Button contactButton;
	
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
        
        refreshButton = (Button)findViewById(R.id.buttonUpdate);
        refreshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,RefreshActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        
        contactButton = (Button)findViewById(R.id.buttonContacts);
        contactButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,ContactActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        carButton = (Button)findViewById(R.id.buttonCar);
        carButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,CarActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;               
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	return super.onOptionsItemSelected(item);
    }
}
