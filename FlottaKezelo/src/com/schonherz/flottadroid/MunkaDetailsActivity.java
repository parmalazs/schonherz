package com.schonherz.flottadroid;

import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.MunkaDao.Properties;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class MunkaDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Munka currentMunka;
	MunkaDao munkaDao;
	SessionManager sessionManager;
	
	TextView dateTextView;
	TextView uzemanyagTextView;
	EditText commentEditText;
	Button saveButton;
	Button timePickerButton;
	TextView munkaKezdTextView;
	TextView munkaVegeTextView;
	EditText koltsegEditText;
	EditText bevetelEditText;
	
	static final int TIME_DIALOG_ID = 999;
	private int hour;
	private int minute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_munka_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		sessionManager=new SessionManager(this);
		
		//az intentbõl kiszedjük a listából kiválasztott munka id-t és ez alapján megkeressük
		currentMunka=munkaDao.queryBuilder().
				where(Properties.MunkaID.eq(
						this.getIntent().getLongExtra("selectedMunkaID", 0))).list().get(0);
		
		dateTextView=(TextView)findViewById(R.id.textViewMunkaDate);
		uzemanyagTextView=(TextView)findViewById(R.id.textViewUzemanyagState);
		commentEditText=(EditText)findViewById(R.id.editTextComment);
		saveButton=(Button)findViewById(R.id.buttonJobSave);
		timePickerButton=(Button)findViewById(R.id.buttonTime);
		munkaKezdTextView=(TextView)findViewById(R.id.textViewJobBegin);
		munkaVegeTextView=(TextView)findViewById(R.id.textViewJobEnd);
		koltsegEditText=(EditText)findViewById(R.id.editTextKoltseg);
		bevetelEditText=(EditText)findViewById(R.id.EditTextBevetel);
		
		dateTextView.setText(currentMunka.getMunkaDate());
		commentEditText.setText(currentMunka.getMunkaComment());
		uzemanyagTextView.setText(currentMunka.getMunkaUzemanyagState().toString());
		munkaVegeTextView.setText(currentMunka.getMunkaBefejezesDate());
		koltsegEditText.setText(currentMunka.getMunkaKoltseg().toString());
		bevetelEditText.setText(currentMunka.getMunkaBevetel().toString());
		
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveJob();
			}
		});
		
		timePickerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
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
	protected Dialog onCreateDialog(int id) {
		// time picker indítása
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute, true);
		}
		return null;
	}
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
			// beállítom a visszakapott értékek alapján a kezdést és befejezést
			hour=hourOfDay;
			minute=selectedMinute;
			
			munkaKezdTextView.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(minute)));
			munkaVegeTextView.setText(new StringBuilder().append(pad(hour+currentMunka.getMunkaEstimatedTime().intValue())).append(":").append(pad(minute)));
		}
	};
	
	//ez azért kell, hogy ha egy számjegyû az óra vagy a perc akkor kiegészíti egy 0-val 
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.munka_details, menu);
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
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		munkaDao = daoSession.getMunkaDao();

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		super.onPause();
	}
	
	public void saveJob(){
		
		currentMunka.setMunkaBefejezesDate(munkaVegeTextView.getText().toString());
		currentMunka.setMunkaKoltseg(Long.parseLong(koltsegEditText.getText().toString()));
		currentMunka.setMunkaBevetel(Long.parseLong(bevetelEditText.getText().toString()));
		currentMunka.setMunkaComment(commentEditText.getText().toString());		
		currentMunka.setSoforID(sessionManager.getUserID().get(SessionManager.KEY_USER_ID));
		
		currentMunka.update();		
		munkaDao.update(currentMunka);
		
		finish();
	}

}
