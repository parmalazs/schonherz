package com.schonherz.fragments;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;

import com.schonherz.adapters.AutoKepImageAdapter;
import com.schonherz.adapters.MunkaKepImageAdapter;
import com.schonherz.adapters.PartnerKepImageAdapter;
import com.schonherz.adapters.ProfilKepImageAdapter;
import com.schonherz.dbentities.AutoKep;
import com.schonherz.dbentities.AutoKepDao;
import com.schonherz.dbentities.MunkaKep;
import com.schonherz.dbentities.MunkaKepDao;
import com.schonherz.dbentities.PartnerKep;
import com.schonherz.dbentities.PartnerKepDao;
import com.schonherz.dbentities.ProfilKep;
import com.schonherz.dbentities.ProfilKepDao;
import com.schonherz.dbentities.ProfilKepDao.Properties;
import com.schonherz.flottadroid.R;

public class AdminKepekFragment extends Fragment {

	Context context;
	PartnerKepDao partnerKepDao;
	ProfilKepDao profilKepDao;
	AutoKepDao autoKepDao;
	MunkaKepDao munkaKepDao;

	ProfilKepImageAdapter profilImgAdapter;
	PartnerKepImageAdapter partnerImgAdapter;
	MunkaKepImageAdapter munkaKepImgAdapter;
	AutoKepImageAdapter autoKepImgAdapter;

	Gallery soforPicsGallery;
	Gallery partnerPicsGallery;
	Gallery munkaPicsGallery;
	Gallery autoPicsGallery;

	public AdminKepekFragment(Context context, PartnerKepDao partnerKepDao,
			ProfilKepDao profilKepDao, AutoKepDao autoKepDao,
			MunkaKepDao munkaKepDao) {

		this.context = context;
		this.partnerKepDao = partnerKepDao;
		this.autoKepDao = autoKepDao;
		this.profilKepDao = profilKepDao;
		this.munkaKepDao = munkaKepDao;
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
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View v = inflater.inflate(R.layout.layout_admin_pictures, null);
		
		List<ProfilKep> profilkepek = profilKepDao.queryBuilder().where(Properties.ProfilKepIsActive.eq(true)).list();				
		List<AutoKep> autoKepek = autoKepDao.queryBuilder().where(com.schonherz.dbentities.AutoKepDao.Properties.AutoKepIsActive.eq(true)).list();
		List<PartnerKep> partnerKepek = partnerKepDao.queryBuilder().where(com.schonherz.dbentities.PartnerKepDao.Properties.PartnerKepIsActive.eq(true)).list();
		List<MunkaKep> munkaKepek = munkaKepDao.queryBuilder().where(com.schonherz.dbentities.MunkaKepDao.Properties.MunkaKepIsActive.eq(true)).list();
		
		partnerPicsGallery = (Gallery)v.findViewById(R.id.partnerGallery);
		partnerImgAdapter = new PartnerKepImageAdapter(context, partnerKepDao, 0, partnerKepek);
		partnerPicsGallery.setAdapter(partnerImgAdapter);
		
		partnerPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int pos,
					long id) {			
				// TODO Auto-generated method stub
			 	
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);
								   
			    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
			    
			    Bitmap bm = BitmapFactory.decodeFile(((PartnerKep)parent.getItemAtPosition(pos)).getPartnerKepPath());
			    currProfIv.setImageBitmap(bm);
			    
			    if(((PartnerKep)parent.getItemAtPosition(pos)).getPartner()!=null)
			    {
			    dialog.setTitle(((PartnerKep)parent.getItemAtPosition(pos)).getPartner().getPartnerNev());				    
			    }
			    currProfIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			    
			    currProfIv.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogAl, int which) {
								switch (which) {
									case DialogInterface.BUTTON_POSITIVE :
										//s.removePicture(selectedPicture);
										@SuppressWarnings("unused")
										boolean pix = new File(((PartnerKep)parent.getItemAtPosition(pos)).getPartnerKepPath()).delete();
										
										((PartnerKep)parent.getItemAtPosition(pos)).setPartnerKepIsActive(false);
										partnerKepDao.update(((PartnerKep)parent.getItemAtPosition(pos)));
										((PartnerKep)parent.getItemAtPosition(pos)).refresh();
										
										partnerImgAdapter.remove(((PartnerKep)parent.getItemAtPosition(pos)));
										partnerImgAdapter.notifyDataSetChanged();
										
										
										dialogAl.dismiss();
										dialog.dismiss();											

										break;
									case DialogInterface.BUTTON_NEGATIVE :
										dialogAl.dismiss();
										dialog.dismiss();
										break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage("Biztosan törli?")
								.setPositiveButton("Igen",
										dialogClickListener)
								.setNegativeButton("Nem", dialogClickListener)
								.show();

						
						return false;
					}
				});
			    				   
			    dialog.show();
			}
		});
		
		munkaPicsGallery = (Gallery)v.findViewById(R.id.munkakGallery);
		munkaKepImgAdapter = new MunkaKepImageAdapter(context, munkaKepDao, 0, munkaKepek);
		munkaPicsGallery.setAdapter(munkaKepImgAdapter);
		
		munkaPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int pos,
					long id) {
				// TODO Auto-generated method stub
			 	
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);
								   
			    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
			    
			    Bitmap bm = BitmapFactory.decodeFile(((MunkaKep)parent.getItemAtPosition(pos)).getMunkaKepPath());
			    currProfIv.setImageBitmap(bm);
			    
			    if(((MunkaKep)parent.getItemAtPosition(pos)).getMunka()!=null)
			    {
			    dialog.setTitle("Munka ID: "+((MunkaKep)parent.getItemAtPosition(pos)).getMunka().getMunkaID().toString());				    
			    }
			    currProfIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			    
			    currProfIv.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogAl, int which) {
								switch (which) {
									case DialogInterface.BUTTON_POSITIVE :
										//s.removePicture(selectedPicture);
										@SuppressWarnings("unused")
										boolean pix = new File(((MunkaKep)parent.getItemAtPosition(pos)).getMunkaKepPath()).delete();
										
										((MunkaKep)parent.getItemAtPosition(pos)).setMunkaKepIsActive(false);										
										munkaKepDao.update(((MunkaKep)parent.getItemAtPosition(pos)));

										((MunkaKep)parent.getItemAtPosition(pos)).refresh();
										munkaKepImgAdapter.remove(((MunkaKep)parent.getItemAtPosition(pos)));
										munkaKepImgAdapter.notifyDataSetChanged();
										
										dialogAl.dismiss();
										dialog.dismiss();											

										break;
									case DialogInterface.BUTTON_NEGATIVE :
										dialogAl.dismiss();
										dialog.dismiss();
										break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage("Biztosan törli?")
								.setPositiveButton("Igen",
										dialogClickListener)
								.setNegativeButton("Nem", dialogClickListener)
								.show();

						
						return false;
					}
				});
			    				   
			    dialog.show();
			}
		});
		
		autoPicsGallery = (Gallery)v.findViewById(R.id.autokGallery);
		autoKepImgAdapter = new AutoKepImageAdapter(context, autoKepDao, 0, autoKepek);
		autoPicsGallery.setAdapter(autoKepImgAdapter);
		
		autoPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int pos,
					long id) {
				// TODO Auto-generated method stub
			 	
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.layout_image_dialog);
				dialog.setCancelable(true);
								   
			    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
			    
			    Bitmap bm = BitmapFactory.decodeFile(((AutoKep)parent.getItemAtPosition(pos)).getAutoKepPath());
			    currProfIv.setImageBitmap(bm);
			    
			    if(((AutoKep)parent.getItemAtPosition(pos)).getAuto()!=null)
			    {
			    dialog.setTitle(((AutoKep)parent.getItemAtPosition(pos)).getAuto().getAutoRendszam());				    
			    }
			    currProfIv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			    
			    currProfIv.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						// TODO Auto-generated method stub
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogAl, int which) {
								switch (which) {
									case DialogInterface.BUTTON_POSITIVE :
										//s.removePicture(selectedPicture);
										@SuppressWarnings("unused")
										boolean pix = new File(((AutoKep)parent.getItemAtPosition(pos)).getAutoKepPath()).delete();
									
										((AutoKep)parent.getItemAtPosition(pos)).setAutoKepIsActive(false);
										autoKepDao.update(((AutoKep)parent.getItemAtPosition(pos)));
										((AutoKep)parent.getItemAtPosition(pos)).refresh();
										
										autoKepImgAdapter.remove(((AutoKep)parent.getItemAtPosition(pos)));
										autoKepImgAdapter.notifyDataSetChanged();
										
										dialogAl.dismiss();
										dialog.dismiss();											

										break;
									case DialogInterface.BUTTON_NEGATIVE :
										dialogAl.dismiss();
										dialog.dismiss();
										break;
								}
							}
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						builder.setMessage("Biztosan törli?")
								.setPositiveButton("Igen",
										dialogClickListener)
								.setNegativeButton("Nem", dialogClickListener)
								.show();

						
						return false;
					}
				});
			    				   
			    dialog.show();
			}
			
		});
		
		soforPicsGallery = (Gallery)v.findViewById(R.id.soforPicsGallery);
		profilImgAdapter = new ProfilKepImageAdapter(context, profilKepDao, 0, profilkepek);
		
		soforPicsGallery.setAdapter(profilImgAdapter);
		
		soforPicsGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int pos,
					long id) {
				// TODO Auto-generated method stub
				 	
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.layout_image_dialog);
					dialog.setCancelable(true);
									   
				    ImageView currProfIv = (ImageView) dialog.findViewById(R.id.imgDialogImageView);
				    
				    Bitmap bm = BitmapFactory.decodeFile(((ProfilKep)parent.getItemAtPosition(pos)).getProfilKepPath());
				    currProfIv.setImageBitmap(bm);
				    
				    if(((ProfilKep)parent.getItemAtPosition(pos)).getSofor()!=null)
				    {
				    dialog.setTitle(((ProfilKep)parent.getItemAtPosition(pos)).getSofor().getSoforNev());				    
				    }
				    currProfIv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
				    
				    currProfIv.setOnLongClickListener(new OnLongClickListener() {
						
						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialogAl, int which) {
									switch (which) {
										case DialogInterface.BUTTON_POSITIVE :
											//s.removePicture(selectedPicture);
											@SuppressWarnings("unused")
											boolean pix = new File(((ProfilKep)parent.getItemAtPosition(pos)).getProfilKepPath()).delete();
											
											((ProfilKep)parent.getItemAtPosition(pos)).setProfilKepIsActive(false);
											profilKepDao.update(((ProfilKep)parent.getItemAtPosition(pos)));
											((ProfilKep)parent.getItemAtPosition(pos)).refresh();
											
											profilImgAdapter.remove(((ProfilKep)parent.getItemAtPosition(pos)));
											profilImgAdapter.notifyDataSetChanged();
											
											dialogAl.dismiss();
											dialog.dismiss();											
										
											break;
										case DialogInterface.BUTTON_NEGATIVE :
											dialogAl.dismiss();
											dialog.dismiss();
											break;
									}
								}
							};

							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							builder.setMessage("Biztosan törli?")
									.setPositiveButton("Igen",
											dialogClickListener)
									.setNegativeButton("Nem", dialogClickListener)
									.show();

							
							return false;
						}
					});
				    				   
				    dialog.show();
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

}
