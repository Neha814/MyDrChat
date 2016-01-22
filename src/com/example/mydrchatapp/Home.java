package com.example.mydrchatapp;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;


import com.example.fragments.Chat;

import com.example.fragments.Groups;
import com.example.fragments.Contacts;
import com.example.fragments.Settings;


import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.macrew.functions.Functions;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;


import android.support.v4.app.FragmentTabHost;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost.OnTabChangeListener;


public class Home extends FragmentActivity {

	private FragmentTabHost mTabHost;
	
	public static ContentResolver appContext;
	SharedPreferences sp;
	boolean addList = false;
	public static InputMethodManager imm;
	String PROJECT_NUMBER = "828841915730";
	String regid;
	GoogleCloudMessaging gcm;
	String device = "ANDROID";
	
//	public void back(View paramView) {
//		
//	
//	    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//	    	
//	    	Log.e("getBackStackEntryCount","if===="+getSupportFragmentManager().getBackStackEntryCount());
//	    	Log.e("getFragments","if===="+getSupportFragmentManager().getFragments());
//	    	
//	    	
//	        getSupportFragmentManager().popBackStack();
//	        
//	        
//	        
//	        return;
//	    }
//	    
////	    else {
////	    	Log.e("else","else");
////	    	Intent launchNextActivity;
////	    	launchNextActivity = new Intent(Home.this, Home.class);
////	    	launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////	    	launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);                  
////	    	launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
////	    	startActivity(launchNextActivity);
////	    }
//	    super.onBackPressed();
//	}
	
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
		

        setContentView(R.layout.home);
        
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        Editor e = sp.edit();
        e.putBoolean("inHomeFirstTime", true);
        e.commit();
        
        IntentFilter intentFilter = new IntentFilter(
				"android.intent.action.MAIN");
        registerReceiver(mHandleMessageReceiver, intentFilter);
      
        
      
        ListFragment listFragment = getListFragment();
        FragmentTransaction tx = this.getSupportFragmentManager().beginTransaction();
     
        getRegId();	
        
        
  
         imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
        
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        
        mTabHost.getTabWidget().setDividerDrawable(null);
        
        Resources ressources = getResources();
        
        
        
        Editor e1 = sp.edit();
        e1.putBoolean("inHome", true);
      
        e1.commit();
        
        appContext = getContentResolver();
        
        
       FragmentManager fm = Home.this.getSupportFragmentManager();		
        Fragment f = fm.findFragmentById(android.R.id.tabcontent);
      
        	 mTabHost.addTab(
                     mTabHost.newTabSpec("tab1").setIndicator("", ressources.getDrawable(R.drawable.chat)),
                     Chat.class, null);
      
        mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator("", ressources.getDrawable(R.drawable.groups)),
                Groups.class, null);
        
        mTabHost.addTab(
                mTabHost.newTabSpec("tab3").setIndicator("", ressources.getDrawable(R.drawable.contacts)),
                Contacts.class, null);
        
        	
        mTabHost.addTab(
                mTabHost.newTabSpec("tab4").setIndicator("", ressources.getDrawable(R.drawable.settings)),
                Settings.class, null);
        
 
      
        mTabHost.setCurrentTab(0);  
        
      
      
        
    }
   
    private void sendRegId() {
    	new send_registrationId(sp.getString("user_id", ""),regid,device,sp.getString("mode", "")).execute(new Void[0]);
		
	}


	private void getRegId() {
    	new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					regid = gcm.register(PROJECT_NUMBER);
					msg = regid;
					
					Log.e("GCM", msg);

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();

				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

				Log.e("registration id ===", "==" + msg);
				
				Editor e = sp.edit();
		        e.putBoolean("inHomeFirstTime", false);
		        e.commit();
		        sendRegId();

			}
		}.execute(null, null, null);
		
	}


	@Override
    public void onBackPressed() {
// is there any fragment in backstack, if yes popout.
		
		
	    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
	        getSupportFragmentManager().popBackStack();
	        return;
	    }
	    
	    else {
	    	
	    	finish();
	    }
	   
    	super.onBackPressed();
    }
    



    private ListFragment getListFragment() {
        ListFragment listFragment = (ListFragment) this.getSupportFragmentManager().findFragmentByTag(
    				"myTag");
        if (listFragment == null) {
            listFragment = new ListFragment();
        }
        return listFragment ;
    }
    
    public class send_registrationId extends AsyncTask<Void, Void, Void> {
  
		Functions function = new Functions();
		String id;
		String UDID;
		String deviceType;
		String mode;
		
		HashMap result;
		ArrayList localArrayList = new ArrayList();

		public send_registrationId(String string, String regid, String device , String mode) {
			// TODO Auto-generated constructor stub
			
			this.id = string;
			this.UDID = regid;
			this.deviceType = device;
			this.mode = mode;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("id",this.id));
				localArrayList.add(new BasicNameValuePair("UDID",this.UDID));
				localArrayList.add(new BasicNameValuePair("deviceType",this.deviceType));
				localArrayList.add(new BasicNameValuePair("mode",this.mode));
				result = function.send_registrationId(localArrayList);
				Log.e("result==",""+result);
			

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			try{
				
				if (result.get("result").toString().equalsIgnoreCase("false")) {
					
					new send_registrationId(sp.getString("user_id", ""),regid,device,sp.getString("mode", "")).execute(new Void[0]);
				}
			
				else {
					
				}
				}
				
				catch (Exception ae){
					Log.e("Exception==",""+ae);
					new send_registrationId(sp.getString("user_id", ""),regid,device,sp.getString("mode", "")).execute(new Void[0]);
					
				}
		}

		protected void onPreExecute() {
			super.onPreExecute();
			
		}

	}
    
    @Override
    protected void onResume() {
    	Log.d("Resume","Resume");
        FragmentTransaction tx = this.getSupportFragmentManager().beginTransaction();
        if (this.addList) {
            ListFragment list = this.getListFragment();
            tx.add(android.R.id.tabcontent, list, "list_fragment_tag");
        }

        tx.commit();
        super.onResume();

        this.addList = false;   

    }

    @Override
    protected void onPause() {
    	Log.d("Pause","Pause");
        this.addList = this.getListFragment().isAdded();
      
        if (this.addList) {
            FragmentTransaction tx = this.getSupportFragmentManager().beginTransaction();
            tx.remove(this.getListFragment());
            tx.commit();
        }
        this.getSupportFragmentManager().executePendingTransactions();
        super.onPause();
        
        //unregisterReceiver(mHandleMessageReceiver);

    }
    
    @Override
	protected void onDestroy() {

		try {
			//unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	
		super.onDestroy();
	}
    private  BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("<>BROADCAST<>","<>RECEIVER<>");
		
			
			
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

		
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent); //must store the new intent unless getIntent() will return the old one
		
	
		
		 String open = intent.getStringExtra("open");
			
		 Log.i("<>onNewIntent Home<>","<>onNewIntent<> Home");
		 Log.i("open==","="+open);
		
		//chat.setText(unread_msg);
		
		if(open.equals("group")){
		
			mTabHost.setCurrentTab(1);  
		
		}
		else if(open.equals("single")){
			
			mTabHost.setCurrentTab(0);  
		
		}
		

	}
   
}