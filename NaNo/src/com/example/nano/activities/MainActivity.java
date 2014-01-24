package com.example.nano.activities;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nano.R;
import com.example.nano.adapters.SlidingMenuAdapter;
import com.example.nano.models.NavDrawerItem;
import com.example.nano.supportclasses.BluetoothnFileSupporter;
import com.example.nano.supportclasses.ProfilesDBHelper;
import com.example.nano.supportclasses.ProfilesSupporter;

public class MainActivity extends Activity {

	//UI Related Variables
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	//drawerTitle
	private CharSequence mDrawerTitle;
	//app title
	private CharSequence mTitle;
	//slide menu items
	private String[] navMenuTitles;
	int[] navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private SlidingMenuAdapter adapter;
	
	//Functionality Related Variables
	//private ProfilesDBHelper profileHelper;
	private ProfilesSupporter profileAdder;
	private BluetoothnFileSupporter filerefresher;
	private final String[] acceptedFormats = {".c",".txt",".py",".rb",".ahk"};
	private String fileName = "CHOSENINFO";
	boolean firstLaunchCheck;
	TypedArray a;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
		setContentView(R.layout.main);
		//firstLaunch Configs
		firstLaunchCheck = firstLauchChecker(this);
		Log.d("FIRSTLAUNCH",firstLaunchCheck + "");
		setupFirstLaunch(this);
		//Get Stuff from database and SharedPrefs to setup the UI
		ProfilesSupporter profileInfo = new ProfilesSupporter(this);
		String[] titles = profileInfo.getProfileNames();
		Log.d("TITLES",titles + "");
		String[] icons = profileInfo.getProfileIconAddresses();
		
		int[] icon_addresses = new int[icons.length];
		for(int i = 0;i< icons.length;i++){
			icon_addresses[i] = getResources().getIdentifier(icons[i], "drawable", getPackageName());
			Log.d("CHECKERZZZZZz","" + icon_addresses[i]);
			
		}
		//upload icon 
		
		//load slide menu items from SharedPrefs
		SharedPreferences prefs = getSharedPreferences(fileName, 0);
		int profileReturned = prefs.getInt("profileIndex", 0);
		setupUI(getApplicationContext(), titles,icon_addresses);
		    if(savedInstanceState == null){
        	//default display of first item
        	displayView(profileReturned);
        }
	}
	

	private void setupUI(Context c,String[] titles,int[] icons){
		mTitle = mDrawerTitle = getTitle();
		navMenuTitles = titles;
		navMenuIcons = icons;
		//icons
		//navMenuIcons = typedArray;
		//Log.d("ICON","" + icons[0]);
		//load views
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView)findViewById(R.id.list_slidermenu);
		
		navDrawerItems = new ArrayList<NavDrawerItem>();
		
		//fill the arraylist with each item
		for(int i=0;i<navMenuTitles.length;i++){
			//navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],navMenuIcons.getResourceId(i, -1)));
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],navMenuIcons[i]));
		}
		
		//for garbage collection and space optimization in typed array
		//navMenuIcons.recycle();
		
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		registerForContextMenu(mDrawerList);
		//set up the SlidingMenu View Adapter
		adapter = new SlidingMenuAdapter(c, navDrawerItems);
		mDrawerList.setAdapter(adapter);
		
		// enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
 
	}
	
	private boolean firstLauchChecker(Context c){
		String fileName = "FIRSTLAUNCH";
		SharedPreferences prefs = c.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = prefs.edit();
		boolean check = prefs.getBoolean("isitfirstlaunch", true);
		if(check == true){
			editor.putBoolean("isitfirstlaunch", false);
			editor.commit();
		}
		return check;
	}
	
	private void setupFirstLaunch(Context c){
		if(firstLaunchCheck){
			BluetoothnFileSupporter fileCreator = new BluetoothnFileSupporter(this);
			fileCreator.setupFirstLaunchDirs();
			setupfirstLaunchImageStorage(c);
			ProfilesDBHelper profileHelper = new ProfilesDBHelper(c, "All");
			ProfilesSupporter profileAdder = new ProfilesSupporter(c);
			profileAdder.insertProfileName("All","");
			Toast.makeText(c, "Menu Button -> Guide for a quick start guide of NaNo" , Toast.LENGTH_LONG).show();
			}
	}
	
	private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_action_bar_items, menu);
    	
    	 // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        
    	return super.onCreateOptionsMenu(menu);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
        case R.id.action_settings:
            return true;
        case R.id.action_new_note:
        	Intent i1 = new Intent(MainActivity.this,NewNote.class);
        	SharedPreferences prefs = getSharedPreferences(fileName, 0);
    		SharedPreferences.Editor editor = prefs.edit();
    		editor.putString("profileTitle", getActionBar().getTitle().toString());
        	editor.putBoolean("firstOpen", true);
        	editor.commit();
        	try {
				startActivity(i1);
				finish();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Please try Again", Toast.LENGTH_SHORT).show();
			}
        	return true;
        case R.id.action_create_new_profile:
        	createNewProfile();
			return true;

		case R.id.action_about:
			about();
        	return true;
        case R.id.action_refresh:
        	Toast.makeText(this, "Starting Refresh Process will proceed for 2-5 mins\n" +
        			"Please leave Phone as is during refresh else Application will get corrupted"
        			, Toast.LENGTH_LONG).show();
        	try {
				Thread thread = new Thread(new Runnable(){
				@Override
				public void run(){
					refresher(getApplicationContext());
				}
				});
				thread.start();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	 return true;
        case R.id.action_search:
        	return true;
        case R.id.action_guide:
        	Intent i = new Intent(MainActivity.this,Guide.class);
        	startActivity(i);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void displayView(int position){
    	Fragment myfrag = new NotesSubCategoryListViewFragment();
    	Bundle bundle = new Bundle();
    	bundle.putString("FragName" , navMenuTitles[position]);
    	SharedPreferences prefs = getSharedPreferences(fileName, 0);
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putInt("profileIndex", position);
    	editor.commit();
    	bundle.putInt("fragIconAddress", navMenuIcons[position]);
    	myfrag.setArguments(bundle);
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, myfrag).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
    }
    
    @Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	private String getDate(){

		Date d = new Date();
		CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy, hh:mm:ss ", d.getTime());
		return (s + "");
	}
	
	private void about() {
		Dialog d = new Dialog(MainActivity.this);
		d.setTitle("About NANo");
		TextView tv = new TextView(this);
		tv.setText("Welcome to NANo : Not Another Notes app!.\nWondering about the title?\n" +
				"NANo is not your typical Notes application but a fusion of " +
				"Personalization with the ol' borin' Notes to give you something to make " +
				"Note Making FUN!Add this to the range of features provided...makes NANo The Notes App to use..\n" +
				"Enjoy!\n" +
				"Developed by the Crusade Group:\n" +
				"Saatvik Shah and Sanjay Thakur");
		d.setContentView(tv);
		d.show();
	}
	
	private void createNewProfile() {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("CREATE NEW PROFILE");
		alert.setMessage("Enter profile name and then click OK");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				boolean didItWork = true;
				try {
					ProfilesDBHelper db = new ProfilesDBHelper(
							MainActivity.this, value);
					db.open();
					db.close();
					SharedPreferences prefs = getSharedPreferences(fileName, 0);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("chosenProfile", value);
					editor.commit();
					Intent i = new Intent(MainActivity.this,IconPicker.class);
		        	startActivity(i);
				} catch (Exception e) {
					didItWork = false;
					Toast t = Toast.makeText(MainActivity.this,
							"Operation failed", Toast.LENGTH_LONG);
					e.printStackTrace();
				} finally {
					if (didItWork) {
						Toast t = Toast.makeText(MainActivity.this,
								"New Profile " + value
										+ " created successfully",
								Toast.LENGTH_LONG);
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
	
	public void refresher(Context c){
		filerefresher = new BluetoothnFileSupporter(c);
    	ArrayList<String> foundpaths = new ArrayList<String>();
    	foundpaths = filerefresher.listfilz(new File(Environment.getExternalStorageDirectory().toString()),acceptedFormats,foundpaths);
    	String[] notePaths = new String[foundpaths.size()];
    	notePaths = foundpaths.toArray(notePaths); 	
    	ProfilesSupporter profAdder = new ProfilesSupporter(c);
    	BluetoothnFileSupporter fileSupporter = new BluetoothnFileSupporter(c);
    	//First delete notes stored in MyNotes/Sent_Files/
    	fileSupporter.setupRefresh();
    	//Add recieved files to db
       	ArrayList<String> profilesToAdd = new ArrayList<String>();
    	for(int i = 0; i < notePaths.length ; i++){
    	profilesToAdd.add(fileSupporter.saveMultipleFileTypestoDB(c, notePaths[i]));
    	}
    	
    	Log.d("Profiles to Add", "" + profilesToAdd);
    	for(int i = 0; i <profilesToAdd.size() ; i++){
    		profAdder.insertProfileName(profilesToAdd.get(i),selectIcon(profilesToAdd.get(i)));
    	}
    	Intent i = new Intent(MainActivity.this,MainActivity.class);
    	startActivity(i);
	}

    private String selectIcon(String extension) {
		// TODO Auto-generated method stub
    	if(extension.contentEquals("TextDump")){
			return "icon_txt";
		}else if(extension.contentEquals("Ruby Scripts")){
			return "icon_ruby";
		}else if(extension.contentEquals("Matlab Stuff")){
			return "icon_matlab";
		}else if(extension.contentEquals("Python Scripts")){
			return "icon_python";
		}else if(extension.contentEquals("C Scripts")){
			return "icon_c";
		}else if(extension.contentEquals("AutohotKey Scripts")){
			return "ic_communities";
		}else{
			return "ic_pages";
		}
	}

	public void setupfirstLaunchImageStorage(Context c){
    	BluetoothnFileSupporter imageBackLoader = new BluetoothnFileSupporter(c);
    	imageBackLoader.saveImageBackgrounds("papyrus",R.drawable.papyrus);
    	imageBackLoader.saveImageBackgrounds("beauty",R.drawable.beauty);
    	imageBackLoader.saveImageBackgrounds("natural2", R.drawable.natural2);
    	imageBackLoader.saveImageBackgrounds("diary", R.drawable.diary);
    	imageBackLoader.saveImageBackgrounds("beauty2",R.drawable.beauty2);
    	imageBackLoader.saveImageBackgrounds("game",R.drawable.game);
    	imageBackLoader.saveImageBackgrounds("music1",R.drawable.music1);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu2, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu2, v, menuInfo);
        menu2.add(Menu.NONE, R.id.action_delete_profile, Menu.NONE, "Delete this Profile");
     }
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  switch(item.getItemId()){
	  case R.id.action_delete_profile:
		  Log.d("onLongClick List item","" + info.position);
		  ProfilesSupporter pDeleter = new ProfilesSupporter(this);
		  String[] Profiles = pDeleter.getProfileNames();
		  String chosenProfile = Profiles[info.position];
		  ProfilesDBHelper dbpDeleter= new ProfilesDBHelper(this, chosenProfile);
		  dbpDeleter.open();
		  dbpDeleter.deleteProfile();
		  pDeleter.deleteProfileName(chosenProfile);
		  dbpDeleter.close();
		  String[] newProfiles = pDeleter.getProfileNames();
		  SharedPreferences prefs = getSharedPreferences(fileName, 0);
		  SharedPreferences.Editor editor = prefs.edit();
		  editor.putInt("profileIndex", 0);
		  editor.commit();
		  Intent i = new Intent(MainActivity.this,MainActivity.class);
		  startActivity(i);
		  finish();
		  return true;
	  }
	  return super.onContextItemSelected(item);
	}

    
}
