package com.example.fragments;



import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.MainSplashScreen;
import com.example.mydrchatapp.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class Settings extends Fragment {
	
	Button profile, purchase_msg;
	ImageView logout;
	SharedPreferences sp;
	boolean addList = false;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    //	checkisAdded();
    	
        View rootView = inflater.inflate(R.layout.settings, container, false);
        
        profile = (Button) rootView.findViewById(R.id.profile);
		purchase_msg = (Button) rootView.findViewById(R.id.purchase_msg);
		logout = (ImageView) rootView.findViewById(R.id.logout);
		
		
		
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		 Editor e = sp.edit();
	        e.putString("tab", "3");
	        e.commit();
		
		if(sp.getString("mode", "").equalsIgnoreCase("Doctor")){
			purchase_msg.setVisibility(View.INVISIBLE);
		}
		
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        
			        if(sp.getString("mode", "").equalsIgnoreCase("User")){
			        	FragmentProfile fragment = new FragmentProfile();
			        	
			        	if(fragment != null) {
				            // Replace current fragment by this new one
				            
				        	ft.replace(android.R.id.tabcontent, fragment);
				       }
				            else{
				            	 ft.add(android.R.id.tabcontent, fragment);
				            }
				        ft.addToBackStack(null);
				        ft.commit();
				       
				 
					}
			        
			        else if(sp.getString("mode", "").equalsIgnoreCase("Doctor")){
			        	FragmentProfile_Doctor fragment = new FragmentProfile_Doctor();
			        	
			        	if(fragment != null) {
				            // Replace current fragment by this new one
				            
				        	ft.replace(android.R.id.tabcontent, fragment);
				       }
				            else{
				            	 ft.add(android.R.id.tabcontent, fragment);
				            }
				        ft.addToBackStack(null);
				        ft.commit();
			        }
			        
				
			}
		});
		
		purchase_msg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_Purchase_Messages fragment = new Fragment_Purchase_Messages();
			        
			        
			        
			        if(fragment != null) {
			            // Replace current fragment by this new one
			            
			        	ft.replace(android.R.id.tabcontent, fragment);
			       }
			            else{
			            	 ft.add(android.R.id.tabcontent, fragment);
			            }
			        ft.addToBackStack(null);
			 
			        ft.commit();   
				
				
			}
		});
		
		logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
				localBuilder
						.setMessage("Are you sure you want to logout?")
						.setCancelable(false)
						.setPositiveButton("YES",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface paramAnonymousDialogInterface,
											int paramAnonymousInt) {
										paramAnonymousDialogInterface.cancel();

										Editor e = sp.edit();
										e.putBoolean("inHome", false);
										e.commit();

										Intent localIntent1 = new Intent(getActivity(),
												MainSplashScreen.class);
										localIntent1
												.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
														| Intent.FLAG_ACTIVITY_CLEAR_TASK);
										getActivity().finish();
										startActivity(localIntent1);
										
										
									}
								});
				localBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface paramAnonymousDialogInterface,
									int paramAnonymousInt) {
								paramAnonymousDialogInterface.cancel();
							}
						});
				localBuilder.create().show();

			}
		});

	

        return rootView;
    }

   
 
}