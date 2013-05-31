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
	
	public static class AutoViewHolder
	{
		TextView tvName, tvPhone, tvEmail, tvFuel;
		ImageView iv;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AutoViewHolder holder = null;
		Auto currentAuto = autok.get(position);
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_auto, null);
			
			holder=new AutoViewHolder();
			
			holder.iv = (ImageView)convertView.findViewById(com.schonherz.flottadroid.R.id.imgAvatarAuto);			
			holder.tvName = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoTipus);			
			holder.tvPhone = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoRendszam);			
			holder.tvEmail = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvAutoFoglaltsag);			
			holder.tvFuel=(TextView)convertView.findViewById(R.id.tvAutoFuel);
		}
		else
		{
			holder = (AutoViewHolder)convertView.getTag();
		}
		
		
		holder.tvName.setText(currentAuto.getAutoNev());		
		holder.tvPhone.setText(currentAuto.getAutoRendszam());
				if(currentAuto.getAutoFoglalt())
			holder.tvEmail.setText("Foglalt!");
		else
			holder.tvEmail.setText("Szabad!");		
		holder.tvFuel.setText("�zemanyag: " + currentAuto.getAutoUzemanyag().toString() + " l" );
		convertView.setTag(holder);
		
		return convertView;
	}

}
