package com.example.nano.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nano.supportclasses.ProfilesDBHelper;
import com.example.nano.supportclasses.ProfilesSupporter;

public class ProfilePicker extends ListActivity{

	String[] profile_names;
	ProfilesSupporter profileImporter;
	ProfilesDBHelper profileSwitcher;
	String prevProfileName,NoteName;
	Bundle prevProfileInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		prevProfileInfo = new Bundle();
		prevProfileInfo = getIntent().getBundleExtra("profileChangingInfo");
		prevProfileName = prevProfileInfo.getString("prevProfile");
		NoteName = prevProfileInfo.getString("noteName");
		profileImporter = new ProfilesSupporter(this);
		profile_names = profileImporter.getProfileNames();
		setListAdapter(new ArrayAdapter<String>(ProfilePicker.this,
				android.R.layout.simple_list_item_1, profile_names));
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		profileSwitcher = new ProfilesDBHelper(this, prevProfileName);
		profileSwitcher.open();
		profileSwitcher.changeNoteProfile(NoteName, profile_names[position]);
		profileSwitcher.close();
		Intent i = new Intent(ProfilePicker.this,MainActivity.class);
		startActivity(i);
	}
	
	
	
}
