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

import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaKepDao;

public class MunkaKepImageAdapter extends ArrayAdapter<MunkaKep> {

	MunkaKepDao munkaKepDao;
	ArrayList<MunkaKep> munkaKepek;
	Context context;
	
	
	
	public MunkaKepImageAdapter(Context context, MunkaKepDao munkaKepDao,int textViewResourceId,
			List<MunkaKep> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.munkaKepek = new ArrayList<MunkaKep>();
		munkaKepek.addAll(objects);
		this.munkaKepDao = munkaKepDao;
		
	}



	@Override
	public void add(MunkaKep object) {
		// TODO Auto-generated method stub
		munkaKepek.add(object);
	}



	@Override
	public void addAll(Collection<? extends MunkaKep> collection) {
		// TODO Auto-generated method stub
		munkaKepek.addAll(collection);
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		munkaKepek.clear();
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return munkaKepek.size();
	}



	@Override
	public MunkaKep getItem(int position) {
		// TODO Auto-generated method stub
		return munkaKepek.get(position);
	}



	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return munkaKepek.get(position).getMunkaKepID();
	}



	@Override
	public int getPosition(MunkaKep item) {
		// TODO Auto-generated method stub
		return munkaKepek.indexOf(item);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		// Get a View to display image data 					
					ImageView iv = new ImageView(context);				
					Bitmap myBitmap = BitmapFactory.decodeFile(munkaKepek.get(position).getMunkaKepPath());
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
	public void insert(MunkaKep object, int index) {
		// TODO Auto-generated method stub
		munkaKepek.add(index, object);
	}



	@Override
	public void remove(MunkaKep object) {
		// TODO Auto-generated method stub
		munkaKepek.remove(object);
	}
	
	
}
