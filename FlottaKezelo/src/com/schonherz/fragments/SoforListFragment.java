package com.schonherz.fragments;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	ArrayList<Sofor> soforok;
	MenuItem refreshItem;

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
		inflater.inflate(R.menu.fragment_list_menu, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		setupSearchView(searchView);
		refreshItem = menu.findItem(R.id.menu_refresh);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void setupSearchView(SearchView searchView) {
		SearchManager searchManager = (SearchManager) context
				.getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				if (query.length() > 0) {
					ArrayList<Sofor> templist = new ArrayList();

					for (int i = 0; i < soforok.size(); i++) {
						if (soforok.get(i).getSoforNev().toLowerCase()
								.contains(query.toLowerCase())) {
							templist.add(soforok.get(i));
						}
					}

					adapter.clear();
					adapter.addAll(templist);

					adapter.notifyDataSetChanged();
				}
				else {
					adapter.clear();
					adapter.addAll(soforok);
					adapter.notifyDataSetChanged();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub

				ArrayList<Sofor> templist = new ArrayList();

				for (int i = 0; i < soforok.size(); i++) {
					if (soforok.get(i).getSoforNev().toLowerCase()
							.contains(newText.toLowerCase())) {
						templist.add(soforok.get(i));
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

		soforok = new ArrayList<Sofor>(soforDao.loadAll());

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
						protected void onPreExecute() {
							startRefreshAnimation();
						};

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
								// Play notification sound when refresn finished
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

							ArrayList<Sofor> soforok = new ArrayList<Sofor>(
									soforDao.loadAll());
							adapter.addAll(soforok);
							adapter.notifyDataSetChanged();

							if (refreshItem != null
									&& refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}

							stopRefreshAnimation();

						}

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return saveSoforTable();
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
		switch (item.getItemId()) {
			case R.id.menu_Sort :
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Rendezés");
				final CharSequence[] choiceList = {"Név", "Cím", "Telefonszám" };

				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(choiceList, selected, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						switch(which)
						{
							case 0:
								try {
									Collator huCollator = new RuleBasedCollator(hungarianRules);
									sortSoforNev(huCollator, soforok);
									adapter.clear();
									adapter.addAll(soforok);
									adapter.notifyDataSetChanged();
								} catch (ParseException e) {
									// TODO Auto-generated catch block																		
									e.printStackTrace();
								}
								break;
							case 1:
								try {
									Collator huCollator = new RuleBasedCollator(hungarianRules);
									sortSoforCim(huCollator, soforok);
									adapter.clear();
									adapter.addAll(soforok);
									adapter.notifyDataSetChanged();
								} catch (ParseException e) {
									// TODO Auto-generated catch block																		
									e.printStackTrace();
								}
								break;
							case 2:
								try {
									Collator huCollator = new RuleBasedCollator(hungarianRules);
									sortSoforTelefonSzam(huCollator, soforok);
									adapter.clear();
									adapter.addAll(soforok);
									adapter.notifyDataSetChanged();
								} catch (ParseException e) {
									// TODO Auto-generated catch block																		
									e.printStackTrace();
								}
								break;
						}	
						
						dialog.dismiss();
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
				break;
			case R.id.menu_refresh :

				if (NetworkUtil.checkInternetIsActive(context) == true) {
					new AsyncTask<Void, Void, Boolean>() {
						@Override
						protected void onPreExecute() {
							startRefreshAnimation();
						};

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
								// Play notification sound when refresn finished
								Uri notification = RingtoneManager
										.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
								Ringtone r = RingtoneManager.getRingtone(
										context, notification);
								r.play();
							} catch (Exception e) {
								e.printStackTrace();
							}

							adapter.clear();

							ArrayList<Sofor> soforok = new ArrayList<Sofor>(
									soforDao.loadAll());
							adapter.addAll(soforok);
							adapter.notifyDataSetChanged();

							if (refreshItem != null
									&& refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}

							stopRefreshAnimation();

						}

						@Override
						protected Boolean doInBackground(Void... params) {
							// TODO Auto-generated method stub
							return saveSoforTable();
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

	private void stopRefreshAnimation() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(R.layout.refreshing_layout,
				null);
	}

	private void startRefreshAnimation() {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(R.layout.refreshing_layout,
				null);
		Animation rotation = AnimationUtils.loadAnimation(context,
				R.anim.refresh_rotate);
		rotation.setRepeatCount(Animation.INFINITE);
		iv.startAnimation(rotation);
		refreshItem.setActionView(iv);
	}
	
	String hungarianRules = ("< a,A < á,Á < b,B < c,C < cs,Cs < d,D < dz,Dz < dzs,Dzs "
			+ "< e,E < é,É < f,F < g,G < gy,Gy < h,H < i,I < í,Í < j,J "
			+ "< k,K < l,L < ly,Ly < m,M < n,N < ny,Ny < o,O < ó,Ó "
			+ "< ö,Ö < õ,Õ < p,P < q,Q < r,R < s,S < sz,Sz < t,T "
			+ "< ty,Ty < u,U < ú,Ú < ü,Ü < û,Û < v,V < w,W < x,X < y,Y < z,Z < zs,Zs");
	
	public static void sortSoforNev(Collator collator, List<Sofor> soforList)
	{
		Sofor tmp;
		for(int i = 0; i<soforList.size();i++)
		{
			for(int j= 0; j<soforList.size();j++)
			{
				if(collator.compare(soforList.get(j).getSoforNev(),soforList.get(i).getSoforNev())>0)
				{
					tmp = soforList.get(i);
					soforList.set(i, soforList.get(j));
					soforList.set(j, tmp);
				}
			}
		}
	}
	
	public static void sortSoforCim(Collator collator, List<Sofor> soforList)
	{
		Sofor tmp;
		for(int i = 0; i<soforList.size();i++)
		{
			for(int j= 0; j<soforList.size();j++)
			{
				if(collator.compare(soforList.get(j).getSoforCim(),soforList.get(i).getSoforCim())>0)
				{
					tmp = soforList.get(i);
					soforList.set(i, soforList.get(j));
					soforList.set(j, tmp);
				}
			}
		}
	}
	
	public static void sortSoforTelefonSzam(Collator collator, List<Sofor> soforList)
	{
		Sofor tmp;
		for(int i = 0; i<soforList.size();i++)
		{
			for(int j= 0; j<soforList.size();j++)
			{
				if(collator.compare(soforList.get(j).getSoforTelefonszam(),soforList.get(i).getSoforTelefonszam())>0)
				{
					tmp = soforList.get(i);
					soforList.set(i, soforList.get(j));
					soforList.set(j, tmp);
				}
			}
		}
	}
	
}
