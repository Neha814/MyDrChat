package com.example.mydrchatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;


import com.macrew.functions.Data;
import com.macrew.functions.Functions;
import com.macrew.utils.NetConnection;
import com.macrew.utils.StringUtils;
import com.macrew.utils.TransparentProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DoctorUserLogin extends Activity{
	
	
	EditText username , password ;
	Button signin, signup;
	TransparentProgressDialog db;
	SharedPreferences sp;
	Boolean isConnected ;
	TextView forget_password;
	
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
			
	        setContentView(R.layout.user_doctor_login);
	        
	        sp = PreferenceManager
					.getDefaultSharedPreferences(DoctorUserLogin.this);
	        
	       
	        username = (EditText) findViewById(R.id.username);
	        password = (EditText) findViewById(R.id.password);
	        signin = (Button) findViewById(R.id.signin);
	        signup = (Button) findViewById(R.id.signup);
	        forget_password = (TextView) findViewById(R.id.forget_password);
	        
	        forget_password.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(DoctorUserLogin.this , Forget_Password.class);
					startActivity(i);
					
				}
			});
	        
	        signup.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(DoctorUserLogin.this , UserRegistration.class);
					startActivity(i);
				}
			});
	        
	        signin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String USERNAME = username.getText().toString();
					String PASSWORD = password.getText().toString();
					
					if(USERNAME.equals("") || USERNAME.equals(" ") || USERNAME==null){
						
						showAlertToUser("Please enter username");
					}
					
					else if(PASSWORD.equals("") || PASSWORD.equals(" ") || PASSWORD==null){
						showAlertToUser("Please enter password");
					}
					
					else if(!(StringUtils.verify(USERNAME))){
						showAlertToUser("Please enter valid email address.");
					}
					else {
						isConnected = NetConnection
								.checkInternetConnectionn(DoctorUserLogin.this);
						if (isConnected == true) {
							
							new login(USERNAME,PASSWORD ).execute(new Void[0]);
						}
						
						else {
							showAlertToUser("No internet connection.");
						}
					}
					
				}
			});
	 }
	 
	 public class login extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String username;
			String password;
			
			HashMap result;
			ArrayList localArrayList = new ArrayList();

			public login(String paramString1, String paramString2) {
				this.username = paramString1;
				this.password = paramString2;
				
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("email",this.username));
					localArrayList.add(new BasicNameValuePair("password",this.password));
					
					result = function.login(localArrayList);
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
					
					Editor e = sp.edit();
					
					if(result.get("mode").toString().equalsIgnoreCase("Doctor")){
					e.putString("user_id", result.get("user_id").toString());
					e.putString("mode", result.get("mode").toString());
					}
					else if(result.get("mode").toString().equalsIgnoreCase("User")){
						e.putString("user_id", result.get("user_id").toString());
						e.putString("mode", result.get("mode").toString());
					}
					e.commit();
					Intent i = new Intent(DoctorUserLogin.this , Home.class);
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
				db = new TransparentProgressDialog(DoctorUserLogin.this,
						R.drawable.loading);
				db.show();
			}

		}

}
