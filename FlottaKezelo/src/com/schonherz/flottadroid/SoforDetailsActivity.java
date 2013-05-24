package com.schonherz.flottadroid;


import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.SoforDao.Properties;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SoforDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	private SoforDao soforDao;
	private Sofor currentSofor;
	
	Button saveButton;
	EditText nevEditTetx, cimEditText, telEditText, emailEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sofor_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		currentSofor=soforDao.queryBuilder().where(
				Properties.SoforID.eq(
						getIntent().getLongExtra("selectedSoforID", 0L))).list().get(0);
		
		saveButton=(Button)findViewById(R.id.buttonSoforSave);
		nevEditTetx=(EditText)findViewById(R.id.editTextSoforNev);
		cimEditText=(EditText)findViewById(R.id.editTextSoforCim);
		telEditText=(EditText)findViewById(R.id.editTextSoforTel);
		emailEditText=(EditText)findViewById(R.id.editTextSoforEmail);
		
		nevEditTetx.setText(currentSofor.getSoforNev());
		cimEditText.setText(currentSofor.getSoforCim());
		telEditText.setText(currentSofor.getSoforTelefonszam());
		emailEditText.setText(currentSofor.getSoforEmail());
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveSofor();
			}
		});
	}
	
	public void saveSofor() {
		if (!nevEditTetx.getText().toString().isEmpty()){
			currentSofor.setSoforNev(nevEditTetx.getText().toString());
		}
		
		if (!cimEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforCim(cimEditText.getText().toString());
		}
		else {
			currentSofor.setSoforCim("null");
		}
		
		if (!telEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforTelefonszam(telEditText.getText().toString());
		}
		else {
			currentSofor.setSoforTelefonszam("null");
		}
		
		if (!emailEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforEmail(emailEditText.getText().toString());
		}
		else {
			currentSofor.setSoforEmail("null");
		}
		
		soforDao.update(currentSofor);
		
		finish();
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
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
