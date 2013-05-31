package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.flottadroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TelephelyAdapter extends ArrayAdapter<Telephely>{
	
	Context context;
	ArrayList<Telephely> telephelyek;
	TelephelyDao telephelyDao;
	
	public TelephelyAdapter(Context context, int textViewResourceId,
			List<Telephely> objects, TelephelyDao telephelyDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.telephelyek=new ArrayList<Telephely>();
		telephelyek.addAll(objects);
		this.telephelyDao=telephelyDao;
		
	}
	
	@Override
	public void add(Telephely object) {
		// TODO Auto-generated method stub
		telephelyek.add(object);
	}
	
	@Override
	public void addAll(Collection<? extends Telephely> collection) {
		// TODO Auto-generated method stub
		telephelyek.addAll(collection);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		telephelyek.clear();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return telephelyek.size();
	}
	
	@Override
	public Telephely getItem(int position) {
		// TODO Auto-generated method stub
		return telephelyek.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return telephelyek.get(position).getTelephelyID();
	}
	
	@Override
	public int getPosition(Telephely item) {
		// TODO Auto-generated method stub
		return telephelyek.indexOf(item);
	}
	
	@Override
	public void remove(Telephely object) {
		// TODO Auto-generated method stub
		telephelyek.remove(object);
	}
	
	public static class TelephelyViewHolder {
		TextView telephelyNevTextView, telephelyCim, telephelyEmail, telephelyTel;
		ImageView iv;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TelephelyViewHolder holder = null;
		Telephely currentTelephely=telephelyek.get(position);
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_telephely, null);
			
			holder = new TelephelyViewHolder();
			
			holder.iv=(ImageView)convertView.findViewById(R.id.imgAvatarTelephely);
			holder.telephelyNevTextView = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.textViewTelephelyNev);	
			holder.telephelyCim=(TextView)convertView.findViewById(R.id.tvCimTelephely);
			holder.telephelyEmail=(TextView)convertView.findViewById(R.id.tvEmailTelephely);
			holder.telephelyTel=(TextView)convertView.findViewById(R.id.tvPhoneTelephely);
			
		}
		else
		{
			holder = (TelephelyViewHolder)convertView.getTag();
		}
					
		holder.telephelyNevTextView.setText(currentTelephely.getTelephelyNev());
		holder.telephelyCim.setText(currentTelephely.getTelephelyCim());
		holder.telephelyEmail.setText(currentTelephely.getTelephelyEmail());
		holder.telephelyTel.setText(currentTelephely.getTelephelyTelefonszam());
		
		convertView.setTag(holder);
		
		return convertView;
	}

}
