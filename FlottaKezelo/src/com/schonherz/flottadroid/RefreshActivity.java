package com.schonherz.flottadroid;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.schonherz.classes.ImageDownloadUtil;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.ProfilKepDao.Properties;
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

		taskToUploadTv.setText("");

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		lastSyncTime = preferences.getString("lastSync",
				"Nem volt még frissítés");

		lastSyncTv
				.setText(lastSyncTv.getText().toString() + " " + lastSyncTime);

		// imgToUploadTv.setText(imgToUploadTv.getText().toString()+" "+Integer.toString(getAllUploadPicture()));

		initTables();

		syncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AsyncTask<Void, Void, String>() {

					protected void onPostExecute(String result) {
						progress.dismiss();

						new AsyncTask<Void, Void, String>() {

							protected void onPostExecute(String result) {
								progress.dismiss();
							};

							protected void onPreExecute() {
								progress = ProgressDialog.show(
										RefreshActivity.this, getString(R.string.refreshing),
										getString(R.string.kepekletoltese));
							};

							@Override
							protected String doInBackground(Void... params) {
								// TODO Auto-generated method stub
								return saveAllPictures();
							}

						}.execute();

					};

					protected void onPreExecute() {
						progress = ProgressDialog.show(RefreshActivity.this,
								getString(R.string.refreshing), getString(R.string.adatokRefresh));
					};

					@Override
					protected String doInBackground(Void... params) {
						// TODO Auto-generated method stub
						return saveAlldata();
					}

				}.execute();

			}
		});

		imgUploadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Boolean>() {

					protected void onPostExecute(Boolean result) {
						progress.dismiss();
					};

					protected void onPreExecute() {
						progress = ProgressDialog.show(RefreshActivity.this,
								getString(R.string.refreshing), getString(R.string.uploadingpictures));
					};

					@Override
					protected Boolean doInBackground(Void... params) {
						// TODO Auto-generated method stub
						return uploadAllPictures();
					}

				}.execute();

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
				helper.close();
				NavUtils.navigateUpFromSameTask(this);
				this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		helper.close();
		finish();
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

	public int getAllUploadPicture() {
		List<ProfilKep> uploadProf = profilKepDao.queryBuilder()
				.where(Properties.ProfilkepIsUploaded.eq(false)).list();
		List<PartnerKep> uploadPart = partnerKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsUploaded
						.eq(false)).list();
		List<MunkaKep> uploadMunk = munkaKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsUploaded
						.eq(false)).list();
		List<AutoKep> uploadAut = autoKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsUploaded
						.eq(false)).list();

		int upload = (uploadProf.size() + uploadPart.size() + uploadMunk.size() + uploadAut
				.size());
		return upload;

	}

	public void getAllUploadTask() {

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

			ProfilKepDao.dropTable(profilKepDao.getDatabase(), true);
			ProfilKepDao.createTable(profilKepDao.getDatabase(), true);

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

				profilkepek.get(i).setProfilkepIsUploaded(true);

				profilKepDao.insert(profilkepek.get(i));
			}

			profilkepek = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}

		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					partnerKepUrl, json.toString());

			ArrayList<PartnerKep> partnerKepek = JsonArrayToArrayList
					.JsonArrayToPartnerKepek(jsonArray);

			PartnerKepDao.dropTable(partnerKepDao.getDatabase(), true);
			PartnerKepDao.createTable(partnerKepDao.getDatabase(), true);

			for (int i = 0; i < partnerKepek.size(); i++) {
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/PartnerImages/"
						+ partnerKepek.get(i).getPartnerID();
				ImageDownloadUtil.downloadImage(partnerKepek.get(i)
						.getPartnerKepPath(), photoDirPath,
						Long.toString(partnerKepek.get(i).getPartnerKepID())
								+ ".jpg");
				partnerKepek.get(i).setPartnerKepPath(
						photoDirPath
								+ "/"
								+ Long.toString(partnerKepek.get(i)
										.getPartnerKepID()) + ".jpg");

				partnerKepek.get(i).setPartnerKepIsUploaded(true);

				partnerKepDao.insert(partnerKepek.get(i));
			}

			partnerKepek = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
		 * munkaKepUrl, json.toString());
		 * 
		 * ArrayList<MunkaKep> munkakepek = JsonArrayToArrayList
		 * .JsonArrayToMunkaKepek(jsonArray);
		 * 
		 * munkaKepDao.dropTable(munkaKepDao.getDatabase(), true);
		 * munkaKepDao.createTable(munkaKepDao.getDatabase(), true);
		 * 
		 * for (int i = 0; i < munkakepek.size(); i++) { String photoDirPath =
		 * sdcard.getAbsolutePath() + "/" + "FlottaDroid/MunkaImages/" +
		 * munkakepek.get(i).getMunkaID();
		 * ImageDownloadUtil.downloadImage(munkakepek.get(i) .getMunkaKepPath(),
		 * photoDirPath, Long.toString(munkakepek.get(i).getMunkaKepID()) +
		 * ".jpg"); munkakepek.get(i).setMunkaKepPath( photoDirPath + "/" +
		 * Long.toString(munkakepek.get(i) .getMunkaKepID()) + ".jpg");
		 * 
		 * munkakepek.get(i).setMunkaKepIsUploaded(true);
		 * 
		 * munkaKepDao.insert(munkakepek.get(i)); }
		 * 
		 * munkakepek = null;
		 */
		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					autokepUrl, json.toString());

			ArrayList<AutoKep> autokepek = JsonArrayToArrayList
					.JsonArrayToAutoKepek(jsonArray);

			AutoKepDao.dropTable(autoKepDao.getDatabase(), true);

			AutoKepDao.createTable(autoKepDao.getDatabase(), true);

			for (int i = 0; i < autokepek.size(); i++) {
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/AutoImages/"
						+ autokepek.get(i).getAutoID();
				ImageDownloadUtil
						.downloadImage(autokepek.get(i).getAutoKepPath(),
								photoDirPath,
								Long.toString(autokepek.get(i).getAutoKepID())
										+ ".jpg");
				autokepek.get(i)
						.setAutoKepPath(
								photoDirPath
										+ "/"
										+ Long.toString(autokepek.get(i)
												.getAutoKepID()) + ".jpg");

				autokepek.get(i).setAutoKepIsUploaded(true);

				autoKepDao.insert(autokepek.get(i));
			}

			autokepek = null;

			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String s = sdf.format(now);

			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("lastSync", s);
			editor.commit();
			editor = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";

	}

	public Boolean uploadAllPictures() {
		sdcard = Environment.getExternalStorageDirectory();

		ArrayList<AutoKep> autoKepek = new ArrayList<AutoKep>(
				autoKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive
								.eq(false),
								com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsUploaded
										.eq(true)).list());
		if (autoKepek.size() != 0) {
			for (int i = 0; i < autoKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateAutoKep(autoKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, autoKepek
						.get(i).getAutoKepPath(), autoKepek.get(i)
						.getAutoKepID() + ".jpg", "auto", autoKepek.get(i)
						.getAutoKepID().toString());
			}
		}

		autoKepek.clear();
		autoKepek = new ArrayList<AutoKep>(
				autoKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive
								.eq(true),
								com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsUploaded
										.eq(false)).list());
		if (autoKepek.size() != 0) {
			for (int i = 0; i < autoKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateAutoKep(autoKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, autoKepek
						.get(i).getAutoKepPath(), autoKepek.get(i)
						.getAutoKepID() + ".jpg", "auto", autoKepek.get(i)
						.getAutoKepID().toString());
			}
		}
		autoKepek = null;

		ArrayList<MunkaKep> munkaKepek = new ArrayList<MunkaKep>(
				munkaKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsActive
								.eq(false),
								com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsUploaded
										.eq(true)).list());
		if (munkaKepek.size() != 0) {
			for (int i = 0; i < munkaKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateMunkaKep(munkaKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL,
						munkaKepek.get(i).getMunkaKepPath(), munkaKepek.get(i)
								.getMunkaKepID() + ".jpg", "munka", munkaKepek
								.get(i).getMunkaKepID().toString());
			}
		}

		munkaKepek.clear();
		munkaKepek = new ArrayList<MunkaKep>(
				munkaKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsActive
								.eq(true),
								com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsUploaded
										.eq(false)).list());
		if (munkaKepek.size() != 0) {
			for (int i = 0; i < munkaKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateMunkaKep(munkaKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL,
						munkaKepek.get(i).getMunkaKepPath(), munkaKepek.get(i)
								.getMunkaKepID() + ".jpg", "munka", munkaKepek
								.get(i).getMunkaKepID().toString());
			}
		}
		munkaKepek = null;

		ArrayList<PartnerKep> partnerKepek = new ArrayList<PartnerKep>(
				partnerKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive
								.eq(false),
								com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsUploaded
										.eq(true)).list());
		if (partnerKepek.size() != 0) {
			for (int i = 0; i < partnerKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updatePartnerKep(partnerKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, partnerKepek
						.get(i).getPartnerKepPath(), partnerKepek.get(i)
						.getPartnerKepID() + ".jpg", "partner", partnerKepek
						.get(i).getPartnerKepID().toString());
			}
		}

		partnerKepek.clear();
		partnerKepek = new ArrayList<PartnerKep>(
				partnerKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive
								.eq(true),
								com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsUploaded
										.eq(false)).list());
		if (partnerKepek.size() != 0) {
			for (int i = 0; i < partnerKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updatePartnerKep(partnerKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, partnerKepek
						.get(i).getPartnerKepPath(), partnerKepek.get(i)
						.getPartnerKepID() + ".jpg", "partner", partnerKepek
						.get(i).getPartnerKepID().toString());
			}
		}
		partnerKepek = null;

		ArrayList<ProfilKep> profilKepek = new ArrayList<ProfilKep>(
				profilKepDao
						.queryBuilder()
						.where(Properties.ProfilKepIsActive.eq(false),
								Properties.ProfilkepIsUploaded.eq(true)).list());

		if (profilKepek.size() != 0) {
			for (int i = 0; i < profilKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateProfilKep(profilKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, profilKepek
						.get(i).getProfilKepPath(), profilKepek.get(i)
						.getProfilKepID().toString()
						+ ".jpg", "profilkep", profilKepek.get(i)
						.getProfilKepID().toString());
			}
		}
		profilKepek.clear();
		profilKepek = new ArrayList<ProfilKep>(profilKepDao
				.queryBuilder()
				.where(Properties.ProfilKepIsActive.eq(true),
						Properties.ProfilkepIsUploaded.eq(false)).list());

		if (profilKepek.size() != 0) {
			for (int i = 0; i < profilKepek.size(); i++) {
				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateProfilKep(profilKepek.get(i));
				sender.sendJSON(sender.getFlottaUrl(), obj);
				sender.sendImg(JSONSender.flottaImageUploadURL, profilKepek
						.get(i).getProfilKepPath(), profilKepek.get(i)
						.getProfilKepID().toString()
						+ ".jpg", "profilkep", profilKepek.get(i)
						.getProfilKepID().toString());
			}
		}
		profilKepek = null;

		return true;
	}
}
