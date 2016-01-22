package com.example.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.http.message.BasicNameValuePair;



import com.example.mydrchatapp.R;
import com.example.mydrchatapp.WakeLocker;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class Groups extends Fragment {
	Context context;
	ListView listview;
	TransparentProgressDialog db;
	LazyAdapter mAdapter;
	public ImageLoader imageLoader;
	Boolean isConnected ;
	SharedPreferences sp;
	String formattedDate;
	int longPressPositon ;
	Button create_group;
	 Dialog dialog;
	 
	 Boolean notificationArrive = false;
	 String unread_msg_count_gcm ;
	 String unread_message_gcm ;
	 String id_gcm ;
	 String chatType_gcm ;
	
	public static ArrayList<HashMap<String, String>> group_list;
	
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						
							dialog.dismiss();
						
					}
				});
		localBuilder.create().show();
	}
	
	
    @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group, container, false);
        
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        
        dialog = new Dialog(getActivity(),
				R.style.full_screen_dialog);
        isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
        
		 Editor e = sp.edit();
	        e.putString("tab", "1");
	        e.commit();
	        
	        IntentFilter intentFilter = new IntentFilter(
					"android.intent.action.MAIN");
	        getActivity().registerReceiver(mHandleMessageReceiver, intentFilter);
	        
	        context = getActivity();
			
			imageLoader = new ImageLoader(getActivity());
			
			listview = (ListView) rootView.findViewById(R.id.listview);
			create_group = (Button) rootView.findViewById(R.id.create_group);
			
			create_group.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        
			      
			        	FragmentCreateGroup fragment = new FragmentCreateGroup();
			        	
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
			
			listview.setOnItemLongClickListener(new OnItemLongClickListener() {

	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	                    int pos, long id) {
	               
	            	longPressPositon = pos;
	            
	            	showDialog();

	                return true;
	            }
	        }); 
			
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Bundle bundle = new Bundle();
					bundle.putInt("arg",position);
					bundle.putString("group_id", Groups.group_list.get(position).get("group_id"));
					
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_groupchat_screen  fragment = new Fragment_groupchat_screen();
			        fragment.setArguments(bundle);
			        
			        
			        
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
			
			
			
			Calendar c = Calendar.getInstance();
			System.out.println("Current time => " + c.getTime());

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			formattedDate = df.format(c.getTime());
			
			Log.e("formattedDate===",""+formattedDate);
			
			notificationArrive = false;
			if (isConnected == true) {
				new group(sp.getString("user_id", ""), sp.getString("mode", "")).execute(new Void[0]);
					}
					else {
						showAlertToUser("No internet connection.");
					}
        return rootView;
    }
	

	protected void showDialog() {
		 
			dialog.setCancelable(true);
			
			final Button group_info, exit_group, cancel;
			
			
			 

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.group_info_dialog);
			
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			group_info = (Button) dialog.findViewById(R.id.group_info);
			exit_group = (Button) dialog.findViewById(R.id.exit_group);
			cancel = (Button) dialog.findViewById(R.id.cancel);
			
			cancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			
			exit_group.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new exit_group(sp.getString("user_id", "") , sp.getString("mode", ""), 
							Groups.group_list.get(longPressPositon).get("group_id")).execute(new Void[0]);  
					
				}
			});
			
			group_info.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					 FragmentManager fm = getActivity().getSupportFragmentManager();
				        FragmentTransaction ft = fm.beginTransaction();
				        
				    	Bundle bundle = new Bundle();
						bundle.putInt("arg",longPressPositon);
						
				        	FragmentGroupInfo fragment = new FragmentGroupInfo();
				        	 fragment.setArguments(bundle);
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
			
			
			dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(200);
			dialog.getWindow().setBackgroundDrawable(d);
			dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_top;
			dialog.show();
			
		
	}
	public class group extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String user_id;
			String mode;
			
			
			ArrayList result;
			ArrayList localArrayList = new ArrayList();

			public group(String string , String mode) {
				this.user_id = string;
				this.mode = mode;
				
			}

			

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("id",this.user_id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					
					
					result = function.group(localArrayList);
					Log.e("result==",""+result);
				

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
	 
	 class LazyAdapter extends BaseAdapter {

			LayoutInflater mInflater = null;

		

			public LazyAdapter(ArrayList result, FragmentActivity chat) {
				group_list = new ArrayList<HashMap<String,String>>();
				group_list.addAll(result);
				mInflater = LayoutInflater.from(chat);
			}

			@Override
			public int getCount() {

				return group_list.size();
			}

			@Override
			public Object getItem(int position) {

				return group_list.get(position);
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
					convertView = mInflater.inflate(R.layout.group_list_item,
							null);

					holder.name = (TextView) convertView.findViewById(R.id.name);
					holder.time = (TextView) convertView.findViewById(R.id.time);
					holder.latest_msg = (TextView) convertView.findViewById(R.id.latest_msg);
					holder.image = (ImageView) convertView.findViewById(R.id.image);
					holder.count = (TextView) convertView.findViewById(R.id.count);
					
					holder.count.setVisibility(View.INVISIBLE);
					
					convertView.setTag(holder);

				}

				else {
					holder = (ViewHolder) convertView.getTag();
				}
				
				holder.name.setText(group_list.get(position).get("group_name"));
				if(group_list.get(position).get("attachment_type").equals("") || group_list.get(position).get("attachment")==null ){
					//****** notification ********//
					if(notificationArrive==true){
					
						if(group_list.get(position).get("group_id").equals(id_gcm)){
							holder.latest_msg.setTypeface(null, Typeface.BOLD);
							holder.count.setVisibility(View.VISIBLE);
						
							holder.count.setText(unread_msg_count_gcm);
							
							holder.latest_msg.setText(unread_message_gcm);
							imageLoader.DisplayImage(group_list.get(position).get("group_icon"), holder.image);
						}
						
						else {
							holder.count.setVisibility(View.INVISIBLE);
							holder.latest_msg.setText(group_list.get(position).get("message_text"));
							holder.latest_msg.setTypeface(null, Typeface.NORMAL);
						}
					}
					else {
					String read_status = group_list.get(position).get("read_status");
					List<String> items = Arrays.asList(read_status.split("\\s*,\\s*"));
					
				
					if((items.contains(sp.getString("user_id", "")))){
						
						holder.count.setVisibility(View.INVISIBLE);
						holder.latest_msg.setTypeface(null, Typeface.NORMAL);
					}else {
						holder.latest_msg.setTypeface(null, Typeface.BOLD);
						
						if(group_list.get(position).get("unread_msgs").equals("0")){
							holder.count.setVisibility(View.INVISIBLE);
						}else {
							holder.count.setVisibility(View.VISIBLE);
						holder.count.setText(group_list.get(position).get("unread_msgs"));
						}
					}
					holder.latest_msg.setText(group_list.get(position).get("message_text"));
				}
					}
				
				// ****************** attachment *************************************//
					else {
						
						//******* notification ********//
						if(notificationArrive==true){
							
							if(group_list.get(position).get("group_id").equals(id_gcm)){
								holder.latest_msg.setTypeface(null, Typeface.BOLD);
								holder.count.setVisibility(View.VISIBLE);
							
								holder.count.setText(unread_msg_count_gcm);
								
								holder.latest_msg.setText("attachment");
								imageLoader.DisplayImage(group_list.get(position).get("group_icon"), holder.image);
							}
							
							else {
								holder.count.setVisibility(View.INVISIBLE);
								holder.latest_msg.setText(group_list.get(position).get("message_text"));
								holder.latest_msg.setTypeface(null, Typeface.NORMAL);
							}
						}
						
						else {
						String read_status = group_list.get(position).get("read_status");
						List<String> items = Arrays.asList(read_status.split("\\s*,\\s*"));
						
						if((items.contains(sp.getString("user_id", "")))){
							
							holder.count.setVisibility(View.INVISIBLE);
							holder.latest_msg.setTypeface(null, Typeface.NORMAL);
						}else {
							holder.latest_msg.setTypeface(null, Typeface.BOLD);
							
							if(group_list.get(position).get("unread_msgs").equals("0")){
								holder.count.setVisibility(View.INVISIBLE);
							}else {
								holder.count.setVisibility(View.VISIBLE);
							holder.count.setText(group_list.get(position).get("unread_msgs"));
							}
						}
						
						holder.latest_msg.setText(group_list.get(position).get("attachment_type"));
					}
					}
			
				imageLoader.DisplayImage(group_list.get(position).get("group_icon"), holder.image);
				
				String date = group_list.get(position).get("time");
				try {
				
				StringTokenizer tk = new StringTokenizer(date);
				
				Log.e("date====",""+date);

				String date_text = tk.nextToken();  // <---  yyyy-mm-dd
				String time_text = tk.nextToken();
				
				
				Log.e("date-text==",""+date_text);
				
				if(formattedDate.equals(date_text)){
					holder.time.setText(time_text);
				}
				
				else {
					holder.time.setText(date_text);
				}
				}
				
				catch(Exception ae){
					Log.e("exception==",""+ae);
				}
				return convertView;
			}

		}

		/****************** ENDING OF ADAPTER CLASS ************************************/
		class ViewHolder {
			TextView name , latest_msg , time , count;
			ImageView image;

		}
		
		public class exit_group extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String id;
			String mode;
			String group_id;
			
			
			
			HashMap<String, String> result;
			ArrayList localArrayList = new ArrayList();


			public exit_group(String string, String string2,
					String string3) {
				this.id = string;
				this.mode = string2;
				this.group_id = string3;
			
				
			}



			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("id",this.id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
				
					
					result = function.exit_group(localArrayList);
					Log.e("result==",""+result);
				

				} catch (Exception localException) {

				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();
				
				try{
					if (result.get("result").toString().equalsIgnoreCase("false")) {
						
						showAlertToUser(result.get("error").toString());
					}
					
					else if(result.get("result").toString().equalsIgnoreCase("true")){
						
						
						showAlertToUser("Exit group successfully.");
						
						
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
	
		
		 private  BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					Log.i("<>BROADCAST<>","<>RECEIVER<>");
				
					 unread_msg_count_gcm = intent.getStringExtra("unread_msg");
					 unread_message_gcm = intent.getStringExtra("msg");
					 id_gcm = intent.getStringExtra("messageReceipentId");
					 chatType_gcm = intent.getStringExtra("open");
					
					Log.i("BROADCAST MESSAGE","=="+unread_msg_count_gcm);
				
					
					if(chatType_gcm.equalsIgnoreCase("group")){
					
					if(unread_msg_count_gcm!=null || unread_msg_count_gcm!="" || !unread_msg_count_gcm.equalsIgnoreCase("0")){
						Log.e("notify==","notify==");
						notificationArrive = true;
						mAdapter.notifyDataSetChanged();
						
					} }
				
					// Waking up mobile if it is sleeping
					WakeLocker.acquire(getActivity());

					/**
					 * Take appropriate action on this message depending upon your app
					 * requirement For now i am just displaying it on the screen
					 * */

				
					// Releasing wake lock
					WakeLocker.release();
				}
			};	
			
}
