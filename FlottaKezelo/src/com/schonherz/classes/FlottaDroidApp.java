package com.schonherz.classes;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;

public class FlottaDroidApp extends Application {

	private static DevOpenHelper helper;
	 private static SQLiteDatabase db;
	 public static DaoMaster daoMaster;
	 private static Context context;
	
	 
	@Override
	public void onCreate() {
		  context=getApplicationContext();
		 
		 super.onCreate();
	}
	 @Override
	public void onTerminate() {
			daoMaster.getDatabase().close();
			daoMaster=null;
		super.onTerminate();
	}
	
	public static DaoMaster getDaoMaster() {
		
		if (daoMaster == null) {
			helper = new DaoMaster.DevOpenHelper(context, "flotta-db", null);
			  db = helper.getWritableDatabase();
			  daoMaster = new DaoMaster(db);
		}
		return daoMaster;
	} 
}
