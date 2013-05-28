package com.schonherz.flottadroid;

import org.json.JSONObject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.SoforDao.Properties;

public class SoforDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	private SoforDao soforDao;
	private Sofor currentSofor;

	boolean saveMode = false; // true update, false insert

	Button saveButton;
	EditText nevEditTetx, cimEditText, telEditText, emailEditText,
			loginEditTetx, passEditText, birthEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sofor_details_admin);
		// Show the Up button in the action bar.
		setupActionBar();

		dataBaseInit();

		if (getIntent().getLongExtra("selectedSoforID", 0L) != 0L) {
			currentSofor = soforDao
					.queryBuilder()
					.where(Properties.SoforID.eq(getIntent().getLongExtra(
							"selectedSoforID", 0L))).list().get(0);

			saveMode = true;
		} else {
			saveMode = false;
			currentSofor = new Sofor();
			currentSofor.setSoforID(0L);
		}

		saveButton = (Button) findViewById(R.id.buttonSoforSave);
		nevEditTetx = (EditText) findViewById(R.id.editTextSoforNev);
		cimEditText = (EditText) findViewById(R.id.editTextSoforCim);
		telEditText = (EditText) findViewById(R.id.editTextSoforTel);
		emailEditText = (EditText) findViewById(R.id.editTextSoforEmail);
		loginEditTetx = (EditText) findViewById(R.id.editTextSoforLogin);
		passEditText = (EditText) findViewById(R.id.editTextSoforPass);
		birthEditText = (EditText) findViewById(R.id.editTextSoforBirth);

		if (currentSofor.getSoforID() == 0L) {
			nevEditTetx.setText("");
			cimEditText.setText("");
			telEditText.setText("");
			emailEditText.setText("");
			loginEditTetx.setText("");
			passEditText.setText("");
			birthEditText.setText("");
		} else {
			nevEditTetx.setText(currentSofor.getSoforNev());
			cimEditText.setText(currentSofor.getSoforCim());
			telEditText.setText(currentSofor.getSoforTelefonszam());
			emailEditText.setText(currentSofor.getSoforEmail());
			loginEditTetx.setText(currentSofor.getSoforLogin());
			passEditText.setText(currentSofor.getSoforPass());
			birthEditText.setText(currentSofor.getSoforBirthDate());
		}

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!nevEditTetx.getText().toString().isEmpty()) {
					if (!loginEditTetx.getText().toString().isEmpty()) {
						if (!passEditText.getText().toString().isEmpty()) {
							saveSofor();
						} else {
							Toast.makeText(getApplicationContext(),
									"Elfelejtett jelszót megadni!",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Elfelejtett login nevet megadni!",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Elfelejtett nevet megadni!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	public void saveSofor() {
		currentSofor.setSoforNev(nevEditTetx.getText().toString());
		currentSofor.setSoforLogin(loginEditTetx.getText().toString());
		currentSofor.setSoforPass(passEditText.getText().toString());

		if (!cimEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforCim(cimEditText.getText().toString());
		} else {
			currentSofor.setSoforCim("N/A");
		}

		if (!telEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforTelefonszam(telEditText.getText().toString());
		} else {
			currentSofor.setSoforTelefonszam("null");
		}

		if (!emailEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforEmail(emailEditText.getText().toString());
		} else {
			currentSofor.setSoforEmail("null");
		}

		if (!birthEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforBirthDate(birthEditText.getText().toString());
		} else {
			currentSofor.setSoforBirthDate("null");
		}

		if (currentSofor.getSoforID() == 0L) {
			currentSofor.setSoforID(soforDao.loadAll()
					.get(soforDao.loadAll().size() - 1).getSoforID() + 1L);
			currentSofor.setSoforIsActive(true);
			soforDao.insert(currentSofor);			
		}

		soforDao.update(currentSofor);
		if (NetworkUtil.checkInternetIsActive(getApplicationContext()) == true) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPostExecute(Boolean result) {
					
						helper.close();
						finish();
					

				};

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub

					
					JSONBuilder builder = new JSONBuilder();
					JSONSender sender = new JSONSender();
														
					if(saveMode==false)
					{						
						currentSofor.setSoforIsAdmin(false);
						if(currentSofor.getSoforProfilKepID()==null)
						{
							currentSofor.setSoforProfilKepID(0L);																						
						}
						JSONObject obj = builder.insertSofor(currentSofor);	
						sender.sendJSON(sender.getFlottaUrl(), obj);
						
					}
					else
					{
						if(currentSofor.getSoforProfilKepID()==null)
						{
							currentSofor.setSoforProfilKepID(0L);																						
						}
						JSONObject obj = builder.updateSofor(currentSofor);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}
					
					
					return true;
				}

			}.execute();
		} else {
			
			finish();
		}
	}


	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		soforDao = daoSession.getSoforDao();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sofor_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				helper.close();
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
		helper.close();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right,
				R.anim.slide_in_left);
	}
}
