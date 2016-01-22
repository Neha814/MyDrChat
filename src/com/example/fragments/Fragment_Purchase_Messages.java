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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Fragment_Purchase_Messages extends Fragment {
	
	ListView listview;
	SharedPreferences sp;
	TransparentProgressDialog db;
	LazyAdapter mAdapter;
	Button back;
	public ImageLoader imageLoader;
	Boolean isConnected;
	
	
	public static ArrayList<HashMap<String, String>> DoctorList;
	

	
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
        View rootView = inflater.inflate(R.layout.docotr_list, container, false);
        
        sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
        
        imageLoader = new ImageLoader(getActivity());
		listview = (ListView) rootView.findViewById(R.id.listview);
		back = (Button) rootView.findViewById(R.id.back);
		
		isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
		if (isConnected == true) {
		new doctor_list(sp.getString("user_id", ""),"Doctor Plan" ).execute(new Void[0]);
		}
		
		else {
			showAlertToUser("No internet connection.");
		}
		
			back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Settings  fragment = new Settings();
		     
		  
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

				String dr_id = DoctorList.get(position).get("doctor_id");
				Editor e = sp.edit();
				e.putString("doctor_id", dr_id);
				e.commit();
			
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Fragment_Select_Plan fragment = new Fragment_Select_Plan();
		        
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
    public class doctor_list extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String user_id;
		String check;
		
		ArrayList result;
		ArrayList localArrayList = new ArrayList();

		public doctor_list(String string, String string2) {
			this.user_id = string;
			this.check = string2;
		}

		

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("user_id",this.user_id));
				localArrayList.add(new BasicNameValuePair("Check",this.check));
				
				result = function.doctor_list(localArrayList);
				Log.e("result==",""+result);
				Log.e("result size==",""+result.size());

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

		

	
//		public LazyAdapter(ArrayList<HashMap<String, String>> result,
//				Fragment_Purchase_Messages purchase_Messages) {
//			DoctorList = new ArrayList<HashMap<String,String>>();
//			DoctorList.addAll(result);
//			mInflater = LayoutInflater.from(purchase_Messages);
//		}

		public LazyAdapter(ArrayList<HashMap<String, String>> result, FragmentActivity activity) {
			DoctorList = new ArrayList<HashMap<String,String>>();
			DoctorList.addAll(result);
			mInflater = LayoutInflater.from(activity);
		}

		@Override
		public int getCount() {

			return DoctorList.size();
		}

		@Override
		public Object getItem(int position) {

			return DoctorList.get(position);
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
				convertView = mInflater.inflate(R.layout.dr_list_item,
						null);

				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.specialisation = (TextView) convertView.findViewById(R.id.specialisation);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				
				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setText(DoctorList.get(position).get("name"));
			holder.specialisation.setText(DoctorList.get(position).get("specialty"));
			
			
			imageLoader.DisplayImage(DoctorList.get(position).get("profile_image"), holder.image);

			
			return convertView;
		}

	}

	/****************** ENDING OF ADAPTER CLASS ************************************/
	class ViewHolder {
		TextView name , specialisation;
		ImageView image;

	}


}