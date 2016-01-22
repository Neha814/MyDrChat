package com.example.mydrchatapp;


import com.macrew.functions.Data;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity {
	
	
	Button doctor , user;
	SharedPreferences sp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.activity_main);
        
        sp = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
        
        doctor = (Button) findViewById(R.id.doctor);
        user = (Button) findViewById(R.id.user);
        
       doctor.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Editor e = sp.edit();
			e.putString("mode", "Doctor");
			e.commit();
			Intent i = new Intent(MainActivity.this , DoctorUserLogin.class);
			startActivity(i);
		}
       });
       
       user.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Editor e = sp.edit();
			e.putString("mode", "User");
			e.commit();
			Intent i = new Intent(MainActivity.this , UserLoginSignup.class);
			startActivity(i);
		}
	});
    }
}
    
    
		
	


  

