package com.schonherz.flottadroid;

import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.SoforDao.Properties;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SoforUserDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Sofor currentSofor;
	SoforDao soforDao;
	
	TextView nevEditText, cimEditText, telEditText, birthEditTetx, emailEditText;
	Button okButton, dialButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sofor_user_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		currentSofor=soforDao.queryBuilder().where(
				Properties.SoforID.eq(
						getIntent().getLongExtra("selectedSoforID", 0L))).list().get(0);
		
		nevEditText=(TextView)findViewById(R.id.tvSoforNevDATA);
		cimEditText=(TextView)findViewById(R.id.tvSoforCimDATA);
		telEditText=(TextView)findViewById(R.id.tvSoforTelefonDATA);
		birthEditTetx=(TextView)findViewById(R.id.tvSoforBirthDATA);
		emailEditText=(TextView)findViewById(R.id.tvSoforEmailDATA);
		okButton=(Button)findViewById(R.id.buttonSoforOK);
		dialButton=(Button)findViewById(R.id.buttonSoforUserDial);
		
		nevEditText.setText(currentSofor.getSoforNev());
		cimEditText.setText(currentSofor.getSoforCim());
		telEditText.setText(currentSofor.getSoforTelefonszam());
		birthEditTetx.setText(currentSofor.getSoforBirthDate());
		emailEditText.setText(currentSofor.getSoforEmail());
		
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			}
		});
		
		dialButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentSofor.getSoforTelefonszam())));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sofor_user_details, menu);
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
			this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		soforDao = daoSession.getSoforDao();
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
