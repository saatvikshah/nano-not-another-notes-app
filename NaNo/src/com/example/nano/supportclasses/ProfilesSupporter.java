package com.example.nano.supportclasses;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.nano.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ProfilesSupporter {
	private Context context;
	public ProfilesSupporter(Context c){
		context = c;
	}
	private static String fileName = "PROFILES";
	private String KEY_NAME = "profile_name";
	private String KEY_ICON_PATH = "icon_path";
	
	//Insertion Functions
	public int insertProfileName(String profileName,String address){
		SharedPreferences prefs = context.getSharedPreferences(fileName, 0);
		String[] existingNames = getProfileNames();
		int checker = 1;
		//first check if the profileName has been created previously
		try{
		for(int i = 0; i < existingNames.length ; i++){
			if(existingNames[i].equals(profileName) || existingNames[i].equals(null)){
				checker = checker*0;
			}
			else{
				checker = checker*1;
				}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(checker == 1){
		StringBuilder sb = new StringBuilder();
		//1st index is most probably ""
		if(existingNames[0].contentEquals("")){
			sb.append(profileName);
		}else{
			for (int j = 0; j < existingNames.length; j++) {
			    sb.append(existingNames[j]).append(",");
			}
			sb.append(profileName);
		}
		////Log.d("PROFILE NAME",sb.toString());
		SharedPreferences.Editor editor = prefs.edit();
		addProfileIconAddress(address);
		editor.putString(KEY_NAME, sb.toString());
		editor.commit();
		}
		return checker;
	}
	
	private void addProfileIconAddress(String address){
		SharedPreferences prefs = context.getSharedPreferences(fileName, 0);
		String[] encodedProfileIconAddresses = getProfileIconAddresses();
		StringBuilder sb = new StringBuilder();
		//1st index is most probably ""
		if(encodedProfileIconAddresses[0].contentEquals("")){
			sb.append(address);
		}else{
			for (int j = 0; j < encodedProfileIconAddresses.length; j++) {
			    sb.append(encodedProfileIconAddresses[j]).append(",");
			}
			sb.append(address);
		}
		////Log.d("PROFILE NAME",sb.toString());
		Log.d("ICON ADDRESS", sb.toString());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(KEY_ICON_PATH, sb.toString());
		editor.commit();
		
	}
	
	
	public String[] getProfileIconAddresses() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = context.getSharedPreferences(fileName, 0);
		String encodedProfileIconPaths = prefs.getString(KEY_ICON_PATH, "ic_home");
		////Log.d("NAME", encodedProfileNames);
		String[] profileIconAddresses = encodedProfileIconPaths.split(",");
		
		return profileIconAddresses;
	}

	public String[] getProfileNames(){
		SharedPreferences prefs = context.getSharedPreferences(fileName, 0);
		String encodedProfileNames = prefs.getString(KEY_NAME, "");
		////Log.d("NAME", encodedProfileNames);
		String[] profileNames = encodedProfileNames.split(",");
		return profileNames;
	}
	
	public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> list = new ArrayList<String>();

        for (String t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
	
    public ArrayList<String> removeRepitions(List<String> list1){
    	ArrayList<String> al = new ArrayList<String>();
    	// add elements to al, including duplicates
    	HashSet<String> hs = new HashSet<String>();
    	hs.addAll(list1);
    	al.clear();
    	al.addAll(hs);
    	return al;
    }
    
    public void deleteProfileName(String profileName){
    	SharedPreferences prefs = context.getSharedPreferences(fileName, 0);
		String[] existingNames = getProfileNames();
		String[] existingIcons = getProfileIconAddresses();
		int profileToRemoveIndex = 0;
		for(int i = 0;i<existingNames.length;i++){
			if(existingNames[i].equals(profileName)){
				profileToRemoveIndex = i;
			}
		}
		StringBuilder sb= new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		for (int j = 0; j < existingNames.length; j++) {
			if(j != profileToRemoveIndex){
		    sb.append(existingNames[j]).append(",");
		    sb1.append(existingIcons[j]).append(",");
			}
		}
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(KEY_NAME, sb.toString());
		editor.putString(KEY_ICON_PATH, sb1.toString());
		editor.commit();
		}
    
    }
