package com.schonherz.flottadroid;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;

public class C2DMMessageReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 String action = intent.getAction();
		    Log.w("C2DM", "Message Receiver called");
		    if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
		      Log.w("C2DM", "Received message");
		      final String payload = intent.getStringExtra("message");
		      Log.d("C2DM", "dmControl: payload = " + payload);
		      // Send this to my application server
		      
		     Intent i = new Intent();
		     i.putExtra("message",intent.getStringExtra("message"));
		     i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     i.setClass(context, AlertActivity.class);
		     context.startActivity(i);
		    }
	}

}
