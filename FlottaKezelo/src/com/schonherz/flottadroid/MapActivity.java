package com.schonherz.flottadroid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.directions.route.Routing;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.schonherz.adapters.MunkaAdapter;
import com.schonherz.classes.GPSTracker;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaDao.Properties;
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

	SessionManager sessionManager;
	GPSTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		dataBaseInit();

		layers = getResources().getStringArray(R.array.map_layers_strings);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_spinner_dropdown_item, layers);

		sessionManager = new SessionManager(this);

		ActionBar.OnNavigationListener navListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), layers[itemPosition],
						Toast.LENGTH_SHORT).show();

				switch (itemPosition) {
				// Autok
					case 0 :
						addAutoMarkers();
						break;
					// Partnerek
					case 1 :
						addPartnerMarkers();
						break;
					// Telephelyek
					case 2 :
						addTelephelyMarkers();
						break;
					// Napi utvonal
					case 3 :
						addDailyRouteMarkers();
						break;
					// Szabad munkak
					case 4 :
						addFreeJobMarkers();
						break;
				}

				return false;
			}
		};

		tracker = new GPSTracker(this);

		getActionBar().setListNavigationCallbacks(adapter, navListener);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMyLocationEnabled(true);
	
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(tracker
				.getLatitude(), tracker.getLongitude())));

		// //razoomol 5-re
		map.animateCamera(CameraUpdateFactory.zoomTo(7), 2000, null);
		
		map.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				// TODO Auto-generated method stub
				
			}
		});
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
				helper.close();
				NavUtils.navigateUpFromSameTask(this);
				this.overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		helper.close();
		finish();
		this.overridePendingTransition(R.anim.slide_out_right,
				R.anim.slide_in_left);
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
							.fromResource(R.drawable.mapimage)));
			// markers.add(temp);

		}

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// TODO Auto-generated method stub
				Auto auto = autoDao
						.queryBuilder()
						.where(com.schonherz.dbentities.AutoDao.Properties.AutoRendszam
								.eq(marker.getSnippet())).list().get(0);
				Intent i = new Intent(MapActivity.this,
						CarDetailsActivity.class);
				i.putExtra("selectedAutoID", auto.getAutoID());
				startActivity(i);
				MapActivity.this.overridePendingTransition(R.anim.fade_in,
						R.anim.fade_out);

			}
		});
	}

	public void addPartnerMarkers() {
		map.clear();
		ArrayList<Partner> partnerek = new ArrayList<Partner>(
				partnerDao.loadAll());
		for (int i = 0; i < partnerek.size(); i++) {
			ArrayList<Munka> munkak=new ArrayList<Munka>();
			munkak=(ArrayList<Munka>) munkaDao.queryBuilder().where(Properties.PartnerID.eq(partnerek.get(i).getPartnerID())).list();
			
			if (munkak.size()>0) {
				Marker temp = map.addMarker(new MarkerOptions()
						.position(
								new LatLng(
										partnerek.get(i).getPartnerXkoordinata(),
										partnerek.get(i).getPartnerYkoodinata()))
						.title(partnerek.get(i).getPartnerNev())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.mapimage)));
			}
		}

		map.setOnInfoWindowClickListener(new OnInfoPartnerClickListener());
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
							.fromResource(R.drawable.mapimage)));
		}
		map.setOnInfoWindowClickListener(new OnInfoTelephelyClickListener());
	}

	public void addFreeJobMarkers() {
		map.clear();
		
		ArrayList<Telephely> telephelyek = new ArrayList<Telephely>(
				telephelyDao.loadAll());
		for (int i = 0; i < telephelyek.size(); i++) {
			ArrayList<Munka> munkak=new ArrayList<Munka>();
			munkak=(ArrayList<Munka>) munkaDao.queryBuilder().where(Properties.TelephelyID.eq(telephelyek.get(i).getTelephelyID())).list();
			if (munkak.size()>0) {
				Marker temp = map.addMarker(new MarkerOptions()
						.position(
								new LatLng(telephelyek.get(i)
										.getTelephelyXkoordinata(), telephelyek
										.get(i).getTelephelyYkoordinata()))
						.title(telephelyek.get(i).getTelephelyNev())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.mapimage)));
			}
		}
	
		map.setOnInfoWindowClickListener(new OnInfoFreeJobClickListerner());
	}

	public void addDailyRouteMarkers() {
		map.clear();

		ArrayList<Munka> munkak = new ArrayList<Munka>(munkaDao
				.queryBuilder()
				.where(Properties.MunkaIsActive.eq(true),
						Properties.SoforID.eq(sessionManager.getUserID().get(
								SessionManager.KEY_USER_ID))).list());

		for (int i = 1; i < munkak.size(); i++) {
			Log.w("com.schonherz.flottadroid",Integer.toString(i));
			new Routing(MapActivity.this, map, Color.RED).execute(new LatLng(
					munkak.get(i-1).getPartner().getPartnerXkoordinata(), munkak
							.get(i-1).getPartner().getPartnerYkoodinata()),
					new LatLng(munkak.get(i).getPartner()
							.getPartnerXkoordinata(), munkak.get(i)
							.getPartner().getPartnerYkoodinata()));
		}
		
		map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(munkak.get(0).getPartner().getPartnerXkoordinata(), munkak
				.get(0).getPartner().getPartnerYkoodinata())));
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

	public class OnInfoTelephelyClickListener
			implements
				OnInfoWindowClickListener {

		@Override
		public void onInfoWindowClick(Marker marker) {
			// TODO Auto-generated method stub
			Telephely currTelephely = telephelyDao
					.queryBuilder()
					.where(com.schonherz.dbentities.TelephelyDao.Properties.TelephelyNev
							.eq(marker.getTitle())).list().get(0);

			List<Munka> freeTelephelyMunka = munkaDao
					.queryBuilder()
					.where(Properties.MunkaIsActive.eq(true),
							Properties.SoforID.eq(0),
							Properties.TelephelyID.eq(currTelephely
									.getTelephelyID())).list();

			final Dialog munkaDialog = new Dialog(MapActivity.this);
			munkaDialog
					.setContentView(com.schonherz.flottadroid.R.layout.activity_push_notification_list);

			munkaDialog.setTitle(currTelephely.getTelephelyNev());

			ListView munkaList = (ListView) munkaDialog
					.findViewById(R.id.listNotifications);
			munkaDialog.show();
			MunkaAdapter adapt = new MunkaAdapter(MapActivity.this,
					R.layout.list_item_munka, freeTelephelyMunka, munkaDao);

			munkaList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					Munka current = (Munka) parent.getItemAtPosition(pos);

					Intent i = new Intent(MapActivity.this,
							MunkaDetailsActivity.class);
					i.putExtra("selectedMunkaID", current.getMunkaID());
					munkaDialog.dismiss();
					startActivity(i);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			});

			munkaList.setAdapter(adapt);

		}

	}

	public class OnInfoFreeJobClickListerner implements OnInfoWindowClickListener
	{

		@Override
		public void onInfoWindowClick(Marker marker) {
			// TODO Auto-generated method stub
			ArrayList<Telephely> telephelyek=new ArrayList<Telephely>();
			telephelyek=(ArrayList<Telephely>) telephelyDao
					.queryBuilder()
					.where(com.schonherz.dbentities.TelephelyDao.Properties.TelephelyNev
							.eq(marker.getTitle())).list();
			
			
			if (telephelyek.size()!=0) {
				Telephely currTelephely = telephelyDao
						.queryBuilder()
						.where(com.schonherz.dbentities.TelephelyDao.Properties.TelephelyNev
								.eq(marker.getTitle())).list().get(0);
				
				List<Munka> freeTelephelyMunka = munkaDao
						.queryBuilder()
						.where(Properties.MunkaIsActive.eq(true),
								Properties.SoforID.eq(0),
								Properties.TelephelyID.eq(currTelephely
										.getTelephelyID())).list();

				final Dialog munkaDialog = new Dialog(MapActivity.this);
				munkaDialog
						.setContentView(com.schonherz.flottadroid.R.layout.activity_push_notification_list);

				munkaDialog.setTitle(currTelephely.getTelephelyNev());

				ListView munkaList = (ListView) munkaDialog
						.findViewById(R.id.listNotifications);
				munkaDialog.show();
				MunkaAdapter adapt = new MunkaAdapter(MapActivity.this,
						R.layout.list_item_munka, freeTelephelyMunka, munkaDao);

				munkaList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
						Munka current = (Munka) parent.getItemAtPosition(pos);

						Intent i = new Intent(MapActivity.this,
								MunkaDetailsActivity.class);
						i.putExtra("selectedMunkaID", current.getMunkaID());
						munkaDialog.dismiss();
						startActivity(i);
						overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					}
				});

				munkaList.setAdapter(adapt);
				
			}
			else {
				ArrayList<Partner> partnerek=new ArrayList<Partner>();
				partnerek=(ArrayList<Partner>) partnerDao
						.queryBuilder()
						.where(com.schonherz.dbentities.PartnerDao.Properties.PartnerNev
								.eq(marker.getTitle())).list();
				if (partnerek.size()!=0) {
					Partner currPartner = partnerDao
							.queryBuilder()
							.where(com.schonherz.dbentities.PartnerDao.Properties.PartnerNev
									.eq(marker.getTitle())).list().get(0);
					
					List<Munka> freePartnerMnka = munkaDao
							.queryBuilder()
							.where(Properties.MunkaIsActive.eq(true),
									Properties.SoforID.eq(0),
									Properties.PartnerID.eq(currPartner.getPartnerID()))
							.list();
	
					final Dialog munkaDialog = new Dialog(MapActivity.this);
					munkaDialog
							.setContentView(com.schonherz.flottadroid.R.layout.activity_push_notification_list);
	
					munkaDialog.setTitle(currPartner.getPartnerNev());
	
					ListView munkaList = (ListView) munkaDialog
							.findViewById(R.id.listNotifications);
					munkaDialog.show();
					MunkaAdapter adapt = new MunkaAdapter(MapActivity.this,
							R.layout.list_item_munka, freePartnerMnka, munkaDao);
	
					munkaList.setOnItemClickListener(new OnItemClickListener() {
	
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int pos, long id) {
							// TODO Auto-generated method stub
							Munka current = (Munka) parent.getItemAtPosition(pos);
	
							Intent i = new Intent(MapActivity.this,
									MunkaDetailsActivity.class);
							i.putExtra("selectedMunkaID", current.getMunkaID());
							munkaDialog.dismiss();
							startActivity(i);
							overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
						}
					});
	
					munkaList.setAdapter(adapt);
					
				}
			}
			
			
		}
		
	}
	
	public class OnInfoPartnerClickListener
			implements
				OnInfoWindowClickListener {

		@Override
		public void onInfoWindowClick(Marker marker) {
			// TODO Auto-generated method stub
			Partner currPartner = partnerDao
					.queryBuilder()
					.where(com.schonherz.dbentities.PartnerDao.Properties.PartnerNev
							.eq(marker.getTitle())).list().get(0);

			List<Munka> freePartnerMnka = munkaDao
					.queryBuilder()
					.where(Properties.MunkaIsActive.eq(true),
							Properties.SoforID.eq(0),
							Properties.PartnerID.eq(currPartner.getPartnerID()))
					.list();

			final Dialog munkaDialog = new Dialog(MapActivity.this);
			munkaDialog
					.setContentView(com.schonherz.flottadroid.R.layout.activity_push_notification_list);

			munkaDialog.setTitle(currPartner.getPartnerNev());

			ListView munkaList = (ListView) munkaDialog
					.findViewById(R.id.listNotifications);
			munkaDialog.show();
			MunkaAdapter adapt = new MunkaAdapter(MapActivity.this,
					R.layout.list_item_munka, freePartnerMnka, munkaDao);

			munkaList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
					Munka current = (Munka) parent.getItemAtPosition(pos);

					Intent i = new Intent(MapActivity.this,
							MunkaDetailsActivity.class);
					i.putExtra("selectedMunkaID", current.getMunkaID());
					munkaDialog.dismiss();
					startActivity(i);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			});

			munkaList.setAdapter(adapt);

		}
	}

}
