package com.schonherz.flottadroid;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.schonherz.adapters.PartnerKepImageAdapter;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.StaticGoogleMapImageUtil;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerDao.Properties;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;

public class PartnerUserDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	Partner currentPartner;
	PartnerDao partnerDao;
	PartnerKepDao partnerKepDao;

	Gallery partnerPicsGallery;
	PartnerKepImageAdapter imageadapter;

	TextView nevEditText, cimEditText, telEditText, webEditTetx, emailEditText,
			xEditText, yEditText;
	ImageButton webViewPartner, dialButton, emailPartnerButton;

	ImageView partnerMapImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_user_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		dataBaseInit();

		currentPartner = partnerDao
				.queryBuilder()
				.where(Properties.PartnerID.eq(getIntent().getLongExtra(
						"selectedPartnerID", 0L))).list().get(0);

		nevEditText = (TextView) findViewById(R.id.tvPartnerNevDATA);
		cimEditText = (TextView) findViewById(R.id.tvPartnerCimDATA);
		telEditText = (TextView) findViewById(R.id.tvPartnerTelefonDATA);
		webEditTetx = (TextView) findViewById(R.id.tvPartnerWebDATA);
		emailEditText = (TextView) findViewById(R.id.tvPartnerEmailDATA);
		xEditText = (TextView) findViewById(R.id.tvPartnerXDATA);
		yEditText = (TextView) findViewById(R.id.tvPartnerYDATA);

		webViewPartner = (ImageButton) findViewById(R.id.webPartnerImg);
		dialButton = (ImageButton) findViewById(R.id.callerPartnerImg);
		emailPartnerButton = (ImageButton) findViewById(R.id.emailPartnerImg);

		partnerMapImageView = (ImageView) findViewById(R.id.partnerMapImageView);

		if (NetworkUtil.checkInternetIsActive(PartnerUserDetailsActivity.this)) {
			new AsyncTask<Void, Void, Bitmap>() {

				@Override
				protected void onPostExecute(Bitmap result) {
					// TODO Auto-generated method stub

					partnerMapImageView.setImageBitmap(result);

					super.onPostExecute(result);
				}

				@Override
				protected Bitmap doInBackground(Void... params) {
					// TODO Auto-generated method stub

					StaticGoogleMapImageUtil mapUtil = new StaticGoogleMapImageUtil();

					return mapUtil.getGoogleMapThumbnail(
							(double) currentPartner.getPartnerXkoordinata(),
							(double) currentPartner.getPartnerYkoodinata());
				}

			}.execute();
		}

		partnerPicsGallery = (Gallery) findViewById(R.id.partnerUserPicsGallery);

		List<PartnerKep> partnerkepek = partnerKepDao
				.queryBuilder()
				.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive
						.eq(true),
						com.schonherz.dbentities.PartnerKepDao.Properties.PartnerID
								.eq(currentPartner.getPartnerID())).list();

		imageadapter = new PartnerKepImageAdapter(
				PartnerUserDetailsActivity.this, partnerKepDao, 0, partnerkepek);

		partnerPicsGallery.setAdapter(imageadapter);

		partnerPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(
						PartnerUserDetailsActivity.this);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);

				ImageView currProfIv = (ImageView) dialog
						.findViewById(R.id.imgDialogImageView);

				Bitmap bm = BitmapFactory.decodeFile(((PartnerKep) parent
						.getItemAtPosition(pos)).getPartnerKepPath());
				currProfIv.setImageBitmap(bm);

				if (((PartnerKep) parent.getItemAtPosition(pos)).getPartner() != null) {
					dialog.setTitle(((PartnerKep) parent.getItemAtPosition(pos))
							.getPartner().getPartnerNev());
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

		nevEditText.setText(currentPartner.getPartnerNev());
		cimEditText.setText(currentPartner.getPartnerCim());
		telEditText.setText(currentPartner.getPartnerTelefonszam());
		webEditTetx.setText(currentPartner.getPartnerWeboldal());
		emailEditText.setText(currentPartner.getPartnerEmailcim());
		xEditText.setText(currentPartner.getPartnerXkoordinata().toString());
		yEditText.setText(currentPartner.getPartnerYkoodinata().toString());

		webViewPartner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent partnerWebViewIntent = new Intent(
						getApplicationContext(), PartnerWebViewActivity.class);
				partnerWebViewIntent.putExtra("currentWebPage",
						currentPartner.getPartnerWeboldal());
				startActivity(partnerWebViewIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		dialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ currentPartner.getPartnerTelefonszam())));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		emailPartnerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent emailIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						currentPartner.getPartnerEmailcim().toString());
				startActivity(emailIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		partnerKepDao = daoSession.getPartnerKepDao();
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right,
				R.anim.slide_in_left);
	}

}
