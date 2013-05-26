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
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.schonherz.adapters.PartnerAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.SessionManager;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.DaoMaster;
import com.schonherz.dbentities.DaoSession;
import com.schonherz.dbentities.Partner;
import com.schonherz.dbentities.PartnerDao;
import com.schonherz.dbentities.Sofor;
import com.schonherz.dbentities.SoforDao;
import com.schonherz.dbentities.DaoMaster.DevOpenHelper;
import com.schonherz.dbentities.PartnerDao.Properties;
import com.schonherz.flottadroid.PartnerDetailsActivity;
import com.schonherz.flottadroid.R;

public class PartnerListFragment extends Fragment {
	
	

	Context context;
	PartnerDao partnerDao;
	PartnerAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Partner> partnerek;
	MenuItem refreshItem;
	SessionManager sessionManager;
	
	final int CONTEXT_MENU_DELETE_ITEM =1;
	
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.pulltorefresh_listview) {
			menu.add(Menu.NONE, CONTEXT_MENU_DELETE_ITEM, Menu.NONE, "Törlés");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		Partner selectedPartner=partnerDao.queryBuilder().where(Properties.PartnerID.eq(adapter.getItemId(info.position-1))).list().get(0);
		selectedPartner.setPartnerIsActive(false);
		selectedPartner.refresh();
		partnerDao.update(selectedPartner);
		adapter.remove(selectedPartner);
		adapter.notifyDataSetChanged(); 
		return super.onContextItemSelected(item);
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
		
		partnerek =new ArrayList<Partner>(partnerDao.queryBuilder().where(Properties.PartnerIsActive.eq(true)).list());
		adapter=new PartnerAdapter(context, R.layout.list_item_partner, partnerek, partnerDao);
		pullListView.setAdapter(adapter);
		
		registerForContextMenu(pullListView);
		
		pullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), PartnerDetailsActivity.class);
				intent.putExtra("selectedPartnerID", partnerek.get(position-1).getPartnerID());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		
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
							partnerek=new ArrayList<Partner>(partnerDao.queryBuilder().where(Properties.PartnerIsActive.eq(true)).list());
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
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Rendezés");
				final CharSequence[] choiceList = {"Név", "Cím"};

				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(choiceList, selected,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								switch(which)
								{
									case 0:
										try {
											Collator huCollator = new RuleBasedCollator(hungarianRules);
											sortPartnerNev(huCollator, partnerek);
											adapter.clear();
											adapter.addAll(partnerek);
											adapter.notifyDataSetChanged();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										break;
									case 1:
										try {
											Collator huCollator = new RuleBasedCollator(hungarianRules);
											sortPartnerCim(huCollator, partnerek);
											adapter.clear();
											adapter.addAll(partnerek);
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
							partnerek=new ArrayList<Partner>(partnerDao.queryBuilder().where(Properties.PartnerIsActive.eq(true)).list());
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
		adapter.clear();
		partnerek=new ArrayList<Partner>(partnerDao.queryBuilder().where(Properties.PartnerIsActive.eq(true)).list());
		adapter.addAll(partnerek);
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
	

	String hungarianRules = ("< a,A < á,Á < b,B < c,C < cs,Cs < d,D < dz,Dz < dzs,Dzs "
			+ "< e,E < é,É < f,F < g,G < gy,Gy < h,H < i,I < í,Í < j,J "
			+ "< k,K < l,L < ly,Ly < m,M < n,N < ny,Ny < o,O < ó,Ó "
			+ "< ö,Ö < õ,Õ < p,P < q,Q < r,R < s,S < sz,Sz < t,T "
			+ "< ty,Ty < u,U < ú,Ú < ü,Ü < û,Û < v,V < w,W < x,X < y,Y < z,Z < zs,Zs");
	
	public static void sortPartnerNev(Collator collator, List<Partner> partnerList)
	{
		Partner tmp;
		for(int i = 0; i<partnerList.size();i++)
		{
			for(int j = 0; j <partnerList.size();j++)
			{
				if(collator.compare(partnerList.get(j).getPartnerNev(), partnerList.get(i).getPartnerNev())>0)
				{
					tmp = partnerList.get(i);
					partnerList.set(i, partnerList.get(j));
					partnerList.set(j, tmp);
				}
			}
		}
	}
	
	public static void sortPartnerCim(Collator collator, List<Partner> partnerList)
	{
		Partner tmp;
		for(int i = 0; i<partnerList.size();i++)
		{
			for(int j = 0; j <partnerList.size();j++)
			{
				if(collator.compare(partnerList.get(j).getPartnerCim(), partnerList.get(i).getPartnerCim())>0)
				{
					tmp = partnerList.get(i);
					partnerList.set(i, partnerList.get(j));
					partnerList.set(j, tmp);
				}
			}
		}
	}
	
}
