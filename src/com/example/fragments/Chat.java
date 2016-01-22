package com.example.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.http.message.BasicNameValuePair;

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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



import com.example.fragments.FragmentGroupInfo.ViewHolder1;
import com.example.mydrchatapp.R;
import com.example.mydrchatapp.WakeLocker;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

public class Chat extends Fragment {
	
	Context context;
	ListView listview;
	TransparentProgressDialog db;
	LazyAdapter mAdapter;
	public ImageLoader imageLoader;
	Boolean isConnected;
	SharedPreferences sp;
	String formattedDate;
	static ArrayList<HashMap<String, String>> chat_list;
	Button attach ;
	ArrayList<HashMap<String, String>> participants_list ;
	LazyAdapter1 mAdapter1;
	ViewHolder holder;
	Boolean notificationArrive = false;
	String unread_msg_count_gcm ;
	 String unread_message_gcm ;
	 String id_gcm ;
	 String chatType_gcm ;
	
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
        View rootView = inflater.inflate(R.layout.chat, container, false);
        
        sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
        
        Editor e = sp.edit();
        e.putString("tab", "0");
        e.commit();
        
        IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.MAIN");
        getActivity().registerReceiver(mHandleMessageReceiver, intentFilter);
        
        context = getActivity();
		
		imageLoader = new ImageLoader(getActivity());
		
		listview = (ListView) rootView.findViewById(R.id.listview);
		attach = (Button) rootView.findViewById(R.id.attach);
		
		isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
		
		
		
		
		
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
		formattedDate = df.format(c.getTime());
		
		Log.e("formattedDate==",""+formattedDate);
		
		attach.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new get_participants_list(sp.getString("user_id", "") , sp.getString("mode", "")).execute(new Void[0]); 
				
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();

				bundle.putString("name",Chat.chat_list.get(position).get("name"));
				bundle.putString("profile_image",Chat.chat_list.get(position).get("profile_image"));
				bundle.putString("user_id",Chat.chat_list.get(position).get("user_id"));
				bundle.putString("mobile_number",Chat.chat_list.get(position).get("mobile_number"));
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Fragment_chat_screen  fragment = new Fragment_chat_screen();
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
		
		
			if (isConnected == true) {
				notificationArrive = false;
		new chat_log(sp.getString("user_id", "")).execute(new Void[0]);
			}
			else {
				showAlertToUser("No internet connection.");
			}
		
		
        return rootView;
    }
    /*********************** ADAPTER CLASS ******************************************/

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

	

		public LazyAdapter(ArrayList result, FragmentActivity chat) {
			chat_list = new ArrayList<HashMap<String,String>>();
			chat_list.addAll(result);
			mInflater = LayoutInflater.from(chat);
		}

		@Override
		public int getCount() {

			return chat_list.size();
		}

		@Override
		public Object getItem(int position) {

			return chat_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.chat_list_item,
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
			
			holder.name.setText(chat_list.get(position).get("name"));
			if(chat_list.get(position).get("attachment").equals("") || chat_list.get(position).get("attachment")==null ){
				
				if(notificationArrive==true){
					
					if(chat_list.get(position).get("user_id").equals(id_gcm)){
						Log.e("yes2","yes2");
						Log.e("unread_message_gcm",""+unread_message_gcm);
						holder.latest_msg.setTypeface(null, Typeface.BOLD);
						holder.count.setVisibility(View.VISIBLE);
					
						holder.count.setText(unread_msg_count_gcm);
						
						holder.latest_msg.setText(unread_message_gcm);
						imageLoader.DisplayImage(chat_list.get(position).get("profile_image"), holder.image);
						
					}
					else {
						holder.count.setVisibility(View.INVISIBLE);
						holder.latest_msg.setText(chat_list.get(position).get("message_text"));
						holder.latest_msg.setTypeface(null, Typeface.NORMAL);
					}
					
				}
				
				else {
				
				if(chat_list.get(position).get("reciever_id").equals(sp.getString("user_id", ""))){
			
				if(chat_list.get(position).get("read_status").equals("Unread")){
					holder.latest_msg.setTypeface(null, Typeface.BOLD);
					holder.count.setVisibility(View.VISIBLE);
					Log.e("unread_count==",""+chat_list.get(position).get("unread_msgs"));
					holder.count.setText(chat_list.get(position).get("unread_msgs"));
				}
				else{
					holder.count.setVisibility(View.INVISIBLE);
					holder.latest_msg.setTypeface(null, Typeface.NORMAL);
				}
				
				}
				holder.latest_msg.setText(chat_list.get(position).get("message_text"));
			}
				
			}
			else {
				
				if(notificationArrive==true){
					if(chat_list.get(position).get("user_id").equals(id_gcm)){
						holder.latest_msg.setTypeface(null, Typeface.BOLD);
						holder.count.setVisibility(View.VISIBLE);
					
						holder.count.setText(unread_msg_count_gcm);
					
							holder.latest_msg.setText("attachment");
							imageLoader.DisplayImage(chat_list.get(position).get("profile_image"), holder.image);
					}
					else {
						holder.count.setVisibility(View.INVISIBLE);
						holder.latest_msg.setText(chat_list.get(position).get("attachment_type"));
						holder.latest_msg.setTypeface(null, Typeface.NORMAL);
					}
				}
				
				else {
				
				if(chat_list.get(position).get("reciever_id").equals(sp.getString("user_id", ""))){
					
					if(chat_list.get(position).get("read_status").equals("Unread")){
						holder.latest_msg.setTypeface(null, Typeface.BOLD);
						holder.count.setVisibility(View.VISIBLE);
						Log.e("unread_count==",""+chat_list.get(position).get("unread_msgs"));
						holder.count.setText(chat_list.get(position).get("unread_msgs"));
					}
					else{
						holder.count.setVisibility(View.INVISIBLE);
						holder.latest_msg.setTypeface(null, Typeface.NORMAL);
					}
					
					
					}
				
				
				holder.latest_msg.setText(chat_list.get(position).get("attachment_type"));
				
			}
			}
			imageLoader.DisplayImage(chat_list.get(position).get("profile_image"), holder.image);
			
			try
			{
			String date = chat_list.get(position).get("created");
			StringTokenizer tk = new StringTokenizer(date);

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
				Log.e("Exception==",""+ae);
			}
			return convertView;
		}

	}

	/****************** ENDING OF ADAPTER CLASS ************************************/
	class ViewHolder {
		TextView name , latest_msg , time , count;
		ImageView image;

	}
	 public class chat_log extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String user_id;
			
			
			ArrayList result;
			ArrayList localArrayList = new ArrayList();

			public chat_log(String string) {
				this.user_id = string;
				
			}

			

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("id",this.user_id));
					
					
					result = function.chat_log(localArrayList);
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
				showAlertToUser("No chat log.");
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
	 
	 //********************************** getting participants list **************************************//
	 
	 public class get_participants_list extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String id;
			String mode;
			String group_id;
			String group_name_text;
			
			
			ArrayList result;
			ArrayList localArrayList = new ArrayList();


			public get_participants_list(String string, String string2) {
				this.id = string;
				this.mode = string2;
				
			}


			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("user_id",this.id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					
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
						showDialogToAddParticipants();
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

			 public void showDialogToAddParticipants() {
				 
				 final Dialog add_mem_dialog;
				 
				 add_mem_dialog = new Dialog(getActivity(),
							R.style.full_screen_dialog);
			
				add_mem_dialog.setCancelable(true);
			
				TextView textView1;
				ListView listview;
				Button back;
				final Button add;
				
				add_mem_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				add_mem_dialog.setContentView(R.layout.participants);
				
				add_mem_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
				textView1 = (TextView) add_mem_dialog.findViewById(R.id.textView1);
				listview = (ListView) add_mem_dialog.findViewById(R.id.listview);
				back = (Button) add_mem_dialog.findViewById(R.id.back);
				add = (Button) add_mem_dialog.findViewById(R.id.add);
				
				textView1.setText("Favourites");
				
				
				
				
				mAdapter1 = new LazyAdapter1(participants_list, getActivity());
				listview.setAdapter(mAdapter1);
				
				listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
				
				//--	text filtering
				listview.setTextFilterEnabled(true);
				
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						add_mem_dialog.dismiss();
						Bundle bundle = new Bundle();

						bundle.putString("name",participants_list.get(position).get("name"));
						bundle.putString("profile_image",participants_list.get(position).get("profile_image"));
						bundle.putString("user_id",participants_list.get(position).get("user_id"));
						bundle.putString("mobile_number",participants_list.get(position).get("mobile_number"));
						
						FragmentManager fm = getActivity().getSupportFragmentManager();
				        FragmentTransaction ft = fm.beginTransaction();
				        Fragment_chat_screen  fragment = new Fragment_chat_screen();
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
				
				back.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						add_mem_dialog.dismiss();
						
					}
				});
				
				add_mem_dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				
				add_mem_dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				
				add_mem_dialog.show();
				
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
									R.layout.favourite_list, null);

							holder.name_textview = (TextView) convertView
									.findViewById(R.id.name_textview);
							
							holder.imageView1 = (ImageView) convertView
									.findViewById(R.id.imageView1);
							
							holder.TextView1 = (TextView) convertView
									.findViewById(R.id.TextView1);
						
							convertView.setTag(holder);

						}

						else {
							holder = (ViewHolder1) convertView.getTag();
						}
						holder.name_textview.setText(participants_list.get(position).get("name"));
						
						holder.TextView1.setText(participants_list.get(position).get("mobile_number"));
						String url = participants_list.get(position).get("profile_image");
						imageLoader.DisplayImage(url, holder.imageView1);
						
						
						return convertView;
					}

				}

				/****************** ENDING OF ADAPTER CLASS ************************************/
				class ViewHolder1 {
					TextView name_textview;
					ImageView imageView1;
					TextView TextView1;

				}
				
				 private  BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {
							Log.i("<>BROADCAST<>","<>RECEIVER<>");
						
							 unread_msg_count_gcm = intent.getStringExtra("unread_msg");
							 unread_message_gcm = intent.getStringExtra("msg");
							 id_gcm = intent.getStringExtra("id");
							 chatType_gcm = intent.getStringExtra("open");
							
							Log.i("BROADCAST MESSAGE","=="+unread_msg_count_gcm);
						
							
							
							if(chatType_gcm.equalsIgnoreCase("single")){
							
							if(unread_msg_count_gcm!=null || unread_msg_count_gcm!="" || !unread_msg_count_gcm.equalsIgnoreCase("0")){
								Log.e("notify==","notify==");
								notificationArrive = true;
								mAdapter.notifyDataSetChanged();
									//notificationArrive = false;
							}
							}
						

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