package com.schonherz.flottadroid;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
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

import com.schonherz.adapters.AutoAdapter;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.flottadroid.JobsActivity.JobsPagerAdapter;
import com.schonherz.fragments.AutoListFragment;
import com.schonherz.fragments.SzabadAutoListFragment;
import com.schonherz.fragments.SzabadMunkaListFragment;

public class CarActivity extends FragmentActivity implements ActionBar.TabListener {

	Context context;
	AutoDao autoDao;
	AutoKepDao autoKepDao;
	AutoAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Auto> autok;
	MenuItem refreshItem;
	
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	SzabadAutoListFragment szabadAutoListFragment;
	SzabadAutoListFragment sajatAutoFragment;
	

	Tab szabadAutokTab;
	Tab sajatAutoTab;

	ActionBar actionBar;
	CarsPagerAdapter carsAdapter;
	ViewPager carsPager;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dataBaseInit();
        
        szabadAutoListFragment = new SzabadAutoListFragment(this, autoDao);
        sajatAutoFragment = new SzabadAutoListFragment(this, autoDao);
        
        actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		carsPager = (ViewPager) findViewById(R.id.carsPager);
		carsAdapter = new CarsPagerAdapter(getSupportFragmentManager());
		carsPager.setAdapter(carsAdapter);
		carsPager.setCurrentItem(0);
		
		szabadAutokTab = actionBar.newTab().setText(R.string.title_cars_section1);
		szabadAutokTab.setTabListener(this);
		actionBar.addTab(szabadAutokTab);

		sajatAutoTab = actionBar.newTab().setText(R.string.title_cars_section2);
		sajatAutoTab.setTabListener(this);
		actionBar.addTab(sajatAutoTab);
		
		
		carsPager.setOnPageChangeListener(new OnPageChangeListener() {

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

	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_car, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 		
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		carsPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	
	
	public class CarsPagerAdapter extends FragmentPagerAdapter {

		public CarsPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			switch (position) {
				case 1 :
					return new SzabadAutoListFragment(CarActivity.this, autoDao);
				default : 
					return new SzabadAutoListFragment(CarActivity.this, autoDao);
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

	}
	
	
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = (daoSession.getAutoDao());     
		autoKepDao = daoSession.getAutoKepDao();
	}
	
}
