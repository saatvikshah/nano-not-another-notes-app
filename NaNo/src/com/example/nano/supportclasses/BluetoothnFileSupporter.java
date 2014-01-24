package com.example.nano.supportclasses;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import com.example.nano.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.AlarmClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class BluetoothnFileSupporter {
	private Context c;
	String path = null;
	File fpath = null;
	File file = null;
	Toast t;
	public String ICONS_FOLDER = "Note_Icons";
	public String BACKGROUNDIMAGES_FOLDER = "Background_Images";
	public String BLUETOOTH_FOLDER = "Sent_Files";

	public BluetoothnFileSupporter(Context context) {
		// TODO Auto-generated constructor stub
		c = context;
	}

	@SuppressWarnings({ "unused", "resource" })
	private String readFile(File f) {
		// TODO Auto-generated method stub		
		String text = new String();
		try {
		    BufferedReader br = new BufferedReader(new FileReader(f));
		    String line;
		    while ((line = br.readLine()) != null) {
		        text=text+line+"\n";
		    }
		}
		catch (IOException e) {
		    //You'll need to add proper error handling here
		}
		return text;
	}
	
	public Bitmap readImage(File f) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 8;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}


	public void saveFile(String name, String content) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			path = (Environment.getExternalStorageDirectory().toString())
					+ "/MyNotes/" + BLUETOOTH_FOLDER +"/";
			fpath = new File(path);
			fpath.mkdirs();
			file = new File(fpath, name + ".txt");
			try {
				InputStream is = new ByteArrayInputStream(
						content.getBytes("UTF-8"));
				@SuppressWarnings("resource")
				OutputStream os = new FileOutputStream(file);
				byte[] data = new byte[is.available()];
				is.read(data);
				os.write(data);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			Toast.makeText(c, "Saving file failed", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(c, "Disc is not accessible", Toast.LENGTH_LONG).show();
		}

	}

	public void sendFile(String fileName) {
		Log.d("Processing...", "Sending file...");
		File dir = Environment.getExternalStorageDirectory();
		File manualFile = new File(dir, "/MyNotes/" + BLUETOOTH_FOLDER + "/" + fileName + ".txt");
		Uri uri = Uri.fromFile(manualFile);
		String type = "text/plain";

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType(type);
		sharingIntent.setClassName("com.android.bluetooth",
				"com.android.bluetooth.opp.BluetoothOppLauncherActivity");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
		c.startActivity(sharingIntent);
		manualFile.deleteOnExit();
	}

	public void saveImageIcons(String name, int add) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			path = (Environment.getExternalStorageDirectory().toString())
					+ "/MyNotes/" + ICONS_FOLDER + "/";
			fpath = new File(path);
			fpath.mkdirs();
			file = new File(fpath, name + ".png");
			try {
				InputStream is = c.getResources().openRawResource(add);
				@SuppressWarnings("resource")
				OutputStream os = new FileOutputStream(file);
				byte[] data = new byte[is.available()];
				is.read(data);
				os.write(data);
				t = Toast.makeText(c, "File successfully saved",
						Toast.LENGTH_LONG);
				t.show();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			t = Toast.makeText(c, "Saving file failed", Toast.LENGTH_LONG);
			t.show();
		} else {
			t = Toast.makeText(c, "Disc is not accessible",
					Toast.LENGTH_LONG);
			t.show();
		}

	}
	
	public void saveImageBackgrounds(String name, int add) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			path = (Environment.getExternalStorageDirectory().toString())
					+ "/MyNotes/" + BACKGROUNDIMAGES_FOLDER +"/";
			fpath = new File(path);
			fpath.mkdirs();
			file = new File(fpath, name + ".png");
			try {
				InputStream is = c.getResources().openRawResource(add);
				@SuppressWarnings("resource")
				OutputStream os = new FileOutputStream(file);
				byte[] data = new byte[is.available()];
				is.read(data);
				os.write(data);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			t = Toast.makeText(c, "Saving file failed", Toast.LENGTH_LONG);
			t.show();
		} else {
			t = Toast.makeText(c, "Disc is not accessible",
					Toast.LENGTH_LONG);
			t.show();
		}

	}

	
	//Recursive Algo to iterate thru all files and folders
	public ArrayList<String> listfilz(File file,String[] acceptedFormats,ArrayList<String> paths) {
		
	    File[] list_of_files = file.listFiles();
	    Log.i("DIR", "PATH" +file.getPath());
	    for (int i = 0; i < list_of_files.length; i++) {
	    	if(list_of_files[i].isDirectory()){
	    		//go recursive if its a directory
	    		Log.d("FOLDER","FOL : " + list_of_files[i]);
	    		paths = listfilz(list_of_files[i],acceptedFormats,paths);
	    	}else{
	    		//its a file then right?
	    		Log.d("FILE","FILE: " + list_of_files[i]);
	    		String uncheckedFile = list_of_files[i].toString();
	    		boolean checker = false;
	    		//check if file has acceptable format
	    		for(int j = 0; j < acceptedFormats.length; j++){
	    			checker = checker || uncheckedFile.endsWith(acceptedFormats[j]);
	    		}
	    		Log.d("CHECK","Checker says " + checker);
	    		if(checker){
	    			paths.add(list_of_files[i].getPath().toString());
	    		}
	    	}
	    }
		return paths;
	 }
	
	public void setupFirstLaunchDirs(){
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			path = (Environment.getExternalStorageDirectory().toString())
					+ "/MyNotes/" + BACKGROUNDIMAGES_FOLDER + "/";
			fpath = new File(path);
			fpath.mkdirs();
			path = (Environment.getExternalStorageDirectory().toString())
					+ "/MyNotes/" + BLUETOOTH_FOLDER + "/";
			fpath = new File(path);
			fpath.mkdirs();
		}
	}
	public void setupRefresh(){
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
		path = (Environment.getExternalStorageDirectory().toString())
				+ "/MyNotes/Sent_Files/";
		fpath = new File(path);
		File[] children_to_delete = fpath.listFiles();
		for(int i = 0;i < children_to_delete.length; i++){
			children_to_delete[i].delete();
		}
		}
	}
	
	public String saveMultipleFileTypestoDB(Context c,String path){
		File f = new File(path);
		String fileName = f.getName();
		String fileContent = readFile(f);
		ProfilesDBHelper fileSaver = new ProfilesDBHelper(c, getCorrectProfileName(fileName));
		fileSaver.open();
		String defBackPath = Environment.getExternalStorageDirectory().toString() + "/" + "MyNotes" + 
				"/" + BACKGROUNDIMAGES_FOLDER + "/"
											+ "beauty.png";
		fileSaver.createNote(fileName, fileContent,getDate(), defBackPath, null, 0, null,"");
		fileSaver.close();
		return getCorrectProfileName(fileName);
	}
	
	private String getDate(){
		Date d = new Date();
		CharSequence s  = DateFormat.format("EEEE, MMMM d, yyyy, hh:mm:ss ", d.getTime());
		return (s + "");
	}

	public String getExtension(String FileName)
    {       
         String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
         return ext;
    }
	
	public String getCorrectProfileName(String fileName){
		String extension = getExtension(fileName);
		if(extension.contentEquals("txt")){
			return "TextDump";
		}else if(extension.contentEquals("rb")){
			return "Ruby Scripts";
		}else if(extension.contentEquals("m")){
			return "Matlab Stuff";
		}else if(extension.contentEquals("py")){
			return "Python Scripts";
		}else if(extension.contentEquals("c")){
			return "C Scripts";
		}else if(extension.contentEquals("ahk")){
			return "AutohotKey Scripts";
		}else{
			return "All";
		}
	}

}
