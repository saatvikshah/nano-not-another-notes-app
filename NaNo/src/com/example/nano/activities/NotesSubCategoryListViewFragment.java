package com.example.nano.activities;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nano.R;
import com.example.nano.adapters.CustomizedListViewAdapter;
import com.example.nano.supportclasses.BluetoothnFileSupporter;
import com.example.nano.supportclasses.ProfilesDBHelper;
import com.example.nano.supportclasses.ProfilesSupporter;

public class NotesSubCategoryListViewFragment extends Fragment{
	
	TextView label;
	public static String KEY_TITLE = "TITLE";
	public static String KEY_CONTENT = "CONTENT";
	public static String KEY_DOBNOTE = "DOBNOTE";
	public static String KEY_IMAGE = "IMAGE";
	ListView lv;
	CustomizedListViewAdapter mySubNotesAdapter;
	private ProfilesDBHelper profileAssistant;
	String[] titles,notes_content,note_backgrounds,passwords,notes_attatchments;
	int[] passEnabled;
	private String fileName = "CHOSENINFO";
	int icon_address;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.notes_category_fragment, container, false);
		rootView = setupUI(rootView);
        return rootView;
    }
	
	private View setupUI(View rootView){
		
		label = (TextView)rootView.findViewById(R.id.txtLabel);
		String noteTitle = getArguments().getString("FragName");
		icon_address = getArguments().getInt("fragIconAddress");
		lv = (ListView)rootView.findViewById(R.id.NotesSubCategoryListView);
		//setup db
		profileAssistant = new ProfilesDBHelper(getActivity(), noteTitle);
		profileAssistant.open();
		titles = getTitles();
		notes_content = getContent();
		note_backgrounds = getBackgrounds();
		String[] notes_dob = getDOBNotes();
		notes_attatchments = profileAssistant.getAttatchments();
		passEnabled = profileAssistant.getNotePasswordsEnabled();
		passwords = profileAssistant.getNotePasswords();
		profileAssistant.close();
		//fill the ListView from db
		ArrayList<HashMap<String,String>> notesList = addValues(titles,notes_content,notes_dob,passEnabled);
		mySubNotesAdapter = new CustomizedListViewAdapter(getActivity(), notesList);
		lv.setAdapter(mySubNotesAdapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent i1 = new Intent(getActivity(),NewNote.class);
	        	SharedPreferences prefs = getActivity().getSharedPreferences(fileName, 0);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("profileTitle", getActivity().getActionBar().getTitle().toString());
	        	editor.putBoolean("firstOpen", false);
	        	editor.putString("noteTitle", titles[position]);
	        	editor.putString("noteContent", notes_content[position]);
	        	editor.putString("noteBackground", note_backgrounds[position]);
	        	editor.putString("noteAttatchments", notes_attatchments[position]);
	        	editor.putInt("notePassOn", passEnabled[position]);
	        	editor.putString("notePassword", passwords[position]);
	        	editor.commit();
	        	if(passEnabled[position] == 0){
	        	try {
	        		startActivity(i1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getActivity(), "Error Occured Please Try Again..!", Toast.LENGTH_SHORT).show();
				}
	        	}else{
					checkPasswordOK(passwords[position]);
				}
	        	//getActivity().finish();
			}
		});
		registerForContextMenu(lv);
		label.setText(noteTitle);
		return rootView;
	}

	private String[] getDOBNotes() {
		// TODO Auto-generated method stub
		String[] NoteDOBz = profileAssistant.getDOB();
		//String[] NoteDOBz = {};
		return NoteDOBz;
	}

	private String[] getContent() {
		// TODO Auto-generated method stub
		String[] contentz = profileAssistant.getNoteContents();
		//String[] contentz = {};
		return contentz;
	}

	private String[] getTitles() {
		// TODO Auto-generated method stub
		String[] titles = profileAssistant.getNoteNames();
		//String[] titles = {};
		return titles;
	}
	
	private String[] getBackgrounds(){
		String[] note_backgrounds = profileAssistant.getNoteBackgroundPaths();
		return note_backgrounds;
	}

	private ArrayList<HashMap<String,String>> addValues(String[] titles, String[] notes_content, String[] notes_dob,int[] passwordOn){
		
		ArrayList<HashMap<String,String>> notesList = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<titles.length;i++){
			HashMap<String,String> map = new HashMap<String,String>();
			map.put(KEY_TITLE, titles[i]);
			if(passwordOn[i] == 1){
				map.put(KEY_CONTENT, "<PASSWORD PROTECTED>");
			}else{
				if(notes_content[i].length() < 40){
				map.put(KEY_CONTENT, notes_content[i]);
				}else{
					map.put(KEY_CONTENT, notes_content[i].substring(0, 40));
				}
			}
			map.put(KEY_DOBNOTE, notes_dob[i]);
			map.put(KEY_IMAGE, "" + icon_address);
			notesList.add(map);
		}
		return notesList;
		
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.action_bluetooth_transfer, Menu.NONE, "Send..");
        menu.add(Menu.NONE, R.id.action_delete_note, Menu.NONE, "Delete");
        menu.add(Menu.NONE, R.id.action_change_profile, Menu.NONE, "Change Profile");
        menu.add(Menu.NONE, R.id.action_set_alarm, Menu.NONE, "Set Alarm");
    }
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  switch(item.getItemId()){
	  case R.id.action_delete_note:
		  Log.d("onLongClick List item","" + info.position);
		  if(passEnabled[info.position] == 1){
			checkPasswordOKbeforeDelete(passwords[info.position], titles[info.position]);  
		  }else{
		  profileAssistant.open();
		  profileAssistant.deleteEntry(titles[info.position]);
		  profileAssistant.close();
		  Intent i = new Intent(getActivity(),MainActivity.class);
		  startActivity(i);
		  getActivity().finish();
		  }
		  return true;
	  case R.id.action_bluetooth_transfer:
		  Log.d("onLongClick List item","" + info.position);
		  //transfer selected note and its contents via bluetooth ---Sanjay
		  BluetoothnFileSupporter bt= new BluetoothnFileSupporter(getActivity());
		  bt.saveFile(titles[info.position], notes_content[info.position]);
		  bt.sendFile(titles[info.position]);
		  return true;
	  case R.id.action_change_profile:
		  Bundle profileChangingInfo = new Bundle();
		  profileChangingInfo.putString("prevProfile", getActivity().getActionBar().getTitle().toString());
		  profileChangingInfo.putString("noteName", titles[info.position]);
		  Intent i2 = new Intent(getActivity(),ProfilePicker.class);
		  i2.putExtra("profileChangingInfo", profileChangingInfo);
		  startActivity(i2);
		  return true;
	  case R.id.action_set_alarm:
		  Bundle alarmInfo = new Bundle();
		  alarmInfo.putString("alarmMessage", titles[info.position]);
		  Intent i5 = new Intent(getActivity(),Alarm.class);
		  i5.putExtra("alarmInfo", alarmInfo);
		  startActivity(i5);
		  return true;
	  case R.id.action_delete_profile:
		  ProfilesSupporter pDeleter = new ProfilesSupporter(getActivity());
		  profileAssistant.open();
		  profileAssistant.deleteProfile();
		  profileAssistant.close();
		  pDeleter.deleteProfileName(getActivity().getActionBar().getTitle().toString());
		  Intent i = new Intent(getActivity(),MainActivity.class);
		  startActivity(i);
		  getActivity().finish();
		  return true;
	  }
	  return super.onContextItemSelected(item);
	}

	private void checkPasswordOK(final String actualPass) {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("PASS-LOCKED");
		alert.setMessage("Please Enter password to continue:");

		// Set an EditText view to get user input
		final EditText inp = new EditText(getActivity());
		inp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		alert.setView(inp);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = inp.getText().toString();
				boolean didItWork = true;
				try {
					if(actualPass.equals(value)){
						Intent i = new Intent(getActivity(),NewNote.class);
			        	startActivity(i);
			        	getActivity().finish();
					}else{
						Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					didItWork = false;
					Toast t = Toast.makeText(getActivity(),
							"Operation failed", Toast.LENGTH_LONG);
					e.printStackTrace();
				} finally {
					if (didItWork) {
					}
				}
			}
		});
		
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		alert.show();	
}
	
	private void checkPasswordOKbeforeDelete(final String actualPass,final String title) {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("PASS-LOCKED");
		alert.setMessage("Please Enter password to continue:");

		// Set an EditText view to get user input
		final EditText inp = new EditText(getActivity());
		alert.setView(inp);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = inp.getText().toString();
				boolean didItWork = true;
				try {
					if(actualPass.equals(value)){
						profileAssistant.open();
						profileAssistant.deleteEntry(title);
						  profileAssistant.close();
						  Intent i = new Intent(getActivity(),MainActivity.class);
						  startActivity(i);
						  getActivity().finish();
					}else{
						Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					didItWork = false;
					Toast t = Toast.makeText(getActivity(),
							"Operation failed", Toast.LENGTH_LONG);
					e.printStackTrace();
				} finally {
					if (didItWork) {
					}
				}
			}
		});
		
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		alert.show();	
}
}
