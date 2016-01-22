package com.example.mydrchatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.example.mydrchatapp.VerifyCode.verification;
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

public class Forget_Password extends Activity{
	
	SharedPreferences sp;
	EditText email;
	Button submit;
	Boolean isConnected , success = false;
	TransparentProgressDialog db;
	
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
						if(success){
							Intent i = new Intent(Forget_Password.this , DoctorUserLogin.class);
							startActivity(i);
						}

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
			
	        setContentView(R.layout.forget_password);
	        
	        sp = PreferenceManager
					.getDefaultSharedPreferences(Forget_Password.this);
	        
	        email = (EditText) findViewById(R.id.email);
	        submit = (Button) findViewById(R.id.submit);
	        
	        submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String EMAIL = email.getText().toString();
					// TODO Auto-generated method stub
					
					if(EMAIL.equals("") || EMAIL.equals(" ") || EMAIL ==null){
						showAlertToUser("Please enter email");
					}
					
					else {
						isConnected = NetConnection
								.checkInternetConnectionn(Forget_Password.this);
						if (isConnected == true) {
							new forget_password(EMAIL).execute(new Void[0]);
						}
						
						else {
							showAlertToUser("No internet connection.");
						}
					}
					
				}
			});
	        
	 }
	 public class forget_password extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String email;
		
			HashMap result;
			ArrayList localArrayList = new ArrayList();

			public forget_password(String paramString1) {
				this.email = paramString1;
				
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("email",
							this.email));
				
					
					
					result = function.forget_password(localArrayList);
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
					
					showAlertToUser("Your new password has been sent to your email id.");
					success = true;
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
				db = new TransparentProgressDialog(Forget_Password.this,
						R.drawable.loading);
				db.show();
			}

		}
}
