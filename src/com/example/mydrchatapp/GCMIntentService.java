package com.example.mydrchatapp;

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

	static String PROJECT_NUMBER = "828841915730";

	
	SharedPreferences sp;

	Home home;
	String message ;
	String name;
	String badge ;
	String id;
	String messageReceipentId;
	
	

	public GCMIntentService() {
		super(PROJECT_NUMBER);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.e(TAG, "Device registered: regId = " + registrationId);

	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.e(TAG, "Device unregistered");

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.e(TAG, "Received message");
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		
		String user_id = sp.getString("user_id", "");
		String chatType = intent.getExtras().getString("chatType");
		
		if(chatType.equalsIgnoreCase("single")){

		 message = intent.getExtras().getString("message");
		 name = intent.getExtras().getString("sender_name");
		 badge = intent.getExtras().getString("badge");
		 id = intent.getExtras().getString("sender_id");
		 messageReceipentId = intent.getExtras().getString("sender_id");
		}
		
		else if(chatType.equalsIgnoreCase("group")){
			 message = intent.getExtras().getString("message");
			 name = intent.getExtras().getString("group_name");
			 badge = intent.getExtras().getString("badge");
			 id = intent.getExtras().getString("sender_id");
			 messageReceipentId = intent.getExtras().getString("group_id");
			 
			 Log.e("badge(group)==", "" + badge);
		}
		
	
		Log.e("notification result==", "" + message);
		
		Log.e("chatType===", "" + chatType);

		try {
			
			
			if(!user_id.equalsIgnoreCase(id)){
			// notifies user
				if(chatType.equalsIgnoreCase("single")){
			updateMychatActivity(context , badge, message,id ,messageReceipentId);
				}
				else if(chatType.equalsIgnoreCase("group")){
					updateMygroupActivity(context , badge, message,id,messageReceipentId);
				}
			generateNotification(context, message ,name, badge,id,chatType,messageReceipentId);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	 private void updateMygroupActivity(Context context, String badge,
			String message, String id2, String messageReceipentId) {
		 Intent intent = new Intent("android.intent.action.MAIN");
		 
		 intent.putExtra("unread_msg", badge);
		 intent.putExtra("msg", message);
		 intent.putExtra("id", id2);
		 intent.putExtra("open", "group");
		 intent.putExtra("messageReceipentId", messageReceipentId);
		
		 context.sendBroadcast(intent);
		
	}

	static void updateMychatActivity(Context context, String unread_msg , String message, String id , String messageReceipentId) {
	
	 Intent intent = new Intent("android.intent.action.MAIN");
	 Log.i("unread message===","=="+unread_msg);
	 intent.putExtra("unread_msg", unread_msg);
	 intent.putExtra("msg", message);
	 intent.putExtra("id", id);
	 intent.putExtra("open", "single");
	 intent.putExtra("messageReceipentId", messageReceipentId);
	 context.sendBroadcast(intent);
	 }

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
	private static void generateNotification(Context context, String message,String name,String badge,String id, String chatType,
			String messageReceipentId) {

		Intent notificationIntent = new Intent(context, Home.class);

		notificationIntent.putExtra("unread_msg", badge);
		notificationIntent.putExtra("msg", message);
		notificationIntent.putExtra("id", id);
		notificationIntent.putExtra("open", chatType);
		notificationIntent.putExtra("messageReceipentId", messageReceipentId);
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

		// builder.setContentIntent(intent)
		// .setContentTitle(title)
		// .setContentText("Message: "+message)
		// .setContentInfo("Sender Id: "+sender_id)
		// .setSmallIcon(icon)
		// .setWhen(when)
		// .setSubText("Unread: "+unread_msg);
		// Notification notification = builder.build();
		// notificationManager.notify(0, notification);

		builder.setContentIntent(intent)
		.setContentTitle(name)
		.setContentText(message)
		.setSmallIcon(icon).setWhen(when)
		.setSubText(badge+" unread message.");

		Notification notification = builder.build();
		notificationManager.notify(0, notification);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);

	}

}
