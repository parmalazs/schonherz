package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;

public class ProfilKepImageAdapter extends ArrayAdapter<ProfilKep> {

	Context context;
	ArrayList<ProfilKep> profilkepek;
	ProfilKepDao profilDao;
	
	
	public ProfilKepImageAdapter(Context context, ProfilKepDao profilDao, int textViewResourceId,
			List<ProfilKep> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.profilDao = profilDao;
		this.profilkepek = new ArrayList<ProfilKep>();
		profilkepek.addAll(objects);
	}


	@Override
	public void add(ProfilKep object) {
		// TODO Auto-generated method stub
		profilkepek.add(object);
	}


	@Override
	public void addAll(Collection<? extends ProfilKep> collection) {
		// TODO Auto-generated method stub
		profilkepek.addAll(collection);
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		profilkepek.clear();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return profilkepek.size();
	}


	@Override
	public ProfilKep getItem(int position) {
		// TODO Auto-generated method stub
		return profilkepek.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return profilkepek.get(position).getProfilKepID();
	}


	@Override
	public int getPosition(ProfilKep item) {
		// TODO Auto-generated method stub
		return profilkepek.indexOf(item);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		// Get a View to display image data 					
					ImageView iv = new ImageView(context);				
					Bitmap myBitmap = BitmapFactory.decodeFile(profilkepek.get(position).getProfilKepPath());
					iv.setImageBitmap(myBitmap);
					// Image should be scaled somehow
					//iv.setScaleType(ImageView.ScaleType.CENTER);
					//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
					//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
					//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
					iv.setScaleType(ImageView.ScaleType.FIT_XY);
					//iv.setScaleType(ImageView.ScaleType.FIT_END);
					iv.setBackgroundColor(Color.BLACK);
					// Set the Width & Height of the individual images
					iv.setLayoutParams(new Gallery.LayoutParams(130, 180));

					return iv;
		
	}


	@Override
	public void insert(ProfilKep object, int index) {
		// TODO Auto-generated method stub
		profilkepek.add(index, object);
	}


	@Override
	public void remove(ProfilKep object) {
		// TODO Auto-generated method stub
		profilkepek.remove(object);
	}
	
	
}
