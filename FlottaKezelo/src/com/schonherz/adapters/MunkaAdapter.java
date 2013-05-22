package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_munka, null);
			
			holder = new ViewHolder();
			holder.munkaNevTextView=(TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewMunkaNev);
	
			holder.munkaNevTextView.setText(currentMunka.getMunkaDate());
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		munkaDao.refresh(currentMunka);
		holder=new ViewHolder();
		holder.munkaNevTextView=(TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewMunkaNev);
		holder.munkaNevTextView.setText(currentMunka.getMunkaDate());
		
		return convertView;
	}

}
