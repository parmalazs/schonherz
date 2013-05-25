package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;

public class ProfilKepImageAdapter extends ArrayAdapter<ProfilKep> {

	Context context;
	ArrayList<ProfilKep> profilkepek;
	ProfilKepDao profilDao;
	
	
	public ProfilKepImageAdapter(Context context, int textViewResourceId,
			List<ProfilKep> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

}
