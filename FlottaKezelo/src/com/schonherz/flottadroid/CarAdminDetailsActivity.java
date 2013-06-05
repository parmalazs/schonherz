package com.schonherz.flottadroid;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.schonherz.adapters.AutoKepImageAdapter;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoDao.Properties;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;

public class CarAdminDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	Auto currentAuto;
	AutoDao autoDao;
	TelephelyDao telephelyDao;

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
	private int requestCode;
	Button pictureButton;
	File sdcard;
	Spinner telephelySpinner;
	private ArrayList<String> telephelyNevek;
	ArrayList<Telephely> telephelyek;
	boolean saveMode = false; // true update, false insert

	AutoKepImageAdapter autoKepImgAdapter;
	AutoKepDao autoKepDao;

	Gallery autopicsGallery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_admin_details);
		// Show the Up button in the action bar.
		setupActionBar();

		dataBaseInit();

		autopicsGallery = (Gallery) findViewById(R.id.carAdminPicsGallery);

		if (getIntent().getLongExtra("selectedAutoID", 0L) != 0L) {
			currentAuto = autoDao
					.queryBuilder()
					.where(Properties.AutoID.eq(getIntent().getLongExtra(
							"selectedAutoID", 0L))).list().get(0);
			List<AutoKep> autoKepek = autoKepDao
					.queryBuilder()
					.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive
							.eq(true),
							com.schonherz.dbentities.AutoKepDao.Properties.AutoID
									.eq(currentAuto.getAutoID())).list();

			autoKepImgAdapter = new AutoKepImageAdapter(
					CarAdminDetailsActivity.this, autoKepDao, 0, autoKepek);
			autopicsGallery.setAdapter(autoKepImgAdapter);
			saveMode = true;
		} else {
			currentAuto = new Auto();
			currentAuto.setAutoID(0L);
			currentAuto.setAutoID(autoDao.loadAll()
					.get(autoDao.loadAll().size() - 1).getAutoID() + 1);

			saveMode = false;
		}

		autoTipusEditText = (EditText) findViewById(R.id.editTextAutoTipus);
		autoNevEditText = (EditText) findViewById(R.id.editTextAutoNev);
		autoRendszamEditText = (EditText) findViewById(R.id.editTextAutoRendszam);
		autoKmEditText = (EditText) findViewById(R.id.editTextAutoKm);
		autoUzemanyagEditText = (EditText) findViewById(R.id.editTextAutoUzemanyag);
		autoMuszakiVizsgaDateEditText = (EditText) findViewById(R.id.editTextAutoMuszaki);
		autoLastServiceDateEditText = (EditText) findViewById(R.id.editTextAutoUtolsoSzerviz);
		saveButton = (Button) findViewById(R.id.lefoglalButton);
		telephelySpinner = (Spinner) findViewById(R.id.spinnerLastTelephely);

		pictureButton = (Button) findViewById(R.id.carAdminPhotoBtn);

		pictureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sdcard = Environment.getExternalStorageDirectory();
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/AutoImages" + "/"
						+ Long.toString(currentAuto.getAutoID()) ;
				File dirPa = new File(photoDirPath);
				dirPa.mkdirs();

				String fileName = photoDirPath + "/"
						+ Long.toString(currentAuto.getAutoID()) + "_";

				Intent cameraIntent = new Intent(CarAdminDetailsActivity.this,
						PreviewDemo.class);
				cameraIntent.putExtra("path", fileName);
				
				int putDat = 0;
				if(saveMode == true)
				{
					putDat = currentAuto.getAutoKepList()
							.size();
				}
				cameraIntent.putExtra("photos", putDat);
				startActivityForResult(cameraIntent, requestCode);
			}
		});

		//
		if (telephelyDao.loadAll().size() != 0) {
			telephelyek = new ArrayList<Telephely>(
					telephelyDao
							.queryBuilder()
							.orderAsc(
									com.schonherz.dbentities.TelephelyDao.Properties.TelephelyNev)
							.list());

			telephelyNevek = new ArrayList<String>();

			for (int i = 0; i < telephelyek.size(); i++) {
				telephelyNevek.add(telephelyek.get(i).getTelephelyNev());
			}
			ArrayAdapter<String> munkaTipusAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, telephelyNevek);
			telephelySpinner.setAdapter(munkaTipusAdapter);
		}

		if (currentAuto.getAutoID() == 0L) {
			autoTipusEditText.setText("null");
			autoNevEditText.setText("null");
			autoRendszamEditText.setText("null");
			autoKmEditText.setText("0");
			autoUzemanyagEditText.setText("0");
			autoMuszakiVizsgaDateEditText.setText("null");
			autoLastServiceDateEditText.setText("null");
		} else {

			if (saveMode != false) {
				autoTipusEditText.setText(currentAuto.getAutoTipus());
				autoNevEditText.setText(currentAuto.getAutoNev());
				autoRendszamEditText.setText(currentAuto.getAutoRendszam());
				autoKmEditText.setText(currentAuto.getAutoKilometerOra()
						.toString());
				autoUzemanyagEditText.setText(currentAuto.getAutoUzemanyag()
						.toString());
				autoMuszakiVizsgaDateEditText.setText(currentAuto
						.getAutoMuszakiVizsgaDate());
				autoLastServiceDateEditText.setText(currentAuto
						.getAutoLastSzervizDate());
			}
		}
		
		autopicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int pos,
					long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(CarAdminDetailsActivity.this);
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
			    
			    currProfIv.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogAl, int which) {
								switch (which) {
									case DialogInterface.BUTTON_POSITIVE :
										//s.removePicture(selectedPicture);
										@SuppressWarnings("unused")
										boolean pix = new File(((AutoKep)parent.getItemAtPosition(pos)).getAutoKepPath()).delete();
									
										((AutoKep)parent.getItemAtPosition(pos)).setAutoKepIsActive(false);
										autoKepDao.update(((AutoKep)parent.getItemAtPosition(pos)));
										((AutoKep)parent.getItemAtPosition(pos)).refresh();
										
										autoKepImgAdapter.remove(((AutoKep)parent.getItemAtPosition(pos)));
										autoKepImgAdapter.notifyDataSetChanged();
										
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
								CarAdminDetailsActivity.this);
						builder.setMessage(R.string.promptDel)
								.setPositiveButton(R.string.yes,
										dialogClickListener)
								.setNegativeButton(R.string.no, dialogClickListener)
								.show();

						
						return false;
					}
				});
			    				   
			    dialog.show();
			}
			
			
		});
		
		saveButton.setOnClickListener(new OnClickListener() {
			EditText autoLastTelephelyEditText;
			EditText autoLastSoforNevEditText;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!autoNevEditText.getText().toString().isEmpty()) {
					if (!autoTipusEditText.getText().toString().isEmpty()) {
						if (!autoRendszamEditText.getText().toString()
								.isEmpty()) {
							if (!autoMuszakiVizsgaDateEditText.getText()
									.toString().isEmpty()) {
								if (!autoKmEditText.getText()
										.toString().isEmpty()) {
									if (!autoUzemanyagEditText.getText()
											.toString().isEmpty()) {
										if (!autoLastServiceDateEditText.getText()
												.toString().isEmpty()) {
											if (!autoLastTelephelyEditText.getText()
													.toString().isEmpty()) {
												if (!autoLastSoforNevEditText.getText()
														.toString().isEmpty()) {
													saveCar();
													
												} else {
													Toast.makeText(
															getApplicationContext(),
															R.string.forgetdriver,
															Toast.LENGTH_LONG).show();
												}
												
											} else {
												Toast.makeText(
														getApplicationContext(),
														R.string.forgettelephely,
														Toast.LENGTH_LONG).show();
											}
											
										} else {
											Toast.makeText(
													getApplicationContext(),
													R.string.forgetlastszerviz,
													Toast.LENGTH_LONG).show();
										}
										
									} else {
										Toast.makeText(
												getApplicationContext(),
												R.string.forgetuzemanyag,
												Toast.LENGTH_LONG).show();
									}
									
								} else {
									Toast.makeText(
											getApplicationContext(),
											R.string.forgetkm,
											Toast.LENGTH_LONG).show();
								}
								
							} else {
								Toast.makeText(
										getApplicationContext(),
										R.string.forgetmuszvizsga,
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getApplicationContext(),
									R.string.forgetrendszam,
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								R.string.forgetmunkatipus,
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							R.string.forgetnev, Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	public void saveCar() {
		currentAuto.setAutoNev(autoNevEditText.getText().toString());
		currentAuto.setAutoTipus(autoTipusEditText.getText().toString());
		currentAuto.setAutoRendszam(autoRendszamEditText.getText().toString());
		currentAuto.setAutoMuszakiVizsgaDate(autoMuszakiVizsgaDateEditText
				.getText().toString());

		currentAuto.setAutoLastTelephelyID(telephelyek.get(
				telephelySpinner.getSelectedItemPosition()).getTelephelyID());

		if (!autoKmEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoKilometerOra(Long.parseLong(autoKmEditText
					.getText().toString()));
		} else {
			currentAuto.setAutoKilometerOra(0L);
		}

		if (!autoUzemanyagEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoUzemanyag(Long.parseLong(autoUzemanyagEditText
					.getText().toString()));
		} else {
			currentAuto.setAutoUzemanyag(0L);
		}

		if (!autoLastServiceDateEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastSzervizDate(autoLastServiceDateEditText
					.getText().toString());
		} else {
			currentAuto.setAutoLastSzervizDate("null");
		}

		if (!autoLastTelephelyEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastTelephelyID(Long
					.parseLong(autoLastTelephelyEditText.getText().toString()));
		} else {
			currentAuto.setAutoLastTelephelyID(0L);
		}

		if (!autoLastSoforNevEditText.getText().toString().isEmpty()) {
			currentAuto.setAutoLastSoforID(Long
					.parseLong(autoLastSoforNevEditText.getText().toString()));
		} else {
			currentAuto.setAutoLastSoforID(0L);
		}

		if (currentAuto.getAutoID() == 0L) {
			currentAuto.setAutoID(autoDao.loadAll()
					.get(autoDao.loadAll().size() - 1).getAutoID() + 1L);
			currentAuto.setAutoIsActive(true);
			currentAuto.setAutoFoglalt(false);
			currentAuto.setAutoProfilKepID(0L);
			currentAuto.setAutoLastUpDate("null");
			currentAuto.setAutoXkoordinata(0F);
			currentAuto.setAutoYkoordinata(0F);
			autoDao.insert(currentAuto);

		}

		autoDao.update(currentAuto);

		if (NetworkUtil.checkInternetIsActive(CarAdminDetailsActivity.this) == true) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPostExecute(Boolean result) {
					
					helper.close();
					finish();
					super.onPostExecute(result);
				};

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub

					JSONBuilder builder = new JSONBuilder();
					JSONSender sender = new JSONSender();

					if (saveMode == true) {
						JSONObject obj = builder.updateAuto(currentAuto);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					} else {
						JSONObject obj = builder.insertAuto(currentAuto);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}

					return true;
				}
			}.execute();
		} else {
			helper.close();
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

	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		telephelyDao = daoSession.getTelephelyDao();
		autoDao = daoSession.getAutoDao();
		autoKepDao = daoSession.getAutoKepDao();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		DateFormat dateForm = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		dateForm.setTimeZone(TimeZone.getTimeZone("gmt+1"));

		if (resultCode == RESULT_OK) {
			if (data.hasExtra("photos")) {
				String[] photos = data.getExtras().getStringArray("photos");

				Long lastID = (long) 0;

				if (autoKepDao.loadAll().size() > 0) {
					lastID = autoKepDao.loadAll()
							.get((int) autoKepDao.loadAll().size() - 1)
							.getAutoKepID();
				}

				for (String path : photos) {

					lastID = lastID + 1;

					autoKepDao.insert(new AutoKep(lastID, path, dateForm
							.format(new Date()), false, true, currentAuto
							.getAutoID()));
				}

				List<AutoKep> autopics = autoKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive
								.eq(true),
								com.schonherz.dbentities.AutoKepDao.Properties.AutoID
										.eq(currentAuto.getAutoID())).list();

				autoKepImgAdapter = new AutoKepImageAdapter(
						CarAdminDetailsActivity.this, autoKepDao, 0, autopics);

				autopicsGallery.setAdapter(autoKepImgAdapter);

				Toast.makeText(getApplicationContext(),
						Integer.toString(photos.length) + " " + getString(R.string.pictureSaved),
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
