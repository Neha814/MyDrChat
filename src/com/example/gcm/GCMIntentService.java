package com.example.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;
import com.google.android.gcm.GCMBaseIntentService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	static String PROJECT_NUMBER = "519173785854";

	
	static int id;
	
	Home home;
	
	
	

	public GCMIntentService() {
		super(PROJECT_NUMBER);
	}
	
	

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");

	}
	
	
	

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String result = intent.getExtras().getString("message");

		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result);
			String message = jsonObj.getString("message");

			String unread_msg = jsonObj.getString("unread_msg");

			String sender_id = jsonObj.getString("sender_id");
			id = jsonObj.getInt("sender_id");
			
			
			
			
			Log.i("notification result==", "" + result);
			
			//updateMyActivity(context , unread_msg);
			// notifies user
			generateNotification(context, message, unread_msg, sender_id);
			
			
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

	}

	

//	static void updateMyActivity(Context context, String unread_msg) {
//		
//		Intent intent = new Intent("android.intent.action.MAIN");
//		Log.i("unread message===","=="+unread_msg);
//		intent.putExtra("unread_msg", unread_msg);
//		
//		context.sendBroadcast(intent);
//	}



	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");

		// notifies user

	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);

		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message,
			String unread_msg, String sender_id) {
		
		
Intent notificationIntent = new Intent(context, Home.class);
		
		notificationIntent.putExtra("unread_msg", unread_msg);
		context.sendBroadcast(notificationIntent);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		
		

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);
		
		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(intent)
		.setContentTitle(title)
		.setContentText("Message: "+message)
		.setContentInfo("Sender Id: "+sender_id)
		.setSmallIcon(icon)
		.setWhen(when)
		.setSubText("Unread: "+unread_msg);
		Notification notification = builder.build();
		notificationManager.notify(0, notification);
		
		

		
		
		

		
		
		//notification.setLatestEventInfo(context, title, message, intent);
		
		
		
		//notificationManager.notify(0, notification);	
		
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

}
