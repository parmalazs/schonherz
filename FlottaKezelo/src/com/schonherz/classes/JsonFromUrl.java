package com.schonherz.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import android.util.Log;

import com.loopj.android.http.RequestParams;

public class JsonFromUrl {

	public static JSONArray getJsonObjectFromUrl(String url, String jsonString) throws Exception {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		try {
			postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-type", "application/json");

			if (jsonString != null && !jsonString.equals("")) {
				StringEntity se = new StringEntity(jsonString);
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				postRequest.setEntity(se);
			}

			RequestParams requestParams;

			HttpResponse postResponse = client.execute(postRequest);
			final int statusCodePost = postResponse.getStatusLine()
					.getStatusCode();

			if (statusCodePost != HttpStatus.SC_OK) {
				Log.w(JsonFromUrl.class.getSimpleName(), "Error "
						+ statusCodePost + " for URL " + url);
				return null;
			}

			HttpEntity postResponseEntity = postResponse.getEntity();
								
			return getRootJsonObject(postResponseEntity.getContent());
		} catch (IOException e) {
			
			postRequest.abort();
								
		}

		return null;

	}	
	

	public static JSONArray getRootJsonObject(InputStream inputStream) throws Exception {
		InputStreamReader inputStreamReader;
		BufferedReader bufferedReader;
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);


		String inputLine;
		String jsonString = "";
		
		while ((inputLine=bufferedReader.readLine()) !=null) {
			jsonString=inputLine;
		}
		
		Log.i("json", "Nyers string: " + jsonString);
		
		org.json.JSONObject mainJson = new org.json.JSONObject(jsonString);
		org.json.JSONArray jsonArray = mainJson.getJSONArray("rows");
		
		try {
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();

			bufferedReader = null;
			inputStreamReader = null;
			inputStream = null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonArray;

	}
  
}
