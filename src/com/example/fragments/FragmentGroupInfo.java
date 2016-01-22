package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragments.Chat.LazyAdapter;
import com.example.fragments.Chat.chat_log;
import com.example.fragments.FragmentProfile.updateProfileTask;
import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;
import com.example.mydrchatapp.VerifyCode;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.HttpClientUpload;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

public class FragmentGroupInfo extends Fragment {

	ImageView group_pic, add_more_participants;
	TextView group_name;
	ListView listview;
	public ImageLoader imageLoader;
	TransparentProgressDialog db;
	LazyAdapter mAdapter;
	String members_to_add = "" , x="";
	LazyAdapter1 mAdapter1;
	int arg;
	String admin_id = "";
	String groupName = "";
	ArrayList<HashMap<String, String>> group_members_list = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> participants_list ;
	Boolean success = false;
	
	SharedPreferences sp;
	Button edit;
	LinearLayout ll;
	Button camera , gallery , cancel;
	public String photoFileName = "photo.jpg";
	File imgFileGallery;
	Bitmap takenImage;
	ProgressBar progressBar1;
	updateProfileIconTask updateProfileIconObj;
	Boolean isConnected;
	String previous_pic_path;
	int delete_tag ;
	int tag_id ;
	ArrayList<Integer> temp_tag_ids = new ArrayList<Integer>();
	Dialog add_mem_dialog;
	Button back;
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();
						
						if(success){
							add_mem_dialog.dismiss();
							FragmentManager fm = getActivity().getSupportFragmentManager();
					        FragmentTransaction ft = fm.beginTransaction();
					        
					       
							
					        	Groups fragment = new Groups();
					        	
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

					}
				});
		localBuilder.create().show();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.group_info, container, false);

		imageLoader = new ImageLoader(getActivity());

		Bundle bundle = getArguments();
		
		 add_mem_dialog = new Dialog(getActivity(),
					R.style.full_screen_dialog);

		arg = bundle.getInt("arg");
		
		imgFileGallery = new File("");

		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

		group_pic = (ImageView) rootView.findViewById(R.id.group_pic);
		group_name = (TextView) rootView.findViewById(R.id.group_name);
		listview = (ListView) rootView.findViewById(R.id.listview);
		add_more_participants = (ImageView) rootView
				.findViewById(R.id.add_more_participants);
		edit = (Button) rootView.findViewById(R.id.edit);
		ll = (LinearLayout) rootView.findViewById(R.id.ll);
		camera = (Button) rootView.findViewById(R.id.camera);
		gallery = (Button) rootView.findViewById(R.id.gallery);
		cancel = (Button) rootView.findViewById(R.id.cancel);
		back = (Button) rootView.findViewById(R.id.back);
		progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		progressBar1.setVisibility(View.INVISIBLE);
		
		
		isConnected = NetConnection.checkInternetConnectionn(getActivity());

		String url = Groups.group_list.get(arg).get("group_icon");
		try {
			imageLoader.DisplayImage(url, group_pic);
		} catch (Exception ae) {
			Log.e("Exception===", "" + ae);
		}

		groupName = Groups.group_list.get(arg).get("group_name");
		group_name.setText(groupName);
		admin_id = Groups.group_list.get(arg).get("admin_id");
		previous_pic_path = url;

		if (admin_id.equals(sp.getString("user_id", ""))) {
			add_more_participants.setVisibility(View.VISIBLE);
		}

		else {
			add_more_participants.setVisibility(View.INVISIBLE);
		}

		for (int i = 0; i < Functions.group_members.size(); i++) {
			HashMap localHashMap = new HashMap();

			if (Functions.group_members.get(i).get("members_" + arg) == null) {

			}

			else {

				localHashMap.put("member_name", Functions.group_members.get(i)
						.get("members_" + arg));
				localHashMap.put("member_id", Functions.group_members.get(i)
						.get("member_id_" + arg));
				localHashMap.put("member_img", Functions.group_members.get(i)
						.get("members_image_" + arg));

				group_members_list.add(localHashMap);
			}

		}
		
		//**************************** group name **************************************************//
		
		edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showChangeGroupNameDialog();	
			}
		});
		
		// ****************************** group icon **********************************************//
		group_pic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Animation bottomUp = AnimationUtils.loadAnimation(
						getActivity(), R.anim.listview_bottom_up);

				ll.startAnimation(bottomUp);

				ll.setVisibility(View.VISIBLE);
				
			}
		});
		
		//*****************************************************************************************//
		
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getActivity(), R.anim.listview_bottom_down);

				ll.startAnimation(bottomDown);
				ll.setVisibility(View.INVISIBLE);

			}
		});
		
			back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Groups  fragment = new Groups();
		     
		  
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

		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						getPhotoFileUri(photoFileName)); // set the image file
															// name

				startActivityForResult(intent, 0);

			}

		});

		gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent GaleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(GaleryIntent, 1);

			}
		});

			//******************************add more participants ********************************************//
				
			add_more_participants.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new get_participants_list(sp.getString("user_id", "") , sp.getString("mode", ""), 
							Groups.group_list.get(arg).get("group_id")).execute(new Void[0]); 
					
				}
			});
			//**********************************************************************************************//
		mAdapter = new LazyAdapter(group_members_list, getActivity());
		listview.setAdapter(mAdapter);

		return rootView;
	}

	protected void showChangeGroupNameDialog() {
		 final Dialog dialog = new Dialog(getActivity(),
					R.style.full_screen_dialog);
			dialog.setCancelable(true);
			
			Button done;
			final EditText new_group_name;
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.group_name_dialog);
			
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			done = (Button) dialog.findViewById(R.id.done);
			new_group_name = (EditText) dialog.findViewById(R.id.new_group_name);
			
			done.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					groupName = new_group_name.getText().toString();
					if(groupName.equals("") || groupName.equals(" ") || groupName== null){
						showAlertToUser("Group name cannot be empty.");
					}
					else{
					dialog.dismiss();
					new update_group_name(sp.getString("user_id", "") , sp.getString("mode", ""), 
							Groups.group_list.get(arg).get("group_id"),groupName).execute(new Void[0]);
					}
				}
			});
			
			new_group_name.setText(groupName);
			 
			dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
			dialog.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

			Drawable d = new ColorDrawable(Color.BLACK);
			d.setAlpha(200);
			dialog.getWindow().setBackgroundDrawable(d);
			dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_top;
			dialog.show();
		
	}

	/*********************** ADAPTER CLASS ******************************************/

	class LazyAdapter extends BaseAdapter {

		LayoutInflater mInflater = null;

		public LazyAdapter(ArrayList<HashMap<String, String>> group_members_list,
				FragmentActivity group_members) {
			
			
			mInflater = LayoutInflater.from(group_members);
		}

		@Override
		public int getCount() {

			return group_members_list.size();
		}

		@Override
		public Object getItem(int position) {

			return group_members_list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.group_members_listitem, null);

				holder.group_member_name = (TextView) convertView
						.findViewById(R.id.group_member_name);

				holder.delete = (ImageView) convertView
						.findViewById(R.id.delete);

				convertView.setTag(holder);

			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (admin_id.equals(sp.getString("user_id", ""))) {
				holder.delete.setVisibility(View.VISIBLE);
			}

			else {
				holder.delete.setVisibility(View.INVISIBLE);
			}

			holder.group_member_name.setText(group_members_list.get(position)
					.get("member_name"));
			
			holder.delete.setTag(position);
			
			holder.delete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					delete_tag = (Integer) v.getTag();
					new delete_group_member(sp.getString("user_id", "") , sp.getString("mode", ""), 
							Groups.group_list.get(arg).get("group_id"),
							group_members_list.get(delete_tag).get("member_id"),Groups.group_list.get(arg).get("admin_id")).execute(new Void[0]);
				}
			});
			
			return convertView;
		}

	}

	/****************** ENDING OF ADAPTER CLASS ************************************/
	class ViewHolder {
		TextView group_member_name;
		ImageView delete;

	}
	 public class update_group_name extends AsyncTask<Void, Void, Void> {
			Functions function = new Functions();
			String id;
			String mode;
			String group_id;
			String group_name_text;
			
			
			HashMap<String, String> result;
			ArrayList localArrayList = new ArrayList();


			public update_group_name(String string, String string2,
					String string3, String groupName) {
				this.id = string;
				this.mode = string2;
				this.group_id = string3;
				this.group_name_text = groupName;
				
			}



			protected Void doInBackground(Void... paramVarArgs) {
				try {
					localArrayList.add(new BasicNameValuePair("id",this.id));
					localArrayList.add(new BasicNameValuePair("mode",this.mode));
					localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
					localArrayList.add(new BasicNameValuePair("group_name",this.group_name_text));
					
					result = function.update_group_name(localArrayList);
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
						
						group_name.setText(groupName);
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
				db = new TransparentProgressDialog(getActivity(),
						R.drawable.loading);
				db.show();
			}

		}
	 public Uri getPhotoFileUri(String fileName) {
			// Get safe storage directory for photos
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"");

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
				Log.d("", "failed to create directory");
			}

			// Return the file target for the photo based on filename
			return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
					+ fileName));
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == 0) {

				Uri takenPhotoUri = getPhotoFileUri(photoFileName);
				// by this point we have the camera photo on disk
				takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
				// Load the taken image into a preview
				// takenImage = Bitmap.createScaledBitmap(takenImage, 120, 120,
				// false);
				group_pic.setImageBitmap(takenImage);
				progressBar1.setVisibility(View.VISIBLE);

				Log.e("takenPhotoUri.getPath()==", "" + takenPhotoUri.getPath());

				Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
						R.anim.listview_bottom_down);

				ll.startAnimation(bottomDown);
				ll.setVisibility(View.INVISIBLE);

				imgFileGallery = new File(takenPhotoUri.getPath());
				Log.e("imgFileGallery==", "" + imgFileGallery);
				
				callwebServiceTOChageGroupIcon();
			}

			else if (requestCode == 1) {
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


				group_pic.setImageBitmap(takenImage);
				
				progressBar1.setVisibility(View.VISIBLE);

				/**
				 * saving to file
				 */

				Uri SelectedImage = data.getData();
				String[] FilePathColumn = { MediaStore.Images.Media.DATA };

				Cursor SelectedCursor = Home.appContext.query(SelectedImage,
						FilePathColumn, null, null, null);
				SelectedCursor.moveToFirst();

				int columnIndex = SelectedCursor.getColumnIndex(FilePathColumn[0]);
				String picturePath = SelectedCursor.getString(columnIndex);
				SelectedCursor.close();

				imgFileGallery = new File(picturePath);

				Log.e("imgFileGallery==", "" + imgFileGallery);

				Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
						R.anim.listview_bottom_down);

				ll.startAnimation(bottomDown);
				ll.setVisibility(View.INVISIBLE);
				
				
				callwebServiceTOChageGroupIcon();

			}
		}

		private void callwebServiceTOChageGroupIcon() {
			if (isConnected == true) {
				updateProfileIconObj = new updateProfileIconTask();
				updateProfileIconObj.execute();
			}

			else {
				showAlertToUser("No internet connection.");
			}
			
		}
		
		public class updateProfileIconTask extends AsyncTask<String, Void, String> {
			ByteArrayOutputStream baos;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressBar1.setVisibility(View.VISIBLE);

			}

			@Override
			protected String doInBackground(String... Params) {
				try {
					baos = new ByteArrayOutputStream();
					takenImage.compress(CompressFormat.PNG, 0, baos);
				}

				catch (Exception e) {
					Log.e("excptn==", "" + e);
				}

				try {
					HttpClient httpclient = new DefaultHttpClient();

					HttpClientUpload client = new HttpClientUpload(
							"http://mydrchat.com/api/update_group.php");
					client.connectForMultipart();
					
					
					client.addFormPart("id", sp.getString("user_id", ""));
					client.addFormPart("mode", sp.getString("mode", ""));
					client.addFormPart("group_id", Groups.group_list.get(arg).get("group_id"));
					
					Log.e("imgFileGallery.getName()==",""+imgFileGallery.getName());
					
					
					if(imgFileGallery.getName().equals("") || imgFileGallery.getName()==null){
						
						
						client.addFormPart("group_icon", previous_pic_path);
					}
					
					else {
						client.addFilePart("group_icon", imgFileGallery.getName(),
								baos.toByteArray());
					}
					
					client.finishMultipart();
					
					Log.e("client==", "" + client+"==="+imgFileGallery.getName());

					String data = client.getResponse();

					Log.e("data==", "" + data);
					Log.e("Image==", "" + imgFileGallery.getName());

					return data;
				} catch (Throwable t) {
					t.printStackTrace();
				}

				return null;

			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				progressBar1.setVisibility(View.INVISIBLE);
				String error;
				Boolean status;
				try {
					 Log.e("result of grp icon change===",""+result);
					JSONObject json = new JSONObject(result);
					if (json.getBoolean("status")) {
						
						showAlertToUser("Profile updated successfully.");
					}

					else {
						error = json.getString("error");
						showAlertToUser(error);
					}
				} catch (Exception e) {
					Log.e("Exception===", "" + e);
					showAlertToUser("Something went wrong while processing your request.Please try again.");
				}
			}

		}
		/**
		 * delete group member
		 */
		
		 public class delete_group_member extends AsyncTask<Void, Void, Void> {
				Functions function = new Functions();
				String id;
				String mode;
				String group_id;
				String member_id;
				String admin_id;
				
				
				HashMap<String, String> result;
				ArrayList localArrayList = new ArrayList();


				public delete_group_member(String string, String string2,
						String string3, String string4, String string5) {
					this.id = string;
					this.mode = string2;
					this.group_id = string3;
					this.member_id = string4;
					this.admin_id = string5;
				}



				protected Void doInBackground(Void... paramVarArgs) {
					try {
						localArrayList.add(new BasicNameValuePair("id",this.id));
						localArrayList.add(new BasicNameValuePair("mode",this.mode));
						localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
						localArrayList.add(new BasicNameValuePair("member_id",this.member_id));
						localArrayList.add(new BasicNameValuePair("admin_id",this.admin_id));
						
						result = function.delete_group_member(localArrayList);
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
							
							showAlertToUser("Successfully deleted.");
						
							group_members_list.remove(delete_tag);
							
							mAdapter.notifyDataSetChanged();
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
					db = new TransparentProgressDialog(getActivity(),
							R.drawable.loading);
					db.show();
				}

			}
		 
		 //********************************** getting participants list **************************************//
		 
		 public class get_participants_list extends AsyncTask<Void, Void, Void> {
				Functions function = new Functions();
				String id;
				String mode;
				String group_id;
				String group_name_text;
				
				
				ArrayList result;
				ArrayList localArrayList = new ArrayList();


				public get_participants_list(String string, String string2,
						String string3) {
					this.id = string;
					this.mode = string2;
					this.group_id = string3;
				
					
				}



				protected Void doInBackground(Void... paramVarArgs) {
					try {
						localArrayList.add(new BasicNameValuePair("user_id",this.id));
						localArrayList.add(new BasicNameValuePair("mode",this.mode));
						localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
					
						
						result = function.get_participants_list(localArrayList);
						Log.e("result==",""+result);
					

					} catch (Exception localException) {

					}

					return null;
				}

				protected void onPostExecute(Void paramVoid) {
					db.dismiss();
					
					try{
						if(result.size()>0){
							participants_list = new ArrayList<HashMap<String, String>>();
							participants_list.addAll(result);
							showDialogToAddParticipants();
						}
						
						else {
							showAlertToUser("No active participants are available.");
						}
					}
					
					catch (Exception ae){
						Log.e("Exception==",""+ae);
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
		 public void showDialogToAddParticipants() {
			
				add_mem_dialog.setCancelable(true);
			
				TextView textView1;
				ListView listview;
				Button back;
				final Button add;
				
				add_mem_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				add_mem_dialog.setContentView(R.layout.participants);
				
				add_mem_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
				textView1 = (TextView) add_mem_dialog.findViewById(R.id.textView1);
				listview = (ListView) add_mem_dialog.findViewById(R.id.listview);
				back = (Button) add_mem_dialog.findViewById(R.id.back);
				add = (Button) add_mem_dialog.findViewById(R.id.add);
				
				textView1.setText("Favourites");
				
				final Handler localHandler3 = new Handler();
				localHandler3.postDelayed(new Runnable() {
					public void run() {
						if (temp_tag_ids.size() > 0) {
							add.setVisibility(View.VISIBLE);
						}

						else {
							add.setVisibility(View.INVISIBLE);
						}
						localHandler3.postDelayed(this, 1000L);
					}
				}, 1000L);
				
				add.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						for(int i = 0;i<temp_tag_ids.size();i++){
							
							members_to_add = participants_list.get(temp_tag_ids.get(i)).get("user_id");
							
							x= members_to_add +"," +x ;
							
							Log.e("x====",""+x);
						}
						new add_participants(x , sp.getString("mode", ""), 
								Groups.group_list.get(arg).get("group_id")).execute(new Void[0]);  
						
					}
				});

				
				mAdapter1 = new LazyAdapter1(participants_list, getActivity());
				listview.setAdapter(mAdapter1);
				
				listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
				
				//--	text filtering
				listview.setTextFilterEnabled(true);
				
				back.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						add_mem_dialog.dismiss();
						
					}
				});
				
				add_mem_dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				add_mem_dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
				add_mem_dialog.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

				Drawable d = new ColorDrawable(Color.BLACK);
				d.setAlpha(200);
				add_mem_dialog.getWindow().setBackgroundDrawable(d);
				add_mem_dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_top;
				add_mem_dialog.show();
				
			}
		 
		 // *************************************** lazy loader 1 ******************************************//
		 
		 /*********************** ADAPTER CLASS ******************************************/

			class LazyAdapter1 extends BaseAdapter {

				LayoutInflater mInflater = null;

				public LazyAdapter1(ArrayList<HashMap<String, String>> participants_list,
						FragmentActivity group_members) {
					
					
					mInflater = LayoutInflater.from(group_members);
				}

				@Override
				public int getCount() {

					return participants_list.size();
				}

				@Override
				public Object getItem(int position) {

					return participants_list.get(position);
				}

				@Override
				public long getItemId(int position) {

					return position;
				}

				@Override
				public View getView(final int position, View convertView,
						ViewGroup parent) {
					final ViewHolder1 holder;
					if (convertView == null) {

						holder = new ViewHolder1();
						convertView = mInflater.inflate(
								R.layout.participants_list, null);

						holder.checkedTextView1 = (CheckedTextView) convertView
								.findViewById(R.id.checkedTextView1);
						
						holder.imageView1 = (ImageView) convertView
								.findViewById(R.id.imageView1);
						
						holder.TextView1 = (TextView) convertView
								.findViewById(R.id.TextView1);
					
						convertView.setTag(holder);

					}

					else {
						holder = (ViewHolder1) convertView.getTag();
					}
					holder.checkedTextView1.setText(participants_list.get(position).get("name"));
					holder.checkedTextView1.setTag(position);
					holder.TextView1.setText(participants_list.get(position).get("mobile_number"));
					String url = participants_list.get(position).get("profile_image");
					imageLoader.DisplayImage(url, holder.imageView1);
					
					try {
						
						if (temp_tag_ids == null || temp_tag_ids.isEmpty()) {
							holder.checkedTextView1.setChecked(false);

						}

						else {
							if (temp_tag_ids.contains(position)) {
								
								holder.checkedTextView1.setChecked(true);
							}

							else {
								holder.checkedTextView1.setChecked(false);
							}
						}
					}

					catch (Exception e) {
						Log.e("Exception==", "" + e);
					}
					
					
					holder.checkedTextView1.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							tag_id = (Integer) v.getTag();
							if (holder.checkedTextView1.isChecked())
							{
								// remove
								Log.e("isChecked==",""+holder.checkedTextView1.isChecked());
								holder.checkedTextView1.setChecked(false);
								 if (temp_tag_ids.contains(position)){
									 int i = temp_tag_ids.indexOf(position);
								temp_tag_ids.remove(i);
								 }
								
								holder.checkedTextView1.getTag();
							}
							     else {
							    	 // add
							    	 Log.e("else==","else");
							    	 holder.checkedTextView1.setChecked(true);
							    	 if (!temp_tag_ids.contains(position)){
							    	 temp_tag_ids.add(position);
							    	 }
							    	 
							     }
							
						}
					});
					
					return convertView;
				}

			}

			/****************** ENDING OF ADAPTER CLASS ************************************/
			class ViewHolder1 {
				CheckedTextView checkedTextView1;
				ImageView imageView1;
				TextView TextView1;

			}
				//*********************** to add participants ****************************//
			
			public class add_participants extends AsyncTask<Void, Void, Void> {
				Functions function = new Functions();
				String members_to_add;
				String mode;
				String group_id;
				
				
				
				HashMap<String, String> result;
				ArrayList localArrayList = new ArrayList();


				public add_participants(String string, String string2,
						String string3) {
					this.members_to_add = string;
					this.mode = string2;
					this.group_id = string3;
				
					
				}



				protected Void doInBackground(Void... paramVarArgs) {
					try {
						localArrayList.add(new BasicNameValuePair("members",this.members_to_add));
						localArrayList.add(new BasicNameValuePair("mode",this.mode));
						localArrayList.add(new BasicNameValuePair("group_id",this.group_id));
					
						
						result = function.add_participants(localArrayList);
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
							success = true;
							
							showAlertToUser("participants added successfully.");
							
							
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
					db = new TransparentProgressDialog(getActivity(),
							R.drawable.loading);
					db.show();
				}

			}
}
