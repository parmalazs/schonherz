package com.schonherz.flottadroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;

public class SplashActivity extends Activity {

	// Handle first start from preference
	SharedPreferences preferences;
	String lastSyncTime;

	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	String soforUrl = "http://www.flotta.host-ed.me/querySoforTable.php";
	String autoUrl = "http://www.flotta.host-ed.me/queryAutoTable.php";
	String partnerUrl = "http://www.flotta.host-ed.me/queryPartnerTable.php";
	String telephelyUrl = "http://www.flotta.host-ed.me/queryTelephelyTable.php";
	String munkaUrl = "http://www.flotta.host-ed.me/queryMunkaTable.php";
	String munkaTipusUrl = "http://www.flotta.host-ed.me/queryMunkaTipusTable.php";
	String partnerKepUrl = "http://www.flotta.host-ed.me/queryPartnerKepekTable.php";
	String munkaKepUrl = "http://www.flotta.host-ed.me/queryMunkaKepTable.php";
	String munkaEszkozUrl = "http://www.flotta.host-ed.me/queryMunkaEszkozTable.php";
	String profilKepUrl = "http://www.flotta.host-ed.me/queryProfilkepTable.php";
	String autokepUrl = "http://www.flotta.host-ed.me/queryAutoKepTable.php";

	// Greendao objects
	private AutoDao autoDao;
	private MunkaDao munkaDao;
	private MunkaEszkozDao munkaEszkozDao;
	private MunkaTipusDao munkaTipusDao;
	private PartnerDao partnerDao;
	private SoforDao soforDao;
	private TelephelyDao telephelyDao;

	private static String TAG = SplashActivity.class.getName();
	private static long SLEEP_TIME = 5; // Sleep for some time
	private SessionManager sessionManager;
	ImageView iv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sessionManager = new SessionManager(getApplicationContext());

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
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getBackground();
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
				if (NetworkUtil.checkInternetIsActive(SplashActivity.this)) {
					initTables();
					saveAlldata();
				} else {
					Thread.sleep(SLEEP_TIME * 500);
				}

			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			if (!sessionManager.isLoggedIn()) {
				// Start login activity
				Intent intent = new Intent(SplashActivity.this,
						LoginActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();

			} else {
				// Start main activity
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
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

	private void initTables() {

		// Get a Session and init sofor Table
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = daoSession.getAutoDao();
		munkaDao = daoSession.getMunkaDao();
		munkaTipusDao = daoSession.getMunkaTipusDao();
		partnerDao = daoSession.getPartnerDao();
		soforDao = daoSession.getSoforDao();
		telephelyDao = daoSession.getTelephelyDao();

		AutoDao.createTable(db, true);
		AutoKepDao.createTable(db, true);
		MunkaDao.createTable(db, true);
		MunkaEszkozDao.createTable(db, true);
		MunkaKepDao.createTable(db, true);
		MunkaTipusDao.createTable(db, true);
		PartnerDao.createTable(db, true);
		PartnerKepDao.createTable(db, true);
		ProfilKepDao.createTable(db, true);
		SoforDao.createTable(db, true);
		TelephelyDao.createTable(db, true);

	}

	public String saveAlldata() {
		JSONArray jsonArray;
		JSONObject json = new JSONObject();

		try {
			// get sofortable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(soforUrl,
					json.toString());
			SoforDao.dropTable(soforDao.getDatabase(), true);
			SoforDao.createTable(soforDao.getDatabase(), true);

			ArrayList<Sofor> soforok = JsonArrayToArrayList
					.JsonArrayToSofor(jsonArray);

			for (int i = 0; i < soforok.size(); i++) {
				soforDao.insert(soforok.get(i));
			}

			soforok = null;

			// get autotable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(autoUrl,
					json.toString());

			AutoDao.dropTable(autoDao.getDatabase(), true);
			AutoDao.createTable(autoDao.getDatabase(), true);

			ArrayList<Auto> autok = JsonArrayToArrayList
					.JsonArrayToAuto(jsonArray);

			for (int i = 0; i < autok.size(); i++) {
				autoDao.insert(autok.get(i));
			}

			autok = null;

			// get partnertable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					partnerUrl, json.toString());

			PartnerDao.dropTable(partnerDao.getDatabase(), true);
			PartnerDao.createTable(partnerDao.getDatabase(), true);

			ArrayList<Partner> partnerek = JsonArrayToArrayList
					.JsonArrayToPartner(jsonArray);

			for (int i = 0; i < partnerek.size(); i++) {
				partnerDao.insert(partnerek.get(i));
			}

			partnerek = null;

			// get telephelytable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					telephelyUrl, json.toString());

			TelephelyDao.dropTable(telephelyDao.getDatabase(), true);
			TelephelyDao.createTable(telephelyDao.getDatabase(), true);

			ArrayList<Telephely> telephelyek = JsonArrayToArrayList
					.JsonArrayToTelephely(jsonArray);

			for (int i = 0; i < telephelyek.size(); i++) {
				telephelyDao.insert(telephelyek.get(i));
			}
			telephelyek = null;

			// get munkatable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(munkaUrl,
					json.toString());

			MunkaDao.dropTable(munkaDao.getDatabase(), true);
			MunkaDao.createTable(munkaDao.getDatabase(), true);

			ArrayList<Munka> munkak = JsonArrayToArrayList
					.JsonArrayToMunka(jsonArray);

			for (int i = 0; i < munkak.size(); i++) {
				munkaDao.insert(munkak.get(i));
			}
			munkak = null;

			// get munkatipustable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					munkaTipusUrl, json.toString());

			MunkaTipusDao.dropTable(munkaTipusDao.getDatabase(), true);
			MunkaTipusDao.createTable(munkaTipusDao.getDatabase(), true);

			ArrayList<MunkaTipus> munkatipusok = JsonArrayToArrayList
					.JsonArrayToMunkaTipusok(jsonArray);

			for (int i = 0; i < munkatipusok.size(); i++) {
				munkaTipusDao.insert(munkatipusok.get(i));
			}
			munkatipusok = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
		return "";
	}

}