/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
    	//ez lesz a package neve
        Schema schema = new Schema(3, "com.schonherz.dbentities");

        AddFlottaDroidTables(schema);
        //Oda ird be a gepeden talalhato project pathjat!
        new DaoGenerator().generateAll(schema, "C:/Users/rcsk/git/flotta/schonherz/FlottaKezelo/src");
    }

    private static void AddFlottaDroidTables(Schema schema)
    {
    	

    	//Sofőr tábla
    	Entity sofor = schema.addEntity("Sofor");
    	sofor.setTableName("Soforok");
    	sofor.addLongProperty("soforID").primaryKey();
    	sofor.addStringProperty("soforNev");
    	sofor.addStringProperty("soforCim");
    	sofor.addStringProperty("soforTelefonszam");
    	sofor.addStringProperty("soforLogin");
    	sofor.addStringProperty("soforPass");
    	sofor.addStringProperty("soforBirthDate");
    	sofor.addStringProperty("soforRegTime");
    	sofor.addBooleanProperty("soforIsAdmin");
    	sofor.addStringProperty("soforEmail");    	    	
    	
    	//Profilkép tábla
    	Entity profilKep = schema.addEntity("ProfilKep");
    	profilKep.setTableName("Profilkepek");
    	profilKep.addLongProperty("profilKepID").primaryKey();
    	profilKep.addStringProperty("profilKepPath");
    	profilKep.addStringProperty("profilKepDateTime");
    	profilKep.addBooleanProperty("profilkepIsUploaded");
    	
	    	
	    	Property pictureSofor = profilKep.addLongProperty("soforID").getProperty();
	    	//Profilkép sofőrje
	    	profilKep.addToOne(sofor, pictureSofor);
	    	//Sofőr profilképei
	    	ToMany soforToPictures = sofor.addToMany(profilKep, pictureSofor);
	    		    	    
	    	Property soforProfilePicture = sofor.addLongProperty("soforProfilKepID").getProperty();
	    	//Sofőr beállított profilképe
	    	sofor.addToOne(profilKep, soforProfilePicture);
	    	
	    	//Autó tábla
	    Entity auto = schema.addEntity("Auto");
	    auto.setTableName("Autok");
	    auto.addLongProperty("autoID").primaryKey();
	    auto.addFloatProperty("autoXkoordinata");
	    auto.addFloatProperty("autoYkoordinata");
	    auto.addStringProperty("autoNev");
	    auto.addStringProperty("autoTipus");
	    auto.addStringProperty("autoRendszam");
	    auto.addLongProperty("autoKilometerOra");
	    auto.addLongProperty("autoUzemanyag");
	    auto.addStringProperty("autoMuszakiVizsgaDate");
	    auto.addStringProperty("autoLastSzervizDate");
	    auto.addStringProperty("autoLastUpDate");
	    auto.addBooleanProperty("autoFoglalt");
	    	
			
	    	Property autoLastSoforID =auto.addLongProperty("autoLastSoforID").getProperty();
	    	//Autó Lastsofőrje
	    	auto.addToOne(sofor, autoLastSoforID);
	    		    
	    //Autókép tábla
    	Entity autoKep = schema.addEntity("AutoKep");
    	autoKep.setTableName("Autokepek");
    	autoKep.addLongProperty("autoKepID").primaryKey();
    	autoKep.addStringProperty("autoKepPath");
    	autoKep.addStringProperty("autoKepDateTime");
    	autoKep.addBooleanProperty("autoKepIsUploaded");
    	
    		
    		Property autoKepAuto = autoKep.addLongProperty("autoID").getProperty();
    		//Autókép autója
    		autoKep.addToOne(auto, autoKepAuto);
    		//Autó autóképei
    		ToMany autoToautoKepek = auto.addToMany(autoKep, autoKepAuto);
    	
    		
    		Property autoProfilKep = auto.addLongProperty("autoProfilKepID").getProperty();
    		//Autó profilképe
    		auto.addToOne(autoKep, autoProfilKep);
    		
    	//Munkatipus tábla
        Entity munkaTipus = schema.addEntity("MunkaTipus");
        munkaTipus.setTableName("Munkatipusok");
    	munkaTipus.addLongProperty("munkaTipusID").primaryKey();
    	munkaTipus.addStringProperty("munkaTipusNev");
        
    	//Partner tábla
    	Entity partner = schema.addEntity("Partner");
    	partner.setTableName("Partnerek");
    	partner.addLongProperty("partnerID").primaryKey();
    	partner.addStringProperty("partnerNev");
    	partner.addStringProperty("partnerCim");
    	partner.addFloatProperty("partnerXkoordinata");
    	partner.addFloatProperty("partnerYkoodinata");
    	partner.addStringProperty("partnerTelefonszam");
    	partner.addStringProperty("partnerWeboldal");
    	partner.addStringProperty("partnerEmailcim");
    	
    	//Partnerkép tábla
    	Entity partnerKep = schema.addEntity("PartnerKep");
    	partnerKep.setTableName("Partnerkepek");
    	partnerKep.addLongProperty("partnerKepID").primaryKey();
    	partnerKep.addStringProperty("partnerNev");
    	partnerKep.addStringProperty("partnerKepDate");
    	partnerKep.addBooleanProperty("partnerKepIsUploaded");
    	    		
    		Property partnerKepPartner = partnerKep.addLongProperty("partnerID").getProperty();
    		//Partnerkép partnere
    		partnerKep.addToOne(partner, partnerKepPartner);
    		//Partner partnerképei
    		ToMany partnerPartnerkepe = partner.addToMany(partnerKep, partnerKepPartner);
    	   
    	//Telephely tábla
        Entity telephely = schema.addEntity("Telephely");
        telephely.setTableName("Telephelyek");
        telephely.addLongProperty("telephelyID").primaryKey();
        telephely.addStringProperty("telephelyNev");
        telephely.addStringProperty("telephelyCim");
        telephely.addStringProperty("telephelyTelefonszam");
        telephely.addFloatProperty("telephelyXkoordinata");
        telephely.addFloatProperty("telephelyYkoordinata");
        telephely.addStringProperty("telephelyEmail");
        
        	
        	Property autoLastTelephely = auto.addLongProperty("autoLastTelephelyID").getProperty();
        	//Autó telephelye
        	auto.addToOne(telephely, autoLastTelephely);
        	//Telephelyen talalhato autok
        	ToMany telephelyAutoi = telephely.addToMany(auto, autoLastTelephely);
        
    	//Munkaeszköz tábla
    	Entity munkaEszkoz = schema.addEntity("MunkaEszkoz");
    	munkaEszkoz.setTableName("MunkaEszkozok");    	
    	munkaEszkoz.addLongProperty("munkaEszkozID").primaryKey();
    	munkaEszkoz.addStringProperty("munkaEszkozNev");
    	munkaEszkoz.addLongProperty("munkaEszkozAr");
        
    		    	
    	//Munka tábla
        Entity munka = schema.addEntity("Munka");
        munka.setTableName("Munkak");
        munka.addLongProperty("munkaID").primaryKey();
        munka.addLongProperty("munkaKoltseg");
        munka.addLongProperty("munkaBevetel");
        munka.addLongProperty("munkaUzemanyagState");
        munka.addStringProperty("munkaComment");
        munka.addStringProperty("munkaBefejezesDate");
        munka.addBooleanProperty("munkaIsActive");
        munka.addLongProperty("munkaEstimatedTime");
    	munka.addStringProperty("munkaDate");
    	
    		
    		Property munkaSofor = munka.addLongProperty("soforID").getProperty();
    		//Munka sofőrje
    		munka.addToOne(sofor, munkaSofor);
    		//Sofőr munkái
    		ToMany soformunka = sofor.addToMany(munka, munkaSofor);
    		    		
    		Property munkaPartner = munka.addLongProperty("partnerID").getProperty();
    		//Munka partnere
    		munka.addToOne(partner, munkaPartner);    		
    		//Partner munkai
    		ToMany partnerMunka = partner.addToMany(munka, munkaPartner);
    		
    		Property munkaTelephely = munka.addLongProperty("telephelyID").getProperty();
    		//Munka telephelye
    		munka.addToOne(telephely,munkaTelephely);
    		//Telephely munkái
    		ToMany telephelyMunka = telephely.addToMany(munka, munkaTelephely);
    		
    		Property munkaMunkaTipus = munka.addLongProperty("munkaTipusID").getProperty();
    		//Munka munkatipusa
    		munka.addToOne(munkaTipus, munkaMunkaTipus);
    		//Munkatipusok munkai
    		ToMany munkatipusokMunkai = munkaTipus.addToMany(munka, munkaMunkaTipus);
    		    		
    		Property munkaEszkozMunkaID = munkaEszkoz.addLongProperty("munkaID").getProperty();
    		//Munkaeszkoz munka
    		munkaEszkoz.addToOne(munka, munkaEszkozMunkaID);
    		//Munka munkaeszkozei
    		ToMany munkaEszkozei = munka.addToMany(munkaEszkoz, munkaEszkozMunkaID);
    		
    		
    	//Munkakép tábla
    	Entity munkaKep = schema.addEntity("MunkaKep");
    	munkaKep.setTableName("Munkakepek");
    	munkaKep.addLongProperty("munkaKepID").primaryKey();
    	munkaKep.addStringProperty("munkaKepPath");
    	munkaKep.addStringProperty("munkaKepDate");
    	munkaKep.addBooleanProperty("munkaKepIsUploaded");   	
    	
    		Property munkakepMunka = munkaKep.addLongProperty("munkaID").getProperty();
    		//Munkakep munkaja
    		munkaKep.addToOne(munka, munkakepMunka);
    		//Munka képei
    		ToMany munkaMunkakepei = munka.addToMany(munkaKep, munkakepMunka);
               	  
   }
} 
   