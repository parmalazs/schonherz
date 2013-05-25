package com.schonherz.flottadroid;

import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.TelephelyDao.Properties;

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

public class TelephelyDetailsActivity extends Activity {
	
	Button saveTelephelyButton;
	EditText nevEditText, cimEditText, telEditText, emailEditText, xEditText, yEditText;
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Telephely currentTelephely;
	TelephelyDao telephelyDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_telephely_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		if (getIntent().getLongExtra("selectedTelephelyID", 0L)!=0L) {
			currentTelephely=telephelyDao.queryBuilder().where(
					Properties.TelephelyID.eq(
							getIntent().getLongExtra("selectedTelephelyID", 0L))).list().get(0);
		}
		else {
			currentTelephely=new Telephely();
			currentTelephely.setTelephelyID(0L);
		}
		
		nevEditText=(EditText)findViewById(R.id.editTextTelephelyNev);
		cimEditText=(EditText)findViewById(R.id.editTextTelephelyCim);
		telEditText=(EditText)findViewById(R.id.editTextTelephelyTel);
		emailEditText=(EditText)findViewById(R.id.editTextTelephelyEmail);
		xEditText=(EditText)findViewById(R.id.editTextTelephelyX);
		yEditText=(EditText)findViewById(R.id.editTextTelephelyY);
		saveTelephelyButton=(Button)findViewById(R.id.buttonTelephelySave);
		
		if (currentTelephely.getTelephelyID()==0L){
			nevEditText.setText("null");
			cimEditText.setText("null");
			telEditText.setText("null");
			emailEditText.setText("null");
			xEditText.setText("0F");
			yEditText.setText("0F");
		}
		else {
			nevEditText.setText(currentTelephely.getTelephelyNev());
			cimEditText.setText(currentTelephely.getTelephelyCim());
			telEditText.setText(currentTelephely.getTelephelyTelefonszam());
			emailEditText.setText(currentTelephely.getTelephelyEmail());
			xEditText.setText(currentTelephely.getTelephelyXkoordinata().toString());
			yEditText.setText(currentTelephely.getTelephelyYkoordinata().toString());
		}
		
		saveTelephelyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!nevEditText.getText().toString().isEmpty()) {
					saveTelephely();
				}
				else {
					Toast.makeText(getApplicationContext(), "Elfelejtett nevet megadni!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void saveTelephely() {
		currentTelephely.setTelephelyNev(nevEditText.getText().toString());
		if (!cimEditText.getText().toString().isEmpty()) {
			currentTelephely.setTelephelyCim(cimEditText.getText().toString());
		}
		else {
			currentTelephely.setTelephelyCim("null");
		}
		if (!telEditText.getText().toString().isEmpty()) {
			currentTelephely.setTelephelyTelefonszam(telEditText.getText().toString());
		}
		else {
			currentTelephely.setTelephelyTelefonszam("null");
		}
		if (!emailEditText.getText().toString().isEmpty()) {
			currentTelephely.setTelephelyEmail(emailEditText.getText().toString());
		}
		else {

			currentTelephely.setTelephelyEmail("null");
		}
		if (!xEditText.getText().toString().isEmpty()) {
			currentTelephely.setTelephelyXkoordinata(Float.parseFloat(xEditText.getText().toString()));
		}
		else {
			currentTelephely.setTelephelyXkoordinata(0F);
		}
		if (!yEditText.getText().toString().isEmpty()) {
			currentTelephely.setTelephelyYkoordinata(Float.parseFloat(yEditText.getText().toString()));
		}
		else {
			currentTelephely.setTelephelyYkoordinata(0F);
		}
		
		if (currentTelephely.getTelephelyID()==0L) {
			currentTelephely.setTelephelyID(telephelyDao.loadAll().get(telephelyDao.loadAll().size()-1).getTelephelyID()+1L);
			currentTelephely.setTelephelyIsActive(true);
			telephelyDao.insert(currentTelephely);
			finish();
		}
		
		telephelyDao.update(currentTelephely);
		finish();
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
		getMenuInflater().inflate(R.menu.telephely_details, menu);
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
			NavUtils.navigateUpFromSameTask(this);
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
		
		telephelyDao = daoSession.getTelephelyDao();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 
	}

}
