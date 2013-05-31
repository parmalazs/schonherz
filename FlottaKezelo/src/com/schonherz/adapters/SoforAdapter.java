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
import com.schonherz.flottadroid.R;

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
		SoforViewHolder holder = null;
		Sofor currentSofor = soforok.get(position);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					com.schonherz.flottadroid.R.layout.list_item_sofor, null);

			holder=new SoforViewHolder();

			holder.iv = (ImageView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.imgAvatar);
			holder.tvName = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvNameSofor);
			holder.tvPhone = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvPhoneSofor);
			holder.tvEmail = (TextView) convertView
					.findViewById(com.schonherz.flottadroid.R.id.tvEmailSofor);			
			holder.tvWeb=(TextView)convertView.findViewById(R.id.tvHomePageSofor);
			
		} else {
			holder = (SoforViewHolder) convertView.getTag();
		}

		holder.tvName.setText(currentSofor.getSoforNev());
		holder.tvPhone.setText(currentSofor.getSoforTelefonszam());
		holder.tvEmail.setText(currentSofor.getSoforEmail());
		holder.tvWeb.setText(currentSofor.getSoforBirthDate());
		
		convertView.setTag(holder);
		
		return convertView;
	}

	@Override
	public void remove(Sofor object) {
		// TODO Auto-generated method stub
		soforok.remove(object);
	}

	public static class SoforViewHolder {
		TextView tvName, tvPhone, tvEmail, tvWeb;;
		ImageView iv;
	}

}
