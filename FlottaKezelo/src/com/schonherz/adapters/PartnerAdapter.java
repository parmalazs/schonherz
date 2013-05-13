package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		ViewHolder holder=null;
		
		Partner currentPartner=partnerek.get(position);
		
		if(convertView==null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_partner,null);
			holder=new ViewHolder();
			
			holder.partnerNevTextView = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewPartnerNev);
			holder.partnerNevTextView.setText(currentPartner.getPartnerNev());
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		partnerDao.refresh(currentPartner);
		holder=new ViewHolder();
		holder.partnerNevTextView = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewPartnerNev);
		holder.partnerNevTextView.setText(currentPartner.getPartnerNev());
		
		return convertView;
	}
	
	@Override
	public void remove(Partner object) {
		// TODO Auto-generated method stub
		partnerek.remove(object);
	}
	
	static class ViewHolder
	{
		TextView partnerNevTextView;
	}

}
