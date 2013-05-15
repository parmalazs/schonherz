package com.schonherz.classes;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtil {
	
	public static boolean checkInternetIsActive(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isConnected() || mobile.isConnected()) {
			return true;
		}

		return false;

	}
	
	public static boolean checkWifiIsActive(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifi.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static boolean checkMobileIsActive(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobile.isConnected()) {
			return true;
		}
		return false;
	}

}
