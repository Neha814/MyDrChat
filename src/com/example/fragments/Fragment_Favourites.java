package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fragments.FragmentGroupInfo.LazyAdapter1;
import com.example.fragments.FragmentGroupInfo.ViewHolder1;
import com.example.fragments.FragmentGroupInfo.add_participants;
import com.example.fragments.FragmentGroupInfo.get_participants_list;
import com.example.fragments.FragmentProfile_Doctor.updateProfileTask;
import com.example.mydrchatapp.R;
import com.macrew.functions.Functions;
import com.macrew.utils.HttpClientUpload;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

public class Fragment_Favourites extends Fragment {
	
	TextView textView1;
	Button add;
	SharedPreferences sp;
	TransparentProgressDialog db;
	ArrayList<Integer> temp_tag_ids = new ArrayList<Integer>();
	ArrayList<HashMap<String, String>> participants_list ;
	ListView listview;
	LazyAdapter1 mAdapter1;
	String members_to_add = "" , x="";
	int tag_id ;
	Boolean isConnected;
	createGroupTask createGroupObj;
	String grp_name;
	String grp_img;
	Boolean isUpdated = false ;
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						
						if(isUpdated){
						
						 FragmentManager fm = getActivity().getSupportFragmentManager();
					        FragmentTransaction ft = fm.beginTransaction();
					      
					        	Groups fragment = new Groups();
					        	
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
		localBuilder.create().show();
	}
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		
		 
		 sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		 isConnected = NetConnection.checkInternetConnectionn(getActivity());
		
		 
		 new get_participants_list(sp.getString("user_id", "") , sp.getString("mode", "") , "").execute(new Void[0]); 
			
	        View rootView = inflater.inflate(R.layout.participants, container, false);
	        
	        Bundle bundle = getArguments();
	       grp_name = bundle.getString("group_name");  
	       grp_img = bundle.getString("img_name");
	       
	       Log.e("grp_img==",""+grp_img);
	        
	        textView1 = (TextView) rootView.findViewById(R.id.textView1);
	        add = (Button) rootView.findViewById(R.id.add);
	        listview = (ListView) rootView.findViewById(R.id.listview);
	        
	        textView1.setText("Favourites");
	        add.setText("CREATE");
	        
	        add.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for(int i = 0;i<temp_tag_ids.size();i++){
						
						members_to_add = participants_list.get(temp_tag_ids.get(i)).get("user_id");
						
						x= members_to_add +"," +x ;
						
						Log.e("x====",""+x);
					}
					
				
					
					if (isConnected == true) {
						createGroupObj = new createGroupTask();
						createGroupObj.execute();
					}

					else {
						showAlertToUser("No internet connection.");
					}
					
					
				}
			});
	        
	        final Handler localHandler3 = new Handler();
			localHandler3.postDelayed(new Runnable() {
				public void run() {
					if (temp_tag_ids.size() > 0) {
						add.setVisibility(View.VISIBLE);
					}

					else {
						add.setVisibility(View.INVISIBLE);
					}
					localHandler3.postDelayed(this, 1000L);
				}
			}, 1000L);
			
	        return rootView;
	 }
	 //********************************** getting participants list **************************************//
	 
	 public class get_participants_list extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String id;
			String mode;
			String group_id;
			String group_name_text;
			
			
			ArrayList result;
			ArrayList localArrayList = new ArrayList();


			public get_participants_list(String string, String string2,
					String string3) {
				this.id = string;
				this.mode = string2;
				this.group_id = string3;
			
				
			}



			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("user_id",this.id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
				
					
					result = function.get_participants_list(localArrayList);
					Log.e("result==",""+result);
				

				} catch (Exception localException) {

				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();
				
				try{
					if(result.size()>0){
						participants_list = new ArrayList<HashMap<String, String>>();
						participants_list.addAll(result);
						
						mAdapter1 = new LazyAdapter1(participants_list, getActivity());
						listview.setAdapter(mAdapter1);
						
						listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
						
						//--	text filtering
						listview.setTextFilterEnabled(true);
						
					}
					
					else {
						showAlertToUser("No active participants are available.");
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

		class LazyAdapter1 extends BaseAdapter {

			LayoutInflater mInflater = null;

			public LazyAdapter1(ArrayList<HashMap<String, String>> participants_list,
					FragmentActivity group_members) {
				
				
				mInflater = LayoutInflater.from(group_members);
			}

			@Override
			public int getCount() {

				return participants_list.size();
			}

			@Override
			public Object getItem(int position) {

				return participants_list.get(position);
			}

			@Override
			public long getItemId(int position) {

				return position;
			}

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				final ViewHolder1 holder;
				if (convertView == null) {

					holder = new ViewHolder1();
					convertView = mInflater.inflate(
							R.layout.participants_list, null);

					holder.checkedTextView1 = (CheckedTextView) convertView
							.findViewById(R.id.checkedTextView1);
				
					convertView.setTag(holder);

				}

				else {
					holder = (ViewHolder1) convertView.getTag();
				}
				holder.checkedTextView1.setText(participants_list.get(position).get("name"));
				holder.checkedTextView1.setTag(position);
				
				try {
					
					if (temp_tag_ids == null || temp_tag_ids.isEmpty()) {
						holder.checkedTextView1.setChecked(false);

					}

					else {
						if (temp_tag_ids.contains(position)) {
							
							holder.checkedTextView1.setChecked(true);
						}

						else {
							holder.checkedTextView1.setChecked(false);
						}
					}
				}

				catch (Exception e) {
					Log.e("Exception==", "" + e);
				}
				holder.checkedTextView1.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						tag_id = (Integer) v.getTag();
						if (holder.checkedTextView1.isChecked())
						{
							// remove
							Log.e("isChecked==",""+holder.checkedTextView1.isChecked());
							holder.checkedTextView1.setChecked(false);
							 if (temp_tag_ids.contains(position)){
								 int i = temp_tag_ids.indexOf(position);
							temp_tag_ids.remove(i);
							 }
							
							holder.checkedTextView1.getTag();
						}
						     else {
						    	 // add
						    	 Log.e("else==","else");
						    	 holder.checkedTextView1.setChecked(true);
						    	 if (!temp_tag_ids.contains(position)){
						    	 temp_tag_ids.add(position);
						    	 }
						    	 
						     }
						
					}
				});
				
				return convertView;
			}

		}

		/****************** ENDING OF ADAPTER CLASS ************************************/
		class ViewHolder1 {
			CheckedTextView checkedTextView1;
			

		}
		
		public class createGroupTask extends AsyncTask<String, Void, String> {
			ByteArrayOutputStream baos;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				db = new TransparentProgressDialog(getActivity(),
						R.drawable.loading);
				db.show();

			}

			@Override
			protected String doInBackground(String... Params) {
				try {
					baos = new ByteArrayOutputStream();
					//takenImage.compress(CompressFormat.PNG, 0, baos);
				}

				catch (Exception e) {
					Log.e("excptn==", "" + e);
				}

				try {
					HttpClient httpclient = new DefaultHttpClient();

					HttpClientUpload client = new HttpClientUpload(
							"http://mydrchat.com/api/group.php");
					client.connectForMultipart();
					
					
					client.addFormPart("id", sp.getString("user_id", ""));
					client.addFormPart("group_name", grp_name);
					client.addFormPart("members", x);
					
					
					if(grp_img.equals("") || grp_img==null){
						
						Log.e("grp_img null==",""+grp_img);
						client.addFormPart("group_icon", "");
					}
					
					else {
						Log.e("grp_img ==",""+grp_img);
						client.addFilePart("group_icon", grp_img,
								baos.toByteArray());
				}
					
					client.finishMultipart();
					
					

					String data = client.getResponse();


					return data;
				} catch (Throwable t) {
					t.printStackTrace();
				}

				return null;

			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				db.dismiss();
				String error;
				Boolean status;
				try {
					// Log.e("result===",""+result);
					JSONObject json = new JSONObject(result);
					if (json.getBoolean("status")) {
						isUpdated = true;
						showAlertToUser("Profile updated successfully.");
					}

					else {
						error = json.getString("error");
						showAlertToUser(error);
					}
				} catch (Exception e) {
					Log.e("Exception===", "" + e);
					showAlertToUser("Something went wrong while processing your request.Please try again.");
				}
			}

		}

}
