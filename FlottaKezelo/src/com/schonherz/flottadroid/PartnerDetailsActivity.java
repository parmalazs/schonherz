package com.schonherz.flottadroid;

import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerDao.Properties;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class PartnerDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Partner currentPartner;
	PartnerDao partnerDao;
	
	EditText nevEditText, cimEditText, telEditText, webEditTetx, emailEditText, xEditText, yEditText;
	Button savePartner;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		if (getIntent().getLongExtra("selectedPartnerID", 0L)!=0L) {
			currentPartner=partnerDao.queryBuilder().where(
					Properties.PartnerID.eq(
							getIntent().getLongExtra("selectedPartnerID", 0L))).list().get(0);
		}
		else {
			currentPartner=new Partner();
			currentPartner.setPartnerID(0L);
		}
		
		nevEditText=(EditText)findViewById(R.id.editTextPartnerNev);
		cimEditText=(EditText)findViewById(R.id.editTextPartnerCim);
		telEditText=(EditText)findViewById(R.id.editTextPartnerTel);
		webEditTetx=(EditText)findViewById(R.id.editTextPartnerWeb);
		emailEditText=(EditText)findViewById(R.id.editTextPartnerEmail);
		xEditText=(EditText)findViewById(R.id.editTextPartnerX);
		yEditText=(EditText)findViewById(R.id.editTextPartnerY);
		savePartner=(Button)findViewById(R.id.buttonNewPartner);
		
		if (currentPartner.getPartnerID()==0L){
			nevEditText.setText("null");
			cimEditText.setText("null");
			telEditText.setText("null");
			webEditTetx.setText("null");
			emailEditText.setText("null");
			xEditText.setText("0F");
			yEditText.setText("0F");
		}
		else {
			nevEditText.setText(currentPartner.getPartnerNev());
			cimEditText.setText(currentPartner.getPartnerCim());
			telEditText.setText(currentPartner.getPartnerTelefonszam());
			webEditTetx.setText(currentPartner.getPartnerWeboldal());
			emailEditText.setText(currentPartner.getPartnerEmailcim());
			xEditText.setText(currentPartner.getPartnerXkoordinata().toString());
			yEditText.setText(currentPartner.getPartnerYkoodinata().toString());
		}
		
		savePartner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!nevEditText.getText().toString().isEmpty()) {
					savePartner();
				}
				else {
					Toast.makeText(getApplicationContext(), "Elfelejtett nevet megadni!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void savePartner() {
		currentPartner.setPartnerNev(nevEditText.getText().toString());
		if (!cimEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerCim(cimEditText.getText().toString());
		}
		else {
			currentPartner.setPartnerCim("null");
		}
		if (!telEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerTelefonszam(telEditText.getText().toString());
		}
		else {
			currentPartner.setPartnerTelefonszam("null");
		}
		if (!webEditTetx.getText().toString().isEmpty()) {
			currentPartner.setPartnerWeboldal(webEditTetx.getText().toString());
		}
		else {
			currentPartner.setPartnerWeboldal("null");
		}
		if (!emailEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerEmailcim(emailEditText.getText().toString());
		}
		else {

			currentPartner.setPartnerEmailcim("null");
		}
		if (!xEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerXkoordinata(Float.parseFloat(xEditText.getText().toString()));
		}
		else {
			currentPartner.setPartnerXkoordinata(0F);
		}
		if (!yEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerYkoodinata(Float.parseFloat(yEditText.getText().toString()));
		}
		else {
			currentPartner.setPartnerYkoodinata(0F);
		}
		
		if (currentPartner.getPartnerID()==0L) {
			currentPartner.setPartnerID(partnerDao.loadAll().get(partnerDao.loadAll().size()-1).getPartnerID()+1L);
			currentPartner.setPartnerIsActive(true);
			partnerDao.insert(currentPartner);
			finish();
		}
		
		partnerDao.update(currentPartner);
		finish();
	}
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		partnerDao = daoSession.getPartnerDao();

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
		getMenuInflater().inflate(R.menu.partner_details, menu);
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 
	}
	
}
