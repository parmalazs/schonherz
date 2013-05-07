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

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.RequestParams;

public class JsonToUrl {

	
public static JsonObject sendJsonToUrl(String url, String jsonString) throws Exception{
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);
		try {
          	postRequest.setHeader("Accept", "application/json");
			postRequest.setHeader("Content-type", "application/json");
			
			if (jsonString != null && !jsonString.equals("") ) {
				  StringEntity se = new StringEntity(jsonString);  
	              se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	              postRequest.setEntity(se);
			}

			RequestParams requestParams ;
			
			HttpResponse postResponse = client.execute(postRequest);
			final int statusCodePost = postResponse.getStatusLine()
					.getStatusCode();

			if (statusCodePost != HttpStatus.SC_OK) {
				Log.w(JsonFromUrl.class.getSimpleName(), "Error " + statusCodePost
						+ " for URL " + url);
				HttpEntity responseEntity = null;
				if(postResponse.getEntity()!=null)
					 responseEntity = postResponse.getEntity();

				return getRootJsonObject(responseEntity.getContent());
			}

			HttpEntity postResponseEntity = postResponse.getEntity();

			// Log.d("sssss",getResponseEntity.getContent().toString());
			//return postResponseEntity.getContent();

			return getRootJsonObject(postResponseEntity.getContent());
		} catch (IOException e) {
			postRequest.abort();
			//Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;

	}
	
	public static JsonObject getRootJsonObject(InputStream inputStream) {
		InputStreamReader inputStreamReader;
		BufferedReader bufferedReader;
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(bufferedReader);
		JsonParser parser = new JsonParser();
		JsonObject jsonObjectRoot = (JsonObject) parser.parse(jsonReader);
		
		//String str = jsonObj.toString();
		//Log.d("xxxx", str);
		
		//{"d":"Hiba történt a kép feltöltésénél"}
		
		//JsonArray jsonArrayUserAccounts = jsonObjectRootD.getAsJsonArray("userAccount");
		try {
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			
			bufferedReader=null;
			inputStreamReader=null;
			inputStream=null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		return 	 jsonObjectRoot;

	}
}
