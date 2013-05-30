package com.schonherz.flottadroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.PushMessage;
import com.schonherz.dbentities.PushMessageDao;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.sax.StartElementListener;
import android.util.Log;

public class C2DMMessageReceiver extends BroadcastReceiver{
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	private PushMessageDao pushMessageDao;
	private PushMessage currentPushMessage;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		dataBaseInit(context);
		
		//aktuális message beállítása
		currentPushMessage=new PushMessage();
		currentPushMessage.setPushMessageText(intent.getStringExtra("message"));
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String s = sdf.format(now);
		currentPushMessage.setPushMessageDate(s);		
		pushMessageDao.insert(currentPushMessage);
		
		Log.i("push log", pushMessageDao.loadAll().get(0).getPushMessageText());
		
		 String action = intent.getAction();
		    Log.w("C2DM", "Message Receiver called");
		    if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
		      Log.w("C2DM", "Received message");
		      final String payload = intent.getStringExtra("message");
		      Log.d("C2DM", "dmControl: payload = " + payload);
		   // Send this to my application server
		      /*
		      Intent i = new Intent();
		      i.putExtra("message",intent.getStringExtra("message"));
		      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		      i.setClass(context, AlertActivity.class);
		      context.startActivity(i);*/
		      
		     
		      // Prepare intent which is triggered if the
		      // notification is selected
		      Intent notificationIntent = new Intent(context, PushNotificationListActivity.class);
		      PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

		      // Build notification		      
		      NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		      
		      
		      Notification.Builder noti = new Notification.Builder(context)
		          .setContentTitle("Üzenet via FlottaDroid")
		          .setContentText(intent.getStringExtra("message")).setSmallIcon(R.drawable.ic_message)
		          .setContentIntent(pIntent);
		      noti.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
		      

		      notificationManager.notify(0, noti.getNotification());
		    }
	}
	
	public void dataBaseInit(Context context) {
		helper = new DaoMaster.DevOpenHelper(context, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		pushMessageDao=daoSession.getPushMessageDao();

	}

}
