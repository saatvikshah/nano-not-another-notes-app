package com.example.nano.activities;
//CHANGES!!
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nano.R;
import com.example.nano.supportclasses.BluetoothnFileSupporter;
import com.example.nano.supportclasses.ProfilesDBHelper;
import com.ipaulpro.afilechooser.utils.FileUtils;

public class NewNote extends Activity {
	
	private static final int REQUEST_CODE = 6234;
	String pathChosen,extraPath;
	LinearLayout ll;
	private TextView attatchments;
	static NewNote newnote;
	private ProfilesDBHelper infoadder;
	private EditText notetitle,notecontent,password;
	CheckBox passEnable;
	boolean firstOpen;
	BluetoothnFileSupporter bg_loader;
	private String fileName = "CHOSENINFO";
	private String oldTitle,BackPath,profileTitle,defBackPath;
	String[] allAttatchments;
	ArrayList<String> attatcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(R.animator.anim_in, R.animator.anim_out);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_layout);
		setupvariables();
		SharedPreferences prefs = getSharedPreferences(fileName, 0);
		profileTitle = prefs.getString("profileTitle", "");
		firstOpen = prefs.getBoolean("firstOpen",true);//not sure what to set this
		bg_loader = new BluetoothnFileSupporter(this);
		defBackPath = Environment.getExternalStorageDirectory().toString() + "/" + "MyNotes" + 
							"/" + bg_loader.BACKGROUNDIMAGES_FOLDER + "/"
														+ "beauty.png";
		if(firstOpen){
			//nothing to do
			infoadder = new ProfilesDBHelper(this, profileTitle);
			BackPath = defBackPath;
		}else{
			allAttatchments = convertStringtoStringArray(prefs.getString("noteAttatchments", null));
			oldTitle = prefs.getString("noteTitle","");
			String noteContent = prefs.getString("noteContent","");
			password.setText(prefs.getString("notePassword", null));
			int passOn = prefs.getInt("notePassOn", 0);
			if(passOn == 1){
				passEnable.setChecked(true);
				password.setVisibility(View.VISIBLE);
			}
			notetitle.setText(oldTitle);
			notecontent.setText(noteContent);
			infoadder = new ProfilesDBHelper(this, profileTitle);
			BackPath = prefs.getString("noteBackground", defBackPath);
			//setup Attatchments
			for(int i = 0;i < allAttatchments.length;i++){
				attatcher.add(allAttatchments[i]);
				setupTextView(allAttatchments[i]);
			}
			//chosen_img_path = ...needs to be got here too from db
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        //default image
        setMyBackground(new BitmapDrawable(getResources(),bg_loader.readImage(new File(BackPath))));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.new_note_action_bar_items, menu);
    	return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_done:
			doneActionSimpleNote();
			return true;
		case R.id.action_bgchooser:
			Log.d("ACTION","BackgroundChooser Activity");
			Intent i2 = new Intent(NewNote.this,NoteBackGroundPicker.class);
			try {
				startActivity(i2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast.makeText(this, "Delete Extra Images from your Background_Images Folder(Max : 5)", Toast.LENGTH_LONG).show();
			}
        	return true;
        default:
        	return super.onOptionsItemSelected(item);
        }
	}
	
	public void setMyBackground(Drawable d){
		FrameLayout fl = (FrameLayout)findViewById(R.id.FLNote);
		fl.setBackground(d);
	}
	
	public static NewNote getInstance(){
		   return newnote;
		 }
	
	private String getDate(){
		Date d = new Date();
		CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy, hh:mm:ss ", d.getTime());
		return (s + "");
	}
	
	private void setupvariables(){
		attatcher = new ArrayList<String>();
		notetitle = (EditText)findViewById(R.id.ETNoteTitle);
		notecontent = (EditText)findViewById(R.id.ETNoteContent);
		passEnable = (CheckBox)findViewById(R.id.CBpasscheck);
		passEnable.setChecked(false);
		password = (EditText)findViewById(R.id.ETpassword);
		attatchments = (TextView)findViewById(R.id.TVAttatch);
		password.setVisibility(View.GONE);
		password.setBackground(null);
		notetitle.setBackground(null);
		notecontent.setBackground(null);
		newnote = this;
		ll = (LinearLayout)findViewById(R.id.LL);
		attatchments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showChooser();
			}
		});
	}

	public void itemClicked(View v){
		CheckBox checkBox = (CheckBox)passEnable;
        if(checkBox.isChecked()){
        	password.setVisibility(View.VISIBLE);
        }else{
        	password.setVisibility(View.GONE);
        }
	}

	@SuppressLint("ResourceAsColor")
	private void setupTextView(String pathProvided){
		final TextView tv = new TextView(getApplicationContext());
		tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tv.setTypeface(Typeface.SANS_SERIF, Typeface.ITALIC);
		tv.setTextColor(getResources().getColor(R.color.Blue));
		tv.setTextSize(15);
		/*if(firstOpen){
			Log.d("TAGGG",extraPath);
		tv.setText(extraPath);
		}else{
		*/
		if(pathProvided.equals("")){
		tv.setText(pathChosen);
		}else{
		tv.setText(pathProvided);
		}
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//gotta open the file in this text view in whichever required format
				String clickedPath = tv.getText().toString();
				openFile(clickedPath);
			}
		});
		ll.addView(tv, ll.getChildCount());
	}
	private void doneActionSimpleNote(){
		int passOn;String pass;
		infoadder.open();
		String nTitle = notetitle.getText().toString();
		String nContent = notecontent.getText().toString();
		String ndate = getDate();
		if(passEnable.isChecked()){
			pass = password.getText().toString();
			passOn = 1;
		}else{
			pass = null;
			passOn = 0;
		}
		if(firstOpen){
			String[] myAttatchments = new String[attatcher.size()];
			Log.d("NULL",attatcher.toString());
			myAttatchments = (String[]) attatcher.toArray(myAttatchments);
			infoadder.createNoteWithoutBack(nTitle, nContent,ndate,passOn, pass, convertStringArraytoString(myAttatchments));
			infoadder.updateBackgroundPath(nTitle, BackPath);
			infoadder.close();
		}else{
			//update
			String[] myAttatchments = new String[attatcher.size()];
			myAttatchments = (String[]) attatcher.toArray(myAttatchments);
			infoadder.updateNotewithoutBack(oldTitle, nTitle, nContent, getDate(), passOn, pass,convertStringArraytoString((String[]) attatcher.toArray(allAttatchments)));
			infoadder.close();
		}
		Intent i = new Intent(NewNote.this,MainActivity.class);
		startActivity(i);
		finish();
	}
	
	private void showChooser() {
	        // Use the GET_CONTENT intent from the utility class
	        Intent target = FileUtils.createGetContentIntent();
	        // Create the chooser Intent
	        Intent intent = Intent.createChooser(
	                target, "File Manager");
	        try {
	            startActivityForResult(intent, REQUEST_CODE);
	        } catch (ActivityNotFoundException e) {
	            // The reason for the existence of aFileChooser
	        }
	    }

	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	            case REQUEST_CODE:
	                // If the file selection was successful
	                if (resultCode == RESULT_OK) {
	                    if (data != null) {
	                        // Get the URI of the selected file
	                        final Uri uri = data.getData();
	                        Log.i("PATH URI", "Uri = " + uri.toString());
	                        try {
	                            // Get the file path from the URI
	                            final String path = FileUtils.getPath(this, uri);
	                            //pathChosen = path.substring(path.lastIndexOf("/") + 1, path.length());
	                            pathChosen = path;
	                            attatcher.add(path);
	                            setupTextView("");
	                            Toast.makeText(this,
	                                    "File Selected: " + path, Toast.LENGTH_LONG).show();
	                        } catch (Exception e) {
	                            Log.e("FileSelectorTestActivity", "File select error", e);
	                        }
	                    }
	                }
	                break;
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }

	private void openFile(String path){

             Intent intent = new Intent();
             intent.setAction(android.content.Intent.ACTION_VIEW);
             File file = new File(path);
           
             MimeTypeMap mime = MimeTypeMap.getSingleton();
             String ext=file.getName().substring(file.getName().lastIndexOf(".")+1);
             String type = mime.getMimeTypeFromExtension(ext);
          
             intent.setDataAndType(Uri.fromFile(file),type);
           
             startActivity(intent); 
	    }
	
	private String convertStringArraytoString(String[] origArr){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < origArr.length;i++){
			sb.append(origArr[i]).append(",");
		}
		return sb.toString(); 
	}
	
	private String[] convertStringtoStringArray(String origString){
		String[] finalArr = origString.split(",");
		return finalArr;
	}
	
	
}

