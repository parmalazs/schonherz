package com.schonherz.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.schonherz.flottadroid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

public class CameraActivity extends Activity {
	@Override
	public void onBackPressed() {
		//finish();
		super.onBackPressed();

	}

	@Override
	public void finish() {
		if (photos != null) {
			Intent data = new Intent();
			Bundle b = new Bundle();
			
			b.putStringArray("photos", (String[])photos.toArray((String []) Array.newInstance(String.class, photos.size())));
			
			data.putExtras(b);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

	private static final String TAG = "CamTestActivity";
	CamPreview preview;
	ImageButton buttonClick;
	TextView photoNubTxt;
	Camera camera;
	String fileName;
	Activity act;
	Context ctx;
	private int photoNumber = 0;
	String path = null;
	String namePrefix = null;
	ArrayList<String> photos = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;
		act = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.preview);

		preview = new CamPreview(this,
				(SurfaceView) findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);
		
		photoNubTxt = (TextView) findViewById(R.id.photoNumberTxt);	

		buttonClick = (ImageButton) findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				camera.autoFocus(new AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							camera.takePicture(null, null,
									jpegCallback);
							//camera.stopPreview();
						}
					}
				});
			}
		});

		buttonClick.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				//camera.stopPreview();
				return true;
			}
		});

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		path = extras.getString("path");
		photoNumber = extras.getInt("photos");
		if (path != null) {
			photos = new ArrayList<String>();
		}
		
		photoNubTxt.setText(Integer.toString(photos.size()));
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		//preview.mCamera = Camera.open();
		camera = Camera.open();
		preview.setCamera(camera);
	}

	@Override
	protected void onPause() {
		if (camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
	}

	private void resetCam() {
		//camera.startPreview();
		preview.setCamera(camera);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				DateFormat dateForm = new SimpleDateFormat("yyyyMMddHHmmss");
				dateForm.setTimeZone(TimeZone.getTimeZone("gmt+1"));
				// Write to SD Card
				String date = dateForm.format(new Date());
				date.replace(".", "");
				date.replace("-", "");
				fileName = String.format(path + "%s.jpg",
						date);
				// fileName = path+photoNumber;
				File file = new File(fileName); 
				file.createNewFile(); 
				outStream = new FileOutputStream(fileName);

				outStream.write(data);
				outStream.close();
				photos.add(fileName);
				
				photoNubTxt.setText(Integer.toString(photos.size()));

				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);

				//keepPhotoDialog(CameraActivity.this, fileName).show();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpeg");
			preview.mCamera.startPreview();
			buttonClick.setEnabled(true);
		}
	};

	public static AlertDialog keepPhotoDialog(final Activity a,
			final String photoPath) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage("Keep photo?");
		builder.setCancelable(false);
		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				// save to DB
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				final File file = new File(photoPath);
				if (file.exists()) {
					file.delete();

					// greendaoo ellenorzes
				}
				dialog.dismiss();
				// you might want to call startPreview() again at this point
			}
		});
		return builder.create();
	}
	
	//disable the Home key
	@Override
	public void onAttachedToWindow() {
	    this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	    super.onAttachedToWindow();
	}
	
}
