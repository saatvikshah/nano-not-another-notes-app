package com.example.nano.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nano.R;
import com.example.nano.supportclasses.ProfilesDBHelper;

public class SearchActivity extends ListActivity {

	// Functionality Related Vars
	private TextView txtquery;
	String[] allTitles,parsedallTitles, allContent,parsedallContent, allDOBs, allProfiles, allBackPaths,
			chosenTitles, chosenContent, chosenDOBs, chosenProfiles,allPasswords,chosenPasswords,
			chosenBackPaths,allNoteAttatchments,chosenNoteAttatchments;
	int[] allPassOn,chosenPassOn;
	Set<Integer> chosenNoteIndices = new HashSet<Integer>();
	ProfilesDBHelper searcher = new ProfilesDBHelper(this, "");
	private String fileName = "CHOSENINFO";

	// List Related Vars
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter notes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		setupUI();
		// Load DB Data
		loadDBData();
		// initialize the list View
		setupLV();
		
		handleIntent(getIntent());
	}

	private void fillValuesinList() {
		chosenTitles = new String[chosenNoteIndices.size()];
		chosenContent = new String[chosenNoteIndices.size()];
		chosenDOBs = new String[chosenNoteIndices.size()];
		chosenProfiles = new String[chosenNoteIndices.size()];
		chosenBackPaths = new String[chosenNoteIndices.size()];
		chosenPasswords = new String[chosenNoteIndices.size()];
		chosenPassOn = new int[chosenNoteIndices.size()];
		chosenNoteAttatchments = new String[chosenNoteIndices.size()];
		Integer[] corrIndices = chosenNoteIndices
				.toArray(new Integer[chosenNoteIndices.size()]);
		for (int i = 0; i < chosenNoteIndices.size(); i++) {
			chosenTitles[i] = allTitles[corrIndices[i]];
			chosenContent[i] = allContent[corrIndices[i]];
			chosenDOBs[i] = allDOBs[corrIndices[i]];
			chosenBackPaths[i] = allBackPaths[corrIndices[i]];
			chosenProfiles[i] = allProfiles[corrIndices[i]];
			chosenPasswords[i] = allPasswords[corrIndices[i]];
			chosenPassOn[i] = allPassOn[corrIndices[i]];
			chosenNoteAttatchments[i] = allNoteAttatchments[corrIndices[i]];
			
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("line1", chosenTitles[i]);
			if(chosenPassOn[i] == 1){
			item.put("line2", "<PASSWORD PROTECTED>");
			}else{
			if(chosenContent[i].length() < 40){
				item.put("line2", chosenContent[i]);
			}else{
				item.put("line2", chosenContent[i].substring(0, 40));
			}
			}
			list.add(item);
		}
		notes.notifyDataSetChanged();
	}

	private void setupLV() {
		// TODO Auto-generated method stub
		notes = new SimpleAdapter(this, list, R.layout.two_item_row,
				new String[] { "line1", "line2" }, new int[] { R.id.text1,
						R.id.text2 });
		setListAdapter(notes);
	}

	private void loadDBData() {
		// TODO Auto-generated method stub
		searcher.open();
		allTitles = searcher.getAllNoteNames();
		parsedallTitles = parseData(allTitles);
		allContent = searcher.getAllNoteContents();
		parsedallContent = parseData(allContent);
		allDOBs = searcher.getAllNoteDOBs();
		allProfiles = searcher.getAllNoteProfiles();
		allBackPaths = searcher.getAllNoteBackgroundPaths();
		allPassOn = searcher.getAllNotePassOn();
		allPasswords = searcher.getAllNotePasswords();
		allNoteAttatchments = searcher.getAllAttachments();
		searcher.close();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY)
					.toLowerCase();
			// First get All note titles and content from db to search through
			getResults(query, parsedallTitles);
			getResults(query, parsedallContent);
			// fill the list and notify regarding the changes
			fillValuesinList();

		}

	}

	private void setupUI() {
		// getActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	private void getResults(String query, String[] data) {
		for (int i = 0; i < data.length; i++) {
			if (data[i].contains(query)) {
				chosenNoteIndices.add(i);// since its a set no repitions occur
			}
		}
	}

	private String[] parseData(String[] data) {
		String[] returnedData = new String[data.length];
		for (int i = 0; i < data.length; i++) {
			// Convert everything to lower case
			returnedData[i] = data[i].toLowerCase();
		}
		return returnedData;
	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent i1 = new Intent(SearchActivity.this,NewNote.class);
    	SharedPreferences prefs = getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("profileTitle", chosenProfiles[position]);
    	editor.putBoolean("firstOpen", false);
    	editor.putString("noteTitle", chosenTitles[position]);
    	editor.putString("noteContent", chosenContent[position]);
    	editor.putString("noteBackground", chosenBackPaths[position]);
    	editor.putInt("notePassOn", chosenPassOn[position]);
    	editor.putString("notePassword", chosenPasswords[position]);
    	editor.putString("noteAttatchments", chosenNoteAttatchments[position]);
    	editor.commit();
    	if(chosenPassOn[position] == 0){
        	try {
				startActivity(i1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Error Occured Please Try Again..!", Toast.LENGTH_SHORT).show();
			}
        	}else{
				checkPasswordOK(chosenPasswords[position]);
			}
		
	}
	
	private void checkPasswordOK(final String actualPass) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("PASS-LOCKED");
		alert.setMessage("Please Enter password to continue:");

		// Set an EditText view to get user input
		final EditText inp = new EditText(this);
		alert.setView(inp);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = inp.getText().toString();
				boolean didItWork = true;
				try {
					if(actualPass.equals(value)){
						Intent i = new Intent(SearchActivity.this,NewNote.class);
			        	startActivity(i);
			        	finish();	
					}else{
						Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
						finish();
					}
				} catch (Exception e) {
					didItWork = false;
					Toast t = Toast.makeText(getApplicationContext(),
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
