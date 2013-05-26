package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.flottadroid.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class PartnerAdapter extends ArrayAdapter<Partner> {

	Context context;
	ArrayList<Partner> partnerek;
	PartnerDao partnerDao;

	public PartnerAdapter(Context context, int textViewResourceId,
			List<Partner> objects, PartnerDao partnerDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.partnerek = new ArrayList<Partner>();
		partnerek.addAll(objects);
		this.partnerDao = partnerDao;
	}

	@Override
	public void add(Partner object) {
		// TODO Auto-generated method stub
		partnerek.add(object);
	}

	@Override
	public void addAll(Collection<? extends Partner> collection) {
		// TODO Auto-generated method stub
		partnerek.addAll(collection);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		partnerek.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return partnerek.size();
	}

	@Override
	public Partner getItem(int position) {
		// TODO Auto-generated method stub
		return partnerek.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return partnerek.get(position).getPartnerID();
	}

	@Override
	public int getPosition(Partner item) {
		// TODO Auto-generated method stub
		return partnerek.indexOf(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		Partner currentPartner = partnerek.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					com.schonherz.flottadroid.R.layout.list_item_partner, null);
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
					.findViewById(com.schonherz.flottadroid.R.id.tvNamePartner);
			tvName.setText(currentPartner.getPartnerNev());
			// set phone
			TextView tvPhone = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvPhonePartner);
			tvPhone.setText(currentPartner.getPartnerTelefonszam());
			// set email
			TextView tvEmail = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvEmailPartner);
			tvEmail.setText(currentPartner.getPartnerEmailcim());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		partnerDao.refresh(currentPartner);
		holder = new ViewHolder();
		// holder.partnerNevTextView =
		// (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewPartnerNev);
		// holder.partnerNevTextView.setText(currentPartner.getPartnerNev());

		ImageView iv = (ImageView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);

		// set name
		TextView tvName = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvNamePartner);
		tvName.setText(currentPartner.getPartnerNev());
		// set phone
		TextView tvPhone = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvPhonePartner);
		tvPhone.setText(currentPartner.getPartnerTelefonszam());
		// set email
		TextView tvEmail = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvEmailPartner);
		tvEmail.setText(currentPartner.getPartnerEmailcim());

		return convertView;
	}

	@Override
	public void remove(Partner object) {
		// TODO Auto-generated method stub
		partnerek.remove(object);
	}

	static class ViewHolder {
		TextView partnerNevTextView;
	}

}
