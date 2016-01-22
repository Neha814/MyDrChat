package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
 



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.MediaController;
import android.widget.TextView;

import android.widget.VideoView;

import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;

import com.macrew.enitites.RefreshableListView;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.HttpClientUpload;
import com.macrew.utils.NetConnection;
import com.macrew.utils.SimpleMultipartEntity;
import com.macrew.utils.TransparentProgressDialog;


public class Fragment_chat_screen extends Fragment {

	ImageView chat_img;
	TextView name;
	Button attach;
	//int arg;
	public ImageLoader imageLoader;
	String receiver_id;
	SharedPreferences sp;
	String sender_id;
	String last_id;
	//String video_url;
	TransparentProgressDialog db;
	ArrayList<HashMap<String, String>> chat_list = new ArrayList<HashMap<String, String>>();
	LazyAdapter1 mAdapter1;
	RefreshableListView listview;
	String formattedDate;
	
	String formattedDatetoSend;
	int chatRecordsLength=0;
	int  page = 1;
	int  current_page;
	int last_page ;
	Boolean first = true;
	Boolean inchatscreeFirstTime = true;
	Button send;
	EditText message_to_send;
	String message_text;
	sendMessageTask sendMessageObj ;
	sendMessage1Task sendMessage1Obj ;
	Boolean isConnected;
	SimpleDateFormat df1;
	Calendar c;
	
	Bitmap takenImage;
	File imgFileGallery;
	Bitmap myBitmap;
	String imageOrVideo;
	int selectedVideoPosition = -1;
	int selectedImgPosition = -1;
	MediaController mc;
	String picturePath;
	InputStream fileInputStream;
	 VideoView videoview;
	 Button back;
	
	ArrayList<Integer> temp_list = new ArrayList<Integer>();
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				getActivity());
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater
				.inflate(R.layout.chat_screen, container, false);

		chat_img = (ImageView) rootView.findViewById(R.id.chat_img);
		name = (TextView) rootView.findViewById(R.id.name);
		attach = (Button) rootView.findViewById(R.id.attach);
		send = (Button) rootView.findViewById(R.id.send);
		back = (Button) rootView.findViewById(R.id.back);
		message_to_send = (EditText) rootView.findViewById(R.id.message_to_send);
		
		
		
		
		listview = (RefreshableListView) rootView.findViewById(R.id.listview);
		imgFileGallery = new File("");
		
	
		

		c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		formattedDate = df.format(c.getTime());
		
		df1 = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		
		
		Log.e("formattedDate==",""+formattedDate);

		imageLoader = new ImageLoader(getActivity());
		final Bundle bundle = getArguments();
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		//Editor e = sp.edit();
		//e.putString("last_id", "0");
		//e.commit();

		name.setText(bundle.getString("name"));

		try {
			imageLoader.DisplayImage(bundle.getString("profile_image"), chat_img);
		} catch (Exception ae) {
			Log.e("Exception===", "" + ae);
		}
		receiver_id = sp.getString("user_id", "");
		sender_id = bundle.getString("user_id");
	
		receivepreviouschat();
		new resetbadge(sp.getString("user_id", "")).execute(new Void[0]);
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Chat  fragment = new Chat();
		     
		  
		        if(fragment != null) {
		            // Replace current fragment by this new one
		            
		        	ft.replace(android.R.id.tabcontent, fragment);
		        	
		       }
		            else{
		            	 ft.add(android.R.id.tabcontent, fragment);
		            }
		        ft.addToBackStack(null);
		 
		        ft.commit();   
				
			}
		});
		
		listview.setOnRefreshListener(new RefreshableListView.OnRefreshListener() {
			
			@Override
			public void onRefresh(RefreshableListView listView) {
				
				 if(page<last_page){
				 new load_previous_message(receiver_id, String.valueOf(page) , sender_id).execute(new Void[0]);
				 }
				 
				 else{
					 new NewDataTask().execute();
				 }
				
			}
		});
		
		
		chat_img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final Dialog dialog;
				 dialog = new Dialog(getActivity(),
							R.style.full_screen_dialog);
				dialog.setCancelable(true);
				
				Button back;
				ImageView pro_pic;
				TextView name , phone;
				
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.contact_info);
				back = (Button) dialog.findViewById(R.id.back);
				pro_pic = (ImageView) dialog.findViewById(R.id.pro_pic);
				name = (TextView) dialog.findViewById(R.id.name);
				phone = (TextView) dialog.findViewById(R.id.phone);
				
				imageLoader.DisplayImage(bundle.getString("profile_image"), pro_pic);
				name.setText(bundle.getString("name"));
				phone.setText(bundle.getString("mobile_number"));
				
				back.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
				
				
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				
				
				
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
			
				dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			dialog.show();
				
			}
		});
		
			send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				formattedDatetoSend=df1.format(c.getTime());
			
				message_text = message_to_send.getText().toString();
				if(message_text.equals("")||message_text.equals(" ")||message_text==null){
					//showAlertToUser("please enter message");
				}
				
				else if(message_text.length()<15){
					showAlertToUser("Message too short.Minimum limit is 15 characters");
				}
				else {
					
				HashMap localHashMap2 = new HashMap();
				localHashMap2.put("message_text", message_text);
				localHashMap2.put("created",formattedDatetoSend );
				localHashMap2.put("sender_id", sp.getString("user_id",""));
				localHashMap2.put("attachment", null);
				
				
				
				int size = chat_list.size();
			
				
				if(size<=0){
					chat_list.add(0, localHashMap2);
					message_to_send.setText("");
					message_to_send.clearFocus();
					
					temp_list.add(0);
					
					mAdapter1 = new LazyAdapter1(chat_list, getActivity());
					listview.setAdapter(mAdapter1);
				}
				else {
					chat_list.add(size, localHashMap2);
					message_to_send.setText("");
					message_to_send.clearFocus();
					
					temp_list.add(size);
				
					mAdapter1.notifyDataSetChanged();
					scrollChatListToBottom();
				}
				
				Home.imm.hideSoftInputFromWindow(message_to_send.getWindowToken(), 0);
				
				 isConnected = NetConnection.checkInternetConnectionn(getActivity());
				 if (isConnected == true) {
						sendMessage1Obj = new sendMessage1Task();
						sendMessage1Obj.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}

					else {
						showAlertToUser("No internet connection.");
					}
				}
				
				
				
			}
		});
		

		attach.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent galleryIntent = new Intent(
						Intent.ACTION_GET_CONTENT);
				galleryIntent.setType("video/*, image/*");
				//galleryIntent.setType("image/*");
				startActivityForResult(galleryIntent, 1);

			}
		});
		
		// ********************** thread for receiving current msg from another person ************************//
		
		final Handler localHandler2 = new Handler();
		localHandler2.postDelayed(new Runnable() {
			public void run() {
			
					
					if(inchatscreeFirstTime==false){
					
					new receivecurrentchat(sender_id , receiver_id , sp.getString("last_id", ""), sp.getString("user_id", "")).execute(new Void[0]);
					}
				localHandler2.postDelayed(this, 6000L);
			}
		}, 1000L);

		return rootView;

	}

	
	private void receivepreviouschat() {
		

		new load_archieve_message(sender_id, String.valueOf(page) , receiver_id).execute(new Void[0]);

	}

	public class receivecurrentchat extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String sender_id;
		String last_id;
		String receiver_id;
		String user_id;

		ArrayList result;
		ArrayList localArrayList = new ArrayList();

		public receivecurrentchat(String sender_id, String receiver_id,
				String last_id, String string) {
			this.sender_id = sender_id;
			this.receiver_id = receiver_id;
			this.last_id = last_id;
			this.user_id = string;
		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("senderid",
						this.sender_id));
				localArrayList.add(new BasicNameValuePair("receiverid",
						this.receiver_id));
				localArrayList.add(new BasicNameValuePair("last_id",
						this.last_id));
				localArrayList.add(new BasicNameValuePair("user_id",
						this.user_id));

				result = function.receivecurrentchat(localArrayList);
				

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
		

			try {
				if (result.size() > 0) {
					
					if(chat_list.size()<=0){
						chat_list.addAll(0,result);
						
						Editor e = sp.edit();
						e.putString("last_id", chat_list.get(0).get("msg_id"));
						e.commit();
					}
					else{
						int size = chat_list.size();
					chat_list.addAll(size,result);
					
					Editor e = sp.edit();
					e.putString("last_id", chat_list.get(chat_list.size()-1).get("msg_id"));
					e.commit();
					}
				mAdapter1.notifyDataSetChanged();
					scrollChatListToBottom();

				}

				else {
				//	showAlertToUser("No active participants are available.");

				}
			}

			catch (Exception ae) {
				Log.e("Exception==", "" + ae);
				//showAlertToUser("Something went wrong while processing your request.Please try again.");
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
		
		}

	}

	/*********************** ADAPTER CLASS ******************************************/

	class LazyAdapter1 extends BaseAdapter {

		LayoutInflater mInflater = null;

		public LazyAdapter1(ArrayList<HashMap<String, String>> chat_list,
				FragmentActivity group_members) {

			mInflater = LayoutInflater.from(group_members);
		}

		@Override
		public int getCount() {
			
		
			return chat_list.size();
		}

		@Override
		public Object getItem(int position) {
			
			
			return chat_list.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder1 holder;
			
			holder = new ViewHolder1();
			
			if (chat_list.get(position).get("sender_id")
					.equals(sp.getString("user_id", ""))) {

				convertView = mInflater.inflate(R.layout.my_chat, null);

			} else {
				convertView = mInflater
						.inflate(R.layout.another_chat, null);
			}
			
			holder.message_time = (TextView) convertView.findViewById(R.id.message_time);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.chat_comment = (TextView) convertView.findViewById(R.id.chat_comment);
			holder.chat_img = (ImageView) convertView.findViewById(R.id.chat_img);
			holder.play = (Button) convertView.findViewById(R.id.play);
		
			
			holder.chat_img.setTag(position);
			holder.play.setTag(position);
			
			convertView.setTag(holder);
			
			try{

				String date = chat_list.get(position).get("created");
				StringTokenizer tk = new StringTokenizer(date);

				String date_text = tk.nextToken(); // <--- yyyy-mm-dd
				String time_text = tk.nextToken();
				
				if (formattedDate.equals(date_text)) {
					holder.message_time.setText(time_text);
				}

				else {
					holder.message_time.setText(date_text);
				}
				}
				
				catch(Exception e){
					Log.e("Exception e==",""+e);
				}
			
			if (chat_list.get(position).get("sender_id")
					.equals(sp.getString("user_id", ""))) {
				holder.name.setText("Me");
			}
			else {
				holder.name.setText(chat_list.get(position).get("name"));
			}


			 if (!(chat_list.get(position).get("message_text")==null || chat_list.get(position).get("message_text").equals("") )){
			
				if(!chat_list.get(position).get("message_text").equals(" ")){
				
				holder.chat_img.setVisibility(View.INVISIBLE);
				holder.play.setVisibility(View.INVISIBLE);
			
				holder.chat_comment.setVisibility(View.VISIBLE);
				
				
				holder.chat_comment.setText(chat_list.get(position).get(
						"message_text"));
			
				if (chat_list.get(position).get("sender_id")
						.equals(sp.getString("user_id", ""))) {
					
					if(temp_list.size()>0){
					if(temp_list.contains(position)){
						holder.chat_comment.setBackgroundResource(R.drawable.blue_edittext);
					}
					else{
						holder.chat_comment.setBackgroundResource(R.drawable.dark_blue_edittext);
					}
					}
				} 
				
			 }
			}
			 
			 else if(!( chat_list.get(position).get("attachment")==null)){
				 holder.chat_img.setVisibility(View.VISIBLE);
		
				 holder.chat_comment.setVisibility(View.INVISIBLE);
				 if(chat_list.get(position).get("attachment_type").equalsIgnoreCase("Image")){
					 holder.play.setVisibility(View.INVISIBLE);
					 
				 if(chat_list.get(position).get("attachment_id")==null){
				 if(temp_list.size()>0){
					 if(temp_list.contains(position)){
						 if(holder.chat_img.getDrawable()==null){
						 Bitmap myBitmap;
						 myBitmap = BitmapFactory.decodeFile(chat_list.get(position).get("attachment"));
					 holder.chat_img.setImageBitmap(myBitmap);
					 holder.chat_img.setAlpha(100);
						 }
					 }}
					 else {
						
						 Bitmap myBitmap;
						 myBitmap = BitmapFactory.decodeFile(chat_list.get(position).get("attachment"));
						 holder.chat_img.setImageBitmap(myBitmap);
						 holder.chat_img.setAlpha(255); 
						
					 }
				 
				 
				 }
				 else{
					 String url = chat_list.get(position).get("attachment");
					
					 imageLoader.DisplayImage(url, holder.chat_img);
					
					
				 }
			 }
				 
				 else if(chat_list.get(position).get("attachment_type").equalsIgnoreCase("Video")) {
//					 BitmapFactory.Options options=new BitmapFactory.Options();
//				      options.inSampleSize = 1;
//				      Bitmap videoThumb = MediaStore.Video.Thumbnails.getThumbnail(Home.appContext, position, MediaStore.Video.Thumbnails.MICRO_KIND, options);
//				      Log.e("videoThumb==",""+videoThumb);
//				      holder.chat_img.setImageBitmap(videoThumb);
					 holder.play.setVisibility(View.VISIBLE);
				      Bitmap bMap = ThumbnailUtils.createVideoThumbnail(chat_list.get(position).get("attachment"), MediaStore.Video.Thumbnails.MICRO_KIND);
				      holder.chat_img.setImageBitmap(bMap);
				      
				      if(temp_list.size()>0){
							 if(temp_list.contains(position)){
								 holder.chat_img.setAlpha(100); 
								 holder.play.setAlpha(100); 
							 }
							 else{
								 holder.chat_img.setAlpha(255);
								 holder.play.setAlpha(255); 
							 }
				      }
				 }
		}
			if(first){
			if(mAdapter1.getCount()>chatRecordsLength){
				scrollChatListToBottom();
				first = false;
			
				chatRecordsLength = mAdapter1.getCount();
			
			}
			
			}
			
			holder.chat_img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					selectedImgPosition = (Integer) v.getTag();
					showimgDialog();
					
				}
			});
			
			holder.play.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					selectedVideoPosition = (Integer) v.getTag();
					showvideoDialog();
					
				}

			
			});
			
			return convertView;
		}

		protected void showvideoDialog() {
			 Dialog dialog;
			 dialog = new Dialog(getActivity(),
						R.style.full_screen_dialog);
			dialog.setCancelable(true);
			
			
			
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.video_dialog);
			videoview = (VideoView) dialog.findViewById(R.id.videoview);
			
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			mc = new MediaController(getActivity());
		
			mc.setAnchorView(videoview);
			mc.setMediaPlayer(videoview);
			
			Uri uri = Uri.parse(chat_list.get(selectedVideoPosition).get("attachment"));
			
			//Uri uri = Uri.parse("http://www.w3schools.com/html/mov_bbb.mp4");
			
			Log.e("url====",""+chat_list.get(selectedVideoPosition).get("attachment"));
			
			videoview.setZOrderOnTop(true);
			videoview.setMediaController(mc);
			videoview.requestFocus();
			videoview.setVideoURI(uri);
			
			
			getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			videoview
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(
								MediaPlayer mediaPlayer) {
							 mc.show();
						
							videoview.start();

						}
					});
			
			dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			
		dialog.show();
			
		}

	
		protected void showimgDialog() {
			 Dialog dialog;
			 dialog = new Dialog(getActivity(),
						R.style.full_screen_dialog);
			dialog.setCancelable(true);
			 ImageView imageView1;
			
			
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.img_dialog);
			imageView1 = (ImageView) dialog.findViewById(R.id.imageView1);
			
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			String url = chat_list.get(selectedImgPosition).get("attachment");
		
			imageLoader.DisplayImage(url, imageView1);
			
			dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			dialog.show();
			
		}
	}

	/****************** ENDING OF ADAPTER CLASS ************************************/
	class ViewHolder1 {
		TextView message_time, name, chat_comment;
		ImageView chat_img;
		
		Button play;
	}

	public class load_archieve_message extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String sender_id;
		String page_no;
		String receiver_id;

		ArrayList result;
		ArrayList localArrayList = new ArrayList();

		public load_archieve_message(String sender_id, String page , String receiver_id) {
			this.sender_id = sender_id;
			this.receiver_id = receiver_id;
			this.page_no = page;

		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("senderid",
						this.sender_id));
				localArrayList.add(new BasicNameValuePair("receiverid",
						this.receiver_id));
				localArrayList.add(new BasicNameValuePair("page",this.page_no));

				result = function.load_archieve_message(localArrayList);
				

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			db.dismiss();
			inchatscreeFirstTime=false;
			try {
				
				if (result.size() > 0) {
					
					

					chat_list.addAll(result);
					page =  Integer.parseInt(chat_list.get(0).get("page"));
					last_page = Integer.parseInt(chat_list.get(0).get("lastpage"));
					
					Editor e = sp.edit();
					e.putString("last_id", chat_list.get(chat_list.size()-1).get("msg_id"));
					e.commit();
				
					mAdapter1 = new LazyAdapter1(chat_list, getActivity());
					listview.setAdapter(mAdapter1);

				}

			}

			catch (Exception ae) {
				Log.e("Exception==", "" + ae);
				showAlertToUser("Something went wrong while processing your request.Please try again.");
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();
		}

	}
	
	private class NewDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            
            return "A new list item";
        }

        @Override
        protected void onPostExecute(String result) {
           // mItems.add(0, result);
            // This should be called after refreshing finished
            listview.completeRefreshing();

            super.onPostExecute(result);
        }
    }

	public void scrollChatListToBottom() {
		listview.post(new Runnable() {

			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				listview.setSelection(mAdapter1.getCount() - 1);
			
			
			}
		});
		
	}
	public class load_previous_message extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String sender_id;
		String page_no;
		String receiver_id;

		ArrayList result;
		ArrayList localArrayList = new ArrayList();

		public load_previous_message(String sender_id, String page , String receiver_id) {
			this.sender_id = sender_id;
			this.receiver_id = receiver_id;
			this.page_no = page;

		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("senderid",
						this.sender_id));
				localArrayList.add(new BasicNameValuePair("receiverid",
						this.receiver_id));
				localArrayList.add(new BasicNameValuePair("page",this.page_no));

				result = function.load_archieve_message(localArrayList);
				

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
		//	db.dismiss();
			
			listview.completeRefreshing();

			try {
				if (result.size() > 0) 
					
				{
					
					chat_list.addAll(0, result);
					
					page =  Integer.parseInt(chat_list.get(0).get("page"));
				
					mAdapter1.notifyDataSetChanged();
					

				}

				else {
					//showAlertToUser("No active participants are available.");

				}
			}

			catch (Exception ae) {
				Log.e("Exception==", "" + ae);
				showAlertToUser("Something went wrong while processing your request.Please try again.");
			}

		}

		protected void onPreExecute() {
			super.onPreExecute();
//			db = new TransparentProgressDialog(getActivity(),
//					R.drawable.loading);
//			db.show();
		}

	}
	public class sendMessageTask extends AsyncTask<String, Void, String> {
		ByteArrayOutputStream baos;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


		}

		@Override
		protected String doInBackground(String... Params) {
			Log.d("##############################################","################################################");
			String result = null;
			
				String serviceUri = Functions.uri+"sendmessage.php?";
			    //InputStream fileInputStream = mInputStream; //Your file stream
			    
				try {
					fileInputStream = new FileInputStream(imgFileGallery);
				} catch (Exception e1) {
					Log.e("exception(multipart)==",""+e1);
					e1.printStackTrace();
				}; //Your file stream
			    String fileName = picturePath;
			    String fileKey = imageOrVideo;
			 
				HashMap<String, String> headerparts = new HashMap<String, String>() ; //Other header parts that you need to send along.

			    HttpClient httpClient = new DefaultHttpClient();
			    HttpPost httpPost = new HttpPost(serviceUri);
			    SimpleMultipartEntity entity = new SimpleMultipartEntity();
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "multipart/form-data; boundary="
			            + entity.getBoundary());
			    entity.writeFirstBoundaryIfNeeds();
			    
			    
			    
			    headerparts.put("senderid", receiver_id);
			    headerparts.put("receiverid", sender_id);
			   // headerparts.put("message_text", message_text);
			    headerparts.put("time", formattedDatetoSend);

			    if (headerparts != null) {
			        Object[] keySet = headerparts.keySet().toArray();
			        for (int i = 0; i < keySet.length; i++) {
			            String key = keySet[i].toString();
			            String value = headerparts.get(key);
			            entity.addPart(key, value);
			        }
			    }

			    entity.addPart(fileKey, fileName, fileInputStream);
			    entity.writeLastBoundaryIfNeeds();
			    httpPost.setEntity(entity);

			    try {
			        HttpResponse mResponse = httpClient.execute(httpPost);
			        
			        StatusLine statusLine = mResponse.getStatusLine();

					if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						mResponse.getEntity().writeTo(out);
						out.close();
						Log.i("Status ok", "STATUS OK");

						result = out.toString();
						Log.i("RESULT", "==" + result);
					} else {
						// close connection
						mResponse.getEntity().getContent().close();
						throw new IOException(statusLine.getReasonPhrase());
					}
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
				return result;
				
			    
			    
				

//				baos = new ByteArrayOutputStream();
//				Log.e("message_text==", "" +message_text);
//				if(message_text.equals("")||
//						message_text.equals(" ")||message_text==null){
//					
//					if(imageOrVideo.equalsIgnoreCase("Image")){
//				takenImage.compress(CompressFormat.PNG, 0, baos);
//					}
//				}
//			
//			}
//
//			catch (Exception e) {
//				Log.e("excptn==", "" + e);
//			}
//
//			try {
//				HttpClient httpclient = new DefaultHttpClient();
//
//				HttpClientUpload client = new HttpClientUpload(
//						Functions.uri+"sendmessage.php?");
//				client.connectForMultipart();
//				
//				
//				
//				
//				client.addFormPart("senderid", receiver_id);
//				client.addFormPart("receiverid", sender_id);
//				if(message_text.equals("")||
//						message_text.equals(" ")||message_text==null){
//					client.addFilePart(imageOrVideo, imgFileGallery.getName(),
//							baos.toByteArray());
//				}
//				else{
//				client.addFormPart("message_text", message_text);
//				}
//				client.addFormPart("time", formattedDatetoSend);
//			
//				
//			client.finishMultipart();
//				
//				String data = client.getResponse();
//				
//				
//				Log.e("data(sending message)",""+data);
//
//				return data;
//			} catch (Throwable t) {
//				t.printStackTrace();
//			}
//
//			return null;
			
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			db.dismiss();
			String error;
			Boolean status;
			try {
				// Log.e("result===",""+result);
				JSONObject json = new JSONObject(result);
				picturePath = "";
				if (json.getBoolean("status")) {
					
					message_text = "";
					
					JSONObject c = json.getJSONObject("data");
					last_id = c.getString("msg_id");
					Editor e = sp.edit();
					e.putString("last_id", last_id);
					e.commit();
					
				//	video_url = c.getString("video_url");
					temp_list.clear();
					mAdapter1.notifyDataSetChanged();
				}

				else {
					error = json.getString("error");
					showAlertToUser(error);
				}
			} catch (Exception e) {
			
				showAlertToUser("Something went wrong while processing your request.Please try again.");
			}
		}

	}
			//***************** handling attachment *******************************//
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {

		 if (requestCode == 1) {
			Uri selectedImage = data.getData();
			InputStream imageStream = null;
			try {
				imageStream = Home.appContext.openInputStream(selectedImage);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("Exception==", "" + e);
			}
			takenImage = BitmapFactory.decodeStream(imageStream);

		
			/**
			 * saving to file
			 */

			Uri SelectedImage = data.getData();
			String[] FilePathColumn = { MediaStore.Images.Media.DATA };

			Cursor SelectedCursor = Home.appContext.query(SelectedImage,
					FilePathColumn, null, null, null);
			SelectedCursor.moveToFirst();

			int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
			picturePath = SelectedCursor.getString(columnIndex);
			SelectedCursor.close();
			
			Log.e("picturePath==", "" + picturePath);

			imgFileGallery = new File(picturePath);
			
			 String filenameArray[] = picturePath.split("\\.");
	          String extension = filenameArray[filenameArray.length-1];
	          
	          if(extension.equalsIgnoreCase("jpeg") || extension.equalsIgnoreCase("jpg")
	        		     || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif")){
	        		    imageOrVideo = "Image";
	        		   }
	        		   else {
	        		    imageOrVideo = "Video";
	        		   }
			
//			if (!(imgFileGallery.exists() || imgFileGallery==null || imgFileGallery.equals(""))){
//				myBitmap = ThumbnailUtils.createVideoThumbnail(picturePath,
//			                MediaStore.Video.Thumbnails.MICRO_KIND);
//				
//				imageOrVideo = "Video";
//			}
//			else{
//				imageOrVideo = "Image";
//			}
			
			

		
			
			formattedDatetoSend=df1.format(c.getTime());
			
			
			HashMap localHashMap2 = new HashMap();
			localHashMap2.put("message_text", null);
			localHashMap2.put("created",formattedDatetoSend );
			localHashMap2.put("sender_id", sp.getString("user_id",""));
			localHashMap2.put("attachment", imgFileGallery.getAbsolutePath());
			localHashMap2.put("attachment_id", null);
			localHashMap2.put("attachment_type", imageOrVideo);
			
			
			int size = chat_list.size();
			
			Log.e("size(listtss)==",""+size);
			
			if(size<=0){
				chat_list.add(0, localHashMap2);
			
				temp_list.add(0);
				
		//		mAdapter1.notifyDataSetChanged();
		//		scrollChatListToBottom();
				
				mAdapter1 = new LazyAdapter1(chat_list, getActivity());
				listview.setAdapter(mAdapter1);
			}
			else {
				chat_list.add(size, localHashMap2);
			
				temp_list.add(size);
			
				mAdapter1.notifyDataSetChanged();
				scrollChatListToBottom();
			}
				
			message_text = "";
				sendMessageObj = new sendMessageTask();
				sendMessageObj.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
	}
	} catch(Exception e){
		Log.e("exception(on activity result)==",""+e);
	}
}

	public class sendMessage1Task extends AsyncTask<String, Void, String> {
		ByteArrayOutputStream baos;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


		}

		@Override
		protected String doInBackground(String... Params) {
		
			Log.d("*************************************","********************************************");
			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpClientUpload client = new HttpClientUpload(
						Functions.uri+"sendmessage.php?");
				client.connectForMultipart();
				
				
				
				
				client.addFormPart("senderid", receiver_id);
				client.addFormPart("receiverid", sender_id);
				
				client.addFormPart("message_text", message_text);
				
				client.addFormPart("time", formattedDatetoSend);
			
				
			client.finishMultipart();
				
				String data = client.getResponse();
				
				
				Log.e("data(sending message)",""+data);

				return data;
			} catch (Throwable t) {
				t.printStackTrace();
			}

			return null;
			
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			db.dismiss();
			String error;
			Boolean status;
			try {
				// Log.e("result===",""+result);
				JSONObject json = new JSONObject(result);
				picturePath = "";
				if (json.getBoolean("status")) {
					
					message_text = "";
					
					JSONObject c = json.getJSONObject("data");
					last_id = c.getString("msg_id");
					Editor e = sp.edit();
					e.putString("last_id", last_id);
					e.commit();
					
				//	video_url = c.getString("video_url");
					temp_list.clear();
					mAdapter1.notifyDataSetChanged();
				}

				else {
					error = json.getString("error");
					showAlertToUser(error);
				}
			} catch (Exception e) {
			
				showAlertToUser("Something went wrong while processing your request.Please try again.");
			}
		}

	}
	
	 public class resetbadge extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String id;
			
			HashMap result;
			ArrayList localArrayList = new ArrayList();


			public resetbadge(String string) {
				this.id = string;
				
				
			}


			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("user_id",this.id));
					
					
					result = function.resetbadge(localArrayList);
					Log.e("result==",""+result);
				

				} catch (Exception localException) {

				}

				return null;
			}

			protected void onPostExecute(Void paramVoid) {
				
				
			}
			
	    }
}
