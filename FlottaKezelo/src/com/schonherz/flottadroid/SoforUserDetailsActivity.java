package com.schonherz.flottadroid;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.schonherz.adapters.ProfilKepImageAdapter;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.SoforDao.Properties;

public class SoforUserDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Sofor currentSofor;
	SoforDao soforDao;
	ProfilKepDao profilKepDao;
	
	Gallery soforUserPicsGallery;
	ProfilKepImageAdapter imageadapter;
	
	TextView nevEditText, cimEditText, telEditText, birthEditTetx, emailEditText;
	ImageButton emailButton, dialButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sofor_user_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		currentSofor=soforDao.queryBuilder().where(
				Properties.SoforID.eq(
						getIntent().getLongExtra("selectedSoforID", 0L))).list().get(0);
		
		nevEditText=(TextView)findViewById(R.id.tvSoforNevDATA);
		cimEditText=(TextView)findViewById(R.id.tvSoforCimDATA);
		telEditText=(TextView)findViewById(R.id.tvSoforTelefonDATA);
		birthEditTetx=(TextView)findViewById(R.id.tvSoforBirthDATA);
		emailEditText=(TextView)findViewById(R.id.tvSoforEmailDATA);
		emailButton=(ImageButton)findViewById(R.id.emailSoforImg);
		dialButton=(ImageButton)findViewById(R.id.callerSoforImg);
		
		nevEditText.setText(currentSofor.getSoforNev());
		cimEditText.setText(currentSofor.getSoforCim());
		telEditText.setText(currentSofor.getSoforTelefonszam());
		birthEditTetx.setText(currentSofor.getSoforBirthDate());
		emailEditText.setText(currentSofor.getSoforEmail());
		
		soforUserPicsGallery = (Gallery)findViewById(R.id.soforUserPicsGallery);

		List<ProfilKep> profilkepek = profilKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.ProfilKepDao.Properties.ProfilKepIsActive
						.eq(true),
						com.schonherz.dbentities.ProfilKepDao.Properties.SoforID
								.eq(currentSofor.getSoforID())).list();

		imageadapter = new ProfilKepImageAdapter(SoforUserDetailsActivity.this,
				profilKepDao, 0, profilkepek);

		soforUserPicsGallery.setAdapter(imageadapter);
		
		soforUserPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(SoforUserDetailsActivity.this);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);

				ImageView currProfIv = (ImageView) dialog
						.findViewById(R.id.imgDialogImageView);

				Bitmap bm = BitmapFactory.decodeFile(((ProfilKep) parent
						.getItemAtPosition(pos)).getProfilKepPath());
				currProfIv.setImageBitmap(bm);

				if (((ProfilKep) parent.getItemAtPosition(pos)).getSofor() != null) {
					dialog.setTitle(((ProfilKep) parent.getItemAtPosition(pos))
							.getSofor().getSoforNev());
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
		
		emailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						currentSofor.getSoforEmail().toString());
				startActivity(emailIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		dialButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + currentSofor.getSoforTelefonszam())));
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sofor_user_details, menu);
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
			helper.close();
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
		
		profilKepDao = daoSession.getProfilKepDao();
		soforDao = daoSession.getSoforDao();
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

}
