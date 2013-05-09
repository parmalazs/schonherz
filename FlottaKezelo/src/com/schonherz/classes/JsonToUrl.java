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
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.RequestParams;

public class JsonToUrl {

	
public static JSONObject sendJsonToUrl(String url, String jsonString) throws Exception{
		
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
	
	public static JSONObject getRootJsonObject(InputStream inputStream) {
		InputStreamReader inputStreamReader;
		BufferedReader bufferedReader;
		inputStreamReader = new InputStreamReader(inputStream);
		bufferedReader = new BufferedReader(inputStreamReader);

		//Gson gson = new Gson();
		//JsonReader jsonReader = new JsonReader(bufferedReader);
		//JsonParser parser = new JsonParser();
		//JsonObject jsonObjectRoot = (JsonObject) parser.parse(jsonReader);
		
		String inputLine;
		String jsonString = "";
		JSONObject mainJson = null;
		try {
			while ((inputLine=bufferedReader.readLine()) !=null) {
				jsonString=inputLine;
			}
		
		Log.i("json", "Nyers string: " + jsonString);
		
		 try {
			mainJson = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//String str = jsonObj.toString();
		//Log.d("xxxx", str);
		
		//{"d":"Hiba történt a kép feltöltésénél"}
		
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
	
		
		return 	 mainJson;

	}
}
