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

import com.schonherz.adapters.SoforAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.flottadroid.R;

public class SoforListFragment extends Fragment {

	Context context;
	SoforDao soforDao;
	SoforAdapter adapter;
	PullToRefreshListView pullListView;

	public SoforListFragment(Context context, SoforDao soforDao) {
		this.context = context;
		this.soforDao = soforDao;
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

		ArrayList<Sofor> soforok = new ArrayList<Sofor>(soforDao.loadAll());

		adapter = new SoforAdapter(context, R.layout.list_item_sofor, soforok,
				soforDao);

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
							
						}

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return saveSoforTable();
						}
					}.execute();

					adapter.notifyDataSetChanged();
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
		super.onResume();
	}

	public boolean saveSoforTable() {
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/querySoforTable.php";

		json = new JSONObject();

		try {

			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());

			// Eldobjuk a tablat es ujra letrehozzuk
			soforDao.dropTable(soforDao.getDatabase(), true);
			soforDao.createTable(soforDao.getDatabase(), true);

			ArrayList<Sofor> soforok = JsonArrayToArrayList
					.JsonArrayToSofor(jsonArray);

			for (int i = 0; i < soforok.size(); i++) {
				soforDao.insert(soforok.get(i));
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}

		
	}	

}
