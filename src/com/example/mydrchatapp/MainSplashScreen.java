package com.example.mydrchatapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

public class MainSplashScreen  extends Activity{
	
	SharedPreferences sp;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        setContentView(R.layout.splash_screen);
        
        sp = PreferenceManager
				.getDefaultSharedPreferences(MainSplashScreen.this);
        
        Editor e = sp.edit();
        e.putString("tab", "0");
        e.commit();
        
        Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 3 seconds
					sleep(3 * 1000);
					
					Boolean inHome = sp.getBoolean("inHome", false);
					
					if(inHome){
						Intent i = new Intent(MainSplashScreen.this , Home.class);
						startActivity(i);
					}
					
					else {
						Intent i = new Intent(MainSplashScreen.this , DoctorUserLogin.class);
						startActivity(i);
					}
					
				}
				
				catch(Exception e){
					
				}
        
	}

        };
     // start thread
     	background.start();
        
	}
	
}
