package com.schonherz.flottadroid;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoDao.Properties;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;

public class CarDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Auto currentAuto;
	AutoDao autoDao;
	
	TextView autoTipusTextView;
	TextView autoNevTextView;
	TextView autoRendszamTextView;
	TextView autoKmTextView;
	TextView autoUzemanyagTextView;
	TextView autoMuszakiVizsgaDateTextView;
	TextView autoLastServiceDateTextView;
	TextView autoLastUpDateTextView;
	TextView autoLastTelephelyTextView;
	TextView autoLastSoforNevTextView;
	//TextView autoProfilKepTextView;
	Button lefoglalButton;
	Button imageCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		if (getIntent().getLongExtra("selectedAutoID", 0L)!=0L) {
			currentAuto=autoDao.queryBuilder().where(
					Properties.AutoID.eq(
							getIntent().getLongExtra("selectedAutoID", 0L))).list().get(0);
		}
		else {
			currentAuto=new Auto();
			currentAuto.setAutoID(0L);
		}
		
		
		autoTipusTextView = (TextView)findViewById(R.id.autoTipusTextView);
		autoNevTextView = (TextView)findViewById(R.id.autoNevTextView);
		autoRendszamTextView = (TextView)findViewById(R.id.autoRendszamTextView);
		autoKmTextView = (TextView)findViewById(R.id.autoKmTextView);
		autoUzemanyagTextView = (TextView)findViewById(R.id.autoUzemanyagTextView);
		autoMuszakiVizsgaDateTextView = (TextView)findViewById(R.id.autoMuszakiVizsgaDateTextView);
		autoLastServiceDateTextView = (TextView)findViewById(R.id.autoLastServiceDateTextView);
		autoLastUpDateTextView = (TextView)findViewById(R.id.autoLastUpDateTextView);
		autoLastTelephelyTextView = (TextView)findViewById(R.id.autoLastTelephelyTextView);
		autoLastSoforNevTextView = (TextView)findViewById(R.id.autoLastSoforNevTextView);

			
		if (currentAuto.getAutoID()==0L){
			autoTipusTextView.setText("null");	
			autoNevTextView.setText("null");	
			autoRendszamTextView.setText("null");	
			autoKmTextView.setText("null");	
			autoUzemanyagTextView.setText("null");	
			autoMuszakiVizsgaDateTextView.setText("null");	
			autoLastServiceDateTextView.setText("null");	
			autoLastUpDateTextView.setText("null");	
			autoLastTelephelyTextView.setText("null");
			autoLastSoforNevTextView.setText("null");
		}
		else {
			
			autoTipusTextView.setText("Típus: " + currentAuto.getAutoTipus());	
			autoNevTextView.setText("Név: " + currentAuto.getAutoNev());	
			autoRendszamTextView.setText("Rendszám: " + currentAuto.getAutoRendszam());	
			autoKmTextView.setText("Km óra: " + currentAuto.getAutoKilometerOra());	
			autoUzemanyagTextView.setText("Üzemanyag: " + currentAuto.getAutoUzemanyag());
			autoMuszakiVizsgaDateTextView.setText("Mûszaki vizsga: " + currentAuto.getAutoMuszakiVizsgaDate());	
			autoLastServiceDateTextView.setText("Utolsó szervíz: " + currentAuto.getAutoLastSzervizDate());	
			autoLastUpDateTextView.setText("Utolsó változás: " + currentAuto.getAutoLastUpDate());	
			autoLastTelephelyTextView.setText("Utolsó telephely: " + currentAuto.getAutoLastTelephelyID());
			autoLastSoforNevTextView.setText("Utolsó sofõr: " + currentAuto.getAutoLastSoforID());
		}
		
		lefoglalButton=(Button)findViewById(R.id.lefoglalButton);
		lefoglalButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentAuto.setAutoFoglalt(true);
				
				autoDao.update(currentAuto);
				finish();
			}
		});
	}
	
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		autoDao = daoSession.getAutoDao();

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
		// EZT ÁTÍRNI
		getMenuInflater().inflate(R.menu.auto_details, menu);
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
