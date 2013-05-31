package com.schonherz.flottadroid;

import java.util.Locale;

import com.schonherz.classes.SessionManager;
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
import android.app.ActionBar.Tab;
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

public class ContactActivity extends FragmentActivity implements ActionBar.TabListener {
	
	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;

	// Greendao objects
	private PartnerDao partnerDao;
	private SoforDao soforDao;
	
	PartnerListFragment partnerListFragment;
	SoforListFragment soforListFragment;
	
	Tab partnerTab;
	Tab soforTab;
	
	ActionBar actionBar;
	ContactsPagerAdapter contactsAdapter;
	ViewPager contactsPager;	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_activty);
        
        
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dataBaseInit();       
        
        
        partnerListFragment = new PartnerListFragment(this, partnerDao, false);
        soforListFragment = new SoforListFragment(this, soforDao, false);
        
        actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		contactsPager = (ViewPager) findViewById(R.id.contactsPager);
		contactsAdapter = new ContactsPagerAdapter(getSupportFragmentManager());
		contactsPager.setAdapter(contactsAdapter);
		contactsPager.setCurrentItem(0);
		
		partnerTab = actionBar.newTab().setText(R.string.title_partners);
		partnerTab.setTabListener(this);
		actionBar.addTab(partnerTab);
		
		soforTab = actionBar.newTab().setText(R.string.drivers);
		soforTab.setTabListener(this);
		actionBar.addTab(soforTab);
		
		contactsPager.setOnPageChangeListener(new OnPageChangeListener() {

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
    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		helper.close();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left); 		
	}

	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contact_activty, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	helper.close();
                NavUtils.navigateUpFromSameTask(this);
                this.overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    
    public class ContactsPagerAdapter extends FragmentPagerAdapter {

		public ContactsPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			switch (position) {
				case 0 :
					return new PartnerListFragment(ContactActivity.this, partnerDao, false);
					
				default :
					return new SoforListFragment(ContactActivity.this, soforDao, false);
					
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

		partnerDao = daoSession.getPartnerDao();
		soforDao = daoSession.getSoforDao();

 	}



@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}



@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	contactsPager.setCurrentItem(tab.getPosition());
	
}



@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}

}
