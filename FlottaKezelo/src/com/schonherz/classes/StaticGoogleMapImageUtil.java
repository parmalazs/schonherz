package com.schonherz.classes;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StaticGoogleMapImageUtil {

	public static Bitmap getGoogleMapThumbnail(double lati, double longi) {
		String URL = "http://maps.google.com/maps/api/staticmap?center=" + lati
				+ "," + longi + "&zoom=15&size=400x400&sensor=false";
		Bitmap bmp = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);

		InputStream in = null;
		try {
			in = httpclient.execute(request).getEntity().getContent();
			bmp = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp;
	}
}
