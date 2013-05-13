package com.schonherz.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.schonherz.adapters.MunkaAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.flottadroid.R;

public class MunkaListFragment extends Fragment 
{
	
	Context context;
	MunkaDao munkaDao;
	PullToRefreshListView pullListView;
	MunkaAdapter adapter;

	public MunkaListFragment(Context context, MunkaDao munkaDao)
	{
		this.context = context;
		this.munkaDao = munkaDao;
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
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.layout_pulltorefresh_list, null);

		pullListView = (PullToRefreshListView) v
				.findViewById(R.id.pulltorefresh_listview);
		
		ArrayList<Munka> munkak=new ArrayList<Munka>(munkaDao.loadAll());
		adapter=new MunkaAdapter(context, R.layout.list_item_munka, munkak, munkaDao);
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
							
							ArrayList<Munka> munkak=new ArrayList<Munka>(munkaDao.loadAll());
							adapter.addAll(munkak);
							adapter.notifyDataSetChanged();
							
						}

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return saveMunkaTable();
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
	
	public Boolean saveMunkaTable() {
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/queryMunkaTable.php";

		json = new JSONObject();
		
		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());
			
			// Eldobjuk a tablat es ujra letrehozzuk
			munkaDao.dropTable(munkaDao.getDatabase(), true);
			munkaDao.createTable(munkaDao.getDatabase(), true);
			ArrayList<Munka> munkak=JsonArrayToArrayList.JsonArrayToMunka(jsonArray);
			for(int i=0; i<munkak.size(); i++) {
				munkaDao.insert(munkak.get(i));
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
	}
	
}