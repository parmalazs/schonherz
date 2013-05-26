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

import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;

public class SoforAdapter extends ArrayAdapter<Sofor> {

	Context context;
	ArrayList<Sofor> soforok;
	SoforDao soforDao;

	public SoforAdapter(Context context, int textViewResourceId,
			List<Sofor> objects, SoforDao soforDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub

		this.context = context;
		this.soforok = new ArrayList<Sofor>();
		soforok.addAll(objects);
		this.soforDao = soforDao;
	}

	@Override
	public void add(Sofor object) {
		// TODO Auto-generated method stub
		soforok.add(object);
	}

	@Override
	public void addAll(Collection<? extends Sofor> collection) {
		// TODO Auto-generated method stub
		soforok.addAll(collection);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		soforok.clear();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return soforok.size();
	}

	@Override
	public Sofor getItem(int position) {
		// TODO Auto-generated method stub
		return soforok.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return soforok.get(position).getSoforID();
	}

	@Override
	public int getPosition(Sofor item) {
		// TODO Auto-generated method stub
		return soforok.indexOf(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		Sofor currentSofor = soforok.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					com.schonherz.flottadroid.R.layout.list_item_sofor, null);

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
					.findViewById(com.schonherz.flottadroid.R.id.tvNameSofor);
			tvName.setText(currentSofor.getSoforNev());
			// set phone
			TextView tvPhone = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvPhoneSofor);
			tvPhone.setText(currentSofor.getSoforTelefonszam());
			// set email
			TextView tvEmail = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvEmailSofor);
			tvEmail.setText(currentSofor.getSoforEmail());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		soforDao.refresh(currentSofor);
		holder = new ViewHolder();
		//holder.soforNevTextView = (TextView) convertView
		//		.findViewById(com.schonherz.flottadroid.R.id.textViewSoforNev);

		//holder.soforNevTextView.setText(currentSofor.getSoforNev());

		ImageView iv = (ImageView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);

		// set name
		TextView tvName = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvNameSofor);
		tvName.setText(currentSofor.getSoforNev());
		// set phone
		TextView tvPhone = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvPhoneSofor);
		tvPhone.setText(currentSofor.getSoforTelefonszam());
		// set email
		TextView tvEmail = (TextView) convertView
				.findViewById(com.schonherz.flottadroid.R.id.tvEmailSofor);
		tvEmail.setText(currentSofor.getSoforEmail());
		
		return convertView;
	}

	@Override
	public void remove(Sofor object) {
		// TODO Auto-generated method stub
		soforok.remove(object);
	}

	static class ViewHolder {
		TextView soforNevTextView;
	}

}
