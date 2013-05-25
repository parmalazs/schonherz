package com.schonherz.flottadroid;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.directions.route.Routing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;

public class MapActivity extends Activity {

	private GoogleMap map;
	String[] layers;
	ArrayList<Marker> markers;

	// Database Handlers
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	private AutoDao autoDao;
	private TelephelyDao telephelyDao;
	private PartnerDao partnerDao;
	private MunkaDao munkaDao;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        dataBaseInit();
        
        layers = getResources().getStringArray(R.array.map_layers_strings);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, layers);
        
        
        ActionBar.OnNavigationListener navListener = new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), layers[itemPosition], Toast.LENGTH_SHORT).show();
				
				switch(itemPosition)
				{
					//Autok
					case 0:
						addAutoMarkers();
						break;
					//Partnerek
					case 1: 
						addPartnerMarkers();
						break;
					//Telephelyek
					case 2:
						addTelephelyMarkers();
						break;
					//Napi utvonal
					case 3:
						addDailyRouteMarkers();						
						break;							
					//Szabad munkak
					case 4:
						addFreeJobMarkers();
						break;
				}
				
				return false;
			}
		};
        
		getActionBar().setListNavigationCallbacks(adapter, navListener);
		
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.setMyLocationEnabled(true);                            
        
        //Deak terre megy a camera
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(47.497372, 19.054756)));
        
        //razoomol 15-rol 10-re
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
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

	public void addAutoMarkers() {
		ArrayList<Auto> autok = new ArrayList<Auto>(autoDao.loadAll());
		map.clear();
		for (int i = 0; i < autok.size(); i++) {
			Marker temp = map.addMarker(new MarkerOptions()
					.position(
							new LatLng(autok.get(i).getAutoXkoordinata(), autok
									.get(i).getAutoYkoordinata()))
					.title(autok.get(i).getAutoNev())
					.snippet(autok.get(i).getAutoRendszam())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
			// markers.add(temp);
		}
	}

	public void addPartnerMarkers() {
		map.clear();
		ArrayList<Partner> partnerek = new ArrayList<Partner>(
				partnerDao.loadAll());
		for (int i = 0; i < partnerek.size(); i++) {
			Marker temp = map.addMarker(new MarkerOptions()
					.position(
							new LatLng(
									partnerek.get(i).getPartnerXkoordinata(),
									partnerek.get(i).getPartnerYkoodinata()))
					.title(partnerek.get(i).getPartnerNev())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
		}
	}

	public void addTelephelyMarkers() {
		map.clear();
		ArrayList<Telephely> telephelyek = new ArrayList<Telephely>(
				telephelyDao.loadAll());
		for (int i = 0; i < telephelyek.size(); i++) {
			Marker temp = map.addMarker(new MarkerOptions()
					.position(
							new LatLng(telephelyek.get(i)
									.getTelephelyXkoordinata(), telephelyek
									.get(i).getTelephelyYkoordinata()))
					.title(telephelyek.get(i).getTelephelyNev())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));
		}
	}

	public void addFreeJobMarkers() {
		map.clear();		
	}

	public void addDailyRouteMarkers() {
		map.clear();
		ArrayList<Partner> partnerek = new ArrayList<Partner>(
				partnerDao.loadAll());
		new Routing(MapActivity.this,map,Color.RED).execute(new LatLng(
				partnerek.get(0).getPartnerXkoordinata(),
				partnerek.get(0).getPartnerYkoodinata()),new LatLng(
						partnerek.get(6).getPartnerXkoordinata(),
						partnerek.get(6).getPartnerYkoodinata()));
	}

	// setup connections to database
	public void dataBaseInit() {
		helper = new DaoMaster.DevOpenHelper(this, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		autoDao = daoSession.getAutoDao();
		telephelyDao = daoSession.getTelephelyDao();
		munkaDao = daoSession.getMunkaDao();
		partnerDao = daoSession.getPartnerDao();
	}

}
