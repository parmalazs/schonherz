package com.schonherz.flottadroid;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.schonherz.adapters.MunkaKepImageAdapter;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.MunkaDao.Properties;
import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaKepDao;

public class MunkaDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	Munka currentMunka;
	MunkaDao munkaDao;
	SessionManager sessionManager;
	MunkaKepDao munkaKepDao;

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
	Gallery munkaGallery;
	MunkaKepImageAdapter adapter;

	private int requestCode;

	String serverUr = "http://flotta.host-ed.me/index.php";

	File sdcard;

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

		sessionManager = new SessionManager(this);

		// az intentbõl kiszedjük a listából kiválasztott munka id-t és ez
		// alapján megkeressük
		currentMunka = munkaDao
				.queryBuilder()
				.where(Properties.MunkaID.eq(this.getIntent().getLongExtra(
						"selectedMunkaID", 0))).list().get(0);

		dateTextView = (TextView) findViewById(R.id.textViewMunkaDate);
		uzemanyagTextView = (TextView) findViewById(R.id.textViewUzemanyagState);
		commentEditText = (EditText) findViewById(R.id.editTextComment);
		saveButton = (Button) findViewById(R.id.buttonJobSave);
		timePickerButton = (Button) findViewById(R.id.buttonTime);
		munkaKezdTextView = (TextView) findViewById(R.id.textViewJobBegin);
		munkaVegeTextView = (TextView) findViewById(R.id.textViewJobEnd);
		koltsegEditText = (EditText) findViewById(R.id.editTextKoltseg);
		bevetelEditText = (EditText) findViewById(R.id.editTextBevetel);
		confirmCheckBox = (CheckBox) findViewById(R.id.checkBoxConfirm);
		imageCreate = (Button) findViewById(R.id.buttonImage);
		munkaGallery = (Gallery) findViewById(R.id.munkaGallery);

		List<MunkaKep> munkakepek = munkaKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaID.eq(currentMunka
						.getMunkaID()),
						com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsActive
								.eq(true)).list();

		adapter = new MunkaKepImageAdapter(MunkaDetailsActivity.this,
				munkaKepDao, 0, munkakepek);

		munkaGallery.setAdapter(adapter);

		dateTextView.setText(currentMunka.getMunkaDate());
		commentEditText.setText(currentMunka.getMunkaComment());
		uzemanyagTextView.setText(currentMunka.getMunkaUzemanyagState()
				.toString());
		munkaVegeTextView.setText(currentMunka.getMunkaBefejezesDate());
		koltsegEditText.setText(currentMunka.getMunkaKoltseg().toString());
		bevetelEditText.setText(currentMunka.getMunkaBevetel().toString());

		// itt beállítom a munka kezdési idejét, amit az elmentett befejezési
		// idõ és a szükséges idõ különbségébõl számolok ki
		if (!currentMunka.getMunkaBefejezesDate().toString().equals("null")) {
			long kezdTmpHour = Long.parseLong(currentMunka
					.getMunkaBefejezesDate().substring(0, 2))
					- currentMunka.getMunkaEstimatedTime();
			String kezdTmpMinute = currentMunka.getMunkaBefejezesDate()
					.substring(3, 5);
			munkaKezdTextView.setText(new StringBuilder()
					.append(pad((int) kezdTmpHour)).append(":")
					.append(kezdTmpMinute));
		}

		munkaGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(MunkaDetailsActivity.this);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);

				ImageView currProfIv = (ImageView) dialog
						.findViewById(R.id.imgDialogImageView);

				Bitmap bm = BitmapFactory.decodeFile(((MunkaKep) parent
						.getItemAtPosition(pos)).getMunkaKepPath());
				currProfIv.setImageBitmap(bm);

				dialog.setTitle(((MunkaKep) parent.getItemAtPosition(pos))
						.getMunkaKepDate());

				currProfIv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				currProfIv.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						return false;
					}
				});

				dialog.show();
			}

		});

		imageCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sdcard = Environment.getExternalStorageDirectory();
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/MunkaImages" + "/"
						+ Long.toString(currentMunka.getMunkaID()) + "/";
				File dirPa = new File(photoDirPath);
				dirPa.mkdirs();

				String fileName = photoDirPath + "/"
						+ Long.toString(currentMunka.getMunkaID()) + "_";

				Intent cameraIntent = new Intent(MunkaDetailsActivity.this,
						PreviewDemo.class);
				cameraIntent.putExtra("path", fileName);
				cameraIntent.putExtra("photos", currentMunka.getMunkaKepList()
						.size());
				startActivityForResult(cameraIntent, requestCode);

			}
		});

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
		this.overridePendingTransition(R.anim.slide_out_right,
				R.anim.slide_in_left);
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
			case TIME_DIALOG_ID :
				return new TimePickerDialog(this, timePickerListener, hour,
						minute, true);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int selectedMinute) {
			// beállítom a visszakapott értékek alapján a kezdést és befejezést
			hour = hourOfDay;
			minute = selectedMinute;

			munkaKezdTextView.setText(new StringBuilder().append(pad(hour))
					.append(":").append(pad(minute)));
			munkaVegeTextView.setText(new StringBuilder()
					.append(pad(hour
							+ currentMunka.getMunkaEstimatedTime().intValue()))
					.append(":").append(pad(minute)));
		}
	};

	// ez azért kell, hogy ha egy számjegyû az óra vagy a perc akkor kiegészíti
	// egy 0-val
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
			case android.R.id.home :
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				finish();
				this.overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		DateFormat dateForm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		dateForm.setTimeZone(TimeZone.getTimeZone("gmt+1"));

		if (resultCode == RESULT_OK) {
			if (data.hasExtra("photos")) {
				String[] photos = data.getExtras().getStringArray("photos");

				Long lastID = (long) 0;

				if (munkaKepDao.loadAll().size() > 0) {
					lastID = munkaKepDao.loadAll()
							.get((int) (munkaKepDao.loadAll().size() - 1))
							.getMunkaKepID();
				}
				for (String path : photos) {
					lastID = lastID + 1;
					munkaKepDao.insert(new MunkaKep(lastID, path, dateForm
							.format(new Date()), false, true, currentMunka
							.getMunkaID()));
				}

				adapter.clear();
				munkaDao.refresh(currentMunka);
				List<MunkaKep> munkakepek = munkaKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaID.eq(currentMunka
								.getMunkaID()),
								com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsActive
										.eq(true)).list();
				
				adapter.addAll(munkakepek);

				adapter.notifyDataSetChanged();

				Toast.makeText(getApplicationContext(),
						Integer.toString(photos.length) + "  kép mentve!",
						Toast.LENGTH_SHORT).show();

			}
		}

	}

	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		munkaDao = daoSession.getMunkaDao();
		munkaKepDao = daoSession.getMunkaKepDao();
	}

	public void saveJob() {

		if (!checkUtkozes()) {
			if (!munkaVegeTextView.getText().equals("null")) {
				if (confirmCheckBox.isChecked()) {
					// ide még kéne egy alert dialog, de valamiért mindig
					// megjelenik és bezáródik...
					currentMunka.setMunkaIsActive(false);
					confirmedSaveJob();
				} else {
					confirmedSaveJob();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						R.string.munkaFelvetelNullTime, Toast.LENGTH_LONG)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					R.string.munkaFelvetelUtkozes, Toast.LENGTH_LONG).show();
		}
	}

	public void confirmedSaveJob() {
		currentMunka.setMunkaBefejezesDate(munkaVegeTextView.getText()
				.toString());
		if (!koltsegEditText.getText().toString().isEmpty()) {
			currentMunka.setMunkaKoltseg(Long.parseLong(koltsegEditText
					.getText().toString()));
		}
		if (!bevetelEditText.getText().toString().isEmpty()) {
			currentMunka.setMunkaBevetel(Long.parseLong(bevetelEditText
					.getText().toString()));
		}
		if (!commentEditText.getText().toString().isEmpty()) {
			currentMunka.setMunkaComment(commentEditText.getText().toString());
		}
		currentMunka.setSoforID(sessionManager.getUserID().get(
				SessionManager.KEY_USER_ID));

		munkaDao.update(currentMunka);

		if(NetworkUtil.checkInternetIsActive(MunkaDetailsActivity.this)==true)
		{
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPostExecute(Boolean result) {

				Toast.makeText(MunkaDetailsActivity.this, R.string.refreshed,
						Toast.LENGTH_SHORT);
				finish();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub

				JSONSender sender = new JSONSender();
				JSONBuilder builder = new JSONBuilder();
				JSONObject obj = builder.updateMunka(currentMunka);
				sender.sendJSON(sender.getFlottaUrl(), obj);

				return true;
			}
		}.execute();
		}
		else
		{
			finish();
		}
	}

	public boolean checkUtkozes() {
		List<Munka> sajatMunkak = munkaDao
				.queryBuilder()
				.where(Properties.MunkaIsActive.eq(true),
						Properties.SoforID.eq(sessionManager.getUserID().get(
								SessionManager.KEY_USER_ID))).list();
		if (sajatMunkak.size() == 0) {
			return false;
		}
		for (int i = 0; i < sajatMunkak.size(); i++) {
			if (currentMunka.getMunkaID() == sajatMunkak.get(i).getMunkaID()) {
				return false;
			}
			if ((((hour + currentMunka.getMunkaEstimatedTime().intValue()) <= (Integer
					.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate()
							.substring(0, 2)) - sajatMunkak.get(i)
					.getMunkaEstimatedTime().intValue())) && (hour < (Integer
					.parseInt(sajatMunkak.get(i).getMunkaBefejezesDate()
							.substring(0, 2)) - sajatMunkak.get(i)
					.getMunkaEstimatedTime().intValue())))
					|| ((hour >= Integer.parseInt(sajatMunkak.get(i)
							.getMunkaBefejezesDate().substring(0, 2))) && ((hour + currentMunka
							.getMunkaEstimatedTime().intValue()) > Integer
							.parseInt(sajatMunkak.get(i)
									.getMunkaBefejezesDate().substring(0, 2))))) {
				return false;
			}
		}
		return true;
	}

}
