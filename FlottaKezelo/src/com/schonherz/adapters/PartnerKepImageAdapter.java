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

import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;

public class PartnerKepImageAdapter extends ArrayAdapter<PartnerKep> {
	
	PartnerKepDao partnerKepDao;
	ArrayList<PartnerKep> partnerKepek;
	Context context;
	
	
	
	public PartnerKepImageAdapter(Context context, PartnerKepDao partnerKepDao,int textViewResourceId,
			List<PartnerKep> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.partnerKepek = new ArrayList<PartnerKep>();
		partnerKepek.addAll(objects);
		this.partnerKepDao = partnerKepDao;
		
	}



	@Override
	public void add(PartnerKep object) {
		// TODO Auto-generated method stub
		partnerKepek.add(object);
	}



	@Override
	public void addAll(Collection<? extends PartnerKep> collection) {
		// TODO Auto-generated method stub
		partnerKepek.addAll(collection);
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		partnerKepek.clear();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partnerKepek.size();
	}



	@Override
	public PartnerKep getItem(int position) {
		// TODO Auto-generated method stub
		return partnerKepek.get(position);
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return partnerKepek.get(position).getPartnerKepID();
	}



	@Override
	public int getPosition(PartnerKep item) {
		// TODO Auto-generated method stub
		return partnerKepek.indexOf(item);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		// Get a View to display image data 					
					ImageView iv = new ImageView(context);				
					Bitmap myBitmap = BitmapFactory.decodeFile(partnerKepek.get(position).getPartnerKepPath());
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

					int mPaddingInPixels;
					final float scale = context.getResources().getDisplayMetrics().density;
					mPaddingInPixels = (int) (5 * scale + 0.5f);
					mPaddingInPixels = mPaddingInPixels + 5;
					iv.setPadding(mPaddingInPixels, mPaddingInPixels, mPaddingInPixels, mPaddingInPixels);
					
					return iv;
		
	}



	@Override
	public void insert(PartnerKep object, int index) {
		// TODO Auto-generated method stub
		partnerKepek.add(index, object);
	}



	@Override
	public void remove(PartnerKep object) {
		// TODO Auto-generated method stub
		partnerKepek.remove(object);
	}

}
