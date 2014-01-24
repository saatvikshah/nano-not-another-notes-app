package com.example.nano.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nano.R;
import com.example.nano.supportclasses.ProfilesSupporter;

public class IconPicker extends Activity{

	String[] icon_address = {"icon_music","icon_game",
			"icon_txt","icon_ruby","icon_python",
			"icon_c","icon_matlab"};
	Drawable[] icons = new Drawable[icon_address.length];
	LinearLayout gallery;
	private String fileName = "CHOSENINFO";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bg_gallery);
		gallery = (LinearLayout)findViewById(R.id.mygallery);
		for(int i = 0;i<icons.length;i++){
			icons[i] = convertIntToDrawable(getResources().getIdentifier(icon_address[i], "drawable", getPackageName()));
			gallery.addView(insertPhoto(icons[i],i));
		}
	}
	
	View insertPhoto(Drawable d,final int i){
	     
	     LinearLayout layout = new LinearLayout(getApplicationContext());
	     layout.setLayoutParams(new LayoutParams(100, 100));
	     layout.setGravity(Gravity.CENTER);
	     
	     ImageView imageView = new ImageView(getApplicationContext());
	     imageView.setLayoutParams(new LayoutParams(80, 80));
	     imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	     imageView.setImageDrawable(d);
	     
	     //set an onclick listener for imageView
	     imageView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProfilesSupporter supporter = new ProfilesSupporter(getApplicationContext());
				SharedPreferences prefs = getSharedPreferences(fileName, 0);
				String profileName = prefs.getString("chosenProfile", "");
				Log.d("Chosen Icon",icon_address[i]);
				supporter.insertProfileName(profileName, icon_address[i]);
				Intent i = new Intent(IconPicker.this,MainActivity.class);
				startActivity(i);
				finish();	
			}
	     });
	     layout.addView(imageView);
	     return layout;
	}
	
	private Drawable convertIntToDrawable(int id){
		Drawable d = getResources().getDrawable(id);
		return d;
	}
	
	
}
