package com.schonherz.flottadroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
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
import com.schonherz.dbentities.SoforDao.Properties;
import com.schonherz.dbentities.TelephelyDao;

public class LoginActivity extends Activity {

	// Handle first start from preference
	SharedPreferences preferences;
	long firstStart;

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
	
	
	
	private SessionManager sessionManager;
	private boolean isRefreshed;

	
	Button loginButton;
	ProgressDialog dialog;

	EditText userEditText;
	EditText passEditText;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		context=getApplicationContext();
		sessionManager=new SessionManager(context);
		isRefreshed=false;

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		firstStart = preferences.getLong("firstStart", 0);

		userEditText = (EditText) findViewById(R.id.editTextUser);
		passEditText = (EditText) findViewById(R.id.editTextPass);
		
		userEditText.setText("sofor1");
		passEditText.setText("sofor1");
		
		// First start, full DB init, get soforTable
		if (firstStart == 0) {

			createTables();

			SharedPreferences.Editor editor = preferences.edit();
			editor.putLong("firstStart", 1);
			editor.commit();
			editor = null;

			if (NetworkUtil.checkInternetIsActive(context) == false) {

				Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT)
						.show();
			}

		} else {
			// Get a Session and init sofor Table
			helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
			db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();

			soforDao = daoSession.getSoforDao();
		}

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
						super.onPostExecute(result);

						if (result == true) {
							isRefreshed=true;
							loginButton.setEnabled(true);
							Toast.makeText(LoginActivity.this,
									R.string.refreshed, Toast.LENGTH_SHORT)
									.show();							
						}
						else
						{
							Toast.makeText(LoginActivity.this, R.string.errorRefresh, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					protected Boolean doInBackground(Void... params) {
						// TODO Auto-generated method stub
						return saveSoforTable();
					}

				}.execute();
			}
		

		loginButton = (Button) findViewById(R.id.buttonEntry);
		loginButton.setEnabled(false);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start main activity
				if (checkLogin() == true) {
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					intent.putExtra("isRefreshed", isRefreshed);
					LoginActivity.this.startActivity(intent);
					LoginActivity.this.finish();
				} else {
					Toast.makeText(LoginActivity.this,
							R.string.wrongCredential, Toast.LENGTH_SHORT)
							.show();
				}

			}
		});
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

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	// Tabla letrehozo metodus, elso inditaskor mindekepp lefut
	private void createTables() {
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

	public String update() {
		return "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}	

	public boolean checkLogin() {
		if (!userEditText.getText().toString().equals("")) {
			if (!passEditText.getText().toString().equals("")) {

				// Ez csak bemutatato, a loadAll() az a select * from
				List<Sofor> sofors = soforDao.loadAll();

				// Where-ben 2 feltetellel lekerdezes, a Properties az a
				// SoforDao properties osztalya importalva
				// Minden tablanak van minden rekordjara egy property, amihez
				// lehet hasonlita
				List<Sofor> soforok = soforDao
						.queryBuilder()
						.where(Properties.SoforLogin.eq(userEditText.getText()
								.toString()),
								Properties.SoforPass.eq(passEditText.getText()
										.toString())).list();

				if (soforok.size() > 0) {
					sessionManager.createLoginSession(soforok.get(0).getSoforLogin(), soforok.get(0).getSoforPass(), soforok.get(0).getSoforID());
					return true;
				} else {
					return false;
				}

			} else {
				Toast.makeText(LoginActivity.this, R.string.wrongCredential,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(LoginActivity.this, R.string.wrongCredential,
					Toast.LENGTH_SHORT).show();
		}

		return false;
	}

}
