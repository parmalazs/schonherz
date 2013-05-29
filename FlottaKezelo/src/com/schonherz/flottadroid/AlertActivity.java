package com.schonherz.flottadroid;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.view.Menu;

public class AlertActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);
		NotificationManager notificationManager = 
				  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		String mess = this.getIntent().getStringExtra("message");
		
		showAlert(mess);
		notificationManager.cancel(0);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert, menu);
		return true;
	}

	
	public void showAlert(String message)
	{
		  new AlertDialog.Builder(AlertActivity.this)
	        .setMessage("Üzenet via FlottaDroid:\n\n" + message)
	        .setPositiveButton(android.R.string.ok,
	            new DialogInterface.OnClickListener() {
	              public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	                AlertActivity.this.finish();
	              }
	            }).show();
	}
	
}
