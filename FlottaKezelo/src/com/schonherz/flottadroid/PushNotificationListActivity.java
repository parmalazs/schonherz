package com.schonherz.flottadroid;

import java.util.ArrayList;
import java.util.List;

import com.schonherz.adapters.PushMessageAdapter;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.PushMessage;
import com.schonherz.dbentities.PushMessageDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class PushNotificationListActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	ListView list;
	PushMessageAdapter adapter;
	List<PushMessage> pushMessages;
	PushMessageDao pushMessageDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_notification_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);	
		
		notificationManager.cancel(0);
		
		dataBaseInit();
		
		list=(ListView)findViewById(R.id.listNotifications);
		
		pushMessages=new ArrayList<PushMessage>(pushMessageDao.loadAll());
		
		adapter=new PushMessageAdapter(this, R.layout.list_item_message, pushMessages, pushMessageDao);
		
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getApplication(), AlertActivity.class);
				intent.putExtra("message", pushMessages.get(position+1).getPushMessageText());
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.push_notification_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			this.overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right,
				R.anim.slide_in_left);
	}
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		pushMessageDao = daoSession.getPushMessageDao();
	}

}
