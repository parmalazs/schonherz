package com.schonherz.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.SearchManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.schonherz.adapters.PartnerAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.flottadroid.R;

public class PartnerListFragment extends Fragment {

	Context context;
	PartnerDao partnerDao;
	PartnerAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Partner> partnerek;
	MenuItem refreshItem;
	
	public PartnerListFragment(Context context, PartnerDao partnerDao)
	{
		this.context = context;
		this.partnerDao = partnerDao;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub		
		inflater.inflate(R.menu.fragment_list_menu, menu);
		SearchView searchView=(SearchView) menu.findItem(R.id.menu_search).getActionView();		
		setupSearchView(searchView);
		refreshItem = menu.findItem(R.id.menu_refresh);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	
	public void setupSearchView(SearchView searchView) {
		SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);	
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				if (query.length() > 0) {
					
					ArrayList<Partner> templist = new ArrayList<Partner>();
					for(int i = 0; i < partnerek.size(); i++)
					{
						if(partnerek.get(i).getPartnerNev().toLowerCase().contains(query.toLowerCase()))
						{
							templist.add(partnerek.get(i));
						}
					}
					
					adapter.clear();
					adapter.addAll(templist);
					adapter.notifyDataSetChanged();
				}
				else
				{
				adapter.clear();
				adapter.addAll(partnerek);
				adapter.notifyDataSetChanged();
				}
				return true;
				
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				
				ArrayList<Partner> templist = new ArrayList<Partner>();
				for(int i = 0; i < partnerek.size(); i++)
				{
					if(partnerek.get(i).getPartnerNev().toLowerCase().contains(newText.toLowerCase()))
					{
						templist.add(partnerek.get(i));
					}
				}
				
				adapter.clear();
				adapter.addAll(templist);
				adapter.notifyDataSetChanged();
				
				return true;
			}
		});
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.layout_pulltorefresh_list, null);

		pullListView = (PullToRefreshListView) v
				.findViewById(R.id.pulltorefresh_listview);
		
		partnerek =new ArrayList<Partner>(partnerDao.loadAll());
		adapter=new PartnerAdapter(context, R.layout.list_item_partner, partnerek, partnerDao);
		pullListView.setAdapter(adapter);
		pullListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(NetworkUtil.checkInternetIsActive(context)==true) {
					new AsyncTask<Void, Void, Boolean>() {
						
						@Override
						protected void onPreExecute() {
							startRefreshAnimation();
						};
						
						@Override
						protected void onPostExecute(Boolean result) {
							if(result==true) {
								Toast.makeText(context, R.string.refreshed,
										Toast.LENGTH_SHORT).show();
							}
							else {
								Toast.makeText(context, R.string.errorRefresh,
										Toast.LENGTH_SHORT).show();
							}
							
							try {
								//Play notification sound when refresn finished
								Uri notification = RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
								Ringtone r = RingtoneManager.getRingtone(
										context, notification);
								r.play();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							pullListView.onRefreshComplete();
							adapter.clear();
							ArrayList<Partner> partnerek=new ArrayList<Partner>(partnerDao.loadAll());
							adapter.addAll(partnerek);
							adapter.notifyDataSetChanged();
							
							if (refreshItem != null && refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}
							
							stopRefreshAnimation();
						};

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return savePartnerTable();
						}			
						
					}.execute();
					
				}
				
				else {
					Toast.makeText(context, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
					pullListView.onRefreshComplete();
				}
			}
		});
		
		return v;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.menu_Sort :
				break;
			case R.id.menu_refresh :
				if(NetworkUtil.checkInternetIsActive(context)==true) {
					new AsyncTask<Void, Void, Boolean>() {
						
						@Override
						protected void onPreExecute() {
							startRefreshAnimation();
						};
						
						@Override
						protected void onPostExecute(Boolean result) {
							if(result==true) {
								Toast.makeText(context, R.string.refreshed,
										Toast.LENGTH_SHORT).show();
							}
							else {
								Toast.makeText(context, R.string.errorRefresh,
										Toast.LENGTH_SHORT).show();
							}
							
							try {
								//Play notification sound when refresn finished
								Uri notification = RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
								Ringtone r = RingtoneManager.getRingtone(
										context, notification);
								r.play();
							} catch (Exception e) {
								e.printStackTrace();
							}
														
							adapter.clear();
							ArrayList<Partner> partnerek=new ArrayList<Partner>(partnerDao.loadAll());
							adapter.addAll(partnerek);
							adapter.notifyDataSetChanged();
							
							if (refreshItem != null && refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}
							
							stopRefreshAnimation();
						};

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return savePartnerTable();
						}			
						
					}.execute();
					
				}
				
				else {
					Toast.makeText(context, R.string.no_internet,
							Toast.LENGTH_SHORT).show();
					
				}
				break;
		}		
		return true;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	
	public Boolean savePartnerTable() {
		// TODO Auto-generated method stub
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/queryPartnerTable.php";

		json = new JSONObject();
		
		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());
			
			// Eldobjuk a tablat es ujra letrehozzuk
			partnerDao.dropTable(partnerDao.getDatabase(), true);
			partnerDao.createTable(partnerDao.getDatabase(), true);
			
			ArrayList<Partner> partnerek=JsonArrayToArrayList.JsonArrayToPartner(jsonArray);
			
			for (int i=0; i<partnerek.size(); i++) {
				partnerDao.insert(partnerek.get(i));
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
	private void stopRefreshAnimation() 
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(R.layout.refreshing_layout,
				null);
	}
	
	private void startRefreshAnimation() {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(
				R.layout.refreshing_layout, null);
		Animation rotation = AnimationUtils.loadAnimation(context,
				R.anim.refresh_rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		iv.startAnimation(rotation);
		refreshItem.setActionView(iv);		
	}
	
}
