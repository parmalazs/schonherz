package com.schonherz.flottadroid;

import java.util.Locale;

import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.flottadroid.AdminActivity.AdminPagerAdapter;
import com.schonherz.fragments.AdminKepekFragment;
import com.schonherz.fragments.AutoListFragment;
import com.schonherz.fragments.MunkaListFragment;
import com.schonherz.fragments.PartnerListFragment;
import com.schonherz.fragments.SajatMunkaListFragment;
import com.schonherz.fragments.SoforListFragment;
import com.schonherz.fragments.SzabadMunkaListFragment;
import com.schonherz.fragments.TelephelyListFragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JobsActivity extends FragmentActivity implements ActionBar.TabListener {

	// Database Handlers
		private SQLiteDatabase db;
		private DevOpenHelper helper;
		private DaoSession daoSession;
		private DaoMaster daoMaster;

		// Greendao objects
		private MunkaDao munkaDao;

		SzabadMunkaListFragment szabadMunkaListFragment;
		SajatMunkaListFragment sajatMunkaListFragment;		
		MunkaListFragment munkaListFragment;
		

		Tab szabadMunkaTab;
		Tab sajatMunkaTab;
		Tab osszesMunkaTab;

		ActionBar actionBar;
		JobsPagerAdapter jobsAdapter;
		ViewPager jobsPager;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_jobs);
			// Show the Up button in the action bar.
			setupActionBar();

			dataBaseInit();
			
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(3);

			szabadMunkaListFragment=new SzabadMunkaListFragment(this, munkaDao);
			sajatMunkaListFragment=new SajatMunkaListFragment(this, munkaDao);
			munkaListFragment = new MunkaListFragment(this, munkaDao);

			actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			jobsPager = (ViewPager) findViewById(R.id.jobsPager);
			jobsAdapter = new JobsPagerAdapter(getSupportFragmentManager());
			jobsPager.setAdapter(jobsAdapter);
			jobsPager.setCurrentItem(0);

			szabadMunkaTab = actionBar.newTab().setText(R.string.title_section1);
			szabadMunkaTab.setTabListener(this);
			actionBar.addTab(szabadMunkaTab);

			sajatMunkaTab = actionBar.newTab().setText(R.string.title_section2);
			sajatMunkaTab.setTabListener(this);
			actionBar.addTab(sajatMunkaTab);

			/*osszesMunkaTab = actionBar.newTab().setText(R.string.title_section3);
			osszesMunkaTab.setTabListener(this);
			actionBar.addTab(osszesMunkaTab);*/

			jobsPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					getActionBar().setSelectedNavigationItem(position);

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub

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
			
			getMenuInflater().inflate(R.menu.jobs, menu);			
			
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
					NavUtils.navigateUpFromSameTask(this);
					overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
					return true;
					
				case R.id.new_job:
					Intent intentNewJob=new Intent(JobsActivity.this, NewJobActivity.class);
					startActivity(intentNewJob);
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
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
		

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			jobsPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		public class JobsPagerAdapter extends FragmentPagerAdapter {

			public JobsPagerAdapter(FragmentManager fm) {
				super(fm);
				// TODO Auto-generated constructor stub
			}

			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 1 :

						return new SajatMunkaListFragment(JobsActivity.this, munkaDao);
					/*case 2 :
						return new MunkaListFragment(JobsActivity.this, munkaDao);*/
					default :
						return new SzabadMunkaListFragment(JobsActivity.this, munkaDao);
				}
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 2;
			}

		}

		// setup connections to database
		public void dataBaseInit() {
			helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
			db = helper.getWritableDatabase();
			daoMaster = new DaoMaster(db);
			daoSession = daoMaster.newSession();

			munkaDao = daoSession.getMunkaDao();

		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			helper.close();
			finish();
			this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 		
		}

}
