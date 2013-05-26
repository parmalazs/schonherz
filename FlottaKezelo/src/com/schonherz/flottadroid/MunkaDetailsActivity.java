package com.schonherz.flottadroid;

import java.util.List;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
	CheckBox confirmCheckBox;
	Button imageCreate;
	
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
		bevetelEditText=(EditText)findViewById(R.id.editTextBevetel);
		confirmCheckBox=(CheckBox)findViewById(R.id.checkBoxConfirm);
		imageCreate=(Button)findViewById(R.id.buttonImage);
		
		dateTextView.setText(currentMunka.getMunkaDate());
		commentEditText.setText(currentMunka.getMunkaComment());
		uzemanyagTextView.setText(currentMunka.getMunkaUzemanyagState().toString());
		munkaVegeTextView.setText(currentMunka.getMunkaBefejezesDate());
		koltsegEditText.setText(currentMunka.getMunkaKoltseg().toString());
		bevetelEditText.setText(currentMunka.getMunkaBevetel().toString());
		
		//itt beállítom a munka kezdési idejét, amit az elmentett befejezési idõ és a szükséges idõ különbségébõl számolok ki
		if(!currentMunka.getMunkaBefejezesDate().toString().equals("null")) {
			long kezdTmpHour=Long.parseLong(currentMunka.getMunkaBefejezesDate().substring(0, 2))-currentMunka.getMunkaEstimatedTime();
			String kezdTmpMinute=currentMunka.getMunkaBefejezesDate().substring(3, 5);
			munkaKezdTextView.setText(new StringBuilder().append(pad((int)kezdTmpHour)).append(":").append(kezdTmpMinute));
		}
		
		
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

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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

	}	
	
	public void saveJob(){
		
		if(!checkUtkozes()) {
			if(!munkaVegeTextView.getText().equals("null")) {
				if(confirmCheckBox.isChecked()){
					//ide még kéne egy alert dialog, de valamiért mindig megjelenik és bezáródik...
					currentMunka.setMunkaIsActive(false);
					confirmedSaveJob();
				}
				
				confirmedSaveJob();
			}
			else {
				Toast.makeText(getApplicationContext(), R.string.munkaFelvetelNullTime, Toast.LENGTH_LONG).show();
			}
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.munkaFelvetelUtkozes, Toast.LENGTH_LONG).show();
		}
	}
	
	public void confirmedSaveJob() {
		currentMunka.setMunkaBefejezesDate(munkaVegeTextView.getText().toString());
		if (!koltsegEditText.getText().toString().isEmpty()){
			currentMunka.setMunkaKoltseg(Long.parseLong(koltsegEditText.getText().toString()));
		}
		if (!bevetelEditText.getText().toString().isEmpty()) {
			currentMunka.setMunkaBevetel(Long.parseLong(bevetelEditText.getText().toString()));
		}
		if (!commentEditText.getText().toString().isEmpty()) {
			currentMunka.setMunkaComment(commentEditText.getText().toString());	
		}
		currentMunka.setSoforID(sessionManager.getUserID().get(SessionManager.KEY_USER_ID));
			
		munkaDao.update(currentMunka);
		
		finish();
	}
	
	public boolean checkUtkozes() {
		List<Munka> sajatMunkak=munkaDao.queryBuilder().where(Properties.MunkaIsActive.eq(true), Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list();
		if (sajatMunkak.size()==0) {
			return false;
		}
		for (int i=0; i<sajatMunkak.size(); i++) {
			if(currentMunka.getMunkaID()==sajatMunkak.get(i).getMunkaID()) {
				return false;
			}
			if ((((hour + currentMunka.getMunkaEstimatedTime().intValue()) <=	(Integer.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate().substring(0,2))
					-sajatMunkak.get(i).getMunkaEstimatedTime().intValue())) && (hour < (Integer.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate().substring(0,2))
					-sajatMunkak.get(i).getMunkaEstimatedTime().intValue())))
					||
					((hour >= Integer.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate().substring(0,2))) && ((hour + currentMunka.getMunkaEstimatedTime().intValue()) > Integer.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate().substring(0,2))))) {
				return false;
			}
		}
		return true;
	}	

}
