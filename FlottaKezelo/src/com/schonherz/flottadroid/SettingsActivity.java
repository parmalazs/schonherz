package com.schonherz.flottadroid;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.SoforDao.Properties;

public class SettingsActivity extends PreferenceActivity {

	SharedPreferences prefs;
	Preference passwordPreference;

	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	private SoforDao soforDao;
	Context context;
	Sofor user;
	SessionManager manager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_settings);

		manager = new SessionManager(this);
		addPreferencesFromResource(R.xml.settings_prefs);
		context = this;
		dataBaseInit();
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		passwordPreference = (Preference) findPreference("changepass");
		passwordPreference
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					public boolean onPreferenceClick(Preference preference) {

						final Dialog passDialog = new Dialog(context);
						passDialog
								.setContentView(com.schonherz.flottadroid.R.layout.changepassword);
						passDialog.setTitle(R.string.passwordSummary);

						final EditText oldpass = (EditText) passDialog
								.findViewById(com.schonherz.flottadroid.R.id.oldPasswordET1);
						final EditText newPass1 = (EditText) passDialog
								.findViewById(com.schonherz.flottadroid.R.id.newPasswordET1);
						final EditText newPass2 = (EditText) passDialog
								.findViewById(com.schonherz.flottadroid.R.id.newPasswordET2);

						Button saveButton = (Button) passDialog
								.findViewById(com.schonherz.flottadroid.R.id.passwordSave);
						Button cancelButton = (Button) passDialog
								.findViewById(com.schonherz.flottadroid.R.id.cancelBtn);

						passDialog.show();

						cancelButton.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								passDialog.cancel();

							}
						});

						saveButton.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {

								user = soforDao
										.queryBuilder()
										.where(Properties.SoforID.eq(manager
												.getUserID().get(
														manager.KEY_USER_ID)))
										.list().get(0);

								String oldPas = oldpass.getText().toString();
								String newpas1 = newPass1.getText().toString();
								String newpas2 = newPass2.getText().toString();

								if (oldPas.equals(user.getSoforPass()))
									if (!newpas1.equals("")) {
										if (newpas1.equals(newpas2)) {
											user.setSoforPass(newpas2);
											user.update();

											SharedPreferences.Editor edit = prefs
													.edit();
											edit.putString("password",
													user.getSoforPass());
											edit.commit();

											Toast ts = Toast.makeText(
													SettingsActivity.this,
													R.string.settingsPWChanged,
													Toast.LENGTH_SHORT);
											ts.show();

											if (NetworkUtil
													.checkInternetIsActive(context)) {

												new AsyncTask<Void, Void, Boolean>() {

													protected void onPostExecute(
															Boolean result) {

														passDialog.dismiss();
													};

													@Override
													protected Boolean doInBackground(
															Void... params) {
														// TODO Auto-generated
														// method stub

														JSONBuilder builder = new JSONBuilder();
														JSONObject obj = builder
																.updateSofor(user);
														JSONSender send = new JSONSender();
														send.sendJSON(
																send.getFlottaUrl(),
																obj);
														return true;
													}
												}.execute();
											} else {
												passDialog.dismiss();
											}

										} else {
											Toast ts = Toast.makeText(
													SettingsActivity.this,
													R.string.settingsPWNotSame,
													Toast.LENGTH_SHORT);
											ts.show();
										}
									} else {
										Toast ts = Toast.makeText(
												SettingsActivity.this,
												R.string.PWRequired,
												Toast.LENGTH_SHORT);
										ts.show();
									}
								else {
									Toast ts = Toast.makeText(
											SettingsActivity.this,
											R.string.settingsPWOldError,
											Toast.LENGTH_SHORT);
									ts.show();

								}

							}
						});
						return false;
					}
				});

	}

	// setup connections to database
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		soforDao = daoSession.getSoforDao();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
}
