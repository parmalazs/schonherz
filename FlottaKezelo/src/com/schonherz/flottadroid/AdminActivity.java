package com.schonherz.flottadroid;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
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
import com.schonherz.fragments.AdminKepekFragment;
import com.schonherz.fragments.AutoListFragment;
import com.schonherz.fragments.MunkaListFragment;
import com.schonherz.fragments.PartnerListFragment;
import com.schonherz.fragments.SoforListFragment;
import com.schonherz.fragments.TelephelyListFragment;

public class AdminActivity extends FragmentActivity
		implements
			ActionBar.TabListener {

	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	// Greendao objects
	private AutoDao autoDao;
	private AutoKepDao autoKepDao;
	private MunkaDao munkaDao;
	private MunkaEszkozDao munkaEszkozDao;
	private MunkaKepDao munkaKepDao;
	private MunkaTipusDao munkaTipusDao;
	private PartnerDao partnerDao;
	private PartnerKepDao partnerKepDao;
	private ProfilKepDao profilKepDao;
	private SoforDao soforDao;
	private TelephelyDao telephelyDao;

	SoforListFragment soforListFragment;
	MunkaListFragment munkaListFragment;
	PartnerListFragment partnerListFragment;

	Tab munkaTab;
	Tab soforTab;
	Tab partnerTab;
	Tab telephelyTab;
	Tab autoTab;
	Tab kepekTab;

	ActionBar actionBar;
	AdminPagerAdapter adminAdapter;
	ViewPager adminPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		// Show the Up button in the action bar.
		setupActionBar();

		dataBaseInit();

		soforListFragment = new SoforListFragment(this, soforDao);
		munkaListFragment = new MunkaListFragment(this, munkaDao);
		partnerListFragment=new PartnerListFragment(this, partnerDao);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		adminPager = (ViewPager) findViewById(R.id.adminPager);
		adminAdapter = new AdminPagerAdapter(getSupportFragmentManager());
		adminPager.setAdapter(adminAdapter);
		adminPager.setCurrentItem(0);

		munkaTab = actionBar.newTab().setText(R.string.jobs);
		munkaTab.setTabListener(this);
		actionBar.addTab(munkaTab);

		soforTab = actionBar.newTab().setText(R.string.drivers);
		soforTab.setTabListener(this);
		actionBar.addTab(soforTab);

		autoTab = actionBar.newTab().setText(R.string.autos);
		autoTab.setTabListener(this);
		actionBar.addTab(autoTab);

		telephelyTab = actionBar.newTab().setText(R.string.sites);
		telephelyTab.setTabListener(this);
		actionBar.addTab(telephelyTab);

		partnerTab = actionBar.newTab().setText(R.string.partners);
		partnerTab.setTabListener(this);
		actionBar.addTab(partnerTab);

		kepekTab = actionBar.newTab().setText(R.string.pictures);
		kepekTab.setTabListener(this);
		actionBar.addTab(kepekTab);

		adminPager.setOnPageChangeListener(new OnPageChangeListener() {

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
		getMenuInflater().inflate(R.menu.admin, menu);
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
				NavUtils.navigateUpFromSameTask(this);
				return true;
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
		adminPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public class AdminPagerAdapter extends FragmentPagerAdapter {

		public AdminPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			switch (position) {
				case 1 :

					return new SoforListFragment(AdminActivity.this, soforDao);
				case 2 :
					return new AutoListFragment(AdminActivity.this, autoDao);
				case 3 :
					return new TelephelyListFragment(AdminActivity.this,
							telephelyDao);
				case 4 :
					return new PartnerListFragment(AdminActivity.this,
							partnerDao);
				case 5 :
					return new AdminKepekFragment(AdminActivity.this,
							partnerKepDao, profilKepDao, autoKepDao,
							munkaKepDao);
				default :
					return new MunkaListFragment(AdminActivity.this, munkaDao);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}

	}

	// setup connections to database
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = daoSession.getAutoDao();
		autoKepDao = daoSession.getAutoKepDao();
		munkaDao = daoSession.getMunkaDao();
		munkaEszkozDao = daoSession.getMunkaEszkozDao();
		munkaKepDao = daoSession.getMunkaKepDao();
		munkaTipusDao = daoSession.getMunkaTipusDao();
		partnerDao = daoSession.getPartnerDao();
		partnerKepDao = daoSession.getPartnerKepDao();
		profilKepDao = daoSession.getProfilKepDao();
		soforDao = daoSession.getSoforDao();
		telephelyDao = daoSession.getTelephelyDao();

	}

}
