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
        
        partnerListFragment = new PartnerListFragment(this, partnerDao);
        soforListFragment = new SoforListFragment(this, soforDao);
        
        actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		contactsPager = (ViewPager) findViewById(R.id.contactsPager);
		contactsAdapter = new ContactsPagerAdapter(getSupportFragmentManager());
		contactsPager.setAdapter(contactsAdapter);
		contactsPager.setCurrentItem(0);
		
		partnerTab = actionBar.newTab().setText(R.string.title_partners);
		partnerTab.setTabListener(this);
		actionBar.addTab(partnerTab);
		
		soforTab = actionBar.newTab().setText(R.string.title_soforok);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contact_activty, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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
					return new SoforListFragment(ContactActivity.this, soforDao);
				default :
					return new PartnerListFragment(ContactActivity.this, partnerDao);
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



@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}



@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}



@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	// TODO Auto-generated method stub
	
}

}
