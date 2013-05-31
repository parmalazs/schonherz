package com.schonherz.flottadroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.schonherz.adapters.AutoKepImageAdapter;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.classes.StaticGoogleMapImageUtil;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoDao.Properties;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.TelephelyDao;

public class CarDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	private SessionManager sessionManager;
	boolean denided = false;

	Auto currentAuto;
	Auto vaneAutoja;
	AutoDao autoDao;
	SoforDao soforDao;
	TelephelyDao telephelyDao;
	Sofor sofor;
	
	AutoKepDao autoKepDao;
	AutoKepImageAdapter imageadapter;
	
	Gallery autoPicsGallery;
	
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
	// TextView autoProfilKepTextView;
	Button lefoglalButton;
	Button imageCreate;
	ImageView autoMapsImageView;
	boolean saveMode = false; // false = insert, true = update

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_details);
		// Show the Up button in the action bar.
		setupActionBar();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		notificationManager.cancel(2);

		dataBaseInit();
		sessionManager = new SessionManager(this);

		if (getIntent().getLongExtra("selectedAutoID", 0L) != 0L) {
			currentAuto = autoDao
					.queryBuilder()
					.where(Properties.AutoID.eq(getIntent().getLongExtra(
							"selectedAutoID", 0L))).list().get(0);

			saveMode = true;
		} else {
			currentAuto = new Auto();
			currentAuto.setAutoID(0L);

			saveMode = false;
		}

		autoTipusTextView = (TextView) findViewById(R.id.autoTipusTextView);
		autoNevTextView = (TextView) findViewById(R.id.autoNevTextView);
		autoRendszamTextView = (TextView) findViewById(R.id.autoRendszamTextView);
		autoKmTextView = (TextView) findViewById(R.id.autoKmTextView);
		autoUzemanyagTextView = (TextView) findViewById(R.id.autoUzemanyagTextView);
		autoMuszakiVizsgaDateTextView = (TextView) findViewById(R.id.autoMuszakiVizsgaDateTextView);
		autoLastServiceDateTextView = (TextView) findViewById(R.id.autoLastServiceDateTextView);
		autoLastUpDateTextView = (TextView) findViewById(R.id.autoLastUpDateTextView);
		autoLastTelephelyTextView = (TextView) findViewById(R.id.autoLastTelephelyTextView);
		autoLastSoforNevTextView = (TextView) findViewById(R.id.autoLastSoforNevTextView);
		autoMapsImageView = (ImageView) findViewById(R.id.autoMapImageView);
		
		autoPicsGallery = (Gallery)findViewById(R.id.carUserPicsGallery);
		
		List<AutoKep> autoKepek = autoKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive
						.eq(true),
						com.schonherz.dbentities.AutoKepDao.Properties.AutoID
								.eq(currentAuto.getAutoID())).list();

		imageadapter = new AutoKepImageAdapter(
				CarDetailsActivity.this, autoKepDao, 0, autoKepek);
		autoPicsGallery.setAdapter(imageadapter);
		
		autoPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(CarDetailsActivity.this);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);
								   
			    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
			    
			    Bitmap bm = BitmapFactory.decodeFile(((AutoKep)parent.getItemAtPosition(pos)).getAutoKepPath());
			    currProfIv.setImageBitmap(bm);
			    
			    if(((AutoKep)parent.getItemAtPosition(pos)).getAuto()!=null)
			    {
			    dialog.setTitle(((AutoKep)parent.getItemAtPosition(pos)).getAuto().getAutoRendszam());				    
			    }
			    currProfIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			    
			    dialog.show();
			}
		});
		
		if (currentAuto.getAutoID() == 0L) {
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
		} else {

			autoTipusTextView.setText("T�pus: " + currentAuto.getAutoTipus());
			autoNevTextView.setText("N�v: " + currentAuto.getAutoNev());
			autoRendszamTextView.setText("Rendsz�m: "
					+ currentAuto.getAutoRendszam());
			autoKmTextView.setText("Km �ra: "
					+ currentAuto.getAutoKilometerOra());
			autoUzemanyagTextView.setText("�zemanyag: "
					+ currentAuto.getAutoUzemanyag());
			autoMuszakiVizsgaDateTextView.setText("M�szaki vizsga: "
					+ currentAuto.getAutoMuszakiVizsgaDate());
			autoLastServiceDateTextView.setText("Utols� szerv�z: "
					+ currentAuto.getAutoLastSzervizDate());
			autoLastUpDateTextView.setText("Utols� v�ltoz�s: "
					+ currentAuto.getAutoLastUpDate());
			// nem megy vmi�rt!
			// ArrayList<Telephely> telephelylist = new
			// ArrayList<Telephely>(telephelyDao.queryBuilder().where(com.schonherz.dbentities.TelephelyDao.Properties.TelephelyID.eq(currentAuto.getAutoLastTelephelyID())).list());
			// autoLastTelephelyTextView.setText("Utols� telephely: " +
			// telephelylist.get(0).getTelephelyNev());
			autoLastTelephelyTextView.setText("Utols� telephely: "
					+ currentAuto.getAutoLastTelephelyID());
			ArrayList<Sofor> soforlist = new ArrayList<Sofor>(soforDao
					.queryBuilder()
					.where(com.schonherz.dbentities.SoforDao.Properties.SoforID
							.eq(currentAuto.getAutoLastSoforID())).list());
			autoLastSoforNevTextView.setText("Utols� sof�r: "
					+ soforlist.get(0).getSoforNev());

			if (NetworkUtil.checkInternetIsActive(CarDetailsActivity.this)) {
				new AsyncTask<Void, Void, Bitmap>() {

					@Override
					protected void onPostExecute(Bitmap result) {
						// TODO Auto-generated method stub

						autoMapsImageView.setImageBitmap(result);

						super.onPostExecute(result);
					}

					@Override
					protected Bitmap doInBackground(Void... params) {
						// TODO Auto-generated method stub

						StaticGoogleMapImageUtil mapUtil = new StaticGoogleMapImageUtil();

						return mapUtil.getGoogleMapThumbnail(
								(double) currentAuto.getAutoXkoordinata(),
								(double) currentAuto.getAutoYkoordinata());
					}

				}.execute();
			}
		}

		lefoglalButton = (Button) findViewById(R.id.lefoglalButton);

		Log.i(CarDetailsActivity.class.getName(), "Saj�t aut�: "
				+ getIntent().getBooleanExtra("sajatAuto", false));
		// ha saj�t aut�t jelen�t�nk meg akkor �t�rjuk a lefoglal�st lead�sra
		if (getIntent().getBooleanExtra("sajatAuto", false)) {
			lefoglalButton.setText("Lead");
		}
		lefoglalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// ha nem saj�t aut� akkor elmentj�k
				if (!getIntent().getBooleanExtra("sajatAuto", false)) {
					// ellen�rz�s, hogy van-e �ltala foglalva aut�!
					ArrayList<Auto> vaneAutoja = new ArrayList<Auto>(
							autoDao.queryBuilder()
									.where(com.schonherz.dbentities.AutoDao.Properties.AutoLastSoforID
											.eq(sessionManager.getUserID().get(
													SessionManager.KEY_USER_ID)),
											com.schonherz.dbentities.AutoDao.Properties.AutoFoglalt
													.eq(true)).list());

					if (vaneAutoja.size() == 0) {
						currentAuto.setAutoFoglalt(true);
						currentAuto.setAutoLastSoforID(sessionManager
								.getUserID().get(SessionManager.KEY_USER_ID));
						currentAuto.setAutoUzemanyag(100L);
						Log.i(CarDetailsActivity.class.getName(),
								"v�lasztott auto ID: "
										+ currentAuto.getAutoID().toString()
										+ " sof�r: "
										+ sessionManager
												.getUserID()
												.get(SessionManager.KEY_USER_ID)
												.toString());
						autoDao.update(currentAuto);

						new sendAutoData().execute();

					} else {
						// vmt feldobni
						Toast.makeText(getApplicationContext(),
								R.string.denided_autofelvetel,
								Toast.LENGTH_LONG).show();
					}
				}
				// ha saj�t aut� akkor leadjuk
				else {
					currentAuto.setAutoFoglalt(false);
					autoDao.update(currentAuto);
					new sendAutoData().execute();
				}
			}
		});
	}
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		
		autoKepDao = daoSession.getAutoKepDao();
		autoDao = daoSession.getAutoDao();
		soforDao = daoSession.getSoforDao();
		telephelyDao = daoSession.getTelephelyDao();
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
		getMenuInflater().inflate(R.menu.auto_details, menu);
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
				helper.close();
				finish();
				this.overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
				return true;
		}
		return super.onOptionsItemSelected(item);
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

	public class sendAutoData extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			finish();
			super.onPostExecute(result);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			if (NetworkUtil.checkInternetIsActive(CarDetailsActivity.this) == true) {
				JSONBuilder builder = new JSONBuilder();
				JSONSender sender = new JSONSender();
				JSONObject obj = builder.updateAuto(currentAuto);
				sender.sendJSON(sender.getFlottaUrl(), obj);
			}
			return true;
		}

	}
}
