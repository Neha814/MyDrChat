package com.example.mydrchatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class UserLoginSignup extends Activity{
	
	Button already_reg , not_reg;
	
	public void back(View paramView) {
		finish();
		onBackPressed();
	}
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
	        setContentView(R.layout.user_login_signup);
	        
	        already_reg = (Button) findViewById(R.id.already_reg);
	        not_reg = (Button) findViewById(R.id.not_reg);
	        
	        already_reg.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(UserLoginSignup.this , DoctorUserLogin.class);
					startActivity(i);
					
				}
			});
	        
	        not_reg.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(UserLoginSignup.this , UserRegistration.class);
					startActivity(i);
				}
			});
	        
	 }

}
