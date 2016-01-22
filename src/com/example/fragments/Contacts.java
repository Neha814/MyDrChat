package com.example.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;



import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;
import com.macrew.enitites.enitity;
import com.macrew.functions.Functions;
import com.macrew.utils.NetConnection;
import com.macrew.utils.StringUtils;
import com.macrew.utils.TransparentProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;

public class Contacts extends Fragment {
	
	ListView list;
	LazyAdapter mAdapter;
	EditText search;
	int i = 0;
	TextView search_text;
	ImageView save;
	SharedPreferences sp;
	Boolean isConnected ;
	String toNumbers = "";
	TransparentProgressDialog db;
	
	Functions function = new Functions();
	public ArrayList<enitity> contact_list = new ArrayList<enitity>();
	
	public ArrayList<String> invited = new ArrayList<String>();
	
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
        View rootView = inflater.inflate(R.layout.contacts, container, false);
        
        sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
        
        Editor e = sp.edit();
        e.putString("tab", "2");
        e.commit();
        
    	list = (ListView) rootView.findViewById(R.id.list);
		search = (EditText) rootView.findViewById(R.id.search);
		search_text = (TextView) rootView.findViewById(R.id.search_text);
		save = (ImageView) rootView.findViewById(R.id.save);

		final Handler localHandler3 = new Handler();
		localHandler3.postDelayed(new Runnable() {
			public void run() {
				if (invited.size() > 0) {
					save.setVisibility(View.VISIBLE);
				}

				else {
					save.setVisibility(View.INVISIBLE);
				}
				localHandler3.postDelayed(this, 1000L);
			}
		}, 1000L);

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
		        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
					localBuilder
							.setMessage(
									"Are you sure you want to send message ?")
							.setCancelable(false)
							.setPositiveButton("YES",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface paramAnonymousDialogInterface,
												int paramAnonymousInt) {
											paramAnonymousDialogInterface.cancel();
											
									        sendMessage();
										}
									});
					localBuilder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface paramAnonymousDialogInterface,
										int paramAnonymousInt) {
									paramAnonymousDialogInterface.cancel();
									
									for(int i = 0 ; i<invited.size();i++){
									contact_list.get(i).setSelected(false);
									}
									//invited.clear();
								}
							});
					localBuilder.create().show();
				

			}
		});

		
		
		if(sp.getString("mode", "").equals("Doctor")){
		fetchContacts();
		Log.e("size==", "" + contact_list.size());
		mAdapter = new LazyAdapter(contact_list, getActivity());
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setAdapter(mAdapter);
		}
		else if(sp.getString("mode", "").equals("User")) {
			fetchUserContacts();
		}
		

		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = search.getText().toString()
						.toLowerCase(Locale.getDefault());
				mAdapter.filter(text);
			}
		});
        return rootView;
    }
    private void fetchUserContacts() {
		isConnected = NetConnection
				.checkInternetConnectionn(getActivity());
		if (isConnected == true) {
			
			new doctor_data(sp.getString("user_id", "")).execute(new Void[0]);
		}
		
		else {
			showAlertToUser("No internet connection.");
		}
		
	}

	private void fetchContacts() {
		// TODO Auto-generated method stub

		String phoneNumber = null;
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		StringBuffer output = new StringBuffer();

		

		Cursor cursor = Home.appContext.query(CONTENT_URI, null, null, null,
				null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				Map<String, String> map = new HashMap<String, String>();

				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));

				// AlertsUtils.convertToupperCase(name);

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0 && name.length() > 0) {

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = Home.appContext.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));

						phoneNumber = StringUtils.replaceWords(phoneNumber);

						break;
					}

					contact_list.add(new enitity(name, phoneNumber));

					Collections.sort(contact_list, new Comparator() {

						public int compare(Object o1, Object o2) {
							enitity p1 = (enitity) o1;
							enitity p2 = (enitity) o2;
							return p1.getName().compareToIgnoreCase(
									p2.getName());
						}

					});

					phoneCursor.close();

				}

			}

		}
	}

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;
		private ArrayList<enitity> mDisplayedValues; // Values to be displayed

		public LazyAdapter(ArrayList<enitity> contact_list, FragmentActivity contacts) {
			// TODO Auto-generated constructor stub
			mInflater = LayoutInflater.from(contacts);
			mDisplayedValues = new ArrayList<enitity>();
			mDisplayedValues.addAll(contact_list);

			Log.e("mDisplayedValues===", "" + mDisplayedValues.size());

		}

		// Filter Class
		public void filter(String charText) {

			charText = charText.toLowerCase(Locale.getDefault());

			contact_list.clear();
			if (charText.length() == 0) {

				contact_list.addAll(mDisplayedValues);
			} else {

				for (enitity en : mDisplayedValues) {

					if (en.getName().toLowerCase(Locale.getDefault())
							.startsWith(charText)) {

						Log.e("en===", "" + en);
						contact_list.add(en);

					}
				}
			}
			notifyDataSetChanged();

		}

		@Override
		public int getCount() {
			return contact_list.size();
		}

		@Override
		public Object getItem(int position) {

			return contact_list.get(position);
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
				convertView = mInflater.inflate(R.layout.list_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.checkbox);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				
			

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			holder.checkbox.setTag(position);
			search_text.clearComposingText();

			holder.name.setText(contact_list.get(position).NAME);

			holder.phone.setText(contact_list.get(position).PHONE);

			holder.checkbox.setChecked(contact_list.get(position).isSelected());

			String c = contact_list.get(position).getName().substring(0, 1);

			String n = holder.name.getText().toString();
			
			if(sp.getString("mode", "").equals("User")){
				holder.checkbox.setVisibility(View.INVISIBLE);
			}

		//	search_text.setText(String.valueOf(n.charAt(0)));

			holder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton vw,
								boolean isChecked) {

							int getPosition = (Integer) vw.getTag();
							contact_list.get(getPosition).setSelected(
									vw.isChecked());

							if (contact_list.get(getPosition).isSelected()) {
								Log.e("getPosition(true)==", "" + getPosition);
								if (!invited.contains(contact_list
										.get(getPosition).PHONE)) {
									invited.add(contact_list.get(getPosition).PHONE);
								}

								Log.e("invited size(add)==",
										"" + invited.size());
								Log.e("invited(add) ==", "" + invited);
							}

							else if (contact_list.get(getPosition).isSelected() == false) {
								Log.e("getPosition(false)==", "" + getPosition);

								if (invited.contains(contact_list
										.get(getPosition).PHONE)) {
									invited.remove(contact_list
											.get(getPosition).PHONE);
								}

								Log.e("invited size(remove)==",
										"" + invited.size());
								Log.e("invited(remove) ==", "" + invited);
							}

						}
					});
			return convertView;
		}

	}

	class ViewHolder {
		TextView name, phone;
		Button addFriend;
		CheckBox checkbox;
	}

	/**
	 * send SMS
	 */
	public void sendMessage() {
		
		Log.e("invited size==", "" + invited.size());
		Log.e("invited ==", "" + invited);
		for (String s : invited) {
			toNumbers = toNumbers + s + ";";
		}
		toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
		String message = "Download MyDrChat app from the following link : www.mydrchatapp.com";

		if (message.equals("") || message.equals(" ") || message == null) {
			Toast.makeText(getActivity(), "Please enter message.",
					Toast.LENGTH_LONG).show();
		}

		else {
			// Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
			// Intent intent = new Intent(
			// android.content.Intent.ACTION_SENDTO, sendSmsTo);
			// intent.putExtra("sms_body", message);
			// startActivity(intent);

			try {

				String SENT = "sent";
				String DELIVERED = "delivered";

				Intent sentIntent = new Intent(SENT);
				/* Create Pending Intents */
				PendingIntent sentPI = PendingIntent.getBroadcast(
						getActivity(), 0, sentIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				Intent deliveryIntent = new Intent(DELIVERED);

				PendingIntent deliverPI = PendingIntent.getBroadcast(
						getActivity(), 0, deliveryIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				/* Register for SMS send action */
				getActivity().registerReceiver(new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						String result = "";

						switch (getResultCode()) {

						case Activity.RESULT_OK:
							result = "Transmission successful";
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							result = "Transmission failed";
							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							result = "Radio off";
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							result = "No PDU defined";
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							result = "No service";
							break;
						}

						Toast.makeText(getActivity(), result,
								Toast.LENGTH_LONG).show();
					}

				}, new IntentFilter(SENT));
				/* Register for Delivery event */
				getActivity().registerReceiver(new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						Toast.makeText(getActivity(), "Deliverd",
								Toast.LENGTH_LONG).show();
						
						isConnected = NetConnection
								.checkInternetConnectionn(getActivity());
						if (isConnected == true) {
							
							new invite_users(sp.getString("doctor_id", ""),sp.getString("mode", "") ,toNumbers ).execute(new Void[0]);
						}
						
						else {
							showAlertToUser("No internet connection.");
						}
					}

				}, new IntentFilter(DELIVERED));

				/* Send SMS */
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(toNumbers, null, message, sentPI,
						deliverPI);
				
				
			} catch (Exception ex) {
				Toast.makeText(getActivity(),
						ex.getMessage().toString(), Toast.LENGTH_LONG).show();
				ex.printStackTrace();
			}
		}

	}
	/**
	 * invite users.php
	 */
	
	 public class invite_users extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String dr_id;
			String mode;
			String numbers;
			
			HashMap result;
			ArrayList localArrayList = new ArrayList();

			public invite_users(String paramString1, String paramString2 ,String paramString3) {
				this.dr_id = paramString1;
				this.mode = paramString2;
				this.numbers = paramString3;
				
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("doctor_id",this.dr_id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					localArrayList.add(new BasicNameValuePair("mobile_number",this.numbers));
					
					result = function.invite_users(localArrayList);
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
					showAlertToUser("Successfully invited");
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
	 
	 /**
	  * to fetch user contacts
	  */
	 
	 public class doctor_data extends AsyncTask<Void, Void, Void> {
			
			String user_id;
			
			
			ArrayList<HashMap<String , String>> result;
			ArrayList localArrayList = new ArrayList();

			public doctor_data(String paramString1) {
				this.user_id = paramString1;
			
				
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("user_id",this.user_id));
					
					result = function.doctor_data(localArrayList);
					Log.e("result==",""+result);

				} catch (Exception localException) {

				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				db.dismiss();
				
				try{
				if(result.size()>0){
					contact_list.clear();
			for(int i = 0;i<result.size();i++) 
			{		
				String name = result.get(i).get("name");
				String phoneNumber = result.get(i).get("mobile_number");
				contact_list.add(new enitity(name, phoneNumber));
					
				}

					Collections.sort(contact_list, new Comparator() {

						public int compare(Object o1, Object o2) {
							enitity p1 = (enitity) o1;
							enitity p2 = (enitity) o2;
							return p1.getName().compareToIgnoreCase(
									p2.getName());
						}

					});
					
					Log.e("size==", "" + contact_list.size());
					mAdapter = new LazyAdapter(contact_list, getActivity());
					list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					list.setAdapter(mAdapter);
				}
				
				else {
					showAlertToUser("User has no active contact.");
				}
				
				}
				
				catch (Exception ae){
					showAlertToUser("Something went wrong while processing the request.Please try again.");
				}

			}

			protected void onPreExecute() {
				super.onPreExecute();
				db = new TransparentProgressDialog(getActivity(),
						R.drawable.loading);
				db.show();
			}

		}
}
