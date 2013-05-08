package com.schonherz.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.schonherz.dbentities.Partner;
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
				if (soforJsonObj.getString("soforProfilKepID") != null) {
					currSofor.setSoforProfilKepID(Long.parseLong(soforJsonObj
							.getString("soforProfilKepID")));
				}
				else
				{
					currSofor.setSoforProfilKepID(null);
				}

				soforList.add(currSofor);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return soforList;
	}
	
	public static ArrayList<Partner> JsonArrayToPartner(JSONArray jsonArray)

	{

		ArrayList<Partner> partnerList = new ArrayList<Partner>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject partnerJsonObj = null;

				partnerJsonObj = jsonArray.getJSONObject(i);

				// New partner
				Partner currPartner = new Partner();
				// set properties from jsonobject
				currPartner.setPartnerID(Long.parseLong(partnerJsonObj
						.getString("partnerID")));
				currPartner.setPartnerNev(partnerJsonObj.getString("partnerNev"));
				currPartner.setPartnerCim(partnerJsonObj.getString("partnerCim"));
				currPartner.setPartnerTelefonszam(partnerJsonObj
						.getString("partnerTelefonszam"));		
				
				if (partnerJsonObj.getString("partnerXkoordinata") != null) {
					currPartner.setPartnerXkoordinata(Float.parseFloat(partnerJsonObj
							.getString("partnerXkoordinata")));
				}
				else
				{
					currPartner.setPartnerXkoordinata(null);
				}
				
				if (partnerJsonObj.getString("partnerYkoodinata") != null) {
					currPartner.setPartnerYkoodinata(Float.parseFloat(partnerJsonObj
							.getString("partnerYkoodinata")));
				}
				else
				{
					currPartner.setPartnerYkoodinata(null);
				}
				
				currPartner.setPartnerWeboldal(partnerJsonObj
						.getString("partnerWeboldal"));
				currPartner.setPartnerEmailcim(partnerJsonObj
						.getString("partnerEmailcim"));
				partnerList.add(currPartner);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return partnerList;
	}
}
