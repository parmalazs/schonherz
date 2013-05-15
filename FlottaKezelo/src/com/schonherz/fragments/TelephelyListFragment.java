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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

import com.schonherz.adapters.TelephelyAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.flottadroid.R;

public class TelephelyListFragment extends Fragment {

	Context context;
	TelephelyDao telephelyDao;
	TelephelyAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Telephely> telephelyek;
	
	public TelephelyListFragment(Context context, TelephelyDao telephelyDao)
	{
		this.context = context;
		this.telephelyDao = telephelyDao;
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
				
				adapter.clear();
				adapter.addAll(telephelyek);
				adapter.notifyDataSetChanged();
				
				return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				
				ArrayList<Telephely> templist = new ArrayList<Telephely>();
				for(int i = 0; i<telephelyek.size();i++)
				{
					if(telephelyek.get(i).getTelephelyNev().toLowerCase().contains(newText.toLowerCase()))
					{
						templist.add(telephelyek.get(i));
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
		
		telephelyek =new ArrayList<Telephely>(telephelyDao.loadAll());
		
		adapter=new TelephelyAdapter(context, R.layout.list_item_telephely, telephelyek, telephelyDao);
		pullListView.setAdapter(adapter);
		
		pullListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (NetworkUtil.checkInternetIsActive(context) == true) {
					new AsyncTask<Void, Void, Boolean>() {

						@Override
						protected void onPostExecute(Boolean result) {
							// TODO Auto-generated method stub
							if (result == true) {
								Toast.makeText(context, R.string.refreshed,
										Toast.LENGTH_SHORT).show();
								
								
							} else {
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
							
							ArrayList<Telephely> telephelyek=new ArrayList<Telephely>(telephelyDao.loadAll());
							adapter.addAll(telephelyek);
							adapter.notifyDataSetChanged();
							
						}
						
						protected Boolean doInBackground(Void... params) {
							return saveTelephelyTable();
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		super.onResume();
	}
	
	public boolean saveTelephelyTable() {
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/queryTelephelyTable.php";

		json = new JSONObject();
		
		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());
			
			// Eldobjuk a tablat es ujra letrehozzuk
			telephelyDao.dropTable(telephelyDao.getDatabase(), true);
			telephelyDao.createTable(telephelyDao.getDatabase(), true);
			ArrayList<Telephely> telephelyek=JsonArrayToArrayList.JsonArrayToTelephely(jsonArray);
			
			for (int i=0; i<telephelyek.size(); i++) {
				telephelyDao.insert(telephelyek.get(i));
			}
			
			Log.w("telephely", Integer.toString(telephelyDao.loadAll().size()));
			
			
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}
	

