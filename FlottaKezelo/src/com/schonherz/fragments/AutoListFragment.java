package com.schonherz.fragments;

import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.schonherz.adapters.AutoAdapter;
import com.schonherz.classes.JsonArrayToArrayList;
import com.schonherz.classes.JsonFromUrl;
import com.schonherz.classes.NetworkUtil;
import com.schonherz.classes.PullToRefreshListView;
import com.schonherz.classes.PullToRefreshListView.OnRefreshListener;
import com.schonherz.dbentities.Auto;
import com.schonherz.dbentities.AutoDao;
import com.schonherz.flottadroid.CarAdminDetailsActivity;
import com.schonherz.flottadroid.R;

import de.greenrobot.dao.QueryBuilder;

public class AutoListFragment extends Fragment {

	Context context;
	AutoDao autoDao;
	AutoAdapter adapter;
	PullToRefreshListView pullListView;
	ArrayList<Auto> autok;
	MenuItem refreshItem;

	public AutoListFragment(Context context, AutoDao autoDao) {
		this.context = context;
		this.autoDao = autoDao;
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
					ArrayList<Auto> templist = new ArrayList<Auto>();

					for (int i = 0; i < autok.size(); i++) {
						if (autok.get(i).getAutoNev().toLowerCase()
								.contains(query.toLowerCase()))

						{
							templist.add(autok.get(i));

						}
					}

					adapter.clear();
					adapter.addAll(templist);
					adapter.notifyDataSetChanged();
				} else {
					adapter.clear();
					adapter.addAll(autok);
					adapter.notifyDataSetChanged();
				}
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub

				ArrayList<Auto> templist = new ArrayList<Auto>();

				for (int i = 0; i < autok.size(); i++) {
					if (autok.get(i).getAutoNev().toLowerCase()
							.contains(newText.toLowerCase()))

					{
						templist.add(autok.get(i));

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

		autok = new ArrayList<Auto>(autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list());

		adapter = new AutoAdapter(context, R.layout.list_item_auto, autok,
				autoDao);

		pullListView.setAdapter(adapter);	
		
		pullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), CarAdminDetailsActivity.class);
				intent.putExtra("selectedAutoID", autok.get(position-1).getAutoID());
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		
		pullListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (NetworkUtil.checkInternetIsActive(context)) {
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

							autok = new ArrayList<Auto>(autoDao
									.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list());

							adapter.addAll(autok);

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
							return saveAutoTable();
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
				final CharSequence[] choiceList = {"Foglalt", "Név", "Típus",
						"Rendszám", "Kilóméter óra", "Mûszaki vizsga idõ",
						"Szervíz idõ", "Telephely név"};

				int selected = -1; // does not select anything

				builder.setSingleChoiceItems(choiceList, selected,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {	
								QueryBuilder<Auto> q=autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true));
								switch (which) {
									case 0 :
										Collections.sort(autok,
												new Comparator<Auto>() {
													public int compare(
															Auto lhs, Auto rhs) {
														return lhs
																.getAutoFoglalt()
																.compareTo(
																		rhs.getAutoFoglalt());
													};
												});
										adapter.clear();
										adapter.addAll(autok);
										adapter.notifyDataSetChanged();
										break;
									case 1 :
										;
										try {
											RuleBasedCollator huCollator = new RuleBasedCollator(
													hungarianRules);
											sortAutoNev(huCollator, autok);
											adapter.clear();
											adapter.addAll(autok);
											adapter.notifyDataSetChanged();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									case 2 :

										try {
											RuleBasedCollator huCollator = new RuleBasedCollator(
													hungarianRules);
											sortAutoTipus(huCollator, autok);
											adapter.clear();
											adapter.addAll(autok);
											adapter.notifyDataSetChanged();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									case 3 :
										try {
											RuleBasedCollator huCollator = new RuleBasedCollator(
													hungarianRules);
											sortAutoRendszam(huCollator, autok);
											adapter.clear();
											adapter.addAll(autok);
											adapter.notifyDataSetChanged();
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									case 4 :
										Collections.sort(autok,
												new Comparator<Auto>() {
													public int compare(
															Auto lhs, Auto rhs) {
														return lhs
																.getAutoKilometerOra()
																.compareTo(
																		rhs.getAutoKilometerOra());
													};
												});
										adapter.clear();
										adapter.addAll(autok);
										adapter.notifyDataSetChanged();
										break;
									case 5 :
										List<Auto> temp = q
												.orderDesc(
														com.schonherz.dbentities.AutoDao.Properties.AutoMuszakiVizsgaDate)
												.list();
										autok.clear();
										autok.addAll(temp);
										adapter.clear();
										adapter.addAll(autok);
										adapter.notifyDataSetChanged();										
										break;
									case 6 :
										List<Auto> temp2 = q
										.orderDesc(
												com.schonherz.dbentities.AutoDao.Properties.AutoLastSzervizDate)
										.list();
										autok.clear();
										autok.addAll(temp2);
										adapter.clear();
										adapter.addAll(autok);
										adapter.notifyDataSetChanged();	
										break;
									case 7 :
										try {
											RuleBasedCollator huCollator = new RuleBasedCollator(
													hungarianRules);
											sortAutoTelephelyNev(huCollator, autok);
											adapter.clear();
											adapter.addAll(autok);
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

							autok = new ArrayList<Auto>(
									autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list());

							adapter.addAll(autok);

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
							return saveAutoTable();
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

		autok = new ArrayList<Auto>(autoDao
				.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list());

		adapter.addAll(autok);

		adapter.notifyDataSetChanged();
		super.onResume();
	}

	public Boolean saveAutoTable() {
		JSONArray jsonArray;
		JSONObject json;

		String serverAddres = "http://www.flotta.host-ed.me/queryAutoTable.php";

		json = new JSONObject();

		try {
			jsonArray = (JSONArray) JsonFromUrl.getJsonObjectFromUrl(
					serverAddres, json.toString());

			// Eldobjuk a tablat es ujra letrehozzuk
			autoDao.dropTable(autoDao.getDatabase(), true);
			autoDao.createTable(autoDao.getDatabase(), true);

			ArrayList<Auto> autok = JsonArrayToArrayList
					.JsonArrayToAuto(jsonArray);

			for (int i = 0; i < autok.size(); i++) {
				autoDao.insert(autok.get(i));
			}

			Log.w("autolistsize", Integer.toString(autoDao.queryBuilder().where(com.schonherz.dbentities.AutoDao.Properties.AutoIsActive.eq(true)).list().size()));

			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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

	// This code here for Hungarian String comparation

	String hungarianRules = ("< a,A < á,Á < b,B < c,C < cs,Cs < d,D < dz,Dz < dzs,Dzs "
			+ "< e,E < é,É < f,F < g,G < gy,Gy < h,H < i,I < í,Í < j,J "
			+ "< k,K < l,L < ly,Ly < m,M < n,N < ny,Ny < o,O < ó,Ó "
			+ "< ö,Ö < õ,Õ < p,P < q,Q < r,R < s,S < sz,Sz < t,T "
			+ "< ty,Ty < u,U < ú,Ú < ü,Ü < û,Û < v,V < w,W < x,X < y,Y < z,Z < zs,Zs");


	public static void sortAutoNev(Collator collator, List<Auto> autoList) {
		Auto temp;
		for (int i = 0; i < autoList.size(); i++) {
			for (int j = 0; j < autoList.size(); j++) {
				if (collator.compare(autoList.get(j).getAutoNev(), autoList
						.get(i).getAutoNev()) > 0) {
					temp = autoList.get(i);
					autoList.set(i, autoList.get(j));
					autoList.set(j, temp);
				}
			}
		}
	}

	public static void sortAutoTipus(Collator collator, List<Auto> autoList) {
		Auto temp;
		for (int i = 0; i < autoList.size(); i++) {
			for (int j = 0; j < autoList.size(); j++) {
				if (collator.compare(autoList.get(j).getAutoTipus(), autoList
						.get(i).getAutoTipus()) > 0) {
					temp = autoList.get(i);
					autoList.set(i, autoList.get(j));
					autoList.set(j, temp);
				}
			}
		}
	}

	public static void sortAutoRendszam(Collator collator, List<Auto> autoList) {
		Auto temp;
		for (int i = 0; i < autoList.size(); i++) {
			for (int j = 0; j < autoList.size(); j++) {
				if (collator.compare(autoList.get(j).getAutoRendszam(),
						autoList.get(i).getAutoRendszam()) > 0) {
					temp = autoList.get(i);
					autoList.set(i, autoList.get(j));
					autoList.set(j, temp);
				}
			}
		}
	}
	
	public static void sortAutoTelephelyNev(Collator collator, List<Auto> autoList) {
		Auto temp;
		for (int i = 0; i < autoList.size(); i++) {
			for (int j = 0; j < autoList.size(); j++) {
				if (collator.compare(autoList.get(i).getTelephely().getTelephelyNev(),
						autoList.get(j).getTelephely().getTelephelyNev()) > 0) {
					temp = autoList.get(i);
					autoList.set(i, autoList.get(j));
					autoList.set(j, temp);
				}
			}
		}
	}

}