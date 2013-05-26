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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PartnerUserDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Partner currentPartner;
	PartnerDao partnerDao;
	
	TextView nevEditText, cimEditText, telEditText, webEditTetx, emailEditText, xEditText, yEditText;
	Button okButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_user_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
		
		nevEditText=(TextView)findViewById(R.id.tvPartnerNevDATA);
		cimEditText=(TextView)findViewById(R.id.tvPartnerCimDATA);
		telEditText=(TextView)findViewById(R.id.tvPartnerTelefonDATA);
		webEditTetx=(TextView)findViewById(R.id.tvPartnerWebDATA);
		emailEditText=(TextView)findViewById(R.id.tvPartnerEmailDATA);
		xEditText=(TextView)findViewById(R.id.tvPartnerXDATA);
		yEditText=(TextView)findViewById(R.id.tvPartnerYDATA);
		okButton=(Button)findViewById(R.id.buttonPartnerOK);
		
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
		
	}
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		partnerDao = daoSession.getPartnerDao();
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
