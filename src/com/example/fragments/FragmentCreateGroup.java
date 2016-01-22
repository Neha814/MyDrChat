package com.example.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import com.example.mydrchatapp.Home;
import com.example.mydrchatapp.R;
import com.macrew.utils.HttpClientUpload;
import com.macrew.utils.NetConnection;
import com.macrew.utils.TransparentProgressDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ProgressBar;

public class FragmentCreateGroup extends Fragment { 
	
	ImageView group_pic ; 
 	Button camera , gallery , cancel , next;
 	EditText group_name;
 	ProgressBar progressBar1;
 	LinearLayout ll;
 	public String photoFileName = "photo.jpg";
 	Bitmap takenImage;
 	File imgFileGallery;
 	String GROUP_NAME = "";
 	SharedPreferences sp;
 
 	Boolean isConnected;
 	TransparentProgressDialog db;
 	
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
   
    	
        View rootView = inflater.inflate(R.layout.create_group, container, false);
        
        group_pic = (ImageView) rootView.findViewById(R.id.group_pic);
		camera = (Button) rootView.findViewById(R.id.camera);
		gallery = (Button) rootView.findViewById(R.id.gallery);
		cancel = (Button) rootView.findViewById(R.id.cancel);
		next = (Button) rootView.findViewById(R.id.next);
		group_name = (EditText) rootView.findViewById(R.id.group_name);
		progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		ll = (LinearLayout) rootView.findViewById(R.id.ll);
		
		imgFileGallery = new File("");
		progressBar1.setVisibility(View.INVISIBLE);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		isConnected = NetConnection.checkInternetConnectionn(getActivity());
	
		
		next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GROUP_NAME = group_name.getText().toString();
				if(GROUP_NAME.equals("") || GROUP_NAME.equals(" ")){
					showAlertToUser("Please enter group name.");
				}
				
				else {
					 FragmentManager fm = getActivity().getSupportFragmentManager();
				        FragmentTransaction ft = fm.beginTransaction();
				        
				        Bundle bundle = new Bundle();
						bundle.putString("group_name",GROUP_NAME);
						bundle.putString("img_name",imgFileGallery.getName());
						
				     
				        	Fragment_Favourites fragment = new Fragment_Favourites();
				        	 fragment.setArguments(bundle);
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
		
		group_pic.setOnClickListener(new View.OnClickListener() {
			
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
			group_pic.setImageBitmap(takenImage);

			

			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.listview_bottom_down);

			ll.startAnimation(bottomDown);
			ll.setVisibility(View.INVISIBLE);

			imgFileGallery = new File(takenPhotoUri.getPath());
			
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

			group_pic.setImageBitmap(takenImage);

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

			

			Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
					R.anim.listview_bottom_down);

			ll.startAnimation(bottomDown);
			ll.setVisibility(View.INVISIBLE);

		}
	}
	

}
