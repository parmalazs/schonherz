package com.schonherz.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Environment;
import au.com.bytecode.opencsv.CSVWriter;

import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.Telephely;

public class CSVUtil {

	public Uri createMunkaCSVFile(ArrayList<Munka> munkak) {
		Uri res = null;

		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root.getAbsolutePath() + "/FlottaDroid");
			dir.mkdirs();
			file = new File(dir, "Jobs.csv");

			try {
				ArrayList<String[]> allData = new ArrayList<String[]>();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-16"), '\t', '\'');

				for (int i = 0; i < munkak.size() + 1; i++) {
					if (i == 0) {
						String[] header = {"ID", "Munka idõ","Munkatipus","Becsült idõ",
								"Munka vége", "Partner", "Telephely",
								"Sofõr", "Bevétel", "Költség"};
						allData.add(header);
					} else {
						String[] curr = {munkak.get(i-1).getMunkaID().toString(),
								munkak.get(i-1).getMunkaDate(),
								munkak.get(i-1).getMunkaTipus().getMunkaTipusNev(),
								munkak.get(i-1).getMunkaEstimatedTime().toString(),								
								munkak.get(i-1).getMunkaBefejezesDate(),
								munkak.get(i-1).getPartner().getPartnerNev(),
								munkak.get(i-1).getTelephely().getTelephelyNev(),
								munkak.get(i-1).getSofor().getSoforNev(),
								munkak.get(i-1).getMunkaBevetel().toString(),
								munkak.get(i-1).getMunkaKoltseg().toString()
						};									
						allData.add(curr);

					}
				}
				writer.writeAll(allData);
				writer.close();
				res = Uri.fromFile(file);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	public Uri createSoforCSVFile(ArrayList<Sofor> soforok) {
		Uri res = null;

		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root.getAbsolutePath() + "/FlottaDroid");
			dir.mkdirs();
			file = new File(dir, "Drivers.csv");

			try {
				ArrayList<String[]> allData = new ArrayList<String[]>();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-16"), '\t', '\'');

				for (int i = 0; i < soforok.size() + 1; i++) {
					if (i == 0) {
						String[] header = {"ID", "Név", "Születési idõ", "Cím",
								"Telefonszám", "E-mail", "Regisztráció ideje",
								"Admin"};
						allData.add(header);
					} else {
						String[] curr = {
								soforok.get(i - 1).getSoforID().toString(),
								soforok.get(i - 1).getSoforNev(),
								soforok.get(i - 1).getSoforBirthDate(),
								soforok.get(i - 1).getSoforCim(),
								soforok.get(i - 1).getSoforTelefonszam(),
								soforok.get(i - 1).getSoforEmail(),
								soforok.get(i - 1).getSoforRegTime(),
								soforok.get(i - 1).getSoforIsAdmin().toString()};
						allData.add(curr);

					}
				}
				writer.writeAll(allData);
				writer.close();
				res = Uri.fromFile(file);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public Uri createTelephelyCSVFile(ArrayList<Telephely> telephelyek) {
		Uri res = null;

		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root.getAbsolutePath() + "/FlottaDroid");
			dir.mkdirs();
			file = new File(dir, "Stores.csv");

			try {
				ArrayList<String[]> allData = new ArrayList<String[]>();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-16"), '\t', '\'');

				for (int i = 0; i < telephelyek.size() + 1; i++) {
					if (i == 0) {
						String[] header = {"ID", "Név", "Cím", "Telefonszám",
								"X koord", "Y koord", "E-mail"};
						allData.add(header);
					} else {
						String[] curr = {
								telephelyek.get(i - 1).getTelephelyID()
										.toString(),
								telephelyek.get(i - 1).getTelephelyNev(),
								telephelyek.get(i - 1).getTelephelyCim(),
								telephelyek.get(i - 1)
										.getTelephelyTelefonszam(),
								telephelyek.get(i - 1)
										.getTelephelyXkoordinata().toString(),
								telephelyek.get(i - 1)
										.getTelephelyYkoordinata().toString(),
								telephelyek.get(i - 1).getTelephelyEmail()};
						allData.add(curr);

					}
				}
				writer.writeAll(allData);
				writer.close();
				res = Uri.fromFile(file);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public Uri createAutoCSVFile(ArrayList<Auto> autok) {
		Uri res = null;

		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root.getAbsolutePath() + "/FlottaDroid");
			dir.mkdirs();
			file = new File(dir, "Cars.csv");

			try {
				ArrayList<String[]> allData = new ArrayList<String[]>();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-16"), '\t', '\'');

				for (int i = 0; i < autok.size() + 1; i++) {
					if (i == 0) {
						String[] header = {"ID", "Név", "Típus", "Rendszám",
								"X koord", "Y koord", "Üzemanyag",
								"Kilométer óra", "Mûszaki vizsga",
								"Szervíz idõ", "Foglalt", "Telephely", "Sofõr"};
						allData.add(header);
					} else {
						String[] curr = {
								autok.get(i - 1).getAutoID().toString(),
								autok.get(i - 1).getAutoNev(),
								autok.get(i - 1).getAutoTipus(),
								autok.get(i - 1).getAutoXkoordinata()
										.toString(),
								autok.get(i - 1).getAutoYkoordinata()
										.toString(),
								autok.get(i - 1).getAutoUzemanyag().toString(),
								autok.get(i - 1).getAutoKilometerOra()
										.toString(),
								autok.get(i - 1).getAutoMuszakiVizsgaDate(),
								autok.get(i - 1).getAutoLastSzervizDate(),
								autok.get(i - 1).getAutoFoglalt().toString(),
								autok.get(i - 1).getAutoLastTelephelyID()
										.toString(),
								autok.get(i - 1).getAutoLastSoforID()
										.toString()};
						allData.add(curr);

					}
				}
				writer.writeAll(allData);
				writer.close();
				res = Uri.fromFile(file);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public Uri createPartnerCSVFile(ArrayList<Partner> partnerek) {
		Uri res = null;

		File file = null;
		File root = Environment.getExternalStorageDirectory();
		if (root.canWrite()) {
			File dir = new File(root.getAbsolutePath() + "/FlottaDroid");
			dir.mkdirs();
			file = new File(dir, "Partners.csv");

			try {
				ArrayList<String[]> allData = new ArrayList<String[]>();
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(
						new FileOutputStream(file), "UTF-16"), '\t', '\'');

				for (int i = 0; i < partnerek.size() + 1; i++) {
					if (i == 0) {
						String[] header = {"ID", "Név", "Cím", "Telefonszám",
								"X koord", "Y koord", "Web", "E-mail"};
						allData.add(header);
					} else {
						String[] curr = {
								partnerek.get(i - 1).getPartnerID().toString(),
								partnerek.get(i - 1).getPartnerNev(),
								partnerek.get(i - 1).getPartnerCim(),
								partnerek.get(i - 1).getPartnerEmailcim(),
								partnerek.get(i - 1).getPartnerXkoordinata()
										.toString(),
								partnerek.get(i - 1).getPartnerYkoodinata()
										.toString(),
								partnerek.get(i - 1).getPartnerWeboldal(),
								partnerek.get(i - 1).getPartnerEmailcim()};
						allData.add(curr);

					}
				}
				writer.writeAll(allData);
				writer.close();
				res = Uri.fromFile(file);
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return res;
	}
}
