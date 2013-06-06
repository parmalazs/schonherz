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
import com.schonherz.dbentities.PushMessageDao;
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
	private DevOpenHelper newhelper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	private SoforDao soforDao;
	
	private SessionManager sessionManager;

	
	Button loginButton;
	ProgressDialog dialog;

	EditText userEditText;
	EditText passEditText;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		context=this;
		sessionManager=new SessionManager(context);
		
		newhelper = new DevOpenHelper(context,"flotta-db",null);
		
		db = newhelper.getWritableDatabase();
		
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		firstStart = preferences.getLong("firstStart", 0);

		userEditText = (EditText) findViewById(R.id.editTextUser);
		passEditText = (EditText) findViewById(R.id.editTextPass);
		
		//userEditText.setText("sofor1");
		//passEditText.setText("sofor1");
		
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

		} 
		
			// Get a Session and init sofor Table
		

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
						super.onPostExecute(result);

						if (result == true) {
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
					newhelper.close();
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
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
			SoforDao.dropTable(soforDao.getDatabase(), true);
			SoforDao.createTable(soforDao.getDatabase(), true);
			
						
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
		newhelper.close();
		super.finish();
	}

	// Tabla letrehozo metodus, elso inditaskor mindekepp lefut
	private void createTables() {

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
		PushMessageDao.createTable(db, true);

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
					sessionManager.createLoginSession(soforok.get(0).getSoforID());
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
