/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.schonherz.flottadroid;

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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.schonherz.flottadroid.R;
;

public class PreviewDemo extends Activity {
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;

	private static final String TAG = "CamTestActivity";
	ImageButton buttonClick;
	TextView photoNubTxt;
	String fileName;
	Activity act;
	Context ctx;
	private int photoNumber = 0;
	String path = null;
	String namePrefix = null;
	ArrayList<String> photos = null;
	LayoutInflater controlInflater = null;

	SharedPreferences pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.preview_camera);

		// getWindow().setFormat(PixelFormat.UNKNOWN);

		preview = (SurfaceView) findViewById(R.id.previewx);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.camera_control,
				null);
		LayoutParams layoutParamsControl = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		ctx = this;
		act = this;

		photoNubTxt = (TextView) findViewById(R.id.photoNumberTxt);

		buttonClick = (ImageButton) findViewById(R.id.buttonClick);

		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				camera.autoFocus(new AutoFocusCallback() {
					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							camera.takePicture(null, null, jpegCallback);
							// camera.stopPreview();
						}
					}
				});
			}
		});

		buttonClick.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				// camera.stopPreview();
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
	public void onResume() {
		super.onResume();

		camera = Camera.open();
		startPreview();
	}

	@Override
	public void onPause() {
		if (inPreview) {
			camera.stopPreview();
		}

		camera.release();
		camera = null;
		inPreview = false;

		super.onPause();
	}

	@Override
	public void finish() {
		if (photos != null) {
			Intent data = new Intent();
			Bundle b = new Bundle();
			// String[] photosStringArray = Arrays.copyOf(photos.toArray(),
			// photos.toArray().length, String[].class);
			// Arrays.asList(photos.toArray().toArray(new
			// String[photos.toArray().length]);

			b.putStringArray("photos", (String[]) photos
					.toArray((String[]) Array.newInstance(String.class,
							photos.size())));

			data.putExtras(b);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}

	private Camera.Size getBestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	private Camera.Size getBestPctureSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPictureSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea > resultArea) {
						result = size;
					}
				}
			}
		}

		return (result);
	}

	private void initPreview(int width, int height) {
		if (camera != null && previewHolder.getSurface() != null) {
			try {
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
				Toast.makeText(PreviewDemo.this, t.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			if (!cameraConfigured) {
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);

				if (size != null) {
					parameters.setPreviewSize(size.width, size.height);
					pref = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					String reqRes = pref.getString("cameraSize", "640x480");
					String[] pSize = reqRes.split("x");
					Size picSize = getBestPctureSize(
							Integer.parseInt(pSize[0]),
							Integer.parseInt(pSize[1]), parameters);
					Toast.makeText(getApplicationContext(),
							Integer.toString(picSize.width), Toast.LENGTH_SHORT)
							.show();
					parameters.setPictureSize(picSize.width, picSize.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}

			}
		}
	}

	private void startPreview() {
		if (cameraConfigured && camera != null) {
			camera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			// no-op -- wait until surfaceChanged()
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};

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
				DateFormat dateForm = new SimpleDateFormat(
						"yyyy.MM.dd-HH.mm.ss.SSS");
				dateForm.setTimeZone(TimeZone.getTimeZone("gmt+1"));
				// Write to SD Card
				String date = dateForm.format(new Date());
				fileName = String.format(path + "%s.jpg", date);
				// fileName = path+photoNumber;
				File file = new File(fileName);
				file.createNewFile();
				outStream = new FileOutputStream(fileName);

				outStream.write(data);
				outStream.close();
				photos.add(fileName);

				photoNubTxt.setText(Integer.toString(photos.size()));

				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);

				// keepPhotoDialog(CameraActivity.this, fileName).show();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Nem all rendelkezesre SD kartya!", Toast.LENGTH_SHORT)
						.show();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Nem all rendelkezesre SD kartya!", Toast.LENGTH_SHORT)
						.show();

			} finally {
				startPreview();
				buttonClick.setEnabled(true);
			}
			Log.d(TAG, "onPictureTaken - jpeg");

		}
	};

	public static AlertDialog keepPhotoDialog(final Activity a,
			final String photoPath) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(a);
		builder.setMessage("Keep photo?");
		builder.setCancelable(false);
		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// final Intent i = new Intent(a, DoFunThingsActivity.class);
				// i.putExtra(DoFunThingsActivity.PHOTO, photo);
				// a.startActivity(i);

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

	/*
	 * //disable the Home key
	 * 
	 * @Override public void onAttachedToWindow() {
	 * this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD); //
	 * super.onAttachedToWindow(); }
	 */

}