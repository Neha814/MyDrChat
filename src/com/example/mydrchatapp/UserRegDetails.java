package com.example.mydrchatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;


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

public class UserRegDetails extends Activity {
	
	Button submit;
	EditText name, email, password, confirm_password;
	Boolean isConnected;
	SharedPreferences sp;
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
			
	        setContentView(R.layout.user_reg_detail);
	        
	        sp = PreferenceManager
					.getDefaultSharedPreferences(UserRegDetails.this);
	        
	        submit = (Button) findViewById(R.id.submit);
	        name = (EditText) findViewById(R.id.name);
	        email = (EditText) findViewById(R.id.email);
	        password = (EditText) findViewById(R.id.password);
	        confirm_password = (EditText) findViewById(R.id.confirm_password);
	        
	        submit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					String NAME = name.getText().toString();
					String EMAIL = email.getText().toString();
					String PASSWORD = password.getText().toString();
					String CONFIRM_PASSWORD = confirm_password.getText().toString();
					if(NAME.equals("") || NAME.equals(" ") || NAME==null){
						showAlertToUser("Please enter name.");
					}
					
					else if(!(StringUtils.verify(EMAIL))){
						showAlertToUser("Please enter valid email address.");
					}
					
					else if(PASSWORD.equals("") || PASSWORD.equals(" ") || PASSWORD==null){
						showAlertToUser("Please enter password.");
					}
					
					else if(PASSWORD.equals(CONFIRM_PASSWORD)){
						isConnected = NetConnection
								.checkInternetConnectionn(UserRegDetails.this);
						if (isConnected == true) {
							new user_reg_detail(NAME , EMAIL ,PASSWORD ,sp.getString("user_id", "")).execute(new Void[0]);
						}
						
						else {
							showAlertToUser("No internet connection.");
						}
					}
				}
			});
	        
	 }
	 
	 public class user_reg_detail extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String name;
			String email;
			String password;
			String user_id;
			HashMap result;
			ArrayList localArrayList = new ArrayList();

			public user_reg_detail(String paramString1, String paramString2, String paramString3,
					String paramString4) {
				this.name = paramString1;
				this.email = paramString2;
				this.password = paramString3;
				this.user_id = paramString4;
			}

			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("name",
							this.name));
					localArrayList.add(new BasicNameValuePair("email",
							this.email));
					localArrayList.add(new BasicNameValuePair("password",
							this.password));
					localArrayList.add(new BasicNameValuePair("user_id",
							this.user_id));
					result = function.user_reg_detail(localArrayList);
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
					
					Intent i = new Intent(UserRegDetails.this , VerifyCode.class);
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
				db = new TransparentProgressDialog(UserRegDetails.this,
						R.drawable.loading);
				db.show();
			}

		}
}
