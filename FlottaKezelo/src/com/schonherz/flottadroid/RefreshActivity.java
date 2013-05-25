package com.schonherz.flottadroid;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.schonherz.classes.ImageDownloadUtil;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
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
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;

public class RefreshActivity extends Activity {

	// Handle first start from preference
	SharedPreferences preferences;
	String lastSyncTime;

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

	Button syncButton;
	Button imgUploadButton;
	ProgressDialog progress;

	File sdcard;

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

	TextView taskToUploadTv;
	TextView imgToUploadTv;
	TextView lastSyncTv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refresh);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		syncButton = (Button) findViewById(R.id.syncButton);
		imgUploadButton = (Button) findViewById(R.id.uploadImgBtn);
		taskToUploadTv = (TextView) findViewById(R.id.taskwaitforTW);
		lastSyncTv = (TextView) findViewById(R.id.lastsyncTimeTW);
		imgToUploadTv = (TextView) findViewById(R.id.imgwaitforTW);

		initTables();

		syncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AsyncTask<Void, Void, String>() {

					protected void onPostExecute(String result) {
						progress.dismiss();
					};

					protected void onPreExecute() {
						progress = ProgressDialog.show(RefreshActivity.this,
								"Frissítés", "Adatok frissítése...");
					};

					@Override
					protected String doInBackground(Void... params) {
						// TODO Auto-generated method stub
						return saveAlldata();
					}

				}.execute();

				new AsyncTask<Void, Void, String>() {

					protected void onPostExecute(String result) {
						progress.dismiss();
					};

					protected void onPreExecute() {
						progress = ProgressDialog.show(RefreshActivity.this,
								"Frissítés", "Képek letöltése...");
					};

					@Override
					protected String doInBackground(Void... params) {
						// TODO Auto-generated method stub
						return saveAllPictures();
					}

				}.execute();

			}
		});

		imgUploadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_refresh, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				NavUtils.navigateUpFromSameTask(this);
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

	private void initTables() {

		// Get a Session and init sofor Table
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = daoSession.getAutoDao();
		autoKepDao = daoSession.getAutoKepDao();
		munkaDao = daoSession.getMunkaDao();
		munkaEszkozDao = daoSession.getMunkaEszkozDao();
		munkaKepDao = daoSession.getMunkaKepDao();
		munkaTipusDao = daoSession.getMunkaTipusDao();
		partnerDao = daoSession.getPartnerDao();
		partnerKepDao = daoSession.getPartnerKepDao();
		profilKepDao = daoSession.getProfilKepDao();
		soforDao = daoSession.getSoforDao();
		telephelyDao = daoSession.getTelephelyDao();

		autoDao.createTable(db, true);
		autoKepDao.createTable(db, true);
		munkaDao.createTable(db, true);
		munkaEszkozDao.createTable(db, true);
		munkaKepDao.createTable(db, true);
		munkaTipusDao.createTable(db, true);
		partnerDao.createTable(db, true);
		partnerKepDao.createTable(db, true);
		profilKepDao.createTable(db, true);
		soforDao.createTable(db, true);
		telephelyDao.createTable(db, true);

	}

	public String saveAlldata() {
		JSONArray jsonArray;
		JSONObject json = new JSONObject();

		try {
			// get sofortable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(soforUrl,
					json.toString());
			soforDao.dropTable(soforDao.getDatabase(), true);
			soforDao.createTable(soforDao.getDatabase(), true);

			ArrayList<Sofor> soforok = JsonArrayToArrayList
					.JsonArrayToSofor(jsonArray);

			for (int i = 0; i < soforok.size(); i++) {
				soforDao.insert(soforok.get(i));
			}

			soforok = null;

			// get autotable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					autokepUrl, json.toString());

			autoDao.dropTable(autoDao.getDatabase(), true);
			autoDao.createTable(autoDao.getDatabase(), true);

			ArrayList<Auto> autok = JsonArrayToArrayList
					.JsonArrayToAuto(jsonArray);

			for (int i = 0; i < autok.size(); i++) {
				autoDao.insert(autok.get(i));
			}

			autok = null;

			// get partnertable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					partnerUrl, json.toString());

			partnerDao.dropTable(partnerDao.getDatabase(), true);
			partnerDao.createTable(partnerDao.getDatabase(), true);

			ArrayList<Partner> partnerek = JsonArrayToArrayList
					.JsonArrayToPartner(jsonArray);

			for (int i = 0; i < partnerek.size(); i++) {
				partnerDao.insert(partnerek.get(i));
			}

			partnerek = null;

			// get telephelytable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					telephelyUrl, json.toString());

			telephelyDao.dropTable(telephelyDao.getDatabase(), true);
			telephelyDao.createTable(telephelyDao.getDatabase(), true);

			ArrayList<Telephely> telephelyek = JsonArrayToArrayList
					.JsonArrayToTelephely(jsonArray);

			for (int i = 0; i < telephelyek.size(); i++) {
				telephelyDao.insert(telephelyek.get(i));
			}
			telephelyek = null;

			// get munkatable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(munkaUrl,
					json.toString());

			munkaDao.dropTable(munkaDao.getDatabase(), true);
			munkaDao.createTable(munkaDao.getDatabase(), true);

			ArrayList<Munka> munkak = JsonArrayToArrayList
					.JsonArrayToMunka(jsonArray);

			for (int i = 0; i < munkak.size(); i++) {
				munkaDao.insert(munkak.get(i));
			}
			munkak = null;

			// get munkatipustable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					munkaTipusUrl, json.toString());

			munkaTipusDao.dropTable(munkaTipusDao.getDatabase(), true);
			munkaTipusDao.createTable(munkaTipusDao.getDatabase(), true);

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

	public String saveAllPictures() {
		JSONArray jsonArray;
		JSONObject json = new JSONObject();
		sdcard = Environment.getExternalStorageDirectory();
		try {
			// get profilkeptable

			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					profilKepUrl, json.toString());

			ArrayList<ProfilKep> profilkepek = JsonArrayToArrayList
					.JsonArrayToProfilKepek(jsonArray);

			profilKepDao.dropTable(profilKepDao.getDatabase(), true);
			profilKepDao.createTable(profilKepDao.getDatabase(), true);

			for (int i = 0; i < profilkepek.size(); i++) {
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/SoforImages/"
						+ profilkepek.get(i).getSoforID();
				ImageDownloadUtil.downloadImage(profilkepek.get(i)
						.getProfilKepPath(), photoDirPath,
						Long.toString(profilkepek.get(i).getProfilKepID())
								+ ".jpg");
				profilkepek.get(i).setProfilKepPath(
						photoDirPath
								+ "/"
								+ Long.toString(profilkepek.get(i)
										.getProfilKepID()) + ".jpg");

				profilKepDao.insert(profilkepek.get(i));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
		return "";

	}

}
