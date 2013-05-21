package com.schonherz.flottadroid;

import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.MunkaDao.Properties;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MunkaDetailsActivity extends Activity {
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	Munka currentMunka;
	MunkaDao munkaDao;
	SessionManager sessionManager;
	
	TextView date;
	TextView uzemanyag;
	EditText comment;
	Button save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_munka_details);
		// Show the Up button in the action bar.
		setupActionBar();
		
		dataBaseInit();
		
		sessionManager=new SessionManager(this);
		
		currentMunka=munkaDao.queryBuilder().
				where(Properties.MunkaID.eq(
						this.getIntent().getLongExtra("currentMunkaID", 0))).list().get(0);
		
		date=(TextView)findViewById(R.id.textViewMunkaDate);
		uzemanyag=(TextView)findViewById(R.id.textViewUzemanyagState);
		comment=(EditText)findViewById(R.id.editTextComment);
		save=(Button)findViewById(R.id.buttonJobSave);
		
		date.setText(currentMunka.getMunkaDate());
		comment.setText(currentMunka.getMunkaComment());
		uzemanyag.setText(currentMunka.getMunkaUzemanyagState().toString());
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveJob();
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
			NavUtils.navigateUpFromSameTask(this);
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		super.onPause();
	}
	
	public void saveJob(){
		
		
	}

}
