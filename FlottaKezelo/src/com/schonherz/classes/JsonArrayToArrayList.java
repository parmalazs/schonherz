package com.schonherz.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaEszkoz;
import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaTipus;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.ProfilKep;
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
				if(soforJsonObj.getString("soforIsAdmin").equals("1")) {
					currSofor.setSoforIsAdmin(true);
				}
				else {
					currSofor.setSoforIsAdmin(false);
				}
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
				if(autoJsonObj.getString("autoFoglalt").equals("1")) {
					currAuto.setAutoFoglalt(true);
				}
				else{
					currAuto.setAutoFoglalt(false);
				}
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
				if(munkaJsonObj.get("munkaIsActive").equals("1")) {
					currMunka.setMunkaIsActive(true);
				}
				else {
					currMunka.setMunkaIsActive(false);
				}
				if(munkaJsonObj.get("munkaEstimatedTime")!=null) {
					currMunka.setMunkaEstimatedTime(Long.parseLong(munkaJsonObj.getString("munkaEstimatedTime")));
				}
				else {
					currMunka.setMunkaEstimatedTime(null);
				}
				if(munkaJsonObj.get("munkatipusID")!=null) {
					currMunka.setMunkaTipusID(Long.parseLong(munkaJsonObj.getString("munkatipusID")));
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
	
	public static ArrayList<AutoKep> JsonArrayToAutoKepek(JSONArray jsonArray) {
		ArrayList<AutoKep> autoKepList = new ArrayList<AutoKep>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject autoKepJsonObj = null;

				autoKepJsonObj = jsonArray.getJSONObject(i);

				// New AutoKep
				AutoKep currAutoKep = new AutoKep();
				// set properties from jsonobject
				
				currAutoKep.setAutoKepID(Long.parseLong(autoKepJsonObj.getString("autoKepID")));
				currAutoKep.setAutoKepPath(autoKepJsonObj.getString("autoKepPath"));
				currAutoKep.setAutoKepDateTime(autoKepJsonObj.getString("autoKepDateTime"));
				if(autoKepJsonObj.get("autoID")!=null){
					currAutoKep.setAutoID(Long.parseLong(autoKepJsonObj.getString("autoID")));
				}
				else {
					currAutoKep.setAutoID(null);
				}
				autoKepList.add(currAutoKep);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return autoKepList;
	}
	
	public static ArrayList<ProfilKep> JsonArrayToProfilKepek(JSONArray jsonArray) {
		ArrayList<ProfilKep> profilKepList = new ArrayList<ProfilKep>();
		for (int i = 0; i < jsonArray.length(); i++) {

			try {
				JSONObject profilKepJsonObj = null;

				profilKepJsonObj = jsonArray.getJSONObject(i);

				// New AutoKep
				ProfilKep currProfilKep = new ProfilKep();
				// set properties from jsonobject
				
				currProfilKep.setProfilKepID(Long.parseLong(profilKepJsonObj.getString("profilKepID")));
				currProfilKep.setProfilKepPath(profilKepJsonObj.getString("profilKepPath"));
				currProfilKep.setProfilKepDateTime(profilKepJsonObj.getString("profilKepDateTime"));
				if(profilKepJsonObj.getString("soforID")!=null) {
					currProfilKep.setSoforID(Long.parseLong(profilKepJsonObj.getString("soforID")));
				}
				else {
					currProfilKep.setSoforID(null);
				}
				profilKepList.add(currProfilKep);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return profilKepList;
	}
	
	public static ArrayList<MunkaTipus> JsonArrayToMunkaTipusok(JSONArray jsonArray) {
		ArrayList<MunkaTipus> munkaTipusList=new ArrayList<MunkaTipus>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject munkaTipusJsonObj = null;

				munkaTipusJsonObj = jsonArray.getJSONObject(i);

				// New MunkaTipus
				MunkaTipus currMunkaTipus = new MunkaTipus();
				// set properties from jsonobject
				
				currMunkaTipus.setMunkaTipusID(Long.parseLong(munkaTipusJsonObj.getString("munkaTipusID")));
				currMunkaTipus.setMunkaTipusNev(munkaTipusJsonObj.getString("munkaTipusNev"));
				
				munkaTipusList.add(currMunkaTipus);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return munkaTipusList;
	}
	
	public static ArrayList<MunkaEszkoz> JsonArrayToMunkaEszkozok(JSONArray jsonArray) {
		ArrayList<MunkaEszkoz> munkaEszkozList=new ArrayList<MunkaEszkoz>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject munkaEszkozJsonObj = null;

				munkaEszkozJsonObj = jsonArray.getJSONObject(i);

			
				MunkaEszkoz currMunkaEszkoz = new MunkaEszkoz();
				// set properties from jsonobject
				currMunkaEszkoz.setMunkaEszkozID(Long.parseLong(munkaEszkozJsonObj.getString("munkaEszkozID")));
				currMunkaEszkoz.setMunkaEszkozNev(munkaEszkozJsonObj.getString("munkaEszkozNev"));
				if(munkaEszkozJsonObj.getString("munkaEszkozAr")!=null) {
					currMunkaEszkoz.setMunkaEszkozAr(Long.parseLong(munkaEszkozJsonObj.getString("munkaEszkozAr")));
				}
				else {
					currMunkaEszkoz.setMunkaEszkozAr(null);
				}
				if(munkaEszkozJsonObj.getString("munkaID")!=null) {
					currMunkaEszkoz.setMunkaID(Long.parseLong(munkaEszkozJsonObj.getString("munkaID")));
				}
				else {
					currMunkaEszkoz.setMunkaID(null);
				}
				munkaEszkozList.add(currMunkaEszkoz);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return munkaEszkozList;
	}
	
	public static ArrayList<MunkaKep> JsonArrayToMunkaKepek(JSONArray jsonArray) {
		ArrayList<MunkaKep> munkaKepList=new ArrayList<MunkaKep>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject munkaKepJsonObj = null;

				munkaKepJsonObj = jsonArray.getJSONObject(i);

				MunkaKep currMunkaKep=new MunkaKep();
				
				currMunkaKep.setMunkaKepID(Long.parseLong(munkaKepJsonObj.getString("munkaKepID")));
				currMunkaKep.setMunkaKepPath(munkaKepJsonObj.getString("munkaKepPath"));
				currMunkaKep.setMunkaKepDate(munkaKepJsonObj.getString("munkaKepDate"));
				if(munkaKepJsonObj.getString("munkaID")!=null) {
					currMunkaKep.setMunkaID(Long.parseLong(munkaKepJsonObj.getString("munkaID")));
				}
				else {
					currMunkaKep.setMunkaID(null);
				}
				
				munkaKepList.add(currMunkaKep);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return munkaKepList;
	} 
	
	public static ArrayList<PartnerKep> JsonArrayToPartnerKepek(JSONArray jsonArray) {
		ArrayList<PartnerKep> partnerKepList=new ArrayList<PartnerKep>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject partnerKepJsonObj = null;

				partnerKepJsonObj = jsonArray.getJSONObject(i);

				PartnerKep currPartnerKep=new PartnerKep();
				
				currPartnerKep.setPartnerKepID(Long.parseLong(partnerKepJsonObj.getString("partnerKepID")));
				if(partnerKepJsonObj.getString("partnerKepIsUploaded").equals("1")) {
					currPartnerKep.setPartnerKepIsUploaded(true);
				}
				else {
					currPartnerKep.setPartnerKepIsUploaded(false);
				}
				currPartnerKep.setPartnerKepDate(partnerKepJsonObj.getString("partnerKepDate"));
				if(partnerKepJsonObj.getString("partnerID")!=null) {
					currPartnerKep.setPartnerID(Long.parseLong(partnerKepJsonObj.getString("partnerID")));
				}
				else {
					currPartnerKep.setPartnerID(null);
				}			
				
				
				partnerKepList.add(currPartnerKep);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return partnerKepList;
	} 
}
