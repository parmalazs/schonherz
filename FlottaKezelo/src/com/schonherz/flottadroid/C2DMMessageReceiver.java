package com.schonherz.flottadroid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.schonherz.classes.GPSTracker;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaEszkozDao;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.MunkaTipusDao;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.PushMessage;
import com.schonherz.dbentities.PushMessageDao;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.sax.StartElementListener;
import android.util.Log;

public class C2DMMessageReceiver extends BroadcastReceiver{
	
	private SQLiteDatabase db;
	private DevOpenHelper helper;
	private DaoSession daoSession;
	private DaoMaster daoMaster;
	
	private PushMessageDao pushMessageDao;
	private AutoDao autoDao;
	private MunkaDao munkaDao;
	private MunkaEszkozDao munkaEszkozDao;
	private MunkaTipusDao munkaTipusDao;
	private PartnerDao partnerDao;
	private SoforDao soforDao;
	private TelephelyDao telephelyDao;	
	
	
	private PushMessage currentPushMessage;
	private GPSTracker gpsTracker;
	private SessionManager sessionManager;
	private List<Auto> autok;
	private Auto myAuto;
	
	String soforUrl = "http://www.flotta.host-ed.me/querySoforTable.php";
	String autoUrl = "http://www.flotta.host-ed.me/queryAutoTable.php";
	String partnerUrl = "http://www.flotta.host-ed.me/queryPartnerTable.php";
	String telephelyUrl = "http://www.flotta.host-ed.me/queryTelephelyTable.php";
	String munkaUrl = "http://www.flotta.host-ed.me/queryMunkaTable.php";
	String munkaTipusUrl = "http://www.flotta.host-ed.me/queryMunkaTipusTable.php";
	String munkaKepUrl = "http://www.flotta.host-ed.me/queryMunkaKepTable.php";
	String munkaEszkozUrl = "http://www.flotta.host-ed.me/queryMunkaEszkozTable.php";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		dataBaseInit(context);
		
		
		
		
		 String action = intent.getAction();
		    Log.w("C2DM", "Message Receiver called");
		    if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
		      Log.w("C2DM", "Received message");
		      final String payload = intent.getStringExtra("message");
		      Log.d("C2DM", "dmControl: payload = " + payload);
		   // Send this to my application server
		      /*
		      Intent i = new Intent();
		      i.putExtra("message",intent.getStringExtra("message"));
		      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		      i.setClass(context, AlertActivity.class);
		      context.startActivity(i);*/
		      
		     if (intent.getStringExtra("message").equals("autoUpdate")) {
		    	 gpsTracker=new GPSTracker(context);
		 		 sessionManager=new SessionManager(context);
		 		 autok=new ArrayList<Auto>();
		    	 myAuto=new Auto();
		    	 myAuto.setAutoLastSoforID(0L);
		    	 autok.clear();
		 		 autok=autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list();
		 		 for (int i=0; i<autok.size(); i++) {
		 			 if (autok.get(i).getAutoLastSoforID()==sessionManager.getUserID().get(SessionManager.KEY_USER_ID) && autok.get(i).getAutoFoglalt()) {
		 				 myAuto=autok.get(i);
		 			 }
		 		 }
		 		 if (myAuto.getAutoLastSoforID()!=0L && gpsTracker.canGetLocation()) {
		 			 myAuto.setAutoXkoordinata((float) gpsTracker.getLatitude());
		 			 myAuto.setAutoYkoordinata((float) gpsTracker.getLongitude());
		 			 Date now = new Date();
					 SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
					 String s = sdf.format(now);
		 			 myAuto.setAutoLastUpDate(s);
		 			 autoDao.update(myAuto);
		 			 
		 			 Log.i(C2DMMessageReceiver.class.getName(), "saját autó koordinátái frissítve: "
		 					 + gpsTracker.getLatitude() + ", " + gpsTracker.getLongitude());
		 			 
		 			 gpsTracker.stopUsingGPS();
		 			 
		 			if (NetworkUtil.checkInternetIsActive(context) == true) {
		 				new AsyncTask<Void, Void, Boolean>() {

		 					@Override
		 					protected void onPostExecute(Boolean result) {

		 						super.onPostExecute(result);
		 					};

		 					@Override
		 					protected Boolean doInBackground(Void... params) {
		 						// TODO Auto-generated method stub

		 						JSONBuilder builder = new JSONBuilder();
		 						JSONSender sender = new JSONSender();

		 						JSONObject obj = builder.updateAuto(myAuto);
	 							sender.sendJSON(sender.getFlottaUrl(), obj);

		 						return true;
		 					}
		 				}.execute();
		 			} 
		 		 }
		     }
		     else if (intent.getStringExtra("message").equals("refresh")) {
		    	 Log.i("adatbázis frissítés", "kezdés");
		    	 
		    	 new AsyncTask<Void, Void, String>() {

						protected void onPostExecute(String result) {
							Log.i("adatbázis frissítés", "vége");

						};

						protected void onPreExecute() {
							
						};

						@Override
						protected String doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return saveAlldata();
						}

					}.execute();
		    	 
		     }
		     else {
		    	//aktuális message beállítása
		 		currentPushMessage=new PushMessage();
		 		currentPushMessage.setPushMessageText(intent.getStringExtra("message"));
		 		Date now = new Date();
		 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		 		String s = sdf.format(now);
		 		currentPushMessage.setPushMessageDate(s);		
		 		pushMessageDao.insert(currentPushMessage);
		 		
			    // Prepare intent which is triggered if the
			    // notification is selected
			    Intent notificationIntent = new Intent(context, PushNotificationListActivity.class);
			    PendingIntent pIntent = PendingIntent.getActivity(context, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
	
			    // Build notification		      
			    NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			      
			      
			    Notification.Builder noti = new Notification.Builder(context)
			        .setContentTitle("Üzenet via FlottaDroid")
			        .setContentText(intent.getStringExtra("message")).setSmallIcon(R.drawable.ic_message)
			        .setContentIntent(pIntent);
			    noti.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
			      
	
			    notificationManager.notify(0, noti.getNotification());
		    }
		}
	}
	
	public void dataBaseInit(Context context) {
		helper = new DaoMaster.DevOpenHelper(context, "flotta-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		pushMessageDao=daoSession.getPushMessageDao();
		autoDao=daoSession.getAutoDao();;
		munkaDao = daoSession.getMunkaDao();
		munkaEszkozDao = daoSession.getMunkaEszkozDao();
		munkaTipusDao = daoSession.getMunkaTipusDao();
		partnerDao = daoSession.getPartnerDao();
		soforDao = daoSession.getSoforDao();
		telephelyDao = daoSession.getTelephelyDao();

	}
	
	public String saveAlldata() {
		JSONArray jsonArray;
		JSONObject json = new JSONObject();
						
		try {
			// get sofortable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(soforUrl,
					json.toString());
			SoforDao.dropTable(soforDao.getDatabase(), true);
			SoforDao.createTable(soforDao.getDatabase(), true);

			ArrayList<Sofor> soforok = JsonArrayToArrayList
					.JsonArrayToSofor(jsonArray);

			for (int i = 0; i < soforok.size(); i++) {
				soforDao.insert(soforok.get(i));
			}

			soforok = null;

			// get autotable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					autoUrl, json.toString());

			AutoDao.dropTable(autoDao.getDatabase(), true);
			AutoDao.createTable(autoDao.getDatabase(), true);

			ArrayList<Auto> autok = JsonArrayToArrayList
					.JsonArrayToAuto(jsonArray);

			for (int i = 0; i < autok.size(); i++) {
				autoDao.insert(autok.get(i));
			}

			autok = null;

			// get partnertable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					partnerUrl, json.toString());

			PartnerDao.dropTable(partnerDao.getDatabase(), true);
			PartnerDao.createTable(partnerDao.getDatabase(), true);

			ArrayList<Partner> partnerek = JsonArrayToArrayList
					.JsonArrayToPartner(jsonArray);

			for (int i = 0; i < partnerek.size(); i++) {
				partnerDao.insert(partnerek.get(i));
			}

			partnerek = null;

			// get telephelytable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					telephelyUrl, json.toString());

			TelephelyDao.dropTable(telephelyDao.getDatabase(), true);
			TelephelyDao.createTable(telephelyDao.getDatabase(), true);

			ArrayList<Telephely> telephelyek = JsonArrayToArrayList
					.JsonArrayToTelephely(jsonArray);

			for (int i = 0; i < telephelyek.size(); i++) {
				telephelyDao.insert(telephelyek.get(i));
			}
			telephelyek = null;

			// get munkatable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(munkaUrl,
					json.toString());

			MunkaDao.dropTable(munkaDao.getDatabase(), true);
			MunkaDao.createTable(munkaDao.getDatabase(), true);

			ArrayList<Munka> munkak = JsonArrayToArrayList
					.JsonArrayToMunka(jsonArray);

			for (int i = 0; i < munkak.size(); i++) {
				munkaDao.insert(munkak.get(i));
			}
			munkak = null;

			// get munkatipustable
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					munkaTipusUrl, json.toString());

			MunkaTipusDao.dropTable(munkaTipusDao.getDatabase(), true);
			MunkaTipusDao.createTable(munkaTipusDao.getDatabase(), true);

			ArrayList<MunkaTipus> munkatipusok = JsonArrayToArrayList
					.JsonArrayToMunkaTipusok(jsonArray);

			for (int i = 0; i < munkatipusok.size(); i++) {
				munkaTipusDao.insert(munkatipusok.get(i));
			}
			munkatipusok = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
		return "";
	}

}
