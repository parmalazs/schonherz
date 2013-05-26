package com.schonherz.flottadroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;

public class NewJobActivity extends Activity {
	
	// Database Handlers
		private SQLiteDatabase db;
		private DevOpenHelper helper;
		private DaoSession daoSession;
		private DaoMaster daoMaster;

		// Greendao objects
		private MunkaDao munkaDao;
		private MunkaTipusDao munkaTipusDao;
		private PartnerDao partnerDao;
		private SoforDao soforDao;
		
	
		Spinner partnerekSpinner;
		Spinner munkatipusokSpinner;
		Spinner soforokSpinner;
		Button saveButton;
		Button dateSelectButton;
		EditText estimatedTimeEditText;
		TextView dateTextView;
		
		private int year;
		private int month;
		private int day;
		
		static final int DATE_DIALOG_ID = 999;
		private ArrayList<String> soforNevek;
		private ArrayList<String> partnerNevek;
		private ArrayList<String> munkaTipusNevek;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_job);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();	
		
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		partnerekSpinner=(Spinner)findViewById(R.id.spinnerPartners);
		munkatipusokSpinner=(Spinner)findViewById(R.id.spinnerMunkatipus);
		soforokSpinner=(Spinner)findViewById(R.id.spinnerSoforok);
		saveButton=(Button)findViewById(R.id.buttonSave);
		dateSelectButton=(Button)findViewById(R.id.buttonDateSelect);
		estimatedTimeEditText=(EditText)findViewById(R.id.editTextEstimatedTime);
		dateTextView=(TextView)findViewById(R.id.textViewDate);
				
		//spinnerek feltöltése, abc sorrendben a nevekkel
		if (partnerDao.loadAll().size()!=0) {
			ArrayList<Partner> partnerek=new ArrayList<Partner>(partnerDao.queryBuilder().orderAsc(PartnerDao.Properties.PartnerNev).list());
			partnerNevek=new ArrayList<String>();
			for (int i=0; i<partnerek.size(); i++) {
				partnerNevek.add(partnerek.get(i).getPartnerNev());
			}			
			ArrayAdapter<String> partnerAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, partnerNevek);
			partnerekSpinner.setAdapter(partnerAdapter);
		}
	
		if (munkaTipusDao.loadAll().size()!=0) {
			ArrayList<MunkaTipus> munkatipusok=new ArrayList<MunkaTipus>(munkaTipusDao.queryBuilder().orderAsc(MunkaTipusDao.Properties.MunkaTipusNev).list());
			munkaTipusNevek=new ArrayList<String>();
			for (int i=0; i<munkatipusok.size(); i++) {
				munkaTipusNevek.add(munkatipusok.get(i).getMunkaTipusNev());
			}
			ArrayAdapter<String> munkaTipusAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, munkaTipusNevek);
			munkatipusokSpinner.setAdapter(munkaTipusAdapter);
		}
		
		if (soforDao.loadAll().size()!=0) {
			ArrayList<Sofor> soforok=new ArrayList<Sofor>(soforDao.queryBuilder().orderAsc(SoforDao.Properties.SoforNev).list());
			soforNevek=new ArrayList<String>();
			for (int i=0; i<soforok.size(); i++) {
				soforNevek.add(soforok.get(i).getSoforNev());
			}
			soforNevek.add("null");
			ArrayAdapter<String> soforAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soforNevek);						
			soforokSpinner.setAdapter(soforAdapter);
			soforokSpinner.setSelection(soforNevek.size()-1);
			soforokSpinner.setSelected(false);
		}
		
		dateSelectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
				
		dateTextView.setText("null");
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!estimatedTimeEditText.getText().toString().isEmpty()) {
					saveNewJob();
				}
				else {
					Toast.makeText(getApplicationContext(), R.string.newJobNoEstimatedTime, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener 
    = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
			int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			// set selected date into textview
			
			dateTextView.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(year).append(".").append(pad(month+1)).append(".")
			.append(pad(day)).append("."));		
		
		}
	};
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	
	public void saveNewJob() {
		Munka ujMunka=new Munka();
		
		ujMunka.setMunkaDate(dateTextView.getText().toString());
		ujMunka.setMunkaEstimatedTime(Long.parseLong(estimatedTimeEditText.getText().toString()));
		ujMunka.setMunkaIsActive(true);
		ujMunka.setMunkaTipusID(munkaTipusDao.queryBuilder()
				.where(MunkaTipusDao.Properties.MunkaTipusNev.eq(
						munkatipusokSpinner.getSelectedItem())).list().get(0).getMunkaTipusID());
		if (soforokSpinner.getSelectedItem().equals("null")) {
			ujMunka.setSoforID(0L);
		}
		else {
			ujMunka.setSoforID(soforDao.queryBuilder()
					.where(SoforDao.Properties.SoforNev.eq(
							soforokSpinner.getSelectedItem())).list().get(0).getSoforID());
		}
		ujMunka.setPartnerID(partnerDao.queryBuilder()
				.where(PartnerDao.Properties.PartnerNev.eq(
						partnerekSpinner.getSelectedItem())).list().get(0).getPartnerID());
		ujMunka.setTelephelyID(0L);
		ujMunka.setMunkaBefejezesDate("null");
		ujMunka.setMunkaBevetel(0L);
		ujMunka.setMunkaKoltseg(0L);
		ujMunka.setMunkaComment("új munka");
		ujMunka.setMunkaUzemanyagState(0L);
		ujMunka.setMunkaID(munkaDao.loadAll().get(munkaDao.loadAll().size()-1).getMunkaID()+1L);
		
		munkaDao.insert(ujMunka);
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 
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
		getMenuInflater().inflate(R.menu.new_job, menu);
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

		
		munkaDao = daoSession.getMunkaDao();
		munkaTipusDao = daoSession.getMunkaTipusDao();
		partnerDao = daoSession.getPartnerDao();
		soforDao = daoSession.getSoforDao();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 		
	}

}
