package com.schonherz.flottadroid;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.schonherz.adapters.PartnerKepImageAdapter;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerDao.Properties;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;

public class PartnerDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	Partner currentPartner;
	PartnerDao partnerDao;

	PartnerKepDao partnerKepDao;

	ImageButton emailImgButton;
	ImageButton webImageButton;
	ImageButton callImageButton;

	private int requestCode;
	Button pictureButton;
	File sdcard;

	Gallery partnerPicsGallery;
	PartnerKepImageAdapter imageadapter;

	EditText nevEditText, cimEditText, telEditText, webEditTetx, emailEditText,
			xEditText, yEditText;
	Button savePartner, webViewPartner, dialButton;

	boolean saveMode = false; // true update, false insert

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_details);
		// Show the Up button in the action bar.
		setupActionBar();

		dataBaseInit();

		partnerPicsGallery = (Gallery) findViewById(R.id.partnerAdminPicsGallery);
		pictureButton = (Button) findViewById(R.id.partnerAdminPhotoBtn);

		nevEditText = (EditText) findViewById(R.id.editTextPartnerNev);
		cimEditText = (EditText) findViewById(R.id.editTextPartnerCim);
		telEditText = (EditText) findViewById(R.id.editTextPartnerTel);
		webEditTetx = (EditText) findViewById(R.id.editTextPartnerWeb);
		emailEditText = (EditText) findViewById(R.id.editTextPartnerEmail);
		xEditText = (EditText) findViewById(R.id.editTextPartnerX);
		yEditText = (EditText) findViewById(R.id.editTextPartnerY);
		savePartner = (Button) findViewById(R.id.buttonNewPartner);
		
		

		emailImgButton = (ImageButton) findViewById(R.id.emailImageButton);
		webImageButton = (ImageButton) findViewById(R.id.webImgButton);
		callImageButton = (ImageButton) findViewById(R.id.callerImageButton);

		if (getIntent().getLongExtra("selectedPartnerID", 0L) != 0L) {
			currentPartner = partnerDao
					.queryBuilder()
					.where(Properties.PartnerID.eq(getIntent().getLongExtra(
							"selectedPartnerID", 0L))).list().get(0);

			List<PartnerKep> partnerkepek = partnerKepDao
					.queryBuilder()
					.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive
							.eq(true),
							com.schonherz.dbentities.PartnerKepDao.Properties.PartnerID
									.eq(currentPartner.getPartnerID())).list();

			imageadapter = new PartnerKepImageAdapter(
					PartnerDetailsActivity.this, partnerKepDao, 0, partnerkepek);

			partnerPicsGallery.setAdapter(imageadapter);

			saveMode = true;
		} else {
			currentPartner = new Partner();
			currentPartner.setPartnerID(0L);

			currentPartner.setPartnerID(partnerDao.loadAll()
					.get(partnerDao.loadAll().size() - 1).getPartnerID() + 1);

			saveMode = false;
			
			emailImgButton.setEnabled(false);
			emailImgButton.setVisibility(View.INVISIBLE);
			webImageButton.setEnabled(false);
			webImageButton.setVisibility(View.INVISIBLE);			
			callImageButton.setEnabled(false);
			callImageButton.setVisibility(View.INVISIBLE);

		}

		if (saveMode == false) {
			nevEditText.setText("");
			cimEditText.setText("");
			telEditText.setText("");
			webEditTetx.setText("");
			emailEditText.setText("");
			xEditText.setText("0F");
			yEditText.setText("0F");
		} else {
			nevEditText.setText(currentPartner.getPartnerNev());
			cimEditText.setText(currentPartner.getPartnerCim());
			telEditText.setText(currentPartner.getPartnerTelefonszam());
			webEditTetx.setText(currentPartner.getPartnerWeboldal());
			emailEditText.setText(currentPartner.getPartnerEmailcim());
			xEditText
					.setText(currentPartner.getPartnerXkoordinata().toString());
			yEditText.setText(currentPartner.getPartnerYkoodinata().toString());
		}

		pictureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sdcard = Environment.getExternalStorageDirectory();
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/SoforImages" + "/"
						+ Long.toString(currentPartner.getPartnerID()) ;
				File dirPa = new File(photoDirPath);
				dirPa.mkdirs();

				String fileName = photoDirPath + "/"
						+ Long.toString(currentPartner.getPartnerID()) + "_";

				Intent cameraIntent = new Intent(PartnerDetailsActivity.this,
						PreviewDemo.class);
				cameraIntent.putExtra("path", fileName);

				int putDat = 0;
				if (saveMode == true) {
					putDat = currentPartner.getPartnerKepList().size();
				}
				cameraIntent.putExtra("photos", putDat);
				startActivityForResult(cameraIntent, requestCode);
			}
		});

		partnerPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view,
					final int pos, long id) {
				// TODO Auto-generated method stub

				final Dialog dialog = new Dialog(PartnerDetailsActivity.this);
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

				currProfIv.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogAl,
									int which) {
								switch (which) {
									case DialogInterface.BUTTON_POSITIVE :
										// s.removePicture(selectedPicture);
										@SuppressWarnings("unused")
										boolean pix = new File(
												((PartnerKep) parent
														.getItemAtPosition(pos))
														.getPartnerKepPath())
												.delete();

										((PartnerKep) parent
												.getItemAtPosition(pos))
												.setPartnerKepIsActive(false);
										partnerKepDao.update(((PartnerKep) parent
												.getItemAtPosition(pos)));
										((PartnerKep) parent
												.getItemAtPosition(pos))
												.refresh();

										imageadapter.remove(((PartnerKep) parent
												.getItemAtPosition(pos)));
										imageadapter.notifyDataSetChanged();

										dialogAl.dismiss();
										dialog.dismiss();

										break;
									case DialogInterface.BUTTON_NEGATIVE :
										dialogAl.dismiss();
										dialog.dismiss();
										break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								PartnerDetailsActivity.this);
						builder.setMessage(R.string.torles)
								.setPositiveButton(R.string.yes, dialogClickListener)
								.setNegativeButton(R.string.no, dialogClickListener)
								.show();

						return false;
					}
				});

				dialog.show();

			}
		});

		emailImgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, currentPartner.getPartnerEmailcim().toString());
				startActivity(emailIntent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		callImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ currentPartner.getPartnerTelefonszam())));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		webImageButton.setOnClickListener(new OnClickListener() {
			
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
		
		savePartner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!nevEditText.getText().toString().isEmpty()) {
					if (!cimEditText.getText().toString().isEmpty()) {
						if (!telEditText.getText().toString().isEmpty()) {
							if (!emailEditText.getText().toString().isEmpty()) {
								if (!xEditText.getText().toString().isEmpty()) {
									if (!yEditText.getText().toString().isEmpty()) {
										if (!webEditTetx.getText().toString().isEmpty()) {
											savePartner();
											
										} else {
											Toast.makeText(getApplicationContext(),
													R.string.forgetweb, Toast.LENGTH_LONG)
													.show();
										}
										
									} else {
										Toast.makeText(getApplicationContext(),
												R.string.forgety, Toast.LENGTH_LONG)
												.show();
									}
									
								} else {
									Toast.makeText(getApplicationContext(),
											R.string.forgetx, Toast.LENGTH_LONG)
											.show();
								}
								
							} else {
								Toast.makeText(getApplicationContext(),
										R.string.forgetemail, Toast.LENGTH_LONG)
										.show();
							}
							
						} else {
							Toast.makeText(getApplicationContext(),
									R.string.forgettel, Toast.LENGTH_LONG)
									.show();
						}
						
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.forgetaddress, Toast.LENGTH_LONG)
								.show();
					}
					
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.forgetname, Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	public void savePartner() {
		currentPartner.setPartnerNev(nevEditText.getText().toString());
		if (!cimEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerCim(cimEditText.getText().toString());
		} else {
			currentPartner.setPartnerCim("null");
		}
		if (!telEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerTelefonszam(telEditText.getText()
					.toString());
		} else {
			currentPartner.setPartnerTelefonszam("null");
		}
		if (!webEditTetx.getText().toString().isEmpty()) {
			currentPartner.setPartnerWeboldal(webEditTetx.getText().toString());
		} else {
			currentPartner.setPartnerWeboldal("null");
		}
		if (!emailEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerEmailcim(emailEditText.getText()
					.toString());
		} else {

			currentPartner.setPartnerEmailcim("null");
		}
		if (!xEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerXkoordinata(Float.parseFloat(xEditText
					.getText().toString()));
		} else {
			currentPartner.setPartnerXkoordinata(0F);
		}
		if (!yEditText.getText().toString().isEmpty()) {
			currentPartner.setPartnerYkoodinata(Float.parseFloat(yEditText
					.getText().toString()));
		} else {
			currentPartner.setPartnerYkoodinata(0F);
		}

		if (currentPartner.getPartnerID() == 0L) {
			currentPartner.setPartnerID(partnerDao.loadAll()
					.get(partnerDao.loadAll().size() - 1).getPartnerID() + 1L);
			currentPartner.setPartnerIsActive(true);
			partnerDao.insert(currentPartner);
		}

		partnerDao.update(currentPartner);

		if (NetworkUtil.checkInternetIsActive(PartnerDetailsActivity.this) == true) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub

					JSONSender sender = new JSONSender();
					JSONBuilder builder = new JSONBuilder();

					if (saveMode == true) {
						JSONObject obj = builder.updatePartner(currentPartner);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					} else {
						JSONObject obj = builder.insertPartner(currentPartner);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}

					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					helper.close();
					finish();
				}

			}.execute();
		}
	}

	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		partnerDao = daoSession.getPartnerDao();
		partnerKepDao = daoSession.getPartnerKepDao();
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
				helper.close();
				finish();
				this.overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
				return true;
				
			case R.id.menu_settings:
				Intent setIntent = new Intent(this,SettingsActivity.class);
				this.startActivity(setIntent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				break;
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

				if (partnerKepDao.loadAll().size() > 0) {
					lastID = partnerKepDao.loadAll()
							.get(partnerKepDao.loadAll().size() - 1)
							.getPartnerKepID();
				}

				for (String path : photos) {
					lastID = lastID + 1;

					partnerKepDao.insert(new PartnerKep(lastID, "", dateForm
							.format(new Date()), false, path, true,
							currentPartner.getPartnerID()));
				}

				List<PartnerKep> partnerkepek = partnerKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive
								.eq(true),
								com.schonherz.dbentities.PartnerKepDao.Properties.PartnerID
										.eq(currentPartner.getPartnerID()))
						.list();

				imageadapter = new PartnerKepImageAdapter(
						PartnerDetailsActivity.this, partnerKepDao, 0,
						partnerkepek);

				partnerPicsGallery.setAdapter(imageadapter);

				Toast.makeText(getApplicationContext(),
						Integer.toString(photos.length) + " "+ getString(R.string.pictureSaved),
						Toast.LENGTH_SHORT).show();

			}
		}

	}

}
