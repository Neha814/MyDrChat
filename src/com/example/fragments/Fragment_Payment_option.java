package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



import com.example.mydrchatapp.R;
import com.macrew.functions.Functions;
import com.macrew.utils.Encryption_Decryption;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

public class Fragment_Payment_option extends Fragment {
	
	Button new_card, previous_card;
	LinearLayout ll;
	ListView listview;
	Context mContext;
	static byte[] key;
	SharedPreferences sp;
	static String seedValue = "This Is My Secure Password";
	String name;
	String expiry_date;
	String CVV;
	String card_type;
	Boolean isConnected , Success=false;
	Button back;
	TransparentProgressDialog db;
	
	static ArrayList<String> array =new ArrayList<String>();
	
	
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						if(Success){
							
							
							FragmentManager fm = getActivity().getSupportFragmentManager();
					        FragmentTransaction ft = fm.beginTransaction();
					        Settings fragment = new Settings();
					        
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
	        View rootView = inflater.inflate(R.layout.choose_card_option, container, false);
	        
	        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

			
			mContext = getActivity();
			new_card = (Button) rootView.findViewById(R.id.new_card);
			previous_card = (Button) rootView.findViewById(R.id.previous_card);
			ll = (LinearLayout) rootView.findViewById(R.id.ll);
			listview = (ListView) rootView.findViewById(R.id.listview);
			back = (Button) rootView.findViewById(R.id.back);
			
				back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_Select_Plan  fragment = new Fragment_Select_Plan();
			     
			  
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
			
			new_card.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
			        FragmentTransaction ft = fm.beginTransaction();
			        Fragment_Bank_Details fragment = new Fragment_Bank_Details();
			        
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
			previous_card.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						
						loadArray(mContext);
						
					name = sp.getString("encrypted_name","");
					name = Encryption_Decryption.decrypt(seedValue,name);
					
					expiry_date =sp.getString("encrypted_expiry_date","");
					expiry_date = Encryption_Decryption.decrypt(seedValue,expiry_date);
					
					CVV =sp.getString("encrypted_CVV","");
					CVV = Encryption_Decryption.decrypt(seedValue,CVV);
					
					card_type =sp.getString("encrypted_card_type","");
					card_type = Encryption_Decryption.decrypt(seedValue,card_type);
					
					Log.e("name ==",""+name);
					Log.e("expiry_date ==",""+expiry_date);
					Log.e("CVV ==",""+CVV);
					Log.e("card_type ==",""+card_type);
					}
					
					catch(Exception ae){
						Log.e("Exception ==",""+ae);
					}
					
					if(array.size()>0){
						
						ll.setVisibility(View.VISIBLE);
					Animation bottomUp = AnimationUtils.loadAnimation(
							getActivity(), R.anim.listview_bottom_up);

					ll.startAnimation(bottomUp);
					
					 ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				              R.layout.simple_list_item, R.id.text, array);
				    
				    
				            // Assign adapter to ListView
					 listview.setAdapter(adapter); 

					
					}
					
					else {
						showAlertToUser("No saved data.Please try with new details.");
					}
					
				}
			});
			
			listview.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String card_no = array.get(position);
					
					isConnected = NetConnection.checkInternetConnectionn(getActivity());
					if (isConnected == true) {


					new payment(sp.getString("user_id",""),sp.getString("doctor_id",""),sp.getString("plan_id",""),
							card_no,name,expiry_date,CVV,sp.getString("card_type", "")).execute(new Void[0]);
					
					}
					
					else {
						showAlertToUser("No internet connection.");
					}
					
				}
			});
			
	        return rootView;
	    }
	 public static void loadArray(Context mContext)
		{  
		    SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(mContext);
		    array.clear();
		    int size = mSharedPreference1.getInt("Status_size", 0);  
		    Log.e("size==",""+size);
		    for(int i=0;i<size;i++) 
		    {	
		    	String decrypted_value = mSharedPreference1.getString("Status_" + i, null);
		    	
		    	
		    	
		    
		    	try {
					decrypted_value =Encryption_Decryption.decrypt(seedValue,decrypted_value);
					
					String sub_string = decrypted_value.substring(11, 15);
			    	Log.e("sub_string===",""+sub_string);
			    	decrypted_value = "************"+sub_string;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	array.add(decrypted_value);
		    	Log.e("array==",""+array);
		    	Log.e("array.size()==",""+array.size());
		    }
		}

		
		/**
		 * payment class
		 */
		 public class payment extends AsyncTask<Void, Void, Void> {
				Functions function = new Functions();
			
				String  user_id , dr_id, plan_id , card_num , name , expiry_date , cvv , card_typ;
			
				
				HashMap result;
				ArrayList localArrayList = new ArrayList();

				public payment(String string, String string2, String string3,
						String c_number, String nAME, String ex_date, String cVV , String crd_typ) {
					this.user_id = string;
					this.dr_id = string2;
					this.plan_id = string3;
					this.card_num = c_number;
					this.name = nAME;
					this.expiry_date = ex_date;
					this.cvv = cVV;
					this.card_typ = crd_typ;
				}


				

				protected Void doInBackground(Void... paramVarArgs) {
					try {
						localArrayList.add(new BasicNameValuePair("doctor_id",this.dr_id));
						localArrayList.add(new BasicNameValuePair("plan_id",this.plan_id));
						localArrayList.add(new BasicNameValuePair("user_id",this.user_id));
						localArrayList.add(new BasicNameValuePair("card_type",this.card_typ));
						localArrayList.add(new BasicNameValuePair("card_number",this.card_num));
						localArrayList.add(new BasicNameValuePair("card_holder_name",this.name));
						localArrayList.add(new BasicNameValuePair("card_expiry_date",this.expiry_date));
						localArrayList.add(new BasicNameValuePair("security_code",this.cvv));
					
						result = function.payment(localArrayList);
						

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
							showAlertToUser("Successfully purchased.");
							Success = true;
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


	}

