package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class AutoKepImageAdapter extends ArrayAdapter<AutoKep>{
	
	AutoKepDao autoKepDao;
	ArrayList<AutoKep> autoKepek;
	Context context;
	
	
	
	public AutoKepImageAdapter(Context context, AutoKepDao autoKepDao,int textViewResourceId,
			List<AutoKep> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.autoKepek = new ArrayList<AutoKep>();
		autoKepek.addAll(objects);
		this.autoKepDao = autoKepDao;
		
	}



	@Override
	public void add(AutoKep object) {
		// TODO Auto-generated method stub
		autoKepek.add(object);
	}



	@Override
	public void addAll(Collection<? extends AutoKep> collection) {
		// TODO Auto-generated method stub
		autoKepek.addAll(collection);
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		autoKepek.clear();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return autoKepek.size();
	}



	@Override
	public AutoKep getItem(int position) {
		// TODO Auto-generated method stub
		return autoKepek.get(position);
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return autoKepek.get(position).getAutoKepID();
	}



	@Override
	public int getPosition(AutoKep item) {
		// TODO Auto-generated method stub
		return autoKepek.indexOf(item);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		// Get a View to display image data 					
					ImageView iv = new ImageView(context);				
					Bitmap myBitmap = BitmapFactory.decodeFile(autoKepek.get(position).getAutoKepPath());
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
	public void insert(AutoKep object, int index) {
		// TODO Auto-generated method stub
		autoKepek.add(index, object);
	}



	@Override
	public void remove(AutoKep object) {
		// TODO Auto-generated method stub
		autoKepek.remove(object);
	}
}
