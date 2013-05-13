package com.schonherz.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.Telephely;

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
				
				if (partnerJsonObj.getString("partnerYkoordinata") != null) {
					currPartner.setPartnerYkoodinata(Float.parseFloat(partnerJsonObj
							.getString("partnerYkoordinata")));
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
	
	public static ArrayList<Auto> JsonArrayToAuto(JSONArray jsonArray)

	{

		ArrayList<Auto> autoList = new ArrayList<Auto>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject autoJsonObj = null;

				autoJsonObj = jsonArray.getJSONObject(i);

				// New auto
				Auto currAuto = new Auto();
				// set properties from jsonobject
				currAuto.setAutoID(Long.parseLong(autoJsonObj.getString("autoID")));				
				currAuto.setAutoFoglalt(Boolean.parseBoolean(autoJsonObj.getString("autoFoglalt")));
				if (autoJsonObj.getString("autoXkoordinata") != null) {
					currAuto.setAutoXkoordinata(Float.parseFloat(autoJsonObj.getString("autoXkoordinata")));
				}
				else {
					currAuto.setAutoXkoordinata(null);
				}
				if (autoJsonObj.getString("autoYkoordinata") != null) {
					currAuto.setAutoYkoordinata(Float.parseFloat(autoJsonObj.getString("autoYkoordinata")));
				}
				else {
					currAuto.setAutoYkoordinata(null);
				}
				currAuto.setAutoNev(autoJsonObj.getString("autoNev"));
				currAuto.setAutoTipus(autoJsonObj.getString("autoTipus"));
				currAuto.setAutoRendszam(autoJsonObj.getString("autoRendszam"));
				if(autoJsonObj.getString("autoProfilKepID")!=null) {
					currAuto.setAutoProfilKepID(Long.parseLong(autoJsonObj.getString("autoProfilKepID")));
				}
				else {
					currAuto.setAutoProfilKepID(null);
				}
				currAuto.setAutoKilometerOra(Long.parseLong(autoJsonObj.getString("autoKilometerOra")));
				currAuto.setAutoUzemanyag(Long.parseLong(autoJsonObj.getString("autoUzemAnyag")));
				currAuto.setAutoMuszakiVizsgaDate(autoJsonObj.getString("autoMuszakiVizsgaDate"));
				currAuto.setAutoLastSzervizDate(autoJsonObj.getString("autoLastSzervizDate"));
				
				if(autoJsonObj.getString("autoLastSoforID")!=null) {
					currAuto.setAutoLastSoforID(Long.parseLong(autoJsonObj.getString("autoLastSoforID")));
				}
				else {
					currAuto.setAutoLastSoforID(null);
				}
				currAuto.setAutoLastUpDate(autoJsonObj.getString("autoLastUpDate"));
				if(autoJsonObj.getString("autoLastTelephelyID")!=null) {
					currAuto.setAutoLastTelephelyID(Long.parseLong(autoJsonObj.getString("autoLastTelephelyID")));
				}
				else {
					currAuto.setAutoLastTelephelyID(null);
				}
				
				autoList.add(currAuto);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return autoList;
	}
	
	public static ArrayList<Telephely> JsonArrayToTelephely(JSONArray jsonArray)

	{

		ArrayList<Telephely> telephelyList = new ArrayList<Telephely>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject telephelyJsonObj = null;

				telephelyJsonObj = jsonArray.getJSONObject(i);

				// New telephely
				Telephely currTelephely = new Telephely();
				// set properties from jsonobject
				currTelephely.setTelephelyID(Long.parseLong(telephelyJsonObj.getString("telephelyID")));
				currTelephely.setTelephelyNev(telephelyJsonObj.getString("telephelyNev"));
				currTelephely.setTelephelyCim(telephelyJsonObj.getString("telephelyCim"));
				currTelephely.setTelephelyTelefonszam(telephelyJsonObj.getString("telephelyTelefonszam"));
				if(telephelyJsonObj.getString("telephelyXkoordinata")!=null) {
					currTelephely.setTelephelyXkoordinata(Float.parseFloat(telephelyJsonObj.getString("telephelyXkoordinata")));
				}
				else {
					currTelephely.setTelephelyXkoordinata(null);
				}
				if(telephelyJsonObj.getString("telephelyYkoordinata")!=null) {
					currTelephely.setTelephelyYkoordinata(Float.parseFloat(telephelyJsonObj.getString("telephelyYkoordinata")));
				}
				else {
					currTelephely.setTelephelyYkoordinata(null);
				}
				currTelephely.setTelephelyEmail(telephelyJsonObj.getString("telephelyEmail"));
				
				telephelyList.add(currTelephely);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return telephelyList;
	}
	
	public static ArrayList<Munka> JsonArrayToMunka(JSONArray jsonArray)

	{

		ArrayList<Munka> munkaList = new ArrayList<Munka>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject munkaJsonObj = null;

				munkaJsonObj = jsonArray.getJSONObject(i);

				// New munka
				Munka currMunka = new Munka();
				// set properties from jsonobject
				currMunka.setMunkaID(Long.parseLong(munkaJsonObj.getString("munkaID")));
				currMunka.setMunkaDate(munkaJsonObj.getString("munkaDate"));
				if(munkaJsonObj.get("munkaKoltseg")!=null){
					currMunka.setMunkaKoltseg(Long.parseLong(munkaJsonObj.getString("munkaKoltseg")));
				}
				else {
					currMunka.setMunkaKoltseg(null);
				}
				if(munkaJsonObj.get("munkaBevetel")!=null){
					currMunka.setMunkaBevetel(Long.parseLong(munkaJsonObj.getString("munkaBevetel")));
				}
				else {
					currMunka.setMunkaBevetel(null);
				}
				if(munkaJsonObj.get("munkaUzemanyagState")!=null){
					currMunka.setMunkaUzemanyagState(Long.parseLong(munkaJsonObj.getString("munkaUzemanyagState")));
				}
				else {
					currMunka.setMunkaUzemanyagState(null);
				}
				currMunka.setMunkaComment(munkaJsonObj.getString("munkaComment"));
				currMunka.setMunkaBefejezesDate(munkaJsonObj.getString("munkaBefejezesDate"));
				currMunka.setMunkaIsActive(Boolean.parseBoolean(munkaJsonObj.getString("munkaIsActive")));
				if(munkaJsonObj.get("munkaEstimatedTime")!=null) {
					currMunka.setMunkaEstimatedTime(Long.parseLong(munkaJsonObj.getString("munkaEstimatedTime")));
				}
				else {
					currMunka.setMunkaEstimatedTime(null);
				}
				if(munkaJsonObj.get("munkaTipusID")!=null) {
					currMunka.setMunkaTipusID(Long.parseLong(munkaJsonObj.getString("munkaTipusID")));
				}
				else {
					currMunka.setMunkaTipusID(null);
				}
				if(munkaJsonObj.get("telephelyID")!=null) {
					currMunka.setTelephelyID(Long.parseLong(munkaJsonObj.getString("telephelyID")));
				}
				else {
					currMunka.setTelephelyID(null);
				}
				if(munkaJsonObj.get("partnerID")!=null) {
					currMunka.setPartnerID(Long.parseLong(munkaJsonObj.getString("partnerID")));
				}
				else {
					currMunka.setPartnerID(null);
				}				
				
				munkaList.add(currMunka);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return munkaList;
	}
}
