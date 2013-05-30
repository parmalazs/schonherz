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
import android.widget.ImageView;
import android.widget.Toast;

import com.schonherz.adapters.ProfilKepImageAdapter;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.SoforDao.Properties;

public class SoforDetailsActivity extends Activity {

	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	private SoforDao soforDao;
	private Sofor currentSofor;

	private ProfilKepDao profilKepDao;
	ArrayList<ProfilKep> profilKepek;

	private int requestCode;
	Button pictureButton;
	File sdcard;
	boolean saveMode = false; // true update, false insert

	Gallery profilPicsGallery;
	ProfilKepImageAdapter imageadapter;

	Button saveButton, dialButton;
	EditText nevEditTetx, cimEditText, telEditText, emailEditText,
			loginEditTetx, passEditText, birthEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sofor_details);
		// Show the Up button in the action bar.
		setupActionBar();

		dataBaseInit();

		profilPicsGallery = (Gallery) findViewById(R.id.soforAdminPicsGallery);
		pictureButton = (Button) findViewById(R.id.soforAdminPhotoBtn);

		saveButton = (Button) findViewById(R.id.buttonSoforSave);
		nevEditTetx = (EditText) findViewById(R.id.editTextSoforNev);
		cimEditText = (EditText) findViewById(R.id.editTextSoforCim);
		telEditText = (EditText) findViewById(R.id.editTextSoforTel);
		emailEditText = (EditText) findViewById(R.id.editTextSoforEmail);
		loginEditTetx = (EditText) findViewById(R.id.editTextSoforLogin);
		passEditText = (EditText) findViewById(R.id.editTextSoforPass);
		birthEditText = (EditText) findViewById(R.id.editTextSoforBirth);
		dialButton = (Button) findViewById(R.id.buttonSoforAdminDial);

		if (getIntent().getLongExtra("selectedSoforID", 0L) != 0L) {
			currentSofor = soforDao
					.queryBuilder()
					.where(Properties.SoforID.eq(getIntent().getLongExtra(
							"selectedSoforID", 0L))).list().get(0);

			List<ProfilKep> profilkepek = profilKepDao
					.queryBuilder()
					.where(com.schonherz.dbentities.ProfilKepDao.Properties.ProfilKepIsActive
							.eq(true),
							com.schonherz.dbentities.ProfilKepDao.Properties.SoforID
									.eq(currentSofor.getSoforID())).list();

			imageadapter = new ProfilKepImageAdapter(SoforDetailsActivity.this,
					profilKepDao, 0, profilkepek);

			profilPicsGallery.setAdapter(imageadapter);

			saveMode = true;
		} else {
			saveMode = false;
			currentSofor = new Sofor();

			currentSofor.setSoforID(soforDao.loadAll()
					.get((int) soforDao.loadAll().size() - 1).getSoforID() + 1);

			dialButton.setEnabled(false);
			dialButton.setVisibility(View.INVISIBLE);
		}

		if (saveMode == false) {
			nevEditTetx.setText("");
			cimEditText.setText("");
			telEditText.setText("");
			emailEditText.setText("");
			loginEditTetx.setText("");
			passEditText.setText("");
			birthEditText.setText("");
		} else {
			nevEditTetx.setText(currentSofor.getSoforNev());
			cimEditText.setText(currentSofor.getSoforCim());
			telEditText.setText(currentSofor.getSoforTelefonszam());
			emailEditText.setText(currentSofor.getSoforEmail());
			loginEditTetx.setText(currentSofor.getSoforLogin());
			passEditText.setText(currentSofor.getSoforPass());
			birthEditText.setText(currentSofor.getSoforBirthDate());
		}

		pictureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sdcard = Environment.getExternalStorageDirectory();
				String photoDirPath = sdcard.getAbsolutePath() + "/"
						+ "FlottaDroid/SoforImages" + "/"
						+ Long.toString(currentSofor.getSoforID()) + "/";
				File dirPa = new File(photoDirPath);
				dirPa.mkdirs();

				String fileName = photoDirPath + "/"
						+ Long.toString(currentSofor.getSoforID()) + "_";

				Intent cameraIntent = new Intent(SoforDetailsActivity.this,
						PreviewDemo.class);
				cameraIntent.putExtra("path", fileName);

				int putDat = 0;
				if (saveMode == true) {
					putDat = currentSofor.getProfilKepList().size();
				}
				cameraIntent.putExtra("photos", putDat);
				startActivityForResult(cameraIntent, requestCode);
			}
		});

		profilPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view,
					final int pos, long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(SoforDetailsActivity.this);
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
												((ProfilKep) parent
														.getItemAtPosition(pos))
														.getProfilKepPath())
												.delete();

										((ProfilKep) parent
												.getItemAtPosition(pos))
												.setProfilKepIsActive(false);
										profilKepDao.update(((ProfilKep) parent
												.getItemAtPosition(pos)));
										((ProfilKep) parent
												.getItemAtPosition(pos))
												.refresh();

										imageadapter.remove(((ProfilKep) parent
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
								SoforDetailsActivity.this);
						builder.setMessage("Biztosan törli?")
								.setPositiveButton("Igen", dialogClickListener)
								.setNegativeButton("Nem", dialogClickListener)
								.show();

						return false;
					}
				});

				dialog.show();
			}
		});

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!nevEditTetx.getText().toString().isEmpty()) {
					if (!loginEditTetx.getText().toString().isEmpty()) {
						if (!passEditText.getText().toString().isEmpty()) {
							saveSofor();
						} else {
							Toast.makeText(getApplicationContext(),
									"Elfelejtett jelszót megadni!",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Elfelejtett login nevet megadni!",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Elfelejtett nevet megadni!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		dialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ currentSofor.getSoforTelefonszam())));
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	public void saveSofor() {
		currentSofor.setSoforNev(nevEditTetx.getText().toString());
		currentSofor.setSoforLogin(loginEditTetx.getText().toString());
		currentSofor.setSoforPass(passEditText.getText().toString());

		if (!cimEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforCim(cimEditText.getText().toString());
		} else {
			currentSofor.setSoforCim("N/A");
		}

		if (!telEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforTelefonszam(telEditText.getText().toString());
		} else {
			currentSofor.setSoforTelefonszam("null");
		}

		if (!emailEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforEmail(emailEditText.getText().toString());
		} else {
			currentSofor.setSoforEmail("null");
		}

		if (!birthEditText.getText().toString().isEmpty()) {
			currentSofor.setSoforBirthDate(birthEditText.getText().toString());
		} else {
			currentSofor.setSoforBirthDate("null");
		}

		if (currentSofor.getSoforID() == 0L) {
			currentSofor.setSoforID(soforDao.loadAll()
					.get(soforDao.loadAll().size() - 1).getSoforID() + 1L);
			currentSofor.setSoforIsActive(true);
			soforDao.insert(currentSofor);
		}

		soforDao.update(currentSofor);
		if (NetworkUtil.checkInternetIsActive(getApplicationContext()) == true) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected void onPostExecute(Boolean result) {

					helper.close();
					finish();

				};

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub

					JSONBuilder builder = new JSONBuilder();
					JSONSender sender = new JSONSender();

					if (saveMode == false) {
						currentSofor.setSoforIsAdmin(false);
						if (currentSofor.getSoforProfilKepID() == null) {
							currentSofor.setSoforProfilKepID(0L);
						}
						JSONObject obj = builder.insertSofor(currentSofor);
						sender.sendJSON(sender.getFlottaUrl(), obj);

					} else {
						if (currentSofor.getSoforProfilKepID() == null) {
							currentSofor.setSoforProfilKepID(0L);
						}
						JSONObject obj = builder.updateSofor(currentSofor);
						sender.sendJSON(sender.getFlottaUrl(), obj);
					}

					return true;
				}

			}.execute();
		} else {

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		DateFormat dateForm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		dateForm.setTimeZone(TimeZone.getTimeZone("gmt+1"));

		if (resultCode == RESULT_OK) {
			if (data.hasExtra("photos")) {
				String[] photos = data.getExtras().getStringArray("photos");

				Long lastID = (long) 0;

				if (profilKepDao.loadAll().size() > 0) {
					lastID = profilKepDao.loadAll()
							.get(profilKepDao.loadAll().size() - 1)
							.getProfilKepID();
				}

				for (String path : photos) {
					lastID = lastID + 1;
					profilKepDao.insert(new ProfilKep(lastID, path, dateForm
							.format(new Date()), false, true, currentSofor
							.getSoforID()));

				}
				
				List<ProfilKep> profilkepek = profilKepDao
						.queryBuilder()
						.where(com.schonherz.dbentities.ProfilKepDao.Properties.ProfilKepIsActive
								.eq(true),
								com.schonherz.dbentities.ProfilKepDao.Properties.SoforID
										.eq(currentSofor.getSoforID())).list();

				imageadapter = new ProfilKepImageAdapter(SoforDetailsActivity.this,
						profilKepDao, 0, profilkepek);

				profilPicsGallery.setAdapter(imageadapter);
				

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

		soforDao = daoSession.getSoforDao();
		profilKepDao = daoSession.getProfilKepDao();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sofor_details, menu);
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
}
