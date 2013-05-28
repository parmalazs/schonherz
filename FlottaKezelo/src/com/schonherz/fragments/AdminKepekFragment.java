package com.schonherz.fragments;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.schonherz.adapters.AutoKepImageAdapter;
import com.schonherz.adapters.MunkaKepImageAdapter;
import com.schonherz.adapters.PartnerKepImageAdapter;
import com.schonherz.adapters.ProfilKepImageAdapter;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.flottadroid.R;

public class AdminKepekFragment extends Fragment {

	Context context;
	PartnerKepDao partnerKepDao;
	ProfilKepDao profilKepDao;
	AutoKepDao autoKepDao;
	MunkaKepDao munkaKepDao;

	ProfilKepImageAdapter profilImgAdapter;
	PartnerKepImageAdapter partnerImgAdapter;
	MunkaKepImageAdapter munkaKepImgAdapter;
	AutoKepImageAdapter autoKepImgAdapter;
	
	Gallery soforPicsGallery;
	Gallery partnerPicsGallery;
	Gallery munkaPicsGallery;
	Gallery autoPicsGallery;
	
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
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View v = inflater.inflate(R.layout.layout_admin_pictures, null);
		
		List<ProfilKep> profilkepek = profilKepDao.loadAll();				
		List<AutoKep> autoKepek = autoKepDao.loadAll();
		List<PartnerKep> partnerKepek = partnerKepDao.loadAll();
		List<MunkaKep> munkaKepek = munkaKepDao.loadAll();
		
		partnerPicsGallery = (Gallery)v.findViewById(R.id.partnerGallery);
		partnerImgAdapter = new PartnerKepImageAdapter(context, partnerKepDao, 0, partnerKepek);
		partnerPicsGallery.setAdapter(partnerImgAdapter);
				
		munkaPicsGallery = (Gallery)v.findViewById(R.id.munkakGallery);
		munkaKepImgAdapter = new MunkaKepImageAdapter(context, munkaKepDao, 0, munkaKepek);
		munkaPicsGallery.setAdapter(munkaKepImgAdapter);
		
		autoPicsGallery = (Gallery)v.findViewById(R.id.autokGallery);
		autoKepImgAdapter = new AutoKepImageAdapter(context, autoKepDao, 0, autoKepek);
		autoPicsGallery.setAdapter(autoKepImgAdapter);
		
		soforPicsGallery = (Gallery)v.findViewById(R.id.soforPicsGallery);
		profilImgAdapter = new ProfilKepImageAdapter(context, profilKepDao, 0, profilkepek);
		
		soforPicsGallery.setAdapter(profilImgAdapter);
		
		soforPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				 	
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.layout_image_dialog);
					dialog.setCancelable(true);
									   
				    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
				    
				    Bitmap bm = BitmapFactory.decodeFile(((ProfilKep)parent.getItemAtPosition(pos)).getProfilKepPath());
				    currProfIv.setImageBitmap(bm);
				    				  
				    dialog.setTitle(((ProfilKep)parent.getItemAtPosition(pos)).getSofor().getSoforNev());				    
				    
				    currProfIv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
				    
				    currProfIv.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							return false;
						}
					});
				    				   
				    dialog.show();
			}
			
		});
		
		return v;
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
