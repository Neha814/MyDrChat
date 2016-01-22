package com.example.mydrchatapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

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

import com.macrew.functions.Functions;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

public class UserRegistration extends Activity {

	Button register;
	EditText phone;
	TransparentProgressDialog db;
	Boolean isConnected;
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
		
		sp = PreferenceManager
				.getDefaultSharedPreferences(UserRegistration.this);

		setContentView(R.layout.user_registration);

		register = (Button) findViewById(R.id.register);
		phone = (EditText) findViewById(R.id.phone);

		register.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String phone_number = phone.getText().toString();
				Log.e("phone_number==", "" + phone_number);

				isConnected = NetConnection
						.checkInternetConnectionn(UserRegistration.this);
				if (isConnected == true) {

					if (phone_number.equals("") || phone_number.equals(" ")
							|| phone_number == null) {

						showAlertToUser("Please enter phone number.");

					}

					else {
						new user_login(phone_number).execute(new Void[0]);
					}
				}

				else {
					showAlertToUser("No internet connection.");
				}

			}
		});

	}

	public class user_login extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String phone;
		HashMap result;
		ArrayList localArrayList = new ArrayList();

		public user_login(String paramString1) {
			this.phone = paramString1;
			Editor e = sp.edit();
			e.putString("mobile_number", this.phone);
			e.commit();
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("mobile_number",
						this.phone));
				result = function.user_login(localArrayList);
			

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
				e.putString("user_id", result.get("user_id").toString());
				e.commit();
				Intent i = new Intent(UserRegistration.this , UserRegDetails.class);
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
			db = new TransparentProgressDialog(UserRegistration.this,
					R.drawable.loading);
			db.show();
		}

	}

}
