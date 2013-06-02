package com.schonherz.fragments;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.util.Log;
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
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;

import com.schonherz.adapters.TelephelyAdapter;
import com.schonherz.classes.CSVUtil;
import com.schonherz.classes.JSONBuilder;
import com.schonherz.classes.JSONSender;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Telephely;
import com.schonherz.dbentities.TelephelyDao;
import com.schonherz.dbentities.TelephelyDao.Properties;
import com.schonherz.flottadroid.R;
import com.schonherz.flottadroid.TelephelyDetailsActivity;

@SuppressLint("ValidFragment")
public class TelephelyListFragment extends Fragment {

	Context context;
	TelephelyDao telephelyDao;
	TelephelyAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Telephely> telephelyek;
	Telephely selectedTelephely;
	MenuItem refreshItem;
	final int CONTEXT_MENU_DELETE_ITEM =1;

	public TelephelyListFragment(Context context, TelephelyDao telephelyDao) {
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
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		setupSearchView(searchView);
		refreshItem = menu.findItem(R.id.menu_refresh);
		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.pulltorefresh_listview) {
			menu.add(Menu.NONE, CONTEXT_MENU_DELETE_ITEM, Menu.NONE, R.string.torles);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		selectedTelephely =telephelyDao.queryBuilder().where(Properties.TelephelyID.eq(adapter.getItemId(info.position-1))).list().get(0);
		selectedTelephely.setTelephelyIsActive(false);
		selectedTelephely.refresh();
		telephelyDao.update(selectedTelephely);
		adapter.remove(selectedTelephely);
		adapter.notifyDataSetChanged();  
		
		if (NetworkUtil.checkInternetIsActive(context) == true) {
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					// TODO Auto-generated method stub
					JSONBuilder builder = new JSONBuilder();
					JSONSender sender = new JSONSender();
					JSONObject obj = builder.updateTelephely(selectedTelephely);
					sender.sendJSON(sender.getFlottaUrl(), obj);
					return true;
				}

			}.execute();

		}
		
		return super.onContextItemSelected(item);
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
					ArrayList<Telephely> templist = new ArrayList<Telephely>();
					for (int i = 0; i < telephelyek.size(); i++) {
						if (telephelyek.get(i).getTelephelyNev().toLowerCase()
								.contains(query.toLowerCase())) {
							templist.add(telephelyek.get(i));
						}
					}

					adapter.clear();
					adapter.addAll(templist);
					adapter.notifyDataSetChanged();
				} else {
					adapter.clear();
					adapter.addAll(telephelyek);
					adapter.notifyDataSetChanged();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub

				ArrayList<Telephely> templist = new ArrayList<Telephely>();
				for (int i = 0; i < telephelyek.size(); i++) {
					if (telephelyek.get(i).getTelephelyNev().toLowerCase()
							.contains(newText.toLowerCase())) {
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

		telephelyek = new ArrayList<Telephely>(telephelyDao.queryBuilder().where(Properties.TelephelyIsActive.eq(true)).list());

		adapter = new TelephelyAdapter(context, R.layout.list_item_telephely,
				telephelyek, telephelyDao);
		pullListView.setAdapter(adapter);
		
		registerForContextMenu(pullListView);
		
		pullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), TelephelyDetailsActivity.class);
				intent.putExtra("selectedTelephelyID", telephelyek.get(position-1).getTelephelyID());
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

							telephelyek = new ArrayList<Telephely>(
									telephelyDao.queryBuilder().where(Properties.TelephelyIsActive.eq(true)).list());
							adapter.addAll(telephelyek);
							adapter.notifyDataSetChanged();

							if (refreshItem != null
									&& refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}

							stopRefreshAnimation();

						}

						protected Boolean doInBackground(Void... params) {
							return saveTelephelyTable();
						}

					}.execute();
				} else {
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
			case R.id.menu_sendCSV:
				if(NetworkUtil.checkInternetIsActive(context)==true)
				{
					CSVUtil util = new CSVUtil();
					ArrayList<Telephely> stores= new ArrayList<Telephely>(telephelyDao.loadAll());
					Uri u = util.createTelephelyCSVFile(stores);
					
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_SUBJECT,
							R.string.telephely);
					sendIntent.putExtra(Intent.EXTRA_STREAM, u);
					sendIntent.setType("text/html");
					startActivity(sendIntent); 
					
				}	
				
				break;
			case R.id.menu_Sort :
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.sort);
				final CharSequence[] choiceList = {""+R.string.nev, ""+R.string.cim};

				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(choiceList, selected,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								switch (which) {
									case 0:
										try {
											Collator huCollator = new RuleBasedCollator(hungarianRules);
											sortTelephelyNev(huCollator, telephelyek);
											adapter.clear();
											adapter.addAll(telephelyek);
											adapter.notifyDataSetChanged();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									case 1:
										try {
											Collator huCollator = new RuleBasedCollator(hungarianRules);
											sortTelephelyCim(huCollator, telephelyek);
											adapter.clear();
											adapter.addAll(telephelyek);
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

							telephelyek = new ArrayList<Telephely>(
									telephelyDao.queryBuilder().where(Properties.TelephelyIsActive.eq(true)).list());
							adapter.addAll(telephelyek);
							adapter.notifyDataSetChanged();

							if (refreshItem != null
									&& refreshItem.getActionView() != null) {
								refreshItem.getActionView().clearAnimation();
								refreshItem.setActionView(null);
							}

							stopRefreshAnimation();

						}

						protected Boolean doInBackground(Void... params) {
							return saveTelephelyTable();
						}

					}.execute();
				} else {
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
		telephelyek = new ArrayList<Telephely>(
				telephelyDao.queryBuilder().where(Properties.TelephelyIsActive.eq(true)).list());
		adapter.addAll(telephelyek);
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
			TelephelyDao.dropTable(telephelyDao.getDatabase(), true);
			TelephelyDao.createTable(telephelyDao.getDatabase(), true);
			ArrayList<Telephely> telephelyek = JsonArrayToArrayList
					.JsonArrayToTelephely(jsonArray);

			for (int i = 0; i < telephelyek.size(); i++) {
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
			+ "< ö,Ö < ő,Ő < p,P < q,Q < r,R < s,S < sz,Sz < t,T "
			+ "< ty,Ty < u,U < ú,Ú < ü,Ü < ű,Ű < v,V < w,W < x,X < y,Y < z,Z < zs,Zs");
	
	public static void sortTelephelyNev(Collator collator, List<Telephely> telephelyList)
	{
		Telephely tmp;
		for(int i = 0; i<telephelyList.size();i++)
		{
			for(int j = 0; j <telephelyList.size();j++)
			{
				if(collator.compare(telephelyList.get(j).getTelephelyNev(), telephelyList.get(i).getTelephelyNev())>0)
				{
					tmp = telephelyList.get(i);
					telephelyList.set(i, telephelyList.get(j));
					telephelyList.set(j, tmp);
				}
			}
		}
	}
	
	public static void sortTelephelyCim(Collator collator, List<Telephely> telephelyList)
	{
		Telephely tmp;
		for(int i = 0; i<telephelyList.size();i++)
		{
			for(int j = 0; j <telephelyList.size();j++)
			{
				if(collator.compare(telephelyList.get(j).getTelephelyCim(), telephelyList.get(i).getTelephelyCim())>0)
				{
					tmp = telephelyList.get(i);
					telephelyList.set(i, telephelyList.get(j));
					telephelyList.set(j, tmp);
				}
			}
		}
	}

}
