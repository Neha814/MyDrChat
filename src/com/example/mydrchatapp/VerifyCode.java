package com.example.mydrchatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;


import com.macrew.functions.Functions;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class VerifyCode extends Activity{
	
	EditText code;
	Button submit;
	Boolean isConnected;
	TransparentProgressDialog db;
	SharedPreferences sp;
	
	public void back(View paramView) {
		finish();
		onBackPressed();
	}
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
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
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
	        setContentView(R.layout.verify_code);
	        
	        sp = PreferenceManager
					.getDefaultSharedPreferences(VerifyCode.this);
	        
	        code = (EditText) findViewById(R.id.code);
	        submit = (Button) findViewById(R.id.submit);
	        
	        submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String CODE = code.getText().toString();
					// TODO Auto-generated method stub
					
					if(CODE.equals("") || CODE.equals(" ") || CODE ==null){
						showAlertToUser("Please enter code");
					}
					
					else {
						isConnected = NetConnection
								.checkInternetConnectionn(VerifyCode.this);
						if (isConnected == true) {
							new verification(CODE).execute(new Void[0]);
						}
						
						else {
							showAlertToUser("No internet connection.");
						}
					}
					
				}
			});
	 }
	        
	 public class verification extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String code;
		
			HashMap result;
			ArrayList localArrayList = new ArrayList();

			public verification(String paramString1) {
				this.code = paramString1;
				
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("verification_code",
							this.code));
					localArrayList.add(new BasicNameValuePair("mobile_number",
							sp.getString("mobile_number", "")));
					
					
					result = function.verification(localArrayList);
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
					
					Intent i = new Intent(VerifyCode.this , Home.class);
					startActivity(i);
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
				db = new TransparentProgressDialog(VerifyCode.this,
						R.drawable.loading);
				db.show();
			}

		}
}
