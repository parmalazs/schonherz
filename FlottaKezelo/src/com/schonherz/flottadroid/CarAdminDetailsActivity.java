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
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoDao.Properties;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;

public class CarAdminDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Auto currentAuto;
	AutoDao autoDao;
	
	EditText autoTipusEditText;
	EditText autoNevEditText;
	EditText autoRendszamEditText;
	EditText autoKmEditText;
	EditText autoUzemanyagEditText;
	EditText autoMuszakiVizsgaDateEditText;
	EditText autoLastServiceDateEditText;
	EditText autoLastTelephelyEditText;
	EditText autoLastSoforNevEditText;
	Button saveButton;
	
	boolean saveMode = false; //true update, false insert 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_admin_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		if (getIntent().getLongExtra("selectedAutoID", 0L)!=0L) {
			currentAuto=autoDao.queryBuilder().where(
					Properties.AutoID.eq(
							getIntent().getLongExtra("selectedAutoID", 0L))).list().get(0);
			saveMode = true;
		}
		else {
			currentAuto=new Auto();
			currentAuto.setAutoID(0L);
			saveMode = false;
		}
		
		
		autoTipusEditText = (EditText)findViewById(R.id.editTextAutoTipus);
		autoNevEditText = (EditText)findViewById(R.id.editTextAutoNev);
		autoRendszamEditText = (EditText)findViewById(R.id.editTextAutoRendszam);
		autoKmEditText = (EditText)findViewById(R.id.editTextAutoKm);
		autoUzemanyagEditText = (EditText)findViewById(R.id.editTextAutoUzemanyag);
		autoMuszakiVizsgaDateEditText = (EditText)findViewById(R.id.editTextAutoMuszaki);
		autoLastServiceDateEditText = (EditText)findViewById(R.id.editTextAutoUtolsoSzerviz);
		autoLastTelephelyEditText = (EditText)findViewById(R.id.editTextUtolsoTelephely);
		autoLastSoforNevEditText = (EditText)findViewById(R.id.editTextUtolsoSofor);
		saveButton=(Button)findViewById(R.id.lefoglalButton);

			
		if (currentAuto.getAutoID()==0L){
			autoTipusEditText.setText("null");	
			autoNevEditText.setText("null");	
			autoRendszamEditText.setText("null");	
			autoKmEditText.setText("0");	
			autoUzemanyagEditText.setText("0");	
			autoMuszakiVizsgaDateEditText.setText("null");	
			autoLastServiceDateEditText.setText("null");
			autoLastTelephelyEditText.setText("0");
			autoLastSoforNevEditText.setText("0");
		}
		else {
			
			autoTipusEditText.setText(currentAuto.getAutoTipus());	
			autoNevEditText.setText(currentAuto.getAutoNev());	
			autoRendszamEditText.setText(currentAuto.getAutoRendszam());	
			autoKmEditText.setText( currentAuto.getAutoKilometerOra().toString());	
			autoUzemanyagEditText.setText( currentAuto.getAutoUzemanyag().toString());
			autoMuszakiVizsgaDateEditText.setText(currentAuto.getAutoMuszakiVizsgaDate());	
			autoLastServiceDateEditText.setText(currentAuto.getAutoLastSzervizDate());
			autoLastTelephelyEditText.setText(currentAuto.getAutoLastTelephelyID().toString());
			autoLastSoforNevEditText.setText(currentAuto.getAutoLastSoforID().toString());
		}
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!autoNevEditText.getText().toString().isEmpty()) {
					if (!autoTipusEditText.getText().toString().isEmpty()) {
						if (!autoRendszamEditText.getText().toString().isEmpty()) {
							if (!autoMuszakiVizsgaDateEditText.getText().toString().isEmpty()) {
								saveCar();
							}
							else {
								Toast.makeText(getApplicationContext(), "Elfelejtett mûszaki vizsga dátumot megadni!", Toast.LENGTH_LONG).show();
							}
						}
						else {
							Toast.makeText(getApplicationContext(), "Elfelejtett rendszámot megadni!", Toast.LENGTH_LONG).show();
						}
					}
					else {
						Toast.makeText(getApplicationContext(), "Elfelejtett tipust megadni!", Toast.LENGTH_LONG).show();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "Elfelejtett nevet megadni!", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void saveCar() {
		currentAuto.setAutoNev(autoNevEditText.getText().toString());
		currentAuto.setAutoTipus(autoTipusEditText.getText().toString());
		currentAuto.setAutoRendszam(autoRendszamEditText.getText().toString());
		currentAuto.setAutoMuszakiVizsgaDate(autoMuszakiVizsgaDateEditText.getText().toString());
		
		if (!autoKmEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoKilometerOra(Long.parseLong(autoKmEditText.getText().toString()));
		}
		else {
			currentAuto.setAutoKilometerOra(0L);
		}
		
		if (!autoUzemanyagEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoUzemanyag(Long.parseLong(autoUzemanyagEditText.getText().toString()));
		}
		else {
			currentAuto.setAutoUzemanyag(0L);
		}
		
		if (!autoLastServiceDateEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastSzervizDate(autoLastServiceDateEditText.getText().toString());
		}
		else {
			currentAuto.setAutoLastSzervizDate("null");
		}
		
		if (!autoLastTelephelyEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastTelephelyID(Long.parseLong(autoLastTelephelyEditText.getText().toString()));
		}
		else {
			currentAuto.setAutoLastTelephelyID(0L);
		}
		
		if (!autoLastSoforNevEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastSoforID(Long.parseLong(autoLastSoforNevEditText.getText().toString()));
		}
		else {
			currentAuto.setAutoLastSoforID(0L);
		}
		
		if (currentAuto.getAutoID()==0L) {
			currentAuto.setAutoID(autoDao.loadAll().get(autoDao.loadAll().size()-1).getAutoID()+1L);
			currentAuto.setAutoIsActive(true);
			currentAuto.setAutoFoglalt(false);
			currentAuto.setAutoProfilKepID(0L);
			currentAuto.setAutoLastUpDate("null");
			currentAuto.setAutoXkoordinata(0F);
			currentAuto.setAutoYkoordinata(0F);
			autoDao.insert(currentAuto);
		
			
		}
		
		autoDao.update(currentAuto);
		
		if(NetworkUtil.checkInternetIsActive(CarAdminDetailsActivity.this)==true)
		{
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPostExecute(Boolean result) {
					
					finish();
					super.onPostExecute(result);
				};
				
				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					
					JSONBuilder builder = new JSONBuilder();
					JSONSender sender = new JSONSender();
					
					if(saveMode == true)
					{
						JSONObject obj = builder.updateAuto(currentAuto);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}
					else
					{
						JSONObject obj = builder.insertAuto(currentAuto);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}
					
					return true;
				}
			}.execute();
		}
		else
		{
			finish();
		}
		
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
		getMenuInflater().inflate(R.menu.car_admin_details, menu);
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
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		autoDao = daoSession.getAutoDao();

	}

}
