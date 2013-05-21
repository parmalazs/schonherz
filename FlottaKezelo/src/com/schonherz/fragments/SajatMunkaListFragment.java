package com.schonherz.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.schonherz.adapters.MunkaAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.classes.SessionManager;
import com.schonherz.dbentities.Munka;
import com.schonherz.dbentities.MunkaDao;
import com.schonherz.dbentities.MunkaDao.Properties;
import com.schonherz.flottadroid.MunkaDetailsActivity;
import com.schonherz.flottadroid.R;

import de.greenrobot.dao.QueryBuilder;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

public class SajatMunkaListFragment extends Fragment {
	
	Context context;
	MunkaDao munkaDao;
	PullToRefreshListView pullListView;
	MunkaAdapter adapter;
	ArrayList<Munka> munkak;
	MenuItem refreshItem;
	boolean dateSortAsc = true;
	boolean telepheyAsc = true;
	boolean estTimeAsc = true;
	boolean munkaTypeAsc = true;
	
	SessionManager sessionManager;
	
	public SajatMunkaListFragment(Context context, MunkaDao munkaDao) {
		this.context = context;
		this.munkaDao = munkaDao;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		sessionManager=new SessionManager(context);
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
					ArrayList<Munka> templist = new ArrayList<Munka>();

					for (int i = 0; i < munkak.size(); i++) {
						if (munkak.get(i).getMunkaDate().toLowerCase()
								.contains(query.toLowerCase())) {
							templist.add(munkak.get(i));
						}
					}

					adapter.clear();
					adapter.addAll(templist);
					adapter.notifyDataSetChanged();
				}
				else
				{
				adapter.clear();
				adapter.addAll(munkak);
				adapter.notifyDataSetChanged();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub

				ArrayList<Munka> templist = new ArrayList<Munka>();

				for (int i = 0; i < munkak.size(); i++) {
					if (munkak.get(i).getMunkaDate().toLowerCase()
							.contains(newText.toLowerCase())) {
						templist.add(munkak.get(i));
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

		munkak = new ArrayList<Munka>(munkaDao.queryBuilder().where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list());
		adapter = new MunkaAdapter(context, R.layout.list_item_munka, munkak,
				munkaDao);
		pullListView.setAdapter(adapter);
		
		pullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), MunkaDetailsActivity.class);
				intent.putExtra("currentMunkaID", munkak.get(position).getMunkaID());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});

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

							munkak = new ArrayList<Munka>(
									munkaDao.queryBuilder().where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list());
							adapter.addAll(munkak);
							adapter.notifyDataSetChanged();
							
							if (refreshItem != null && refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}
							
							stopRefreshAnimation();
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
		switch(item.getItemId())
		{
			case R.id.menu_Sort :
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Rendezés");
				final CharSequence[] choiceList = {"Idõ", "Telephely","Becsült idõ", "Munkatípus"};

				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(choiceList, selected,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								QueryBuilder<Munka> q=munkaDao.queryBuilder().where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID)));
								switch(which)
								{
									case 0:
										if(dateSortAsc == false)
										{
											List<Munka> tempMunkaSort = q.orderAsc(Properties.MunkaDate).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = true;
										}
										else
										{
											List<Munka> tempMunkaSort = q.orderDesc(Properties.MunkaDate).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = false;
										}
										break;
									case 1:
										if(telepheyAsc == false)
										{
											List<Munka> tempMunkaSort = q.orderAsc(Properties.TelephelyID).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = true;
										}
										else
										{
											List<Munka> tempMunkaSort = q.orderDesc(Properties.TelephelyID).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = false;
										}
										break;
									case 2:
										if(estTimeAsc == false)
										{
											List<Munka> tempMunkaSort = q.orderAsc(Properties.MunkaEstimatedTime).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = true;
										}
										else
										{
											List<Munka> tempMunkaSort = q.orderDesc(Properties.MunkaEstimatedTime).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = false;
										}
										break;
									case 3:
										if(munkaTypeAsc == false)
										{
											List<Munka> tempMunkaSort = q.orderAsc(Properties.MunkaTipusID).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = true;
										}
										else
										{
											List<Munka> tempMunkaSort = q.orderDesc(Properties.MunkaTipusID).list();
											munkak.clear();
											munkak.addAll(tempMunkaSort);
											adapter.clear();
											adapter.addAll(munkak);
											adapter.notifyDataSetChanged();
											dateSortAsc = false;
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
					refreshItem = item;
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

							munkak = new ArrayList<Munka>(
									munkaDao.queryBuilder().where(Properties.SoforID.eq(sessionManager.getUserID().get(SessionManager.KEY_USER_ID))).list());
							adapter.addAll(munkak);
							adapter.notifyDataSetChanged();
							
							if (refreshItem != null && refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}
							
							stopRefreshAnimation();
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
				}
							
				
				break;
		}
		
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
			ArrayList<Munka> munkak = JsonArrayToArrayList
					.JsonArrayToMunka(jsonArray);
			for (int i = 0; i < munkak.size(); i++) {
				munkaDao.insert(munkak.get(i));
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
