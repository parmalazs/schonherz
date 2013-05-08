package com.schonherz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKepDao;

public class AdminKepekFragment extends Fragment {

	Context context;
	PartnerKepDao partnerKepDao;
	ProfilKepDao profilKepDao;
	AutoKepDao autoKepDao;
	MunkaKepDao munkaKepDao;

	public AdminKepekFragment(Context context, PartnerKepDao partnerKepDao,
			ProfilKepDao profilKepDao, AutoKepDao autoKepDao,
			MunkaKepDao munkaKepDao) {
		
		this.context = context;
		this.partnerKepDao = partnerKepDao;
		this.autoKepDao = autoKepDao;
		this.profilKepDao = profilKepDao;
		this.munkaKepDao = munkaKepDao;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
