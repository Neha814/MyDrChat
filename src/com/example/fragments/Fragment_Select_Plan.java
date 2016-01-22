package com.example.fragments;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;


import com.example.mydrchatapp.R;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Fragment_Select_Plan extends Fragment {
	
	LazyAdapter mAdapter;
	public ImageLoader imageLoader;
	ListView listview;
	Boolean isConnected;
	SharedPreferences sp;
	TransparentProgressDialog db;
	Button back;
	public static ArrayList<HashMap<String, String>> Doctorplan;
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

					}
				});
		localBuilder.create().show();
	}
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.select_plan, container, false);
	        
	        sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
	        
	        listview = (ListView)rootView.findViewById(R.id.listview);
			back = (Button) rootView.findViewById(R.id.back);
			
			String dr_id = sp.getString("doctor_id","");
			
			isConnected = NetConnection
					.checkInternetConnectionn(getActivity());
			if (isConnected == true) {
			new doctor_msg_plan(dr_id).execute(new Void[0]);
			}
			else {
				showAlertToUser("No internet connection.");
			}
			
			back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_Purchase_Messages  fragment = new Fragment_Purchase_Messages();
			     
			  
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
			
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					Log.e("listview item","clicked");

					String plan_id = Doctorplan.get(position).get("plan_id");
					Editor e = sp.edit();
					e.putString("plan_id", plan_id);
					e.putString("amount", Doctorplan.get(position).get("price"));
					e.putString("no_of_msg", Doctorplan.get(position).get("number_of_message"));
					e.commit();
					
					
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_Payment_option  fragment = new Fragment_Payment_option();
			        
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
	        
	        return rootView;
	  }
	  public class doctor_msg_plan extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String d_id;
		
			
			ArrayList result;
			ArrayList localArrayList = new ArrayList();

			public doctor_msg_plan(String string) {
				this.d_id = string;
				
			}

			

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("doctor_id",this.d_id));
				
					result = function.doctor_msg_plan(localArrayList);
					

				} catch (Exception localException) {

				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();
				
				try{
				
				if (result.size()>0) {
					
					
					mAdapter = new LazyAdapter(result, getActivity());
					listview.setAdapter(mAdapter);
				}
				
			else {
				showAlertToUser("Something went wrong while processing your request.Please try again.");
				}

				}
				
				catch (Exception ae){
					Log.e("Exception==",""+ae);
					showAlertToUser("Something went wrong while processing your request.Please try again.");
				}

			}

			protected void onPreExecute() {
				super.onPreExecute();
				db = new TransparentProgressDialog(getActivity(),
						R.drawable.loading);
				db.show();
			}

		}
	 
	 /*********************** ADAPTER CLASS ******************************************/

		class LazyAdapter extends BaseAdapter {

			LayoutInflater mInflater = null;

		
			public LazyAdapter(ArrayList<HashMap<String, String>> result, FragmentActivity activity) {
				Doctorplan = new ArrayList<HashMap<String,String>>();
				Doctorplan.addAll(result);
				mInflater = LayoutInflater.from(activity);
			}

			@Override
			public int getCount() {

				return Doctorplan.size();
			}

			@Override
			public Object getItem(int position) {

				return Doctorplan.get(position);
			}

			@Override
			public long getItemId(int position) {

				return position;
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				ViewHolder holder;
				if (convertView == null) {

					holder = new ViewHolder();
					convertView = mInflater.inflate(R.layout.select_plan_list,
							null);

					holder.amount = (TextView) convertView.findViewById(R.id.amount);
					holder.no_of_msg = (TextView) convertView.findViewById(R.id.no_of_msg);
				
					
					convertView.setTag(holder);

				}

				else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.amount.setText("$"+Doctorplan.get(position).get("price"));
				holder.no_of_msg.setText(Doctorplan.get(position).get("number_of_message"));
				
				
				return convertView;
			}

		}

		/****************** ENDING OF ADAPTER CLASS ************************************/
		class ViewHolder {
			TextView amount , no_of_msg;
			
		}

}

