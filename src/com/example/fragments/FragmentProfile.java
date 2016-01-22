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

import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;
import com.macrew.functions.Functions;
import com.macrew.imageloader.ImageLoader;
import com.macrew.utils.HttpClientUpload;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentProfile extends Fragment {

	ImageView pro_pic;
	LinearLayout ll;
	Button camera, gallery, cancel, edit, save;

	EditText name, email, address, phone, landline;
	SharedPreferences sp;
	TransparentProgressDialog db;
	public ImageLoader imageLoader;
	File imgFileGallery;
	Boolean isConnected;
	public String photoFileName = "photo.jpg";
	
	String previous_pic_path;

	updateProfileTask updateProfileObj;
	String NAME = "", ADDRESS = "", LANDLINE = "";

	Bitmap takenImage;
	Boolean isUpdated = false;
	Button back;

	
	
	private void showAlertToUser(String paramString) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(
				getActivity());
		localBuilder.setMessage(paramString).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(
							DialogInterface paramAnonymousDialogInterface,
							int paramAnonymousInt) {
						paramAnonymousDialogInterface.cancel();

						if (isUpdated) {

							FragmentManager fm = getActivity()
									.getSupportFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							Settings fragment = new Settings();

							if (fragment != null) {
								// Replace current fragment by this new one

								ft.replace(android.R.id.tabcontent, fragment);
							} else {
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
		View rootView = inflater.inflate(R.layout.profile, container, false);

		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

		imageLoader = new ImageLoader(getActivity());

		pro_pic = (ImageView) rootView.findViewById(R.id.pro_pic);
		ll = (LinearLayout) rootView.findViewById(R.id.ll);
		camera = (Button) rootView.findViewById(R.id.camera);
		gallery = (Button) rootView.findViewById(R.id.gallery);
		cancel = (Button) rootView.findViewById(R.id.cancel);
		name = (EditText) rootView.findViewById(R.id.name);
		email = (EditText) rootView.findViewById(R.id.email);
		address = (EditText) rootView.findViewById(R.id.address);
		phone = (EditText) rootView.findViewById(R.id.phone);
		landline = (EditText) rootView.findViewById(R.id.landline);
		edit = (Button) rootView.findViewById(R.id.edit);
		save = (Button) rootView.findViewById(R.id.save);
		back = (Button) rootView.findViewById(R.id.back);

		imgFileGallery = new File("");

		pro_pic.setEnabled(false);
		name.setEnabled(false);
		email.setEnabled(false);
		address.setEnabled(false);
		phone.setEnabled(false);
		landline.setEnabled(false);

		ll.setVisibility(View.INVISIBLE);

		isConnected = NetConnection.checkInternetConnectionn(getActivity());
		if (isConnected == true) {
			new profile(sp.getString("user_id", ""), sp.getString("mode", ""))
					.execute(new Void[0]);
		}

		else {
			showAlertToUser("No internet connection.");
		}
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        Settings  fragment = new Settings();
		     
		  
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

		pro_pic.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Animation bottomUp = AnimationUtils.loadAnimation(
						getActivity(), R.anim.listview_bottom_up);

				ll.startAnimation(bottomUp);

				ll.setVisibility(View.VISIBLE);

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Animation bottomDown = AnimationUtils.loadAnimation(
						getActivity(), R.anim.listview_bottom_down);

				ll.startAnimation(bottomDown);
				ll.setVisibility(View.INVISIBLE);

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

		edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				pro_pic.setEnabled(true);
				name.setEnabled(true);
				address.setEnabled(true);
				landline.setEnabled(true);

				phone.setBackgroundResource(R.drawable.grey_edittext);
				email.setBackgroundResource(R.drawable.grey_edittext);

				edit.setVisibility(View.INVISIBLE);
				save.setVisibility(View.VISIBLE);

			}
		});

		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit.setVisibility(View.VISIBLE);
				save.setVisibility(View.INVISIBLE);

				NAME = name.getText().toString();
				ADDRESS = address.getText().toString();
				LANDLINE = landline.getText().toString();

				if (isConnected == true) {
					updateProfileObj = new updateProfileTask();
					updateProfileObj.execute();
				}

				else {
					showAlertToUser("No internet connection.");
				}

			}
		});
		return rootView;
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
			pro_pic.setImageBitmap(takenImage);

			Log.e("takenPhotoUri.getPath()==", "" + takenPhotoUri.getPath());

			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.listview_bottom_down);

			ll.startAnimation(bottomDown);
			ll.setVisibility(View.INVISIBLE);

			imgFileGallery = new File(takenPhotoUri.getPath());
			Log.e("imgFileGallery==", "" + imgFileGallery);
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

			// takenImage = Bitmap.createScaledBitmap(takenImage, 120, 120,
			// false);

			pro_pic.setImageBitmap(takenImage);

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

		}
	}

	public class profile extends AsyncTask<Void, Void, Void> {
		Functions function = new Functions();
		String id;
		String mode;
		Dialog dialog;

		HashMap result;
		ArrayList localArrayList = new ArrayList();

		public profile(String paramString1, String paramString2) {
			this.id = paramString1;
			this.mode = paramString2;

		}

		protected Void doInBackground(Void... paramVarArgs) {
			try {
				localArrayList.add(new BasicNameValuePair("id", this.id));
				localArrayList.add(new BasicNameValuePair("mode", this.mode));

				result = function.profile(localArrayList);
				Log.e("result==", "" + result);

			} catch (Exception localException) {

			}

			return null;
		}

		protected void onPostExecute(Void paramVoid) {
			 db.dismiss();

			try {

				if (result.get("result").toString().equalsIgnoreCase("false")) {
					showAlertToUser(result.get("error").toString());
				}

				else if (result.get("result").toString()
						.equalsIgnoreCase("true")) {

					name.setText(result.get("name").toString());
					email.setText(result.get("email").toString());
					address.setText(result.get("address").toString());
					phone.setText(result.get("mobile_number").toString());
					landline.setText(result.get("landline_number").toString());

					String url = result.get("profile_image").toString();
					
					previous_pic_path = url;
					
					if(url.equals("") || url == null){
						
						pro_pic.setImageResource(R.drawable.default_img);
					}
					else {
					imageLoader.DisplayImage(url, pro_pic);
					}

				}

				else {
					showAlertToUser("Something went wrong while processing your request.Please try again.");
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

	public class updateProfileTask extends AsyncTask<String, Void, String> {
		ByteArrayOutputStream baos;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new TransparentProgressDialog(getActivity(),
					R.drawable.loading);
			db.show();

		}

		@Override
		protected String doInBackground(String... Params) {
			try {
				baos = new ByteArrayOutputStream();
				takenImage.compress(CompressFormat.PNG, 100, baos);
			}

			catch (Exception e) {
				Log.e("excptn==", "" + e);
			}

			try {
				HttpClient httpclient = new DefaultHttpClient();

				HttpClientUpload client = new HttpClientUpload(
						"http://mydrchat.com/api/update_profile.php");
				client.connectForMultipart();
				
				
				client.addFormPart("id", sp.getString("user_id", ""));
				client.addFormPart("mode", sp.getString("mode", ""));
				client.addFormPart("name", NAME);
				client.addFormPart("email", email.getText().toString());
				client.addFormPart("address", ADDRESS);
				client.addFormPart("landline_number", LANDLINE);
				client.addFormPart("mobile_number", phone.getText().toString());
				
				if(imgFileGallery.getName().equals("") || imgFileGallery.getName()==null){
					
					
					client.addFormPart("Image", previous_pic_path);
				}
				
				else {
					client.addFilePart("Image", imgFileGallery.getName(),
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
			db.dismiss();
			String error;
			Boolean status;
			try {
				// Log.e("result===",""+result);
				JSONObject json = new JSONObject(result);
				if (json.getBoolean("status")) {
					isUpdated = true;
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

}