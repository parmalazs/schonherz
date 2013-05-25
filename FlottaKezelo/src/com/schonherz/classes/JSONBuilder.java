package com.schonherz.classes;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

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

public class JSONBuilder {
	private static String URL = "http://www.flotta.host-ed.me/index.php";

	public JSONObject insertSofor(Sofor s) { // TESZTELVE +
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("soforId", s.getSoforID());
			object.put("soforNev", s.getSoforNev());

			object.put("soforCim", s.getSoforCim());
			object.put("soforTelefonszam", s.getSoforTelefonszam());
			object.put("soforLogin", s.getSoforLogin());
			object.put("soforPass", s.getSoforPass());
			object.put("soforBirthDate", s.getSoforBirthDate());
			object.put("soforRegTime", s.getSoforRegTime());
			if (s.getSoforIsAdmin()) {
				object.put("soforIsAdmin", "1");
			} else {
				object.put("soforIsAdmin", "0");
			}

			if (s.getSoforIsActive()) {
				object.put("soforIsActive", 1);
			} else {
				object.put("soforIsActive", 0);
			}
			object.put("soforEmail", s.getSoforEmail());
			object.put("soforProfilKepID", s.getSoforProfilKepID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "sofor");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertAuto(Auto a) { // TESZTELVE ADATBAZIS HIBA
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("autoId", a.getAutoID());
			if (a.getAutoFoglalt()) {
				object.put("autoFoglalt", 1);
			} else {
				object.put("autoFoglalt", 0);
			}
			object.put("autoXkoordinata", a.getAutoXkoordinata());
			object.put("autoYkoordinata", a.getAutoYkoordinata());
			object.put("autoNev", a.getAutoNev());
			object.put("autoTipus", a.getAutoTipus());
			object.put("autoRendszam", a.getAutoRendszam());
			object.put("autoProfilKepID", a.getAutoProfilKepID());
			object.put("autoKilometerOra", a.getAutoKilometerOra());
			object.put("autoUzemAnyag", a.getAutoUzemanyag());
			object.put("autoMuszakiVizsgaDate", a.getAutoMuszakiVizsgaDate());
			object.put("autoLastSzervizDate", a.getAutoLastSzervizDate());
			object.put("autoLastSoforID", a.getAutoLastSoforID());
			object.put("autoKilometerOra", a.getAutoKilometerOra());
			object.put("autoLastUpdate", a.getAutoLastUpDate());
			object.put("autoLastTelephelyID", a.getAutoLastTelephelyID());

			if (a.getAutoIsActive()) {
				object.put("autoIsActive", 1);
			} else {
				object.put("autoIsActive", 0);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "auto");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertAutoKep(AutoKep a) { // TESZTELVE ADATBAZIS HIBA

		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("autoKepId", a.getAutoID());
			object.put("autoKepPath", a.getAutoKepPath());
			object.put("autoKepDateTime", a.getAutoKepDateTime());
			object.put("autoID", a.getAutoID());

			if (a.getAutoKepIsActive()) {
				object.put("autoKepIsActive", 1);
			} else {
				object.put("autoKepIsActive", 0);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "autoKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertMunka(Munka a) {// TESZTELVE +

		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			// object.put("munkaId", a.getMunkaID());
			object.put("munkaKoltseg", a.getMunkaKoltseg());
			object.put("munkaBevetel", a.getMunkaBevetel());
			object.put("munkaUzemAnyagState", a.getMunkaUzemanyagState());
			object.put("munkaComment", a.getMunkaComment());
			object.put("munkaBefejezesDate", a.getMunkaBefejezesDate());
			object.put("munkaIsActive", a.getMunkaIsActive());
			object.put("munkaEstimatedTime", a.getMunkaEstimatedTime());
			object.put("munkaDate", a.getMunkaDate());
			object.put("soforID", a.getSoforID());
			object.put("partnerID", a.getPartnerID());
			object.put("telephelyID", a.getTelephelyID());
			object.put("munkaTipusID", a.getMunkaTipusID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "munka");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertMunkaEszkoz(MunkaEszkoz m) { // HIĂ�NYZIK A
															// MUNKAESZKOZ
															// OSZTĂ�LYBĂ“L A
															// munkaID !!!!!
															// FONTOS !!!!!!!!!
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaEszkozAr", m.getMunkaEszkozAr());
			object.put("munkaEszkozId", m.getMunkaEszkozID());
			object.put("munkaEszkozNev", m.getMunkaEszkozNev());
			object.put("munkaID", m.getMunkaID());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "munkaEszkoz");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertMunkaKep(MunkaKep mk) { // TESZTELVE +
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaKepId", mk.getMunkaKepID());
			object.put("munkaKepPath", mk.getMunkaKepPath());
			object.put("munkaKepDate", mk.getMunkaKepDate());
			object.put("munkaID", mk.getMunkaID());

			if (mk.getMunkaKepIsActive()) {
				object.put("munkaKepIsActive", 1);
			} else {
				object.put("munkaKepIsActive", 0);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "insert");
			ret.put("tableName", "munkaKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertMunkaTipus(MunkaTipus mt) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaTipusId", mt.getMunkaTipusID());
			object.put("munkaTipusNev", mt.getMunkaTipusNev());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("HIBA", "az objectnďż˝l");
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "insert");
			ret.put("tableName", "munkaTipus");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertPartner(Partner p) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("partnerId", p.getPartnerID());
			object.put("partnerCim", p.getPartnerCim());
			object.put("partnerEmailcim", p.getPartnerEmailcim());
			object.put("partnerNev", p.getPartnerNev());
			object.put("partnerTelefonszam", p.getPartnerTelefonszam());
			object.put("partnerWeboldal", p.getPartnerWeboldal());
			object.put("partnerXkoordinata", p.getPartnerXkoordinata());
			object.put("partnerYkoordinata", p.getPartnerYkoodinata());

			if (p.getPartnerIsActive()) {
				object.put("partnerIsActive", 1);
			} else {
				object.put("partnerIsActive", 0);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "insert");
			ret.put("tableName", "partner");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertPartnerKep(PartnerKep pk) { // EZ MĂ‰G NEM JĂ“
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("partnerKepId", pk.getPartnerKepID());
			object.put("partnerKepDate", pk.getPartnerKepDate());
			object.put("partnerpartnerID", pk.getPartnerID());
			// object.put("partnerKepIsUpLoaded",pk.getPartnerKep);

			if (pk.getPartnerKepIsActive()) {
				object.put("partnerKepIsActive", 1);
			} else {
				object.put("partnerKepIsActive", 0);
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "insert");
			ret.put("tableName", "partnerKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertProfilKep(ProfilKep prk) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("profilKepDateTime", prk.getProfilKepDateTime());
			object.put("profilKepId", prk.getProfilKepID());
			// object.put("profil",prk.getProfilkepIsUploaded());
			object.put("profilKepPath", prk.getProfilKepPath());
			object.put("soforID", prk.getSoforID());
			
			
			if (prk.getProfilKepIsActive()) {
				object.put("profilKepIsActive", 1);
			} else {
				object.put("profilKepIsActive", 0);
			}
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "insert");
			ret.put("tableName", "profilKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject insertTelephely(Telephely t) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("telephelyCim", t.getTelephelyCim());
			object.put("telephelyEmail", t.getTelephelyEmail());
			object.put("telephelyId", t.getTelephelyID());
			object.put("telephelyNev", t.getTelephelyNev());
			object.put("telephelyTelefonszam", t.getTelephelyTelefonszam());
			object.put("telephelyXkoordinata", t.getTelephelyXkoordinata());
			object.put("telephelyYkoordinata", t.getTelephelyYkoordinata());
			
			
			if (t.getTelephelyIsActive()) {
				object.put("telephelyIsActive", 1);
			} else {
				object.put("telephelyIsActive", 0);
			}
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "insert");
			ret.put("tableName", "telephely");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject deleteAuto(Auto a) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", a.getAutoID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "auto");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteAutoKep(AutoKep ak) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", ak.getAutoKepID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "autoKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteMunka(Munka m) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", m.getMunkaID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "munka");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteMunkaKep(MunkaKep mk) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", mk.getMunkaKepID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "munkaKep");
			ret.put("objects", object);
			Log.e("JSON: ", ret.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteMunkaTipus(MunkaTipus mt) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", mt.getMunkaTipusID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "munkaTipus");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deletePartner(Partner p) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", p.getPartnerID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "partner");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteProfilKep(ProfilKep prk) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", prk.getProfilKepID());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "profilKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject deleteTelephely(Telephely t) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", t.getTelephelyID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "telephely");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;

	}

	public JSONObject deleteSofor(Sofor s) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("id", s.getSoforID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "delete");
			ret.put("tableName", "sofor");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateSofor(Sofor s) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("soforId", s.getSoforID());
			object.put("soforNev", s.getSoforNev());

			object.put("soforCim", s.getSoforCim());
			object.put("soforTelefonszam", s.getSoforTelefonszam());
			object.put("soforLogin", s.getSoforLogin());
			object.put("soforPass", s.getSoforPass());
			object.put("soforBirthDate", s.getSoforBirthDate());
			object.put("soforRegTime", s.getSoforRegTime());
			if (s.getSoforIsAdmin()) {
				object.put("soforIsAdmin", "1");
			} else {
				object.put("soforIsAdmin", "0");
			}
			object.put("soforEmail", s.getSoforEmail());
			object.put("soforProfilKepID", s.getSoforProfilKepID());
			Log.e("SOFOR", object.toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "update");
			ret.put("tableName", "sofor");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateAuto(Auto a) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("autoId", a.getAutoID());
			if (a.getAutoFoglalt()) {
				object.put("autoFoglalt", 1);
			} else {
				object.put("autoFoglalt", 0);
			}
			object.put("autoXkoordinata", a.getAutoXkoordinata());
			object.put("autoYkoordinata", a.getAutoYkoordinata());
			object.put("autoNev", a.getAutoNev());
			object.put("autoTipus", a.getAutoTipus());
			object.put("autoRendszam", a.getAutoRendszam());
			object.put("autoProfilKepID", a.getAutoProfilKepID());
			object.put("autoKilometerOra", a.getAutoKilometerOra());
			object.put("autoUzemAnyag", a.getAutoUzemanyag());
			object.put("autoMuszakiVizsgaDate", a.getAutoMuszakiVizsgaDate());
			object.put("autoLastSzervizDate", a.getAutoLastSzervizDate());
			object.put("autoLastSoforID", a.getAutoLastSoforID());
			object.put("autoKilometerOra", a.getAutoKilometerOra());
			object.put("autoLastUpdate", a.getAutoLastUpDate());
			object.put("autoLastTelephelyID", a.getAutoLastTelephelyID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "update");
			ret.put("tableName", "auto");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateAutoKep(AutoKep ak) {

		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("autoKepId", ak.getAutoKepID());
			object.put("autoKepPath", ak.getAutoKepPath());
			object.put("autoKepDateTime", ak.getAutoKepDateTime());
			object.put("autoID", ak.getAutoID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "update");
			ret.put("tableName", "autoKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateMunka(Munka m) {

		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaId", m.getMunkaID());
			object.put("munkaKoltseg", m.getMunkaKoltseg());
			object.put("munkaBevetel", m.getMunkaBevetel());
			object.put("munkaUzemAnyagState", m.getMunkaUzemanyagState());
			object.put("munkaComment", m.getMunkaComment());
			object.put("munkaBefejezesDate", m.getMunkaBefejezesDate());
			object.put("munkaIsActive", m.getMunkaIsActive());
			object.put("munkaEstimatedTime", m.getMunkaEstimatedTime());
			object.put("munkaDate", m.getMunkaDate());
			object.put("soforID", m.getSoforID());
			object.put("partnerID", m.getPartnerID());
			object.put("telephelyID", m.getTelephelyID());
			object.put("munkaTipusID", m.getMunkaTipusID());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "update");
			ret.put("tableName", "munka");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	// public JSONObject updateMunkaKep(MunkaKep mk) {
	// JSONObject ret = new JSONObject();
	// JSONObject object = new JSONObject();
	// try {
	// object.put("soforID", m.getMunkaEszkozAr());
	//
	// } catch (JSONException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// try {
	// ret.put("action","update");
	// ret.put("tableName", "sofor");
	// ret.put("objects",object);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return ret;
	// }

	public JSONObject updateMunkaKep(MunkaKep mk) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaKepId", mk.getMunkaKepID());
			object.put("munkaKepPath", mk.getMunkaKepPath());
			object.put("munkaKepDate", mk.getMunkaKepDate());
			object.put("munkaID", mk.getMunkaID());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ret.put("action", "update");
			ret.put("tableName", "munkaKep");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateMunkaTipus(MunkaTipus mt) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("munkaTipusId", mt.getMunkaTipusID());
			object.put("munkaTipusNev", mt.getMunkaTipusNev());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.e("HIBA", "az objectnel");
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "update");
			ret.put("tableName", "munkaTipus");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updatePartner(Partner p) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("partnerId", p.getPartnerID());
			object.put("partnerCim", p.getPartnerCim());
			object.put("partnerEmailcim", p.getPartnerEmailcim());
			object.put("partnerNev", p.getPartnerNev());
			object.put("partnerTelefonszam", p.getPartnerTelefonszam());
			object.put("partnerWeboldal", p.getPartnerWeboldal());
			object.put("partnerXkoordinata", p.getPartnerXkoordinata());
			object.put("partnerYkoordinata", p.getPartnerYkoodinata());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "update");
			ret.put("tableName", "partner");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public JSONObject updateTelephely(Telephely t) {
		JSONObject ret = new JSONObject();
		JSONObject object = new JSONObject();
		try {
			object.put("telephelyCim", t.getTelephelyCim());
			object.put("telephelyEmail", t.getTelephelyEmail());
			object.put("telephelyId", t.getTelephelyID());
			object.put("telephelyNev", t.getTelephelyNev());
			object.put("telephelyTelefonszam", t.getTelephelyTelefonszam());
			object.put("telephelyXkoordinata", t.getTelephelyXkoordinata());
			object.put("telephelyYkoordinata", t.getTelephelyYkoordinata());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.e("OBJECT: ", object.toString());
		try {
			ret.put("action", "update");
			ret.put("tableName", "telephely");
			ret.put("objects", object);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	// public JSONObject insert(Object o) throws JSONException{
	// Field[] fields = o.getClass().getDeclaredFields();
	// JSONObject ret = new JSONObject();
	// SoforDao sd = new SoforDao();
	// sd.getPkColumns();
	// for (Field field : fields) {
	// //System.out.println("Field name = " + field.getName());
	// //System.out.println("Field type = " + field.getType().getName());
	// ret.put(field.getName(), field.getType());
	// }
	// ret.put("FieldNum:"," "+fields.length);
	// ret.put("Object neve:", o.getClass());
	// return ret;
	// }

}