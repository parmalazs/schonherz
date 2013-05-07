package com.schonherz.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.schonherz.dbentities.Sofor;

public class JsonArrayToArrayList {

	public static ArrayList<Sofor> JsonArrayToSofor(JSONArray jsonArray)

	{

		ArrayList<Sofor> soforList = new ArrayList<Sofor>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject soforJsonObj = null;

				soforJsonObj = jsonArray.getJSONObject(i);

				// New sofor
				Sofor currSofor = new Sofor();
				// set properties from jsonobject
				currSofor.setSoforID(Long.parseLong(soforJsonObj
						.getString("soforID")));
				currSofor.setSoforNev(soforJsonObj.getString("soforNev"));
				currSofor.setSoforCim(soforJsonObj.getString("soforCim"));
				currSofor.setSoforTelefonszam(soforJsonObj
						.getString("soforTelefonszam"));
				currSofor.setSoforLogin(soforJsonObj.getString("soforLogin"));
				currSofor.setSoforPass(soforJsonObj.getString("soforPass"));
				currSofor.setSoforBirthDate(soforJsonObj
						.getString("soforBirthDate"));
				currSofor.setSoforRegTime(soforJsonObj
						.getString("soforRegTime"));
				currSofor.setSoforIsAdmin(Boolean.parseBoolean(soforJsonObj
						.getString("soforIsAdmin")));
				currSofor.setSoforEmail(soforJsonObj
						.getString("soforProfilKepID"));
				currSofor.setSoforProfilKepID(Long.parseLong(soforJsonObj
						.getString("soforProfilKepID")));

				soforList.add(currSofor);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return soforList;
	}
}
