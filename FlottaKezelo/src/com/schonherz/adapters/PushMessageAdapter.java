package com.schonherz.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.schonherz.dbentities.PushMessage;
import com.schonherz.dbentities.PushMessageDao;
import com.schonherz.flottadroid.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PushMessageAdapter extends ArrayAdapter<PushMessage> {
	
	Context context;
	ArrayList<PushMessage> pushMessages;
	PushMessageDao pushMessageDao;

	public PushMessageAdapter(Context context, int textViewResourceId,
			List<PushMessage> objects, PushMessageDao pushMessageDao) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.pushMessages=new ArrayList<PushMessage>();
		pushMessages.addAll(objects);
		this.pushMessageDao=pushMessageDao;
		
	}
	
	@Override
	public void add(PushMessage object) {
		// TODO Auto-generated method stub
		pushMessages.add(object);
	}
	
	@Override
	public void addAll(Collection<? extends PushMessage> collection) {
		// TODO Auto-generated method stub
		pushMessages.addAll(collection);
	}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		pushMessages.clear();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pushMessages.size();
	}

	@Override
	public PushMessage getItem(int position) {
		// TODO Auto-generated method stub
		return pushMessages.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return pushMessages.get(position).getPushMessageID();
	}
	
	@Override
	public int getPosition(PushMessage item) {
		// TODO Auto-generated method stub
		return pushMessages.indexOf(item);
	}
	
	@Override
	public void remove(PushMessage object) {
		// TODO Auto-generated method stub
		pushMessages.remove(object);
	}
	
	static class ViewHolder
	{
		TextView autoNevTextView;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		PushMessage currentPushMessage = pushMessages.get(position);
		
		if(convertView == null)
		{
			
			
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(com.schonherz.flottadroid.R.layout.list_item_message, null);
			
		
			
			ImageView iv = (ImageView)convertView.findViewById(com.schonherz.flottadroid.R.id.imgAvatarMessage);			

			
		
			
			
			TextView tvName = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvMessage);
			tvName.setText(currentPushMessage.getPushMessageText());
			TextView tvDate = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvMessageDate);
			tvDate.setText(currentPushMessage.getPushMessageDate());
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		ImageView iv = (ImageView)convertView.findViewById(com.schonherz.flottadroid.R.id.imgAvatarMessage);		

		TextView tvName = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvMessage);
		tvName.setText(currentPushMessage.getPushMessageText());
		TextView tvDate = (TextView)convertView.findViewById(com.schonherz.flottadroid.R.id.tvMessageDate);
		tvDate.setText(currentPushMessage.getPushMessageDate());
		
		pushMessageDao.refresh(currentPushMessage);
		
		return convertView;
	}

}
