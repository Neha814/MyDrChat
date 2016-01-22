package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;



import com.example.mydrchatapp.R;
import com.macrew.functions.Functions;
import com.macrew.utils.Encryption_Decryption;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
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
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Fragment_Bank_Details extends Fragment {
	
	Button card_type, submit , back;
	EditText card_number, name, expiry_date, cvv;

	static SharedPreferences sp;
	Boolean isConnected , Success=false;
	byte[] key;
	TransparentProgressDialog db;
	static Context mContext;
	static ArrayList<String> array =new ArrayList<String>();
	
	String seedValue = "This Is My Secure Password";
	
	
	
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
	        View rootView = inflater.inflate(R.layout.bank_details, container, false);
	        
	        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

			card_type = (Button) rootView.findViewById(R.id.card_type);
			back = (Button) rootView.findViewById(R.id.back);
			submit = (Button) rootView.findViewById(R.id.submit);
			card_number = (EditText) rootView.findViewById(R.id.card_number);
			name = (EditText) rootView.findViewById(R.id.name);
			expiry_date = (EditText) rootView.findViewById(R.id.expiry_date);
			cvv = (EditText) rootView.findViewById(R.id.cvv);
			mContext = getActivity();
			
			Editor e = sp.edit();
			e.putString("card_type", "");
			e.commit();
			
			Success=false;
			
				back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
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
			
			submit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final String c_number = card_number.getText().toString();
					final String NAME = name.getText().toString();
					final String ex_date = expiry_date.getText().toString();
					final String CVV = cvv.getText().toString();
					
					if(c_number.equals("") || c_number.equals(" ") ){
						showAlertToUser("Please enter card number.");
					}
					
					else if(c_number.length()<16){
						showAlertToUser("Please enter 16 digit card number.");
					}
					
					else if(NAME.equals("") || NAME.equals(" ")){
						showAlertToUser("Please enter name.");
					}
					
					else if(ex_date.equals("") || ex_date.equals(" ")){
						showAlertToUser("Please enter expiry date.");
					}
					
					else if(CVV.equals("") || CVV.equals(" ")){
						showAlertToUser("Please enter CVV.");
					}
					
					else if(sp.getString("card_type", "").equals("")){
						showAlertToUser("Please select card type.");
					}
					
					else {

					AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
					localBuilder
							.setMessage(
									"Do you want to purchase message = "
											+ sp.getString("no_of_msg", "")
											+ " of $" + sp.getString("amount", ""))
							.setCancelable(false)
							.setPositiveButton("YES",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface paramAnonymousDialogInterface,
												int paramAnonymousInt) {
											paramAnonymousDialogInterface.cancel();

											showSaveDataAlert();

										}

				private void showSaveDataAlert() {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
				localBuilder.setMessage("Do you want to save the card information for future use?")
							.setCancelable(false)
							.setPositiveButton("YES",new DialogInterface.OnClickListener() {
							public void onClick(
							DialogInterface paramAnonymousDialogInterface,int paramAnonymousInt) {
							paramAnonymousDialogInterface.cancel();
							
							try {
					final String encrypted_card_number = Encryption_Decryption.encrypt(seedValue, c_number);
							array.add(encrypted_card_number);
							
					final String encrypted_name = Encryption_Decryption.encrypt(seedValue,NAME);
					final String encrypted_expiry_date = Encryption_Decryption.encrypt(seedValue,ex_date);
					final String encrypted_CVV = Encryption_Decryption.encrypt(seedValue,CVV);
					final String encrypted_card_type = Encryption_Decryption.encrypt(seedValue,sp.getString("card_type", ""));
					saveArray();

				Editor e = sp.edit();
			//	e.putString("encrypted_card_number",encrypted_card_number);
				e.putString("encrypted_name",encrypted_name);
				e.putString("encrypted_expiry_date",encrypted_expiry_date);
				e.putString("encrypted_CVV",encrypted_CVV);
				e.putString("encrypted_card_type",encrypted_card_type);
			
				e.commit();
							}
							
							catch(Exception ae){
								Log.e("Exception==",""+ae);
							}

				isConnected = NetConnection.checkInternetConnectionn(getActivity());
				if (isConnected == true) {
					
				
					
				new payment(sp.getString("user_id",""),sp.getString("doctor_id",""),sp.getString("plan_id",""),
							c_number,NAME,ex_date,CVV,sp.getString("card_type", "")).execute(new Void[0]);
					
					} else {
					showAlertToUser("No internet connection.");
							}

						}
				});
				localBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface paramAnonymousDialogInterface,int paramAnonymousInt) {
				paramAnonymousDialogInterface.cancel();

				isConnected = NetConnection.checkInternetConnectionn(getActivity());
				if (isConnected == true) {
					
					
					
					
				new payment(sp.getString("user_id",""),sp.getString("doctor_id",""),sp.getString("plan_id",""),
							c_number,NAME,ex_date,CVV,sp.getString("card_type", "")).execute(new Void[0]);
						
					
							} else {
						showAlertToUser("No internet connection.");
					}
				}
			});
				localBuilder.create().show();

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
				}
			});
			
			card_type.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					final Dialog dialog = new Dialog(getActivity(),
							R.style.full_screen_dialog);
					dialog.setCancelable(true);
					
					final Button visa;
					final Button master_card, american_express;

					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.card_choice_dialog);
					
					visa = (Button) dialog.findViewById(R.id.visa);
					master_card = (Button) dialog.findViewById(R.id.master_card);
					american_express = (Button) dialog.findViewById(R.id.american_express);
					
					visa.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Editor e = sp.edit();
							e.putString("card_type", visa.getText().toString());
							e.commit();
							dialog.dismiss();
						}
					});
					master_card.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Editor e = sp.edit();
							e.putString("card_type", master_card.getText().toString());
							e.commit();
							dialog.dismiss();
						}
					});
					
					american_express.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Editor e = sp.edit();
							e.putString("card_type", american_express.getText().toString());
							e.commit();
							dialog.dismiss();
						}
					});
					
					
					dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
					dialog.getWindow().setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

					Drawable d = new ColorDrawable(Color.BLACK);
					d.setAlpha(130);
					dialog.getWindow().setBackgroundDrawable(d);
					dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_top;
					dialog.show();
				}
			});
	        return rootView;
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
		 
		 public static boolean saveArray()
		 {
		   
		     SharedPreferences.Editor mEdit1 = sp.edit();
		     mEdit1.putInt("Status_size", array.size()); /* sKey is an array */ 

		     for(int i=0;i<array.size();i++)  
		     {
		         mEdit1.remove("Status_" + i);
		         Log.e("array.get(i)==",""+array.get(i));
		         mEdit1.putString("Status_" + i, array.get(i));  
		     }

		     return mEdit1.commit();     
		 }

	}
