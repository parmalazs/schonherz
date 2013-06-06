package com.schonherz.flottadroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.SoforDao.Properties;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	// Greendao objects
	private AutoDao autoDao;
	private MunkaDao munkaDao;
	private SoforDao soforDao;
	
		
	private Button jobsButton;
	private Button adminButton;
	private Button mapButton;
	private Button carButton;
	private Button contactButton;
	private Context context;

	private SessionManager sessionManager;
	private Long selectedAutoID;
	private List<Auto> autok;
	private List<Munka> munkak;
	private NotificationManager notificationManager;
	private boolean isAdmin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		sessionManager = new SessionManager(context);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		dataBaseInit();
		autok=new ArrayList<Auto>();
		munkak=new ArrayList<Munka>();
		sajatAutoCheck();
		isAdmin=false;
		
		//Log.i("push log", pushMessageDao.loadAll().get(0).getPushMessageText());

		if (sessionManager.isLoggedIn()) {
			// felhasználó ellenõrzése
			loggedIn();			
		}	
		

		if (NetworkUtil.checkInternetIsActive(this) == true) {
			try {
				GCMIntentService.unregister(getApplicationContext());
				GCMIntentService.register(getApplicationContext());
			} catch (Exception e) {
				Log.e(RegisterActivity.class.getName(),
						"Exception received when attempting to register for Google Cloud "
								+ "Messaging. Perhaps you need to set your virtual device's "
								+ " target to Google APIs? "
								+ "See https://developers.google.com/eclipse/docs/cloud_endpoints_android"
								+ " for more information.", e);
			}
		}
		
		//ha nincs a munkáihoz autó felvéve akkor értesítjük h vegyen fel egyet
		if (!vanMunkahozAutoja()) {
			// Prepare intent which is triggered if the
			// notification is selected

			Intent intent = new Intent(this, CarActivity.class);
			PendingIntent munkahozAutoPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Build notification
			// Actions are just fake
			Notification.Builder noti = new Notification.Builder(this)
			        .setContentTitle(getString(R.string.nocarforjob))
			        .setContentText(getString(R.string.pickcar))
			        .setSmallIcon(R.drawable.autoikon)
			        .setContentIntent(munkahozAutoPendingIntent); 
			  				
			notificationManager.notify(1, noti.getNotification()); 
		}

		Log.i("proba", "pusholashoz");

		jobsButton = (Button) findViewById(R.id.buttonJobs);
		jobsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						JobsActivity.class);				
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		adminButton = (Button) findViewById(R.id.buttonAdmin);
		//ha nem admin a sofõr akkor letiltjuk az admin gombot
		soforIsAdmin();
		Log.i(MainActivity.class.getName(), "admin: " + isAdmin);
				
		adminButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				Intent intent = new Intent(MainActivity.this,
						AdminActivity.class);				
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		mapButton = (Button) findViewById(R.id.buttonMap);
		mapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, MapActivity.class);				
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

		contactButton = (Button) findViewById(R.id.buttonContacts);
		contactButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ContactActivity.class);			
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});		
		
		carButton = (Button) findViewById(R.id.buttonCar);
		carButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ha már van a sofõrnek autója akkor azt jelenítjük meg
		        if (selectedAutoID!=0L) {
		        	Intent intent=new Intent(MainActivity.this, CarDetailsActivity.class);
		        	intent.putExtra("sajatAuto", true);
		        	intent.putExtra("selectedAutoID", selectedAutoID);		        	
		        	MainActivity.this.startActivity(intent);
		        	overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		        }
		        //ha nincs autója, akkor irány a szabad autók
		        else {
					Intent intent = new Intent(MainActivity.this, CarActivity.class);					
					MainActivity.this.startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		        }
			}
		});

	}
	
	public boolean vanMunkahozAutoja() {
		munkak.clear();
		munkak=munkaDao.queryBuilder().where(
				com.schonherz.dbentities.MunkaDao.Properties.SoforID.eq(
						sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list();
		Log.i(MainActivity.class.getName(), "munkái száma: " + munkak.size() + " választott auto id: " + selectedAutoID);
		if (munkak.size()==1 && selectedAutoID!=0L) {
			//ha az utolsó munkájához érkezett figyelmeztetjük, h adja le az autóját ha van
			utolsoMunka();
			return true;
		}
		if (munkak.size()>1 && selectedAutoID==0L) {
			return false;
		}
		return true;		
	}
	
	public void utolsoMunka() {
		Intent utolsoMunkaIntent = new Intent(MainActivity.this, CarDetailsActivity.class);
		utolsoMunkaIntent.putExtra("sajatAuto", true);
		utolsoMunkaIntent.putExtra("selectedAutoID", selectedAutoID);
		PendingIntent utolsoMunkaPendingIntent = PendingIntent.getActivity(this, 0, utolsoMunkaIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		

		// Build notification
		// Actions are just fake
		Notification.Builder noti = new Notification.Builder(this)
		        .setContentTitle("Egy aktív munka")
		        .setContentText("Ne felejtse el leadni az autót!")
		        .setSmallIcon(R.drawable.autoikon)
		        .setContentIntent(utolsoMunkaPendingIntent); 
		  				
		notificationManager.notify(2, noti.getNotification()); 		
	}
	
	public void sajatAutoCheck() {
		selectedAutoID=0L;
		autok.clear();
		autok=autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list();
		for (int i=0; i<autok.size(); i++) {
			if (autok.get(i).getAutoLastSoforID()==sessionManager.getUserID().get(SessionManager.KEY_USER_ID) && autok.get(i).getAutoFoglalt()) {
				selectedAutoID=autok.get(i).getAutoID();
			}
		}
		Log.i(MainActivity.class.getName(), "saját auto ID: " + selectedAutoID.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.menu_logout :
				notificationManager.cancel(1);
				notificationManager.cancel(2);
				notificationManager.cancel(3);
				sessionManager.logoutUser();
				break;
			case R.id.menu_settings:
				Intent setIntent = new Intent(this,SettingsActivity.class);
				this.startActivity(setIntent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
			case R.id.menu_refresh:
				Intent intent = new Intent(MainActivity.this,
						RefreshActivity.class);
				
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
			case R.id.menu_messages:
				Intent messInt = new Intent(MainActivity.this,PushNotificationListActivity.class);
				
				this.startActivity(messInt);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//itt is ellenõrizzük, h vett e fel magának autót, munkát
		dataBaseInit();
		soforIsAdmin();
		sajatAutoCheck();
		if (!vanMunkahozAutoja()) {
			// Prepare intent which is triggered if the
			// notification is selected

			Intent intent = new Intent(this, CarActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

			// Build notification
			// Actions are just fake
			Notification.Builder noti = new Notification.Builder(this)
			        .setContentTitle(getString(R.string.activejobs))
			        .setContentText(getString(R.string.pickCarJob))
			        .setSmallIcon(R.drawable.autoikon)
			        .setContentIntent(pIntent);		

			notificationManager.notify(1, noti.getNotification()); 
		}
		if (!sessionManager.isLoggedIn()) {
			sessionManager.logoutUser();
		}
		super.onResume();
	}

	public void loggedIn() {		
		
		if (!checkLogin()) {
			Toast.makeText(MainActivity.this, R.string.invalidUser,
					Toast.LENGTH_SHORT).show();
			sessionManager.logoutUser();
		}

	}	

	public boolean checkLogin() {

		// Where-ben 2 feltetellel lekerdezes, a Properties az a
		// SoforDao properties osztalya importalva
		// Minden tablanak van minden rekordjara egy property, amihez
		// lehet hasonlita
		List<Sofor> soforok = soforDao
				.queryBuilder()
				.where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list();

		if (soforok.size() > 0) {
			return true;
		}

		return false;

	}
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = daoSession.getAutoDao();;
		munkaDao = daoSession.getMunkaDao();
		soforDao = daoSession.getSoforDao();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();		
		finish();		
	}
	
	public void soforIsAdmin() {
		isAdmin=soforDao.queryBuilder().where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list().get(0).getSoforIsAdmin();
		
		if (!isAdmin) {
			adminButton.setEnabled(false);
			adminButton.setVisibility(View.INVISIBLE);
		}
		else {
			adminButton.setEnabled(true);
			adminButton.setVisibility(View.VISIBLE);
		}
		
	}
}
