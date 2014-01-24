package com.example.nano.activities;

//CHANGES!!!
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nano.R;
import com.example.nano.supportclasses.BluetoothnFileSupporter;
import com.example.nano.supportclasses.ProfilesDBHelper;

public class NoteBackGroundPicker extends Activity {

	Drawable[] images;
	String[] img_paths;
	String[] imageNames;
	LinearLayout gallery;
	private String fileName = "CHOSENINFO";
	private ProfilesDBHelper imgUpdater;
	String mainPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bg_gallery);
		images = getBackgroundImages(this);
		img_paths = getImageFilePaths(this);
		imageNames = new String[images.length];
		gallery = (LinearLayout) findViewById(R.id.mygallery);
		for (int i = 1; i <= images.length; i++) {
			imageNames[i - 1] = i + "th BackGround";
			gallery.addView(insertPhoto(images[i - 1], i - 1));
		}
	}

	View insertPhoto(Drawable d, final int i) {

		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageDrawable(d);

		// set an onclick listener for imageView
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences prefs = getSharedPreferences(fileName, 0);
				String profileTitle = prefs.getString("profileTitle", "All");
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("noteBackground", img_paths[i]);
				editor.commit();
				String noteName = prefs.getString("noteTitle", "");
				imgUpdater = new ProfilesDBHelper(getApplicationContext(),
						profileTitle);
				imgUpdater.open();
				Toast.makeText(getApplicationContext(),
						"Clicked - " + imageNames[i], Toast.LENGTH_LONG).show();
				Log.d("PATH CHOSEN", img_paths[i]);
				imgUpdater.updateBackgroundPath(noteName, img_paths[i]);
				imgUpdater.close();
				NewNote.getInstance().setMyBackground(images[i]);
				finish();

			}

		});
		layout.addView(imageView);
		return layout;
	}

	private Drawable[] getBackgroundImages(Context c){
		 BluetoothnFileSupporter imageLoader = new BluetoothnFileSupporter(c);
		 String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) { 
		 File imagesDir = new File(Environment.getExternalStorageDirectory().toString() + "/MyNotes/" + imageLoader.BACKGROUNDIMAGES_FOLDER + "/");
		 //get List of images in this dir
		 File[] images_list = imagesDir.listFiles();
		 Drawable[] loadedImages = new Drawable[images_list.length]; 
		 for(int j = 0;j < images_list.length ; j++){
			 //convert read bitmap to a drawable
			 //loadedImages[i] = new BitmapDrawable(getResources(),imageLoader.readImage(images_list[i]));
			 BitmapFactory.Options o = new BitmapFactory.Options();
			    for (int i = 1; i<10; i++){
			        o.inSampleSize = i;
			        o.inJustDecodeBounds = true;
			        BitmapFactory.decodeFile(images_list[j].getAbsolutePath(), o);
			        int h = o.outHeight;
			        int w = o.outWidth;
			        Log.d("ANDRO_ASYNC",String.format("going in h=%d w=%d resample = %d",h,w,o.inSampleSize));
			        o.inJustDecodeBounds = false;
			        try{
			        	Drawable d=new BitmapDrawable(getResources(),Bitmap.createScaledBitmap(
		                        BitmapFactory.decodeFile(images_list[j].getAbsolutePath(), o), 
		                        w, 
		                        h, 
		                        true));
			            loadedImages[j]= d;
			            break;
			        }catch(OutOfMemoryError E){
			            Log.d("ANDRO_ASYNC",String.format("catch Out Of Memory error"));
			    //      E.printStackTrace();
			            System.gc();
			        }           
			    }
		 }
		 return loadedImages;
			}else{
				return null;
			}
	 }

	private String[] getImageFilePaths(Context c) {
		BluetoothnFileSupporter imageLoader = new BluetoothnFileSupporter(c);
		mainPath = Environment.getExternalStorageDirectory().toString()
				+ "/MyNotes/" + imageLoader.BACKGROUNDIMAGES_FOLDER + "/";
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File imagesDir = new File(mainPath);
			// get List of images in this dir
			File[] images_list = imagesDir.listFiles();
			String[] paths = new String[images_list.length];
			for (int i = 0; i < images_list.length; i++) {
				paths[i] = mainPath + images_list[i].getName().toString();
			}
			return paths;
		} else {
			return null;
		}
	}

}
