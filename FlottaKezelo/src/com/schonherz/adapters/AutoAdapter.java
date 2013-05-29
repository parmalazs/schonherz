package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.flottadroid.R;



public class AutoAdapter extends ArrayAdapter<Auto> {
	
	Context context;
	ArrayList<Auto> autok;
	AutoDao autoDao;

	public AutoAdapter(Context context, int textViewResourceId,
			List<Auto> objects, AutoDao autoDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.autok=new ArrayList<Auto>();
		autok.addAll(objects);
		this.autoDao=autoDao;
		
	}
	
	@Override
	public void add(Auto object) {
		// TODO Auto-generated method stub
		autok.add(object);
	}
	
	@Override
	public void addAll(Collection<? extends Auto> collection) {
		// TODO Auto-generated method stub
		autok.addAll(collection);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		autok.clear();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return autok.size();
	}

	@Override
	public Auto getItem(int position) {
		// TODO Auto-generated method stub
		return autok.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return autok.get(position).getAutoID();
	}
	
	@Override
	public int getPosition(Auto item) {
		// TODO Auto-generated method stub
		return autok.indexOf(item);
	}
	
	@Override
	public void remove(Auto object) {
		// TODO Auto-generated method stub
		autok.remove(object);
	}
	
	static class ViewHolder
	{
		TextView autoNevTextView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		Auto currentAuto = autok.get(position);
		
		if(convertView == null)
		{/*
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_auto, null);
			
			holder = new ViewHolder();
			holder.autoNevTextView = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewAutoNev);
			
			holder.autoNevTextView.setText(currentAuto.getAutoNev());*/
			
			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_auto, null);
			
			// set avatar
			//	ImageView ivAvatar = 
			//ivAvatar.setImageBitmap(Bitmap.createBitmap(currentPartner.getPartnerKepList().get(0).getPartnerKepPath()));
			
			ImageView iv = (ImageView)convertView.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);				
			//Bitmap myBitmap = BitmapFactory.decodeFile(currentPartner.getPartnerKepList().get(0).getPartnerKepPath());

			
			// Image should be scaled somehow
			//iv.setScaleType(ImageView.ScaleType.CENTER);
			//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
			//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//iv.setScaleType(ImageView.ScaleType.FIT_XY);
			//iv.setScaleType(ImageView.ScaleType.FIT_END);
			//iv.setBackgroundColor(Color.BLACK);
			// Set the Width & Height of the individual images
			//iv.setLayoutParams(new Gallery.LayoutParams(130, 180));
			
			
			// set name
			TextView tvName = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoTipus);
			tvName.setText(currentAuto.getAutoNev());
			// set phone
			TextView tvPhone = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoRendszam);
			tvPhone.setText(currentAuto.getAutoRendszam());
			// set email
			TextView tvEmail = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoFoglaltsag);
			if(currentAuto.getAutoFoglalt())
				tvEmail.setText("Foglalt!");
			else
				tvEmail.setText("Szabad!");
			
			TextView tvFuel=(TextView)convertView.findViewById(R.id.tvAutoFuel);
			tvFuel.setText("Üzemanyag: " + currentAuto.getAutoUzemanyag().toString() + " l" );
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		ImageView iv = (ImageView)convertView.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);				
		//Bitmap myBitmap = BitmapFactory.decodeFile(currentPartner.getPartnerKepList().get(0).getPartnerKepPath());

		
		// Image should be scaled somehow
		//iv.setScaleType(ImageView.ScaleType.CENTER);
		//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
		//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		//iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
		//iv.setScaleType(ImageView.ScaleType.FIT_XY);
		//iv.setScaleType(ImageView.ScaleType.FIT_END);
		//iv.setBackgroundColor(Color.BLACK);
		// Set the Width & Height of the individual images
		//iv.setLayoutParams(new Gallery.LayoutParams(130, 180));
		
		
		// set name
		TextView tvName = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoTipus);
		tvName.setText(currentAuto.getAutoNev());
		// set phone
		TextView tvPhone = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoRendszam);
		tvPhone.setText(currentAuto.getAutoRendszam());
		// set email
		TextView tvEmail = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoFoglaltsag);
		if(currentAuto.getAutoFoglalt())
			tvEmail.setText("Foglalt!");
		else
			tvEmail.setText("Szabad!");
		
		TextView tvFuel=(TextView)convertView.findViewById(R.id.tvAutoFuel);
		tvFuel.setText("Üzemanyag: " + currentAuto.getAutoUzemanyag().toString() + " l" );
		
		autoDao.refresh(currentAuto);
		/*
		holder = new ViewHolder();
		holder.autoNevTextView = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewAutoNev);
		
		holder.autoNevTextView.setText(currentAuto.getAutoNev());
		*/
		return convertView;
	}

}
