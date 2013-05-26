package com.schonherz.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.adapters.PartnerAdapter.ViewHolder;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MunkaAdapter extends ArrayAdapter<Munka> {
	
	Context context;
	ArrayList<Munka> munkak;
	MunkaDao munkaDao;

	public MunkaAdapter(Context context, int textViewResourceId,
			List<Munka> objects, MunkaDao munkaDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.munkak=new ArrayList<Munka>();
		munkak.addAll(objects);
		this.munkaDao=munkaDao;
	}
	
	@Override
	public void add(Munka object) {
		// TODO Auto-generated method stub
		munkak.add(object);
	}
	
	@Override
	public void addAll(Collection<? extends Munka> collection) {
		// TODO Auto-generated method stub
		munkak.addAll(collection);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		munkak.clear();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return munkak.size();
	}
	
	@Override
	public Munka getItem(int position) {
		// TODO Auto-generated method stub
		return munkak.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return munkak.get(position).getMunkaID();
	}
	
	@Override
	public int getPosition(Munka item) {
		// TODO Auto-generated method stub
		return munkak.indexOf(item);
	}
	
	@Override
	public void remove(Munka object) {
		// TODO Auto-generated method stub
		munkak.remove(object);
	}
	
	static class ViewHolder {
		TextView munkaNevTextView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		Munka currentMunka=munkak.get(position);
		if(convertView==null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					com.schonherz.flottadroid.R.layout.list_item_munka, null);
			holder = new ViewHolder();

			// holder.partnerNevTextView =
			// (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewPartnerNev);
			// holder.partnerNevTextView.setText(currentPartner.getPartnerNev());

			// set avatar
			// ImageView ivAvatar =
			// ivAvatar.setImageBitmap(Bitmap.createBitmap(currentPartner.getPartnerKepList().get(0).getPartnerKepPath()));

			ImageView iv = (ImageView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);
			// Bitmap myBitmap =
			// BitmapFactory.decodeFile(currentPartner.getPartnerKepList().get(0).getPartnerKepPath());

			// Image should be scaled somehow
			// iv.setScaleType(ImageView.ScaleType.CENTER);
			// iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			// iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// iv.setScaleType(ImageView.ScaleType.FIT_XY);
			// iv.setScaleType(ImageView.ScaleType.FIT_END);
			// iv.setBackgroundColor(Color.BLACK);
			// Set the Width & Height of the individual images
			// iv.setLayoutParams(new Gallery.LayoutParams(130, 180));

			// set name
			TextView tvName = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvMunkaDatum);
			tvName.setText(currentMunka.getMunkaDate());
			// set phone
			TextView tvPhone = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvMunkaTelephelye);
			tvPhone.setText(currentMunka.getTelephely().getTelephelyNev());
			// set email
			TextView tvEmail = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvMunkaBecsultIdoOraban);
			tvEmail.setText((new DecimalFormat("##.##").format(currentMunka.getMunkaEstimatedTime()/60.0))+" h");
			
			TextView tvLast = (TextView) convertView.findViewById(com.schonherz.flottadroid.R.id.tvMunkaBevetel);
			tvLast.setText((new DecimalFormat("##.##").format(currentMunka.getMunkaBevetel())) + " $");
			
			
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		munkaDao.refresh(currentMunka);
		ImageView iv = (ImageView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);
		// Bitmap myBitmap =
		// BitmapFactory.decodeFile(currentPartner.getPartnerKepList().get(0).getPartnerKepPath());

		// Image should be scaled somehow
		// iv.setScaleType(ImageView.ScaleType.CENTER);
		// iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		// iv.setScaleType(ImageView.ScaleType.FIT_XY);
		// iv.setScaleType(ImageView.ScaleType.FIT_END);
		// iv.setBackgroundColor(Color.BLACK);
		// Set the Width & Height of the individual images
		// iv.setLayoutParams(new Gallery.LayoutParams(130, 180));

		// set name
		TextView tvName = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvMunkaDatum);
		tvName.setText(currentMunka.getMunkaDate());
		// set phone
		TextView tvPhone = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvMunkaTelephelye);
		tvPhone.setText(currentMunka.getTelephely().getTelephelyNev());
		// set email
		TextView tvEmail = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvMunkaBecsultIdoOraban);
		tvEmail.setText((new DecimalFormat("##.##").format(currentMunka.getMunkaEstimatedTime()/60.0))+" h");
		
		TextView tvLast = (TextView) convertView.findViewById(com.schonherz.flottadroid.R.id.tvMunkaBevetel);
		tvLast.setText((new DecimalFormat("##.##").format(currentMunka.getMunkaBevetel())) + " $");
		
		return convertView;
	}

}
