package com.schonherz.flottadroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.SoforDao.Properties;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
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
	private AutoKepDao autoKepDao;
	private MunkaDao munkaDao;
	private MunkaEszkozDao munkaEszkozDao;
	private MunkaKepDao munkaKepDao;
	private MunkaTipusDao munkaTipusDao;
	private PartnerDao partnerDao;
	private PartnerKepDao partnerKepDao;
	private ProfilKepDao profilKepDao;
	private SoforDao soforDao;
	private TelephelyDao telephelyDao;

	Button jobsButton;
	Button adminButton;
	Button mapButton;
	Button carButton;
	Button contactButton;
	Context context;

	SessionManager sessionManager;
	boolean isRefreshed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		sessionManager = new SessionManager(context);
		isRefreshed = this.getIntent().getBooleanExtra("isRefreshed", false);

		if (sessionManager.isLoggedIn() && !isRefreshed) {
			// csinálunk egy frissítést mert a bejelentkezéskor elmaradt
			loggedInRefresh();
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
				Intent intent = new Intent(MainActivity.this, CarActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
		switch (item.getItemId()) {
			case R.id.menu_logout :
				sessionManager.logoutUser();
				break;
				
			case R.id.menu_refresh:
				Intent intent = new Intent(MainActivity.this,
						RefreshActivity.class);
				MainActivity.this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// megnézzük, hogy be van e még jelentkezve a user, ha nincs akkor irány
		// a bejelentkezés
		if (!sessionManager.isLoggedIn()) {
			sessionManager.logoutUser();
		}
		super.onResume();
	}

	public void loggedInRefresh() {

		// Get a Session and init sofor Table
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		soforDao = daoSession.getSoforDao();

		// If internet connection OK, drop sofor Table and get new table
		if (NetworkUtil.checkInternetIsActive(context) == true) {

			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub

					boolean check = checkLogin();

					if (result && check) {
						Toast.makeText(MainActivity.this, R.string.refreshed,
								Toast.LENGTH_SHORT).show();
					} else if ((result == false) && check) {
						Toast.makeText(MainActivity.this,
								R.string.errorRefresh, Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(MainActivity.this, R.string.invalidUser,
								Toast.LENGTH_SHORT).show();
						sessionManager.logoutUser();
					}
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					return saveSoforTable();
				}

			}.execute();
		}

	}

	public boolean saveSoforTable() {
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/querySoforTable.php";

		json = new JSONObject();

		try {

			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());

			// Eldobjuk a tablat es ujra letrehozzuk
			soforDao.dropTable(soforDao.getDatabase(), true);
			soforDao.createTable(soforDao.getDatabase(), true);

			ArrayList<Sofor> soforok = JsonArrayToArrayList
					.JsonArrayToSofor(jsonArray);

			for (int i = 0; i < soforok.size(); i++) {
				soforDao.insert(soforok.get(i));
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
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
}
